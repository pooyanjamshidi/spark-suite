//package uk.ac.ic.spark.monitor.util;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.yarn.api.records.ApplicationId;
//import org.apache.hadoop.yarn.api.records.ApplicationReport;
//import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
//import org.apache.hadoop.yarn.api.records.YarnApplicationState;
//import org.apache.spark.SparkConf;
//import org.apache.spark.deploy.yarn.Client;
//import org.apache.spark.deploy.yarn.ClientArguments;
//import org.apache.spark.deploy.SparkSubmit;
////import org.apache.spark.launcher.Main;
//
//public class SparkSubmiter {
//
//    public static void main(String[] arguments) throws Exception {
//
//        // prepare arguments to be passed to
//        // org.apache.spark.deploy.yarn.Client object
//        String[] args = new String[] {
//
//                "--class",
//                "cn.ac.ict.bigdatabench.Grep",
//
//
//                "--num-executors",
//                "1",
//
//                "--driver-memory",
//                "1g ",
//
//                "--executor-memory",
//                "1g",
//
//                "--executor-cores",
//                "1",
//
////                "--addJars",
////                "lib/spark-assembly-1.5.1-hadoop2.6.0.jar,lib/spark-1.5.1-yarn-shuffle.jar",
//
//
//
//                "--jar",
//                "lib/bigdatabench-spark_1.3.0-hadoop_1.0.4.jar",
//
//                "--arg",
//                "/data-MicroBenchmarks",
//
//                "--arg",
//                "test",
//
//                "--arg",
//                "/spark-grep-result125"
//        };
//
//        // create a Hadoop Configuration object
//        Configuration config = new Configuration();
//
//        config.set("fs.defaultFS", "hdfs://146.169.46.48:9000");// namenode
//        config.set("mapreduce.framework.name","yarn"); // yarn
//        config.set("yarn.resourcemanager.address","146.169.46.48:8032"); // resourcemanager
//        config.set("yarn.resourcemanager.scheduler.address", "146.169.46.48:8030");
//        config.set("mapreduce.jobhistory.address","146.169.46.48:10020");
//
//
//        // identify that you will be using Spark as YARN mode
//        System.setProperty("SPARK_YARN_MODE", "true");
//
////      Just for test
//        // create an instance of SparkConf object
//        SparkConf sparkConf = new SparkConf();
//        sparkConf.set("spark.yarn.jar", "hdfs://146.169.46.48:9000/spark/spark-assembly-1.5.1-hadoop2.6.0.jar");
////        sparkConf.set("spark.yarn.jar", "lib/spark-assembly-1.5.1-hadoop2.6.0.jar");
//
//        // create ClientArguments, which will be passed to Client
//        ClientArguments cArgs = new ClientArguments(args, sparkConf);
//
//        // create an instance of yarn Client client
//        Client client = new Client(cArgs, config, sparkConf);
//
//        // submit Spark job to YARN
//        client.run();
////
////        ApplicationId appliationID = client.submitApplication();
////        client.getApplicationReport(appliationID);
////
////        System.out.println("appliationID: " + appliationID);
//
////        while(true){
////            Thread.sleep(1000);
////
////
////            ApplicationReport applicationReport = client.getApplicationReport(appliationID);
////            YarnApplicationState state = applicationReport.getYarnApplicationState();
////            FinalApplicationStatus finalState = applicationReport.getFinalApplicationStatus();
////
////            System.out.println("ApplicationId " + appliationID + "\n" +
////                "state " + state + "\n"
////                    + "finalState " + finalState + "\n"
////                    + applicationReport.getTrackingUrl());
////
////        }
//
//
//    }
//}