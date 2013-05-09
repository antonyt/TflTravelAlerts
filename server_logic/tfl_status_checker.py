
from xml.dom import minidom
import urllib2
import sys
import json
#from google.appengine.api import urlfetch

url = "http://cloud.tfl.gov.uk/TrackerNet/LineStatus"

def fetch_line_status_as_json():
  #state = urllib2.urlopen(url)
  state = open('sample_raw_results/dummy-line-result.xml', 'r')
  return produce_json(state)

def print_state():
  state = urllib2.urlopen(url)
  for r in parse_raw_state(state):
    print "%25s: %s - %s"%r

def print_json():
  state = urllib2.urlopen(url)
  j = produce_json(state)
  print json.dumps(j)

def str_to_enum(s):
  return s.replace("&", "and").upper().replace(" ", "_")

def produce_json(response):
  json = []
  for (line, description, details) in parse_raw_state(response):
    json.append({'line': str_to_enum(line),
                 'state':str_to_enum(description),
                 'details': details})
  return json
def parse_raw_state(response):
  xmlObj = minidom.parse(response)
  elements = xmlObj.getElementsByTagName("LineStatus")
  for e in elements:
    line = e.getElementsByTagName("Line")[0].getAttribute("Name")
    description = e.getElementsByTagName("Status")[0].getAttribute("Description")
    details = e.getAttribute("StatusDetails")
    if details == "":
      #this happens when the state is good service
      details = description
    yield (line, description, details)

def print_raw():
  print urllib2.urlopen(url).read()

def main():
  if 'raw' in sys.argv:
    print_raw()
  elif 'json' in sys.argv:
    print_json()
  else:
    print_state()

if __name__ == "__main__":
  main()
