package uk.ac.ic.spark.monitor.servlet;


import java.util.Map;

public class JobInfo {
    private int no;
    private Map<String,String> paramsMap;
    private String applicationID;
    private String jarPath;

    public JobInfo(){}
    public JobInfo(int no, Map<String,String> paramsMap,
                   String applicationID, String jarPath){
        this.no = no;
        this.paramsMap = paramsMap;
        this.applicationID = applicationID;
        this.jarPath = jarPath;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public int getNo() {
        return no;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }
}
