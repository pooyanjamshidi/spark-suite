package uk.ac.ic.spark.monitor.exceptions;


public class PropertyNotDefinedExceptions extends Exception{

    private static String PropertyNotDefinedExceptionsMsg = "Specified property do not define in Spark";

    public PropertyNotDefinedExceptions() { super(PropertyNotDefinedExceptionsMsg); }
    public PropertyNotDefinedExceptions(String message) { super(message); }
}
