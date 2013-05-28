
import yaml
import os

LOCALHOST = None # is this running in a dev machine or in the cloud
GCM_KEY = None   # gcm key to send notifications
#limit of recipients you can have in one message. Google says 1000
MAX_DEVICE_TOKENS_PER_MESSAGE = 1000
DRY_RUN = None # use dry run for the GCM messages sent


def read_settings():
  global LOCALHOST, GCM_KEY, DRY_RUN
  LOCALHOST = os.environ.get('SERVER_SOFTWARE', '').startswith('Dev')
  
  f = open('environment.yaml', 'r')
  settings = yaml.load(f)
  
  f2 = open('private_environment.yaml', 'r')
  private = yaml.load(f2)
  settings.update(private)
  
  GCM_KEY = settings['gcm_api_key']
  DRY_RUN = settings['dry_run']
  

read_settings()
