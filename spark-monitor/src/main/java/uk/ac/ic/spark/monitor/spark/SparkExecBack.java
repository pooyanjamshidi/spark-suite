package uk.ac.ic.spark.monitor.spark;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SparkExecBack {

    public static void main(String[] arguments) throws Exception {

        ExecutorService exec = Executors.newFixedThreadPool(2);

        String cmd = "/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/bin/spark-submit " +
                "--class org.apache.spark.examples.SparkPi " +
                "/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/lib/spark-examples-1.5.1-hadoop2.6.0.jar " +
                "10";

        Process process = Runtime.getRuntime().exec(cmd);
//        exec.

        process.waitFor();

    }
}