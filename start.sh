#!/bin/bash
mkdir -p ./runtime
java -Ddw.server.connector.port=$PORT $JAVA_OPTS -jar target/miles-short.jar server config.json
