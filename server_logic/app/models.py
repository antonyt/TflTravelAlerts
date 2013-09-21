
from google.appengine.ext import db
import datetime

class LineStatus(db.Model):
  json_data = db.TextProperty()
  date = db.DateTimeProperty(auto_now=True)

  def set_json_data(self, data):
    self.json_data = db.Text(data, encoding="utf-8")

  @classmethod
  def weekend_status(cls):
    obj = LineStatus.get_or_insert('weekend-status')
    if obj.json_data is None:
      obj.json_data="[]"
    return obj

  @classmethod
  def current_status(cls):
    obj = LineStatus.get_or_insert('current-status')
    if obj.json_data is None:
      obj.json_data="[]"
    return obj



class PushNotificationRegistration(db.Model):
  LINES = set(['BAKERLOO', 'CENTRAL','CIRCLE', 'DISTRICT', 'DLR',
               'HAMMERSMITH_AND_CITY', 'JUBILEE', 'METROPOLITAN', 'NORTHERN',
               'OVERGROUND', 'PICCADILLY', 'VICTORIA', 'WATERLOO_AND_CITY'])

  def validate_lines(lines_interested):
    for line in lines_interested:
      if line not in PushNotificationRegistration.LINES:
        raise Exception('Unexpected line: %s'%line)
    return lines_interested

  gcm_handle = db.StringProperty(required=True)
  lines_interested = db.StringListProperty(required=True, validator=validate_lines)
  date_added = db.DateTimeProperty(auto_now_add=True)


  @classmethod
  def delete_outdated_registrations(cls):
    now = datetime.datetime.now()
    delta = datetime.timedelta(minutes = 90)
    expiry_date = now - delta
    d = PushNotificationRegistration.all(keys_only=True).filter('date_added < ', expiry_date)
    ids = d.run()
    db.delete(ids)


