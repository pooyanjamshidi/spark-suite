package uk.ac.ic.spark.monitor.util;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConstantConfig {

    public static String SPARK_CONF_DIR;
    public static String SPARK_BIN_DIR;

    public static void main(String[] args) throws ConfigurationException {
        Configuration config = new PropertiesConfiguration("./conf/spark-suite.properties");
        SPARK_CONF_DIR = config.getString("spark.conf.dir");
//        System.out.println("SPARK_CONF_DIR: " + SPARK_CONF_DIR);
    }

    public static void init()throws ConfigurationException{
        Configuration config = new PropertiesConfiguration("./conf/spark-suite.properties");
        SPARK_BIN_DIR = config.getString("spark.bin.dir");
    }
}
