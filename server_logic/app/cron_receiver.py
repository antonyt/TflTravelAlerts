import webapp2
from tfl_status_checker import fetch_line_status_as_json
from tfl_weekend_checker import fetch_weekend_status_as_json
from models import LineStatus, PushNotificationRegistration
import json

import logging
from google.appengine.api import taskqueue

FORCE_HANDLING = False

def check_for_modifications(data, json_data, old_json_data):
  """
  this method will compare the current and the last state for any changes.
  if there are changes, it will take the appropriate actions to notify the users

  parameters:
    data          - the newest data (a list of dictionaries)
    json_data     - the newest data as a json string (to avoid calculating it again)
    old_json_data - the previous state as a string with a json format
  """
  old_data = json.loads(old_json_data)
  modified_lines = []
  if len(old_data) != len(data):
    #just in case... this should never happen
    logging.warn('expected sizes to match but they don\'t!')
    modified_lines = [i['line'] for i in data]
  else:
    for i in xrange(len(data)):
      if data[i] != old_data[i]:
        modified_lines.append(data[i]['line'])
  if FORCE_HANDLING and not modified_lines:
    logging.debug('faking modified lines')
    modified_lines = [u'CIRCLE', u'METROPOLITAN']
  if modified_lines:
    lines_str = ','.join(modified_lines)
    logging.debug('scheduling a task because these lines have changed: %s'%lines_str)
    taskqueue.add(url='/find-affected-users', params={'lines':lines_str} )
  else:
    logging.debug('no changes in lines')


class UpdateLineStatus(webapp2.RequestHandler):
  def get(self):
    data = fetch_line_status_as_json()
    json_data = json.dumps(data)
    line_status = LineStatus.current_status()
    old_json_data = line_status.json_data
    line_status.set_json_data(json_data)
    if old_json_data is None:
      line_status.put()
    elif old_json_data != json_data:
      line_status.put()
      check_for_modifications(data, json_data, old_json_data)



class UpdateWeekendStatus(webapp2.RequestHandler):
  def get(self):
    data = fetch_weekend_status_as_json()
    line_status = LineStatus.weekend_status()
    line_status.set_json_data(json.dumps(data))
    line_status.put()

    #import testing
    #testing.inject_many_users(4070)


class ClearOldRegistrations(webapp2.RequestHandler):
  """Handler to clear old clients registered in the db to listen for alerts"""
  def get(self):
    PushNotificationRegistration.delete_outdated_registrations()



app = webapp2.WSGIApplication([('/update-line-status', UpdateLineStatus),
                               ('/clear-old-registrations', ClearOldRegistrations),
                               ('/update-weekend-status', UpdateWeekendStatus),
                              ])