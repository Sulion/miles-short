#!/usr/bin/python
import requests
import json
import time
import pprint

URL = 'https://miles-short.herokuapp.com'
users = []
usermap={}
short_urls=[]
counter = 0;
st_time = time.time()

with open('users.json') as user_file:
    users = json.load(user_file)
for user in users:
    udata = {"AccountId": user }
    response = requests.post(URL + '/account', headers={'Content-type': 'application/json'}, data=json.dumps(udata))
    counter = counter + 1
    print user
    if response.json()['success'] == True:
        usermap[user] = response.json()['password']
#        print user, ": ", usermap[user]
        with open('urls.feed') as urls:
            for line in urls.readlines():
                data_url={'url': line.rstrip(), 'redirectType': 302}
                response = requests.post(URL + '/register' , auth=(user, usermap[user]),
                                         headers={'Content-type': 'application/json'}, data=json.dumps(data_url))
                counter = counter + 1
                short_urls.append(response.json()['shortUrl'])
                if counter % 30 == 0:
                    print('-------------------------------------', time.time(), ", requests: ", counter)
print counter, " requests, ", (time.time() - st_time), " seconds elapsed"                
with open('short-urls.json', 'w') as save:
    json.dump(short_urls, save)
with open('user-pass.json', 'w') as save:
    json.dump(usermap, save)

