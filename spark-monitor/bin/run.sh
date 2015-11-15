#!/usr/bin/env bash

cd ../target

logDir=./logs/

if [ ! -d "$logDir" ]; then  
	mkdir "$logDir" 
fi  

nohup java -jar spark-monitor-jar-with-dependencies.jar > ./$logDir/start.log 2>&1 &

echo $! > spark-monitor.pid

