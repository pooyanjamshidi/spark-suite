package uk.ac.ic.spark.monitor.servlet;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.ac.ic.spark.monitor.Config.ConstantConfig;
import uk.ac.ic.spark.monitor.main.InstantMain;
import uk.ac.ic.spark.monitor.util.ChangeParameter;
//import uk.ac.ic.spark.monitor.util.SparkRequester;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.logging.Level;

@WebServlet(name = "SubmitServlet", urlPatterns = {"/upload"})

@MultipartConfig(fileSizeThreshold=1024*1024*10,    // 10 MB
        maxFileSize=1024*1024*50,          // 50 MB
        maxRequestSize=1024*1024*100)      // 100 MB
public class SubmitServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(HttpServlet.class);
    private final static org.apache.logging.log4j.Logger LOGGER =
            LogManager.getLogger(SubmitServlet.class.getCanonicalName());

    /**
     *
     * @param source file
     * @param target file
     */
    public void fileChannelCopy(File s, File t) {

        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);

            fo = new FileOutputStream(t);

            //get input channel
            in = fi.getChannel();

            //get output channel
            out = fo.getChannel();

            //connect two channels
            in.transferTo(0, in.size(), out);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                fi.close();

                in.close();

                fo.close();

                out.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }


    private String getFileName(Part part) {
        final String partHeader = part.getHeader("content-disposition");
        //LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }


    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException,
            IOException {

        log.info("receive!");

        //add the multipart config
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
        request.setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);


        Multimap<String, String> keyVlues = HashMultimap.create();

        //TODO: back up original configuration file
        File fairscheduler = new File(ConstantConfig.SPARK_FAIR_SCHEDULER);
        File fairschedulerCopy = new File(ConstantConfig.SPARK_FAIR_SCHEDULER_COPY);
        if(!fairschedulerCopy.exists()){
            fairscheduler.createNewFile();
        }
        fileChannelCopy(fairscheduler,fairschedulerCopy);


        File log4j = new File(ConstantConfig.SPARK_LOG4J);
        File log4jCopy = new File(ConstantConfig.SPARK_LOG4J_COPY);
        if(!log4jCopy.exists()){
            log4jCopy.createNewFile();
        }
        fileChannelCopy(log4j,log4jCopy);

        File metrics = new File(ConstantConfig.SPARK_MATRICS);
        File metricsCopy = new File(ConstantConfig.SPARK_MATRICS_COPY);
        if(!metricsCopy.exists()){
            metricsCopy.createNewFile();
        }
        fileChannelCopy(metrics,metricsCopy);

        File slaves = new File(ConstantConfig.SPARK_SLAVES);
        File slavesCopy = new File(ConstantConfig.SPARK_SLAVES_COPY);
        if(!slavesCopy.exists()){
            slavesCopy.createNewFile();
        }
        fileChannelCopy(slaves,slavesCopy);

        File spark_defaults = new File(ConstantConfig.SPARK_DEFAULTS_CONF);
        File spark_defaultsCopy = new File(ConstantConfig.SPARK_DEFAULTS_CONF_COPY);
        if(!spark_defaultsCopy.exists()){
            spark_defaultsCopy.createNewFile();
        }
        fileChannelCopy(spark_defaults,spark_defaultsCopy);

        File spark_env = new File(ConstantConfig.SPARK_ENV_SH_TEMPLATE);
        File spark_envCopy = new File(ConstantConfig.SPARK_ENV_SH_TEMPLATE_COPY);
        if(!spark_envCopy.exists()){
            spark_envCopy.createNewFile();
        }
        fileChannelCopy(spark_env,spark_envCopy);

        //traverse ConfigurationParameter.txt, get which parameter had been change
        File file = new File(ConstantConfig.SPARK_CONFIG);
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //log.info(line);
            String configParameter = request.getParameter(line);
            log.info(configParameter);

            if (configParameter == null) {
                continue;
            } else {
                //add it into the propertyList
                //key = line, configParameter = v1,v2,v3
                //split on ","
                Iterable<String> values = Splitter.on(",")
                        .trimResults()
                        .omitEmptyStrings()
                        .split(configParameter);
                //create key value pairs
                for (String s : values) {

                    keyVlues.put(line, s);
                    log.info("key before combine: " + line + " value before combine: " + s);

                }
            }
        }

        List combinekeyValues = InstantMain.combineAllParams(keyVlues);
        Map<String, String> propertyList = new HashMap<String, String>();
        log.info("Combine: " + combinekeyValues.toString());

        for (int i = 0; i < combinekeyValues.size(); i++) {
            // Each combine run once
            Object keyValues = combinekeyValues.get(i);
            String keyValuesStr = keyValues.toString();
            log.info("keyValuesStr: " + keyValuesStr);
            //get sub-string
            keyValuesStr = keyValuesStr.substring(1,keyValuesStr.length()-1);
            //split on "," "{", "}"
            //get k1 = v1, k2 = v2, k3 = v3
            Iterable<String> keysValues = Splitter.on(",")
                    .trimResults()
                    .omitEmptyStrings()
                    .trimResults(CharMatcher.is('{'))
                    .trimResults(CharMatcher.is('}'))
                    .trimResults(CharMatcher.is('['))
                    .trimResults(CharMatcher.is(']'))
                    .split(keyValuesStr);

            for (String s : keysValues) {
                //split on "="
                //add into property list
                String[] keyValue = s.split("=");
                propertyList.put(keyValue[0], keyValue[1]);
                log.info("key: " + keyValue[0] + " value: " + keyValue[1]);
            }

            //change parameter
            ChangeParameter changeParameter = new ChangeParameter();
            changeParameter.modifyConfig(propertyList);


            //TODO: submit job


            //recover from the original configuration file
            fileChannelCopy(fairschedulerCopy,fairscheduler);
            fileChannelCopy(log4jCopy,log4j);
            fileChannelCopy(metricsCopy,metrics);
            fileChannelCopy(slavesCopy,slaves);
            fileChannelCopy(spark_defaultsCopy,spark_defaults);
            fileChannelCopy(spark_envCopy,spark_env);

        }


        ////////////////////////////////////////////////////////////////////////


        String arg = request.getParameter("PARAMETERS");


        log.info("arg: " + arg);
        // get main class
        String mainClass = request.getParameter("mainClass");
        log.info("mainClass: " + mainClass);
        // get the JAR file
        Part jarPart = request.getPart("file");
        String fileName = getFileName(jarPart);
        log.info("fileName: " + fileName);
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

            response.getWriter().write("Upload Successfully!!");
        }
        //get content of JAR file
        //give the local jar path to the JOB





    /*

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
    */
    }
}
