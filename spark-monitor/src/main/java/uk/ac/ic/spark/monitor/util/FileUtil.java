package uk.ac.ic.spark.monitor.util;

import uk.ac.ic.spark.monitor.config.ConstantConfig;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class FileUtil {

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

    public static String getFileName(Part part) {
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


    public void backUpAllConfigFiles() throws IOException {
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
    }


    public void restoreAllConfigFiles(){
        File fairscheduler = new File(ConstantConfig.SPARK_FAIR_SCHEDULER);
        File fairschedulerCopy = new File(ConstantConfig.SPARK_FAIR_SCHEDULER_COPY);
        fileChannelCopy(fairschedulerCopy,fairscheduler);

        File log4j = new File(ConstantConfig.SPARK_LOG4J);
        File log4jCopy = new File(ConstantConfig.SPARK_LOG4J_COPY);
        fileChannelCopy(log4jCopy,log4j);


        File metrics = new File(ConstantConfig.SPARK_MATRICS);
        File metricsCopy = new File(ConstantConfig.SPARK_MATRICS_COPY);
        fileChannelCopy(metricsCopy,metrics);

        File slaves = new File(ConstantConfig.SPARK_SLAVES);
        File slavesCopy = new File(ConstantConfig.SPARK_SLAVES_COPY);
        fileChannelCopy(slavesCopy,slaves);

        File spark_defaults = new File(ConstantConfig.SPARK_DEFAULTS_CONF);
        File spark_defaultsCopy = new File(ConstantConfig.SPARK_DEFAULTS_CONF_COPY);
        fileChannelCopy(spark_defaultsCopy,spark_defaults);

        File spark_env = new File(ConstantConfig.SPARK_ENV_SH_TEMPLATE);
        File spark_envCopy = new File(ConstantConfig.SPARK_ENV_SH_TEMPLATE_COPY);
        fileChannelCopy(spark_envCopy,spark_env);
    }

}
