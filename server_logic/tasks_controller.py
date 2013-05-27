
import webapp2
import logging

from google.appengine.ext import db
from models import PushNotificationRegistration
import gcm
import settings
import json

class FindAffectedUsersHandler(webapp2.RequestHandler):
  def post(self):
    lines = self.request.get('lines').split(',')
    logging.info('Finding users affected by changes in lines %s'%lines)
    users = set()
    for line in lines:
      q = db.Query(PushNotificationRegistration)
      res = q.filter('lines_interested =', line).run(read_policy=db.EVENTUAL_CONSISTENCY, batch_size = 2000)
      new = set(res)
      logging.debug('%d results in line %s'%(len(new),line))
      users.update(new)
    logging.debug('Total rows = %d'%len(users))
    schedule_gcm_messages(list(users))

def schedule_gcm_messages(users):
  gcm_connection = gcm.GCMConnection()
  for batch in get_in_batches(users):
    handles = map(get_gcm_handle, batch)
    message = create_message(handles)
    logging.info('submitting a batch of %d notifications'%len(handles))
    gcm_connection.notify_device(message, deferred=True)

def get_in_batches(elements, batch_size=1000):
  i = 0
  while i < len(elements):
    yield elements[i:i+batch_size]
    i += batch_size

def get_gcm_handle(element):
  return element.gcm_handle

def create_message(handles):
  payload = {'refresh_current_status': True}
  NINETY_MINUTES = 5400 # in seconds
  is_dry_run = settings.DRY_RUN
  return gcm.GCMMessage(handles,
                     payload,
                     dry_run=is_dry_run,
                     collapse_key='line-status-update',
                     time_to_live=NINETY_MINUTES)

class SendGcmMessage(webapp2.RequestHandler):
  def post(self):
    message_json = self.request.get('message_json')
    logging.debug('restored %d bytes for message'%len(message_json))
    dic = json.loads(message_json)
    tokens = dic['registration_ids']
    notification = dic['data']
    del dic['registration_ids']
    del dic['data']

    message = gcm.GCMMessage(tokens, notification, **dic)
    gcm_connection = gcm.GCMConnection()
    gcm_connection.notify_device(message)


app = webapp2.WSGIApplication([('/find-affected-users', FindAffectedUsersHandler),
                               (gcm.GCM_QUEUE_CALLBACK_URL, SendGcmMessage),
                              ])