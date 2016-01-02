package uk.ac.ic.spark.monitor.util;


import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ic.spark.monitor.config.ConstantConfig;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

public class CSVGenerater {
    private static final Logger log = LogManager.getLogger(CSVGenerater.class);

    private final String CSV_PATH = ConstantConfig.SPARK_USER_CSV_PATH + File.separator;

    private static final Set<String> jobsExcludeSet = new HashSet<String>(){{
        add("stageIds");
    }};


    private static final Set<String> stagesExcludeSet =new HashSet<String>(){{
        add("details");
        add("accumulatorUpdates");
    }};


    private static final Set<String> tasksExcludeSet =new HashSet<String>(){{
//        add("details");
        add("accumulatorUpdates");
    }};

    private static final Set<String> executorsExcludeSet =new HashSet<String>(){{
        add("executorLogs");
    }};

    private long appTimeStamp;

    public CSVGenerater(long appTimeStamp){
        this.appTimeStamp = appTimeStamp;
    }

    public static void main(String[] args) throws IOException {
        CSVGenerater csvGenerater = new CSVGenerater(System.currentTimeMillis());

        SparkRequester sparkRequester = new SparkRequester();


//        csvGenerater.convertJobsToCSV("application_1450721503259_0010",
//                sparkRequester.getJobsList("application_1450721503259_0010"),
//                9232L);


        HashMap<Integer, Integer> stageJobMap = csvGenerater.generateStageJobMap(
                sparkRequester.getJobsList("application_1450721503259_0010"));

//        csvGenerater.convertStagesToCSV("application_1450721503259_0010",
//                stageJobMap,
//                sparkRequester.getStagesList("application_1450721503259_0010"),
//                9232L);

        csvGenerater.convertExecutorsToCSV("application_1450721503259_0010",
                sparkRequester.getExecutorsList("application_1450721503259_0010"),
                111L);

//
//        Map<Integer, List<Map<String, Object>>> tasksMap = sparkRequester.getTasksMap("application_1450721503259_0010", stageJobMap.keySet());
//
//        for(int stageID : tasksMap.keySet()){
//            int jobID = stageJobMap.get(stageID);
//            csvGenerater.convertTasksToCSV("application_1450721503259_0010",
//                    jobID,
//                    stageID,
//                    tasksMap.get(stageID),
//                    122L
//            );
//        }
    }

    public void generateAllCsvFiles(String appID, Map<String, String> changedConfigMap,
                                    long timeStamp) throws IOException {

        CSVGenerater csvGenerater = new CSVGenerater(appTimeStamp);
        SparkRequester sparkRequester = new SparkRequester();

        csvGenerater.generateConfigCSV(appID, changedConfigMap, timeStamp);

        HashMap<Integer, Integer> stageJobMap = csvGenerater.generateStageJobMap(
                sparkRequester.getJobsList(appID));

        csvGenerater.convertJobsToCSV(appID,
                sparkRequester.getJobsList(appID),
                timeStamp);

        csvGenerater.convertExecutorsToCSV(appID,
                sparkRequester.getExecutorsList(appID),
                timeStamp);

        csvGenerater.convertStagesToCSV(appID,
                stageJobMap,
                sparkRequester.getStagesList(appID),
                timeStamp);

        Map<Integer, List<Map<String, Object>>> tasksMap =
                sparkRequester.getTasksMap(appID, stageJobMap.keySet());

        for(int stageID : tasksMap.keySet()){
            int jobID = stageJobMap.get(stageID);
            csvGenerater.convertTasksToCSV(appID,
                    jobID,
                    stageID,
                    tasksMap.get(stageID),
                    timeStamp
            );
        }

    }



