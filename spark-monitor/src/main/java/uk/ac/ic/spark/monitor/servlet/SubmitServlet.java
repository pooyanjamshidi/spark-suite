package uk.ac.ic.spark.monitor.servlet;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;
import uk.ac.ic.spark.monitor.main.InstantMain;
import uk.ac.ic.spark.monitor.util.SparkRequester;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class SubmitServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(HttpServlet.class);
    private final static Logger LOGGER =
            LogManager.getLogger(SubmitServlet.class.getCanonicalName());


    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
            IOException {


        // get the args pass to main class
        //String args[] = request.getParameterValues("argsList");
        String arg = request.getParameter("arg");
        // get main class
        String mainClass = request.getParameter("main_class");
        // get the JAR file
        Part jarPart = request.getPart("file");
        String fileName = getSubmittedFileName(jarPart);

        OutputStream out = null;
        InputStream jarContent = null;
        final PrintWriter writer = response.getWriter();
        //create path to save the file
        String path = "/Users/Qiu/spark-suite";

        try {
            out = new FileOutputStream(new File(path + File.separator
                    + fileName));
            jarContent = jarPart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = jarContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            writer.println("New file " + fileName + " created at " + path);
           // LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
           //         new Object[]{fileName, path});
        } catch (FileNotFoundException fne) {
            writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());

           // LOGGER. log(Level.SEVERE, "Problems during file upload. Error: {0}",
           //         new Object[]{fne.getMessage()});
        } finally {
            if (out != null) {
                out.close();
            }
            if (jarContent != null) {
                jarContent.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
        //get content of JAR file
        //give the local jar path to the JOB

        String[] args = new String[]{
                //the name of the application
                "--name",
                "submitJob",

                //memory for driver
                "--driver-memory",
                "1000M",

                //path to the application's JAR file
                //Get the JAR from the front-end
                "--jar",
                //TODO: wordCount JAR as a test
                //"/Users/Qiu/Documents/workspace/sparkSubmitJob/out/artifacts/sparkSubmitJob_jar/sparkSubmitJob_jar.jar",
                path,

                // name of your application's main class
                // TODO: according to the front-end
                // hard code here
                "--class",
                //"wordcount.JavaWordCount",
                mainClass,

                //"user already add the dependency into JAR"
                //"--addJAR"

                //argument of the program
                //TODO: get arg from front-end, hard code here
                "--arg",
                //path of the file here
                //"/Users/Qiu/Documents/workspace/sparkSubmitJob/src/input.txt",
                arg,


        };

        // create a Hadoop Configuration object
        Configuration config = new Configuration();
        config.set("fs.defaultFS", "hdfs://146.169.46.48:9000");// namenode
        config.set("mapreduce.framework.name","yarn"); // yarn
        config.set("yarn.resourcemanager.address","146.169.46.48:8032"); // resourcemanager
        config.set("yarn.resourcemanager.scheduler.address", "146.169.46.48:8030");
        config.set("mapreduce.jobhistory.address","node101:10020");

        // identify that you will be using Spark as YARN mode
        System.setProperty("SPARK_YARN_MODE", "true");

        // create an instance of SparkConf object
        // spark configuration
        SparkConf sparkConf = new SparkConf();

        // create ClientArguments, which will be passed to Client
        ClientArguments cArgs = new ClientArguments(args, sparkConf);


        // create an instance of yarn Client client
        Client client = new Client(cArgs, config, sparkConf);

        // submit Spark job to YARN
        client.run();


    }


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
            IOException {

        String paramsAndValues = request.getParameter("param");
        String jar = request.getParameter("jar");

        log.info("param: " + paramsAndValues);
        log.info("jar: " + jar);

        Multimap<String, String> paramsMultiMap = InstantMain.parseParams(Splitter.on(";")
                .trimResults()
                .trimResults(CharMatcher.is('['))
                .trimResults(CharMatcher.is(']'))
                .split(paramsAndValues)
                .iterator());

        log.info("parsed params List: " + paramsMultiMap);
        List<Map<String, String>> combinedParams = InstantMain.combineAllParams(paramsMultiMap);

        log.info("combined params List: " + combinedParams);

        log.info("jar file Path: " + jar);

        List<JobInfo> responseMap = new ArrayList<JobInfo>();

        int i = 1;
        for(Map<String, String> paramsMap : combinedParams) {
//            Map<String, Object> jobMap = new HashMap<String, Object>();
//            jobMap.put("no:", i);
//            jobMap.put("paramsMap:", paramsMap);
//            jobMap.put("applicaitionID:", "application_1446044705002_0009");
            JobInfo jobInfo = new JobInfo(i, paramsMap, "application_1446044705002_0009", jar);
            responseMap.add(jobInfo);
            log.info("Change config params map: " + paramsMap);

            log.info("Submit Spark Job: Fake Now");
            log.info("Get application ID: application_1446044705002_0009");
//                log.info("");


            SparkRequester sparkRequester = new SparkRequester();

//            log.info("JobInfo: " + sparkRequester.getJobsList("application_1446044705002_0009"));
            i++;
        }
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(responseMap));
    }
}
