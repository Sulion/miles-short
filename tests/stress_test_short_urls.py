#!/usr/bin/python
import requests
import json
import time
import pprint
import random
from threading import Thread

def stress_short_urls(sh_urls, th_name):
    th_st_time = time.time()
    for i in range(0,9):
        print th_name, ": ", (i*1000), '\t', (time.time() - th_st_time)
        for j in range(1,1000):
            requests.get(random.choice(sh_urls).replace(':8080', ''), allow_redirects=False)
    print th_name, ": ", (10*1000), '\t', (time.time() - th_st_time)
            
short_urls = []
with open("short-urls.json", 'r') as short_url_file:
    short_urls = json.load(short_url_file)
t1 = Thread( target=stress_short_urls, args=(short_urls, "Thread-1") )
t2 = Thread( target=stress_short_urls, args=(short_urls, "Thread-2") )
t3 = Thread( target=stress_short_urls, args=(short_urls, "Thread-3") )
t4 = Thread( target=stress_short_urls, args=(short_urls, "Thread-4") )
t5 = Thread( target=stress_short_urls, args=(short_urls, "Thread-5") )
t6 = Thread( target=stress_short_urls, args=(short_urls, "Thread-6") )
t7 = Thread( target=stress_short_urls, args=(short_urls, "Thread-7") )
t8 = Thread( target=stress_short_urls, args=(short_urls, "Thread-8") )

t1.start()
t2.start()
t3.start()
t4.start()
t5.start()
t6.start()
t7.start()
t8.start()

t1.join()
t2.join()
t3.join()
t4.join()
t5.join()
t6.join()
t7.join()
t8.join()