from xml.dom import minidom
import urllib2
import sys
import json

from tfl_status_checker import str_to_enum

url = "http://www.tfl.gov.uk/tfl/businessandpartners/syndication/feed.aspx?email=tfltravelalerts@gmail.com&feedId=7"

def fetch_weekend_status_as_json():
  state = urllib2.urlopen(url)
  return produce_json(state)

def print_state():
  state = urllib2.urlopen(url)
  for r in parse_raw_state(state):
    print "%25s: %s - %s"%r

def print_json():
  state = urllib2.urlopen(url)
  j = produce_json(state)
  print json.dumps(j)

def produce_json(response, skip_fake_status = False):
  json = []
  for (line, description, details) in parse_raw_state(response):
    json.append({'line': str_to_enum(line), 
                 'state':str_to_enum(description), 
                 'details': details})
  return json

def parse_raw_state(response):
  xmlObj = minidom.parse(response)
  elements = xmlObj.getElementsByTagName("Line")
  for e in elements:
    details = None
    line = e.getElementsByTagName("Name")[0].childNodes[0].wholeText
    description = e.getElementsByTagName("Status")[0].getElementsByTagName("Text")[0].childNodes[0].wholeText
    message = e.getElementsByTagName("Status")[0].getElementsByTagName("Message")
    if len(message) > 0:
      details = message[0].getElementsByTagName("Text")[0].childNodes[0].wholeText
    else:
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
