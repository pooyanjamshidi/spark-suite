package uk.ac.ic.spark.monitor.servlet;


import java.util.ArrayList;
import java.util.Map;

public class JobInfo {
    private int no;
    private Map<String,String> paramsMap;
    private String applicationID;
    private String jarPath;
    // name of your application's main class
    private String mainClass;
    // list of the args
    private ArrayList<String> argsList;

    public JobInfo(){}
    public JobInfo(int no, Map<String,String> paramsMap,
                   String applicationID, String jarPath){
        this.no = no;
        this.paramsMap = paramsMap;
        this.applicationID = applicationID;
        this.jarPath = jarPath;
        this.argsList = new ArrayList<String>();
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

    public void setMainClass(String mainClass) { this.mainClass = mainClass; }

    public String getMainClass() { return this.mainClass; }

    public void setArgsList(String args[]){
        for(int i = 0; i < args.length; i++){
            this.argsList.add(args[i]);
        }
    }

    public ArrayList<String> getArgsList(){ return this.argsList; }

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
