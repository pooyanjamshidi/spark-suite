# Spark-Suite
A suite for automated configuration testing, automated topology deployment and a benchmarking tool for Apache Spark.

## Online Documentation

1. Method and Plan Report


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

## Structure of SMP

The main entry is uk.ac.ic.spark.monito.Main. When Main runs it stats several independent services such as ClusterMonitorService, ConfigRestService and so on.  Those services are implements of:

	com.google.common.util.concurrent.service. 

If you know noting of it, don't worry. The only thing you need to know is to implement the run interface.  


## Environment and Dependence

1. To build and run this program you need install:

		maven 2.*
		JDK 7.*

2. Till now is project depends on:

		Guava
		Junit
		Log4j
	
You can contribute this project without any knowledge about Guava and Log4j. However you should familiar with Junit, as you need to use its interface to write Unit testing.




