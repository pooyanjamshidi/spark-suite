
package uk.ac.ic.spark.monitor.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConstantConfig {
    public static String SPARK_BIN_DIR;
    public static String SPARK_CHECK_URL;
    public static String SPARK_HISTORY_URL;
    public static String SPARK_ENV_SH;
    public static String SPARK_ENV_SH_COPY;
    public static String SPARK_CONF;
    public static String SPARK_CONF_COPY;
    public static String SPARK_LOG4J;
    public static String SPARK_LOG4J_COPY;
    public static String SPARK_SLAVES;
    public static String SPARK_SLAVES_COPY;
    public static String SPARK_CONFIG_PARAMS;
    public static String SPARK_FAIR_SCHEDULER;
    public static String SPARK_FAIR_SCHEDULER_COPY;
    public static String SPARK_MATRICS;
    public static String SPARK_MATRICS_COPY;
    public static String SPARK_USER_JAR_PATH;
    public static String SPARK_USER_CSV_PATH;

//    public ConstantConfig() {
//    }

//    public static void main(String[] args) throws ConfigurationException {
//        new PropertiesConfiguration("./conf/spark-suite.properties");
//    }

    public static void init() throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration("./conf/spark-suite.properties");
        SPARK_BIN_DIR = config.getString("spark.bin.dir");
        SPARK_CHECK_URL = config.getString("spark.app.check.url");
        SPARK_HISTORY_URL = config.getString("spark.app.history.url");
        SPARK_ENV_SH = config.getString("spark.env.sh");
        SPARK_ENV_SH_COPY = config.getString("spark.env.sh.copy");
        SPARK_CONF = config.getString("spark.conf.sh");
        SPARK_CONF_COPY = config.getString("spark.conf.copy");
        SPARK_LOG4J = config.getString("spark.log4j");
        SPARK_LOG4J_COPY = config.getString("spark.log4j.copy");
        SPARK_SLAVES = config.getString("spark.slaves");
        SPARK_SLAVES_COPY = config.getString("spark.slaves.copy");
        SPARK_FAIR_SCHEDULER = config.getString("spark.fair.scheduler");
        SPARK_FAIR_SCHEDULER_COPY = config.getString("spark.fair.scheduler.copy");
        SPARK_MATRICS = config.getString("spark.matrics");
        SPARK_MATRICS_COPY = config.getString("spark.matrics.copy");
        SPARK_CONFIG_PARAMS = config.getString("spark.config.params");
        SPARK_USER_JAR_PATH = config.getString("spark.user.jar.path");
        SPARK_USER_CSV_PATH = config.getString("spark.user.csv.path");
    }
}
