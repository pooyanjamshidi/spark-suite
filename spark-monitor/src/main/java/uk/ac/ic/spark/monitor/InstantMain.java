package uk.ac.ic.spark.monitor;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class InstantMain {

    private static final Logger log = LogManager.getLogger(InstantMain.class);
    private static final Options options = new Options();

    public static void main(final String[] args) {

        initOptions();

        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            log.info("Get args: " + Arrays.toString(args));

            CommandLine line = parser.parse( options, args);

            log.info("params: " + line.getOptionValue("P"));

            String paramsAndValues = line.getOptionValue("P");

            Validate.matchesPattern(paramsAndValues, "\\[(\\w*=(\\w*,)*\\w*;)*\\w*=(\\w*,)*\\w*\\]",
                    paramsAndValues + " does not look like [p1=v11,v12;p2=v21,v22]");


            Multimap<String, String> paramsMultiMap = parseParams(Splitter.on(";")
                    .trimResults(CharMatcher.is('['))
                    .trimResults(CharMatcher.is(']'))
                    .split(paramsAndValues)
                    .iterator());

            log.info("parsed params List: " + paramsMultiMap);
            List<Map<String, String>> combinedParams = combineAllParams(paramsMultiMap);

            log.info("combined params List: " + combinedParams);

            log.info("jar file Path: " + line.getOptionValue("J"));

            for(Map<String, String> paramsMap : combinedParams){
                //TODO:: change configfile and submit job.
            }

        } catch( ParseException exp ) {
            // oops, something went wrong
            String errorMessage = "Parsing failed. Reason: " + exp.getMessage();
            System.err.println(errorMessage);
            log.error(errorMessage );
            printHelp();
        } catch (IllegalArgumentException iae){
            String errorMessage =  "Illegal argument param: " + iae.getMessage();
            System.err.println(errorMessage);
            log.error(errorMessage);
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
                .argName("[p1=v11,v12;p2=v21,v22]")
                .hasArg()
                .desc("params list")
                .build();


        Option jarOpt = Option.builder("J").hasArgs()
                .longOpt("jar")
                .required()
                .hasArg()
                .desc("path of jar file")
                .build();

        options.addOption(paramsOpt);
        options.addOption(jarOpt);

    }




    private static Multimap<String, String>  parseParams(Iterator<String> paramNamesAndValuesIterator){

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


    private static List<Map<String, String>> combineAllParams(Multimap<String, String> paramsMultiMap){
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
