# Spark-Suite
A suite for automated configuration testing, automated topology deployment and a benchmarking tool for Apache Spark.

## Online Documentation

1. [Brief Introdution of Spark-Suite](https://github.com/pooyanjamshidi/spark-suite/blob/master/documents/Brief%20Introdution%20of%20Spark-Suite.md)


## Building and Running SMP

To build and run :

    git clone git@github.com:pooyanjamshidi/spark-suite.git
    cd spark-monitor
    mvn package 
    cd bin
    sh run.sh

After executing the run.sh, you can find that the log :

	../target/logs/monitor.log 
	
is updating.

If you want to stop it, you should run:

	sh stop.sh	

## Structure of  Spark-Suite




## Environment and Dependence

1. To build and run this program you need install:

		maven 2.*
		JDK 7.*

2. Till now is project depends on:

		Guava
		Junit
		Log4j
	
You can contribute this project without any knowledge about Guava and Log4j. However you should familiar with Junit, as you need to use its interface to write Unit testing.




