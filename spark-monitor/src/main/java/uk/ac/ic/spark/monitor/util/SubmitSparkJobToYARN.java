// import required classes and interfaces
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;
import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;




public class SubmitSparkJobToYARN {

    public static void main(String[] arguments) throws Exception {
        // prepare arguments to be passed to
        // org.apache.spark.deploy.yarn.Client object
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
                "/Users/Qiu/Documents/workspace/sparkSubmitJob/out/artifacts/sparkSubmitJob_jar/sparkSubmitJob_jar.jar",

                // name of your application's main class
                // TODO: according to the front-end
                // hard code here
                "--class",
                "wordcount.JavaWordCount",

                //"user already add the dependency into JAR"
                //"--arg"

                //argument of the program
                //TODO: get arg from front-end, hard code here
                "--arg",
                //path of the file here
                "/Users/Qiu/Documents/workspace/sparkSubmitJob/src/input.txt",


        };

        // create a Hadoop Configuration object
        Configuration config = new Configuration();

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
}
