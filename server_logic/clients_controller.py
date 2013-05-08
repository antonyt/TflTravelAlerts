import webapp2
import cgi

from models import LineStatus, PushNotificationRegistration
  
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
    registration = PushNotificationRegistration(
                      gcm_handle = gcm, 
                      lines_interested = lines, 
                      client_alert_id = alert_id)
    registration.put()
  
  def parse_lines(self):
    lines_str = cgi.escape(self.request.get('lines'))
    return lines_str.split(",")

app = webapp2.WSGIApplication([('/get-line-status', GetLineStatus), 
                               ('/get-weekend-status', GetWeekendStatus), 
                               ('/register-for-alerts', RegisterForAlerts), 
                              ],
                              debug=True) 
