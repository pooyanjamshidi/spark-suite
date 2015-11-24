package uk.ac.ic.spark.monitor.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SparkRequester {

    public final static void main(String[] args) throws IOException {
        try{
            System.out.println(getJobsList("http://spark1.haoran.io:18080/api/v1/applications/",
                    "application_1446044705002_0009"));
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    private static List<Map<String, Object>> getJobsList(String url, String appID) throws IOException {
        String jobListString = url + appID + "/jobs";
        String body = requestToSpark(jobListString);
//        System.out.println(body);
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        Gson gson = new Gson();
        List<Map<String, Object>> jobList = gson.fromJson(body, type);


        return jobList;
    }


    private static String requestToSpark(String url) throws IOException {
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
