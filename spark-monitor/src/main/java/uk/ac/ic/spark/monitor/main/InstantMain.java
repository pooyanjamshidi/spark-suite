package uk.ac.ic.spark.monitor.main;

import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ic.spark.monitor.config.ConstantConfig;
import uk.ac.ic.spark.monitor.file.ChangeParameter;
import uk.ac.ic.spark.monitor.file.FileUtil;
import uk.ac.ic.spark.monitor.spark.SparkExec;

import java.io.IOException;
import java.util.*;

public class InstantMain {

    private static final Logger log = LogManager.getLogger(InstantMain.class);
    private static final Options options = new Options();

    public static void main(final String[] args) throws ConfigurationException, IOException {

        initOptions();

        CommandLineParser parser = new DefaultParser();

        try {

            CommandLine line = parser.parse( options, args);

            // parse the command line arguments
            log.info("Get all args: " + Arrays.toString(args));

            log.info("params: " + line.getOptionValue("P"));

            log.info("polling time: " + line.getOptionValue("PT"));

            log.info("check times: " + line.getOptionValue("CT"));

            log.info("jar file path: " + line.getOptionValue("J"));

            log.info("main class name: " + line.getOptionValue("C"));

            log.info("args for spark application: " + line.getOptionValue("A"));

            log.info("the index of args which ends with timestamp: " + line.getOptionValue("TE"));

            ConstantConfig.init();

            String paramsAndValues = line.getOptionValue("P");

            Validate.matchesPattern(paramsAndValues, "(.*=(.*,)*.*;)*.*=(.*,)*.*",
                    paramsAndValues + " does not look like p1=v11,v12;p2=v21,v22");


            Multimap<String, String> paramsMultiMap = parseParams(Splitter.on(";")
                    .trimResults()
//                    .trimResults(CharMatcher.is('['))
//                    .trimResults(CharMatcher.is(']'))
                    .split(paramsAndValues)
                    .iterator());

            log.info("parsed params List: " + paramsMultiMap);
            List<Map<String, String>> combinedParams = combineAllParams(paramsMultiMap);

            log.info("combined params List: " + combinedParams);

            String jarPath = line.getOptionValue("J");
            String className = line.getOptionValue("C");

            List<String> argsList;

            if(line.getOptionValue("A") == null){
                argsList = new ArrayList<String>();
            } else {
                argsList = Splitter.on(",").trimResults().splitToList(line.getOptionValue("A"));
            }

            Set<String> timeEndsIndex;

            if(line.getOptionValue("TE") == null){
                timeEndsIndex = new HashSet<String>();
            } else {
                timeEndsIndex = new HashSet<String>(Splitter.on(",").trimResults().splitToList(line.getOptionValue("TE")));
            }



            int pollingTime = Integer.valueOf(line.getOptionValue("PT"));
            int checkTimes = Integer.valueOf(line.getOptionValue("CT"));


            for(Map<String, String> paramsMap : combinedParams){

                FileUtil.backUpAllConfigFiles();

                log.info("Change config params map: " + paramsMap);
                ChangeParameter changeParameter = new ChangeParameter();
                changeParameter.modifyConfig(paramsMap);
//                log.info("Submit Spark Job: Fake Now");
//                log.info("Get application ID: application_1446044705002_0009");

                SparkExec sparkExec = new SparkExec(paramsMap);
                sparkExec.submitSparkApp(jarPath, className,
                        pollingTime, checkTimes,
                        argsList, timeEndsIndex);

                FileUtil.restoreAllConfigFiles();
//                SparkRequester sparkRequester = new SparkRequester();
//
//                log.info("JobInfo: " + sparkRequester.getJobsList("application_1446044705002_0009"));
            }

        } catch( ParseException exp ) {
            // oops, something went wrong
            String errorMessage = "Parsing failed. Reason: " + exp.getMessage();

            log.error(errorMessage );
            log.error(exp.getMessage(), exp);
            printHelp();
        } catch (IllegalArgumentException iae){
            String errorMessage =  "Illegal argument param: " + iae.getMessage();
            log.error(errorMessage);
            log.error(iae.getMessage(), iae);
            printHelp();
        }
    }

