import random

from models import PushNotificationRegistration
from google.appengine.ext import db

LINES = set(['BAKERLOO', 'CENTRAL','CIRCLE', 'DISTRICT', 'DLR',
               'HAMMERSMITH_AND_CITY', 'JUBILEE', 'METROPOLITAN', 'NORTHERN',
               'OVERGROUND', 'PICADILLY', 'VICTORIA', 'WATERLOO_AND_CITY'])

def inject_many_users(size = 1000, batch_size = 1000):
  odds = {1:0.26,
          2:0.46,
          3:0.15,
          4:0.05,
          5:0.01,
          6:0.01,
          7:0.01,
          8:0.01,
          9:0.01,
          10:0.01,
          11:0.01,
          12:0.01}

  batch = []
  for _ in xrange(size):
    n = pick_n_lines(odds)
    r = str(random.random())
    lines = random.sample(LINES, n)
    registration = PushNotificationRegistration(
                      gcm_handle = r,
                      lines_interested = lines)
    batch.append(registration)
    if len(batch) == batch_size:
      db.put(batch)
      batch=[]
  if batch:
    db.put(batch)
    batch=[]


def pick_n_lines(odds):
  r = random.random()
  acc = 0
  for n,odd in odds.iteritems():
    if odd+acc > r:
      return n
      break
    acc += odd
