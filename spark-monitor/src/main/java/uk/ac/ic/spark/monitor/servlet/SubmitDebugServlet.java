package uk.ac.ic.spark.monitor.servlet;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ic.spark.monitor.config.ConstantConfig;
import uk.ac.ic.spark.monitor.main.InstantMain;
import uk.ac.ic.spark.monitor.util.ChangeParameter;
import uk.ac.ic.spark.monitor.util.FileUtil;
import uk.ac.ic.spark.monitor.util.SparkExec;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

import static uk.ac.ic.spark.monitor.config.ConstantConfig.SPARK_USER_JAR_PATH;

//import uk.ac.ic.spark.monitor.util.SparkRequester;

@WebServlet(name = "SubmitServlet", urlPatterns = {"/upload"})

@MultipartConfig(fileSizeThreshold=1024*1024*10,    // 10 MB
        maxFileSize=1024*1024*50,          // 50 MB
        maxRequestSize=1024*1024*100)      // 100 MB
public class SubmitDebugServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(HttpServlet.class);
//    private final static org.apache.logging.log4j.Logger LOGGER =
//            LogManager.getLogger(SubmitServlet.class.getCanonicalName());


    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        List<String> appNameList = new ArrayList<String>();
        appNameList.add("Sort1451764230681");
        appNameList.add("Sort1451764127803");
        appNameList.add("Sort1451764033591");



        //add the multipart config
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
        request.setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

        ////////////////////////////////////////////////////////////////////////

        log.info("Receive ParameterMap: " + request.getParameterMap());


        Part jarPart = request.getPart("file");
        String fileName = FileUtil.getFileName(jarPart);
        log.info("fileName: " + fileName);

        OutputStream out = null;
        InputStream jarContent = null;
        final PrintWriter writer = response.getWriter();
        //create path to save the file
        String jarFileName  = SPARK_USER_JAR_PATH + File.separator
                + fileName;

        try {
            out = new FileOutputStream(new File(jarFileName));
            jarContent = jarPart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = jarContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
//            writer.println("New file " + fileName + " created at " + path);

        } catch (FileNotFoundException fne) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.println("You either did not specify a file to upload or are "
                    + "trying to upload a file to a protected or nonexistent "
                    + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());
            return;

            // LOGGER. log(Level.SEVERE, "Problems during file upload. Error: {0}",
            //         new Object[]{fne.getMessage()});
        } finally {
            if (out != null) {
                out.close();
            }
            if (jarContent != null) {
                jarContent.close();
            }
//            response.getWriter().write("Upload Successfully!!");
        }


        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        log.info("Return appNameList: " + appNameList);


        StringBuilder stringBuilder = new StringBuilder();
        for(String appName : appNameList){
            stringBuilder.append(appName + ",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

//        response.getWriter().println(gson.toJson(appNameList));
        response.getWriter().println(stringBuilder.toString());
//        writer.close();
        response.setStatus(HttpServletResponse.SC_OK);


    }


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
