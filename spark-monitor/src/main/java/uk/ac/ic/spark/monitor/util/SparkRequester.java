package uk.ac.ic.spark.monitor.util;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import uk.ac.ic.spark.monitor.config.ConstantConfig;

import java.lang.reflect.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class SparkRequester {

//    private static final String url = "http://localhost:4040/api/v1/applications/";
//    private static final String url = "http://spark3.haoran.io:4040/api/v1/applications/";
    private String url = null;
    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {

                public JsonElement serialize(Double originalValue, Type typeOf, JsonSerializationContext context) {
                    BigDecimal bigValue = BigDecimal.valueOf(originalValue);

                    return new JsonPrimitive(bigValue.toPlainString());
                }
            });


    public SparkRequester(){
        url = ConstantConfig.SPARK_CHECK_URL;
    }

    public void setToHistory(){
        url = ConstantConfig.SPARK_HISTORY_URL;
    }




    public final static void main(String[] args) throws IOException {
        try{
            SparkRequester sparkRequester = new SparkRequester();
            System.out.println(sparkRequester.getAppsList());
        } catch (Exception e){
            e.printStackTrace();
        }

    }




    public List<Map<String, Object>> getAppsList() throws IOException {
        String appListString = url ;
        String body = requestToSpark(appListString);
//        System.out.println(body);
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        Gson gson = new Gson();
        List<Map<String, Object>> jobList = gson.fromJson(body, type);


        return jobList;
    }


    public Map<String, Object> getAppInfo(String appID) throws IOException {

        String jobListString = url + appID;
        String body = requestToSpark(jobListString);
//        System.out.println(body);
        Type type = new TypeToken<Map<String,Object>>(){}.getType();
        Gson gson = gsonBuilder.create();
        Map<String, Object> appMap = gson.fromJson(body, type);

        return appMap;
    }


    public Map<Integer, List<Map<String, Object>>> getTasksMap(String appID, Set<Integer> stageIdSet) throws IOException {

        Map<Integer, List<Map<String, Object>>> tasksMap = new HashMap<Integer, List<Map<String, Object>>>();

        for(Integer stageID : stageIdSet){
            String tasksListString = url + appID + "/stages/" + stageID;
            String body = requestToSpark(tasksListString);
//        System.out.println(body);
            Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
            Gson gson = gsonBuilder.create();
            List<Map<String, Object>> stageAttemptsList = gson.fromJson(body, type);
            Map<String, Object> stageMap = stageAttemptsList.get(0);
            Map<String, Object> tasksInfoMap = (Map<String, Object>) stageMap.get("tasks");
            List<Map<String, Object>> tasksList = new ArrayList<Map<String, Object>>();

            for(String key : tasksInfoMap.keySet()){
                tasksList.add((Map<String, Object>) tasksInfoMap.get(key));
            }

            tasksMap.put(stageID, tasksList);
        }



        return tasksMap;
    }

    public List<Map<String, Object>> getJobsList(String appID) throws IOException {
        String jobListString = url + appID + "/jobs";
        String body = requestToSpark(jobListString);
//        System.out.println(body);
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        Gson gson = gsonBuilder.create();
        List<Map<String, Object>> jobList = gson.fromJson(body, type);

        return jobList;
    }


    public List<Map<String, Object>> getExecutorsList(String appID) throws IOException {
        String jobListString = url + appID + "/executors";
        String body = requestToSpark(jobListString);
//        System.out.println(body);
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();


        Gson gson = gsonBuilder.create();
        List<Map<String, Object>> executorsList = gson.fromJson(body, type);
        System.out.println("executorsList: " + executorsList);
        return executorsList;
    }


    public List<Map<String, Object>> getStagesList(String appID) throws IOException {
        String jobListString = url + appID + "/stages";
        String body = requestToSpark(jobListString);
//        System.out.println(body);
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        Gson gson = gsonBuilder.create();
        List<Map<String, Object>> jobList = gson.fromJson(body, type);

        return jobList;
    }

    private String requestToSpark(String url) throws IOException {
        String responseBody;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            responseBody = httpclient.execute(httpget, responseHandler);
        } finally {
            httpclient.close();
        }

        return  responseBody;
    }



}
