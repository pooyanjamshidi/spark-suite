package uk.ac.ic.spark.monitor.config;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConstantConfig {

    public static String SPARK_CONF_DIR;
    public static String SPARK_BIN_DIR;
    public static String SPARK_CHEKC_URL;


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