    private void convertAppToCSV(String appID,
                                 Map<String, Object> appMap,
                                 long timeStamp){
        createTimeStampDIR(appID, timeStamp);

        File csvFile = new File(CSV_PATH + appID + appTimeStamp + "/timeStamp/appInfo.csv");

        try {
            FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(csvFile, Charset.forName("UTF-8"));

            StringBuilder keyBuilder = new StringBuilder();
            for(String key : appMap.keySet()){
                keyBuilder.append(key + ",");
            }

            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
            keyBuilder.append("\n");

            fileWriter.write(keyBuilder.toString());
            fileWriter.close();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    private void convertJobsToCSV(String appID,
                                  List<Map<String, Object>> jobsList,
                                  long timeStamp){
        createTimeStampDIR(appID, timeStamp);

        if(jobsList.size() == 0){
            log.warn("jobsList size is 0");
        } else {

            jobsList = removeUncompletedMap(jobsList);

            for(Map<String, Object> jobInfo : jobsList){
                for(String key : jobsExcludeSet){
                    jobInfo.remove(key);
                }
            }

            File csvFile = new File(CSV_PATH + appID + appTimeStamp + "/"
                    + timeStamp + "/jobsInfo.csv");

            try {
                FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(csvFile, Charset.forName("UTF-8"));

                StringBuilder keyBuilder = new StringBuilder();
                for(String key : jobsList.get(0).keySet()){
                    keyBuilder.append(key + ",");
                }

                keyBuilder.deleteCharAt(keyBuilder.length() - 1);
                keyBuilder.append("\n");

                fileWriter.write(keyBuilder.toString());


                for(Map<String, Object> jobInfo : jobsList){
                    StringBuilder valueBuilder = new StringBuilder();

                    for(String key : jobInfo.keySet()){
                        valueBuilder.append(convertObjectToString(jobInfo.get(key)) + ",");
                    }
                    valueBuilder.deleteCharAt(valueBuilder.length() - 1);
                    valueBuilder.append("\n");
                    fileWriter.write(valueBuilder.toString());
                }

                fileWriter.close();

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }



    public void convertStagesToCSV(String appID,
                                    HashMap<Integer, Integer> stageJobMap,
                                    List<Map<String, Object>> stagesList,
                                    long timeStamp){
        createTimeStampDIR(appID, timeStamp);

        if(stagesList.size() == 0){
            log.warn("stagesList size is 0");
        } else if (stageJobMap.size() == 0){
            log.warn("jobsList size is 0");
        } else {
            stagesList = removeUncompletedMap(stagesList);

            for(Map<String, Object> jobInfo : stagesList){
                for(String key : stagesExcludeSet){
                    jobInfo.remove(key);
                }
            }

            File csvFile = new File(CSV_PATH + appID + appTimeStamp + "/"
                    + timeStamp + "/stagesInfo.csv");

            try {
                FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(csvFile, Charset.forName("UTF-8"));

                StringBuilder keyBuilder = new StringBuilder();
                keyBuilder.append("jobId,");
                for(String key : stagesList.get(0).keySet()){
                    keyBuilder.append(key + ",");
                }

                keyBuilder.deleteCharAt(keyBuilder.length() - 1);
                keyBuilder.append("\n");

                fileWriter.write(keyBuilder.toString());


                for(Map<String, Object> stageInfo : stagesList){

                    int stageID = ((Double)stageInfo.get("stageId")).intValue();
                    int jobID = stageJobMap.get(stageID);

                    StringBuilder valueBuilder = new StringBuilder();

                    valueBuilder.append(jobID + ",");

                    for(String key : stageInfo.keySet()){
                        valueBuilder.append(convertObjectToString(stageInfo.get(key)) + ",");
                    }
                    valueBuilder.deleteCharAt(valueBuilder.length() - 1);
                    valueBuilder.append("\n");
                    fileWriter.write(valueBuilder.toString());
                }

                fileWriter.close();

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    public HashMap<Integer, Integer> generateStageJobMap(List<Map<String, Object>> jobsList){



        HashMap<Integer, Integer> stageJobMap = new HashMap<Integer, Integer>();

        for(Map<String, Object> jobInfo : jobsList){
            int jobID = ((Double) jobInfo.get("jobId")).intValue();
            List<Double> stagesIDs = (List<Double>)jobInfo.get("stageIds");
            for(Double stageID : stagesIDs){
                stageJobMap.put(stageID.intValue(), jobID);
            }
        }

        log.info("stageJobMap: " + stageJobMap);

        return  stageJobMap;
    }

    private void convertTasksToCSV(String appID,
                                    int jobID, int stageID,
                                    List<Map<String,Object>> taskList,
                                    long timeStamp){

        createTimeStampDIR(appID, timeStamp);


        File csvFile = new File(CSV_PATH + appID + appTimeStamp + "/"
                + timeStamp + "/" + jobID + "_"
                + stageID + "_" + "taskInfo.csv");

        if(taskList.size() == 0){
            log.warn("appID: " + appID + " stageID: " + stageID + " taskList size is 0");
        } else {
            try {
                taskList = removeUncompletedMap(taskList);
                FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(csvFile, Charset.forName("UTF-8"));

                StringBuilder keyBuilder = new StringBuilder();
                keyBuilder.append("jobId,");
                keyBuilder.append("stageId,");


                for(Map<String, Object> tasksInfo :  taskList){
                    for(String key : tasksExcludeSet){
                        tasksInfo.remove(key);
                    }
                }

                Map<String, Object> sampleTask = taskList.get(0);

                for(String key : sampleTask.keySet()){
                    if(key.endsWith("Metrics")){
                        metricsKeyBuilder(keyBuilder, key, sampleTask);
                    } else {
                        keyBuilder.append(key + ",");
                    }
                }

                keyBuilder.deleteCharAt(keyBuilder.length() - 1);
                keyBuilder.append("\n");

                fileWriter.write(keyBuilder.toString());


                for(Map<String, Object> tasksInfo :  taskList){

                    StringBuilder valueBuilder = new StringBuilder();

                    valueBuilder.append(jobID + ",");
                    valueBuilder.append(stageID + ",");

                    for(String key : tasksInfo.keySet()){
                        if(key.endsWith("Metrics")){
                            metricsValueBuilder(valueBuilder, key,tasksInfo);
                        } else {
                            valueBuilder.append(convertObjectToString(tasksInfo.get(key)) + ",");
                        }
                    }
                    valueBuilder.deleteCharAt(valueBuilder.length() - 1);
                    valueBuilder.append("\n");
                    fileWriter.write(valueBuilder.toString());
                }

                fileWriter.close();

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private void metricsKeyBuilder(StringBuilder keyBuilder, String key, Map<String, Object> infoMap){
        Map<String, Object> metrics = (Map<String, Object>)infoMap.get(key);
        for(String metricsKey : metrics.keySet()){
            if(metricsKey.endsWith("Metrics")){
                metricsKeyBuilder(keyBuilder, metricsKey,metrics);
            } else {
                keyBuilder.append(metricsKey + ",");
            }

        }
    }

    private void metricsValueBuilder(StringBuilder valueBuilder, String key, Map<String, Object> infoMap){
        Map<String, Object> metrics = (Map<String, Object>)infoMap.get(key);
        for(String metricsKey : metrics.keySet()){
            if(metricsKey.endsWith("Metrics")){
                metricsValueBuilder(valueBuilder, metricsKey,metrics);
            } else {
                valueBuilder.append(convertObjectToString(metrics.get(metricsKey)) + ",");
            }

        }
    }

    private void convertExecutorsToCSV(String appID,
                                       List<Map<String, Object>> executorsList,
                                       long timeStamp){
        createTimeStampDIR(appID, timeStamp);

        if(executorsList.size() == 0){
            log.warn("jobsList size is 0");
        } else {

            executorsList = removeUncompletedMap(executorsList);

            for(Map<String, Object> jobInfo : executorsList){
                for(String key : executorsExcludeSet){
                    jobInfo.remove(key);
                }
            }

            File csvFile = new File(CSV_PATH + appID + appTimeStamp + "/"
                    + timeStamp + "/executorsInfo.csv");

            try {
                FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(csvFile, Charset.forName("UTF-8"));

                StringBuilder keyBuilder = new StringBuilder();
                for(String key : executorsList.get(0).keySet()){
                    keyBuilder.append(key + ",");
                }

                keyBuilder.deleteCharAt(keyBuilder.length() - 1);
                keyBuilder.append("\n");

                fileWriter.write(keyBuilder.toString());


                for(Map<String, Object> executorInfo : executorsList){
                    StringBuilder valueBuilder = new StringBuilder();

                    for(String key : executorInfo.keySet()){
                        valueBuilder.append(convertObjectToString(executorInfo.get(key)) + ",");
                    }
                    valueBuilder.deleteCharAt(valueBuilder.length() - 1);
                    valueBuilder.append("\n");
                    fileWriter.write(valueBuilder.toString());
                }

                fileWriter.close();

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private void generateConfigCSV(String appID,
                                   Map<String, String> changedConfigMap,
                                   long timeStamp){

        createTimeStampDIR(appID, timeStamp);

        File csvFile = new File(CSV_PATH + appID + appTimeStamp + "/"
                + timeStamp + "/config.csv");

        try {
            FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(csvFile, Charset.forName("UTF-8"));

            StringBuilder keyBuilder = new StringBuilder();
            for(String key : changedConfigMap.keySet()){
                keyBuilder.append(key + ",");
            }

            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
            keyBuilder.append("\n");

            fileWriter.write(keyBuilder.toString());


            StringBuilder valueBuilder = new StringBuilder();

            for(String key : changedConfigMap.keySet()){
                valueBuilder.append(convertObjectToString(changedConfigMap.get(key)) + ",");
            }
            valueBuilder.deleteCharAt(valueBuilder.length() - 1);
            valueBuilder.append("\n");
            fileWriter.write(valueBuilder.toString());


            fileWriter.close();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    private void createAppDIR(String appID){
        File appDIR = new File(CSV_PATH + appID);
        if(!appDIR.exists()){
            appDIR.mkdirs();
        }
    }

    private void createTimeStampDIR(String appID,
                                   long timeStamp){
        File timeStampDIR = new File(CSV_PATH + appID + appTimeStamp + "/" + timeStamp);
        if(!timeStampDIR.exists()){
            timeStampDIR.mkdirs();
        }
    }

    private String convertObjectToString(Object o){
        if(o instanceof  Double){
            return  BigDecimal.valueOf(((Double) o).intValue()).toString();
        } else {
            return o.toString();
        }
    }


    private List<Map<String, Object>>
        removeUncompletedMap(List<Map<String, Object>> originalMapList){

        int maxMapSize = -1;

        for(Map<String, Object> map : originalMapList){
            if(computeKeyNum(map) > maxMapSize){
                maxMapSize = computeKeyNum(map);
            }
        }

        List<Map<String, Object>> completedMapList = new ArrayList<Map<String, Object>>();

        for(Map<String, Object> map : originalMapList){
            if(computeKeyNum(map) == maxMapSize){
                completedMapList.add(map);
            } else {
                log.info("Remove uncompleted map");
            }
        }

        log.info("originalMapList size: " + originalMapList.size());
        log.info("completedMapList size: " + completedMapList.size());
        return completedMapList;
    }

    private int computeKeyNum(Map<String, Object> map){
        int total = 0;
        for(String key : map.keySet()){
            if(key.endsWith("Metrics")){
                Map<String, Object> metrics = (Map<String, Object>)map.get(key);
                total += computeKeyNum(metrics);
            } else {
                total++;
            }
        }

        return total;
    }

//    private String converDoubleToString(Double d){
//        return String.valueOf(d.intValue());
//    }

}
