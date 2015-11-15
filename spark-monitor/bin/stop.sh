#!/usr/bin/env bash

cd ../target

pid=`cat spark-monitor.pid`

kill -9 $pid

echo "spark-monitor has stopped"
