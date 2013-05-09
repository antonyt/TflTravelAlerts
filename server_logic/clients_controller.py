import webapp2
import cgi
import logging

from models import LineStatus, PushNotificationRegistration


class HomeHandler(webapp2.RequestHandler):
  def get(self):
    self.response.headers['Content-Type'] = 'text/plain'
    c = PushNotificationRegistration.all(keys_only=True).count(1000*1000)
    self.response.write('Yo world - %d rows'%c)

class GetLineStatus(webapp2.RequestHandler):
  def get(self):
    self.response.headers['Content-Type'] = 'text/plain'
    status = LineStatus.current_status()

    self.response.headers['X-TflTravelAlerts-date'] = str(status.date)
    self.response.write(status.json_data)

class GetWeekendStatus(webapp2.RequestHandler):
  def get(self):
    self.response.headers['Content-Type'] = 'text/plain'
    status = LineStatus.weekend_status()

    self.response.headers['X-TflTravelAlerts-date'] = str(status.date)
    self.response.write(status.json_data)

class RegisterForAlerts(webapp2.RequestHandler):
  def post(self):
    gcm = cgi.escape(self.request.get('gcm_handle'))
    lines = self.parse_lines()
    alert_id = cgi.escape(self.request.get('alert_id'))
    try:
      registration = PushNotificationRegistration(
                      gcm_handle = gcm,
                      lines_interested = lines,
                      client_alert_id = alert_id)
      registration.put()
    except Exception as e:
      logging.warning('Failed to create PushNotificationRegistration: %s', e)
      self.response.set_status(400)

  def parse_lines(self):
    lines_str = cgi.escape(self.request.get('lines'))
    return lines_str.split(",")

app = webapp2.WSGIApplication([('/get-line-status', GetLineStatus),
                               ('/get-weekend-status', GetWeekendStatus),
                               ('/register-for-alerts', RegisterForAlerts),
                               ('/', HomeHandler),
                              ],
                              debug=True)
