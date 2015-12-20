//package uk.ac.ic.spark.monitor.util;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.spark.SparkConf;
//import org.apache.spark.deploy.yarn.Client;
//import org.apache.spark.deploy.yarn.ClientArguments;
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
//                "org.apache.spark.examples.SparkPi",
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
//                "--jar",
//                "lib/spark-examples*.jar",
//
//                "--arg",
//                "10"
//        };
//
//        // create a Hadoop Configuration object
//        Configuration config = new Configuration();
//
//        config.set("fs.defaultFS", "hdfs://146.169.46.48:9000");// namenode
//        config.set("mapreduce.framework.name","yarn"); // yarn
//        config.set("yarn.resourcemanager.address","146.169.46.48:8032"); // resourcemanager
////        config.set("yarn.resourcemanager.scheduler.address", "146.169.46.48:8030");
////        config.set("mapreduce.jobhistory.address","node101:10020");
//
//
//        // identify that you will be using Spark as YARN mode
//        System.setProperty("SPARK_YARN_MODE", "true");
////      Just for test
//        // create an instance of SparkConf object
//        SparkConf sparkConf = new SparkConf();
//
//        // create ClientArguments, which will be passed to Client
//        ClientArguments cArgs = new ClientArguments(args, sparkConf);
//
//        // create an instance of yarn Client client
//        Client client = new Client(cArgs, config, sparkConf);
//
//        // submit Spark job to YARN
//        client.run();
//    }
//}