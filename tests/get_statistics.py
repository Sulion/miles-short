#!/usr/bin/python
import requests
import json
import time
import pprint
import random
from threading import Thread

URL = 'http://localhost:8080/statistic/'            
usermap = {}
with open("user-pass.json", 'r') as userpass_file:
    usermap = json.load(userpass_file)
for user, password in usermap.iteritems():
    response = requests.get(URL + user, auth=(user, password))
    print user, ":"
    pprint.pprint(response.json())