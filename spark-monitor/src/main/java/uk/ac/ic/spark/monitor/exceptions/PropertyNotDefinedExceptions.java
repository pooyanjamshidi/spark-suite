package uk.ac.ic.spark.monitor.exceptions;


public class PropertyNotDefinedExceptions extends Exception{

//    private static String PropertyNotDefinedExceptionsMsg = ;

//    public PropertyNotDefinedExceptions() { }
//    public PropertyNotDefinedExceptions(String message) { super(message); }


    public PropertyNotDefinedExceptions(String propertyName){
        super("Specified property: " + propertyName + " do not define in Spark");

    }
}
