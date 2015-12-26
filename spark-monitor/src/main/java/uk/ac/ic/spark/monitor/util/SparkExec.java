package uk.ac.ic.spark.monitor.util;

import org.apache.commons.exec.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SparkExec {

    private static final Logger log = LogManager.getLogger(SparkExec.class);
//    private static final long pollingTime = 1000;
//    private static final long waitingtTime = 1000 * 1000;


    public static void main(String[] arguments) {

//        String cmd = "/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/bin/spark-submit " +
//                "--class org.apache.spark.examples.SparkPi " +
//                "/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/lib/spark-examples-1.5.1-hadoop2.6.0.jar " +
//                "10";
//
//        CommandLine cmdLine = CommandLine.parse(cmd);
//
//        System.out.println(cmdLine.toString());
////        cmdLine.addArgument("--class org.apache.spark.examples.SparkPi");
////        cmdLine.addArgument("--master yarn-client");
////        cmdLine.addArgument("--num-executors 1");
////        cmdLine.addArgument("--driver-memory 1g");
////        cmdLine.addArgument("--executor-memory 1g");
////        cmdLine.addArgument("--executor-cores 1");
//
////        cmdLine.addArgument("/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/lib/spark-examples-1.5.1-hadoop2.6.0.jar");
//
//
//        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
//
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
//        ExecuteStreamHandler streamHandler = new PumpStreamHandler(outStream,errStream);
//
//        DefaultExecutor executor = new DefaultExecutor();
//        executor.setExitValue(0);
////        ExecuteWatchdog watchdog = new ExecuteWatchdog(600000);
////        executor.setWatchdog(watchdog);
////        int exitValue = executor.execute(cmdLine);
//        executor.setStreamHandler(streamHandler);
//        try {
//            executor.execute(cmdLine, resultHandler);
//        } catch (IOException e) {
//            log.error("outStream: " + outStream.toString());
//            log.error("errStream: " + errStream.toString());
//            log.error(e.getMessage(), e);
//
//        }
//
//        boolean hasJson = false;
//        String appID =  null;
//        SparkRequester sparkRequester = new SparkRequester();
//        while(!resultHandler.hasResult()){

//        boolean sparkIsReady = false;
//        String appID = null;
//        SparkRequester sparkRequester = new SparkRequester();
//        while(true){
//            System.out.println("loop here");
//            if(!sparkIsReady){
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    log.error(e.getMessage(), e);
//                }
//
//
//                try {
//                    List<Map<String, Object>> appsList =
//                            sparkRequester.getAppsList();
//                    log.info("Get appsList:" + appsList);
//                    appID = (String)appsList.get(0).get("id");
//                    log.info("Get appId:" + appID);
//                    sparkIsReady = true;
//                } catch (IOException e) {
//                    log.debug(e.getMessage(), e);
//                    log.info("IOException. Waiting for saprk ready.");
//
//                }
//
//            } else {
//                try {
//                    Thread.sleep(pollingTime);
//
//
//                } catch (InterruptedException e) {
//                    log.error(e.getMessage(), e);
//                }
//
//            }
//
//        }

//        log.info("outStream: " + outStream.toString());
//        log.info("errStream: " + errStream.toString());

    }

    public void submitSparkApp(String jarPath, String className,
                               int pollingTime, int checkTimes,
                               List<String> argsList, Set<Integer> timeEndsIndex) {

        StringBuilder cmdBuilder = new StringBuilder();


        cmdBuilder.append(ConstantConfig.SPARK_BIN_DIR + " ");
        cmdBuilder.append("--class " + className + " ");
        cmdBuilder.append(jarPath + " ");

        for(int i = 0; i < argsList.size(); i++){
            if(timeEndsIndex.contains(i)){
                cmdBuilder.append(argsList.get(i) + System.currentTimeMillis() + " ");
            } else {
                cmdBuilder.append(argsList.get(i) + " ");
            }

        }

//        String cmd = "/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/bin/spark-submit " +
//                "--class org.apache.spark.examples.SparkPi " +
//                "/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/lib/spark-examples-1.5.1-hadoop2.6.0.jar " +
//                "10";

        CommandLine cmdLine = CommandLine.parse(cmdBuilder.toString());
        log.info("cmdLine: " + cmdLine.toString());
////        cmdLine.addArgument("--class org.apache.spark.examples.SparkPi");
////        cmdLine.addArgument("--master yarn-client");
////        cmdLine.addArgument("--num-executors 1");
////        cmdLine.addArgument("--driver-memory 1g");
////        cmdLine.addArgument("--executor-memory 1g");
////        cmdLine.addArgument("--executor-cores 1");
//
////        cmdLine.addArgument("/Users/hubert/spark/spark-1.5.1-bin-hadoop2.6/lib/spark-examples-1.5.1-hadoop2.6.0.jar");
//
//
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        ExecuteStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);
//        ExecuteWatchdog watchdog = new ExecuteWatchdog(600000);
//        executor.setWatchdog(watchdog);
//        int exitValue = executor.execute(cmdLine);
        executor.setStreamHandler(streamHandler);
        try {
            executor.execute(cmdLine, resultHandler);
        } catch (IOException e) {
            log.error("outStream: " + outStream.toString());
            log.error("errStream: " + errStream.toString());
            log.error(e.getMessage(), e);

        }

        String appID = null;
        while (!resultHandler.hasResult()) {

            boolean sparkIsReady = false;
            SparkRequester sparkRequester = new SparkRequester();
            int currentCheckTimes = 0;
            while (true) {

                if(currentCheckTimes >= checkTimes){
                    break;
                }

                log.info("loop here");
                if (!sparkIsReady) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }


                    try {
                        List<Map<String, Object>> appsList =
                                sparkRequester.getAppsList();
                        log.info("Get appsList:" + appsList);
                        if(appsList.size() > 0){
                            appID = (String) appsList.get(0).get("id");
                            log.info("Get appId:" + appID);
                            sparkIsReady = true;
                        }

                    } catch (IOException e) {
                        log.debug(e.getMessage(), e);
                        log.info("IOException. Waiting for saprk ready.");

                    }

                } else {
                    try {
                        Thread.sleep(pollingTime * 1000);
                        CSVGenerater csvGenerater = new CSVGenerater();
                        csvGenerater.generateAllCsvFiles(appID, System.currentTimeMillis());
                        currentCheckTimes++;

                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }

            }
            log.info("outStream: " + outStream.toString());
            log.info("errStream: " + errStream.toString());
        }
    }
}