    private static void printHelp(){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "sprak-suite", options );
    }

    private static void initOptions(){

        Option paramsOpt = Option.builder("P").hasArgs()
                .longOpt("params")
                .required()
                .argName("p1=v11,v12;p2=v21,v22...")
                .hasArg()
                .desc("params list for spark config")
                .build();


        Option jarOpt = Option.builder("J").hasArgs()
                .longOpt("jar")
                .argName("jar path")
                .required()
                .hasArg()
                .desc("path of jar file for spark application")
                .build();

        Option classOpt = Option.builder("C").hasArgs()
                .longOpt("class")
                .argName("main class name")
                .required()
                .hasArg()
                .desc("main class for spark application")
                .build();


        Option pollingTimeOpt = Option.builder("PT").hasArgs()
                .longOpt("pollingTime")
                .argName("polling time")
                .required()
                .hasArg()
                .desc("polling time for check application")
                .build();


        Option monitorTimeOpt = Option.builder("CT").hasArgs()
                .longOpt("checkTimes")
                .argName("check times")
                .required()
                .hasArg()
                .desc("total check times")
                .build();

        Option argsOpt = Option.builder("A").hasArgs()
                .longOpt("args")
                .argName("arg1,arg2,arg3...")
                .hasArg()
                .desc("args for spark application")
                .build();

        Option timeEndsIndexOpt = Option.builder("TE").hasArgs()
                .longOpt("timeEndsArgsIndex")
                .argName("index1,index2,index3...")
                .hasArg()
                .desc("the index of args which ends with timestamp")
                .build();

        options.addOption(paramsOpt);
        options.addOption(jarOpt);
        options.addOption(classOpt);
        options.addOption(pollingTimeOpt);
        options.addOption(monitorTimeOpt);
        options.addOption(argsOpt);
        options.addOption(timeEndsIndexOpt);

    }




    public static Multimap<String, String>  parseParams(Iterator<String> paramNamesAndValuesIterator){

        Multimap<String, String> paramsMultiMap = HashMultimap.create();

        if(paramNamesAndValuesIterator.hasNext()){

            while (paramNamesAndValuesIterator.hasNext()){
                String paramNamesAndValue = paramNamesAndValuesIterator.next();
                Iterator<String> paramNamesAndValueIterator = Splitter.on("=").
                        trimResults().split(paramNamesAndValue).iterator();
                paramsMultiMap.putAll(paramNamesAndValueIterator.next(),
                        Splitter.on(",").trimResults().split(paramNamesAndValueIterator.next()));
            }

        } else {
            throw new RuntimeException("There should have at least one param");
        }
        return paramsMultiMap;
    }


    public static List<Map<String, String>> combineAllParams(Multimap<String, String> paramsMultiMap){
        List<Map<String, String>> combinedParams = new ArrayList<Map<String, String>>();

        for(String paramName : paramsMultiMap.keySet()){
            combinedParams = combineAnotherParams(paramName,
                    paramsMultiMap.get(paramName),
                    combinedParams);
        }

        return combinedParams;
    }


    private static List<Map<String, String>> combineAnotherParams(final String paramName,
                                                                  Collection<String> paramValues,
                                                                  List<Map<String, String>> combinedParams){

        List<Map<String, String>> newCombinedParams = new ArrayList<Map<String, String>>();


        if(combinedParams.size() != 0 && paramValues.size() != 0){
            for (Map<String, String> paramMap : combinedParams){
                for(final String value : paramValues){
                    newCombinedParams.add(new HashMap(paramMap){{ put(paramName, value);}});
                }
            }
        }

        if(combinedParams.size() == 0){
            for(final String value : paramValues){
                newCombinedParams.add(new HashMap(){{ put(paramName, value);}});
            }
        }

        if(paramValues.size() == 0){
            newCombinedParams = combinedParams;
        }

        return newCombinedParams;
    }




}
