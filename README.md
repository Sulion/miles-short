[![Build Status](https://travis-ci.org/Sulion/miles-short.svg?branch=master)](https://travis-ci.org/Sulion/miles-short)
# Miles short

## About 

This project is yet another URL-shortener written as a test assignment.

The name of the project playfully reflects its purpose (to make any URL _'only'_ miles short) and also is a 
tribute to the Bujold's character Miles Vorkosigan, which is a very energetic persona,
but, as the URLs after the application's done with it, is quite short and perhaps a little ugly.

## Installation and Launching

This application is a rather simple application of [Dropwizard](http://www.dropwizard.io/) Java Framework which produces
just one jar. To install it from sources (and I currently don't provider binaries), you just have to clone the stable version from the
repository (that being 0.2.0 currently) and build it with generic Maven:

    git clone git@github.com:Sulion/miles-short.git
	cd ./miles-short
	mvn clean install
	
Maven build script will generate fat jar file named `miles-short-0.2.0.jar` which is almost all you need to start an application:

    mkdir -p ./runtime && java -jar target/miles-short-0.2.0.jar server config.yml
	
The default port is `8080` for business URLs and `8081` for administrative ones (you may find metrics and healthcheck
there). The directory `./runtime` is intended for persistens storage of application data, such as registered URLs and
accounts. You may symlink other directory instead with the same link name. 

You may also customize ports, persistent DB location and returned in `/register` REST method base address by editing
`config.yml`. The example of such configuration is below:

    server:
	  applicationConnectors:
	   - type: http
		 port: 9090
	  adminConnectors:
	   - type: http
		 port: 8091
    logging:
      level: INFO
	loggers:
      ru.sulion.web-applications: DEBUG
    httpUrl: https://miles-short.herokuapp.com:8080
    dbFileName: ./otherLocation/otherdbname.db

## Usage


### Opening of accounts
<table>
<tr><td>HTTP method</td><td>POST</td></tr>
<tr><td>URI</td><td> /account</td></tr>
<tr><td>Request type</td><td> application/json</td></tr>
<tr><td>Request Body</td><td> JSON object with the following parameters:
 AccountId (String, mandatory)
Example: { AccountId : 'myAccountId'}</td></tr>
</table>
|Reponse Type| application/json|
|Response| We distinguish the successful from the unsuccessful registration.
Unsuccessful registration occurs only if the concerned account ID
already exists. The parameters are as follows:
 success: true / false
 description: Description of status, for example: account with that
ID already exists
 password: Returns only if the account was successfully created.
Automatically generated password length of 8 alphanumeric
characters
Example {success: 'true', description: 'Your account is opened',
password: 'xC345Fc0'}|
