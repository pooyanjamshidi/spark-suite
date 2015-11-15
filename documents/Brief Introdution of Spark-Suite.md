# Brief Introdution of Spark-Suite

## 1. Introdution
Spark-Suite is a useful benchmarking tool for spark. It can change configurations of Spark and submit Spark jobs automatically, and provides CVS files containing the performace data of each job. It also provides a website to make the process of getting benchmarking more easily. 

## 2. Usage

There are three ways to use this tool: 

1. Command Line: 

	You can run tool using this commmand:
	
	> java -jar Spark-Suite.jar -P=[p1=v11,v12;p2=v21,v22,v23] -J=jarfile.jar 
	
	Take the above command as an example, when you press the enter, this tool will submit jarfile.jar to spark cluster 6 times with different configurations. In other words, the Spark cluster will execute jobs with 6 different configurations. After the execution of the jobs. this tool will generate 6 CVS files containing the performance figures of each job.


2. Restful API:   

	If you like, you can send a post request to Spark-Suite to submit spark jobs and get the CVS files. The request should contains to parameters:
	
	> 1. params: This the the config params of sparks and should look like this p1=v11,v12;p2=v21,v22,v23

	> 2. jar: This is the content of jar file.
	
	When it receives the request, the tool will submit spark jobs and after the execution the jobs, this tool will return a list of the URL of CVS files and you can download the CVS files through the URL
	

3. Web:

	You can select the params in the web page and then input the values you want. After uploading the jar file which will be executed and pressing the submit button, spark jobs will be submited. After the execution, digrams of performance figures and CVS files will be generated.
	
## 3. Structure

This tool has two parts:

1. Spark-Suite.jar 

	This part has the following functions:
	
		1. Changing the configuration of Spark.
		2. Submitting Spark jobs.
		3. Collectting the performance figures of Spark jobs and generate CVS files.
		4. Providing Restful API
		
2. Web-UI

	This is not only the user interface of Spark-Suite, but also one Restful AIP Clint of Spark-Suite.jar.
	
	This part has the following functions:
	
		1. The user can choose params, input values and upload jar file in the website
		2. Invoking the Restful API of Spark-Suite.jar to submit Spark job.
		3. Generating digrams of the performance figures of Spark jobs and providing the download of CVS files.