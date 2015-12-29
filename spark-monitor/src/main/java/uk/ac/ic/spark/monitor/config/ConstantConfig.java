package uk.ac.ic.spark.monitor.config;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConstantConfig {

    public static String SPARK_CONF_DIR;
    public static String SPARK_BIN_DIR;
    public static String SPARK_CHEKC_URL;
    public static String SPARK_ENV_SH_TEMPLATE = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/spark-env.sh.template";
    public static String SPARK_ENV_SH_TEMPLATE_COPY = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/spark-env.sh.template_copy";
    public static String SPARK_DEFAULTS_CONF = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/spark-defaults.conf.template";
    public static String SPARK_DEFAULTS_CONF_COPY = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/spark-defaults.conf.template_copy";
    public static String SPARK_LOG4J = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/log4j.properties.template";
    public static String SPARK_LOG4J_COPY = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/log4j.properties.template_copy";
    public static String SPARK_SLAVES = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/slaves.template";
    public static String SPARK_SLAVES_COPY = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/slaves.template_copy";
    public static String SPARK_CONFIG = "/Users/Qiu/Documents/workspace/ModifyParameter/src/ConfigurationParameter.txt";
    public static String SPARK_FAIR_SCHEDULER = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/fairscheduler.xml.template";
    public static String SPARK_FAIR_SCHEDULER_COPY = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/fairscheduler.xml.template_copy";
    public static String SPARK_MATRICS = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/metrics.properties.template";
    public static String SPARK_MATRICS_COPY = "/Users/Qiu/Documents/workspace/ModifyParameter/src/conf/metrics.properties.template_copy";


    public static void main(String[] args) throws ConfigurationException {
        Configuration config = new PropertiesConfiguration("./conf/spark-suite.properties");
        SPARK_CONF_DIR = config.getString("spark.conf.dir");
//        System.out.println("SPARK_CONF_DIR: " + SPARK_CONF_DIR);
    }

    public static void init()throws ConfigurationException{
        Configuration config = new PropertiesConfiguration("./conf/spark-suite.properties");
        SPARK_BIN_DIR = config.getString("spark.bin.dir");
        SPARK_CHEKC_URL = config.getString("spark.app.check.url");
    }
}
