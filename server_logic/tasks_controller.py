
import webapp2
import logging

from google.appengine.ext import db
from models import PushNotificationRegistration


class FindAffectedUsersHandler(webapp2.RequestHandler):
  def post(self):
    lines = self.request.get('lines').split(',')
    logging.debug('Finding users affected by changes in lines %s'%lines)
    users = set()
    for line in lines:
      q = db.Query(PushNotificationRegistration, keys_only=True)
      res = q.filter('lines_interested =', line).run(batch_size = 2000)
      new = set(res)
      logging.debug('%d results in line %s'%(len(new),line))
      users.update(new)
    logging.debug('Total rows = %d'%len(users))
    q = db.Query(PushNotificationRegistration, keys_only=True).run(batch_size = 10000)
    c=0
    for _ in q:
      c+=1
    logging.debug('Total users = %d'%c)

app = webapp2.WSGIApplication([('/find-affected-users', FindAffectedUsersHandler),
                              ],
                              debug=True)