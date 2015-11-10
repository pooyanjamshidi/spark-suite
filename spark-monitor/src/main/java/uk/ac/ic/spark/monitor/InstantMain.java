package uk.ac.ic.spark.monitor;

import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.*;
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


            Multimap<String, String> paramsMultiMap = parseParams(line.getOptionValues("P"));

            log.info("params List: " + paramsMultiMap);

            List<Map<String, String>> combinedParams = combineAllParams(paramsMultiMap);

            log.info("combinedParams List: " + combinedParams);

            for(Map<String, String> paramsMap : combinedParams){
                //TODO:: change configfile and submit job.
            }

        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "sprak-suite", options );
        }
    }

    private static void initOptions(){

        Option opt = Option.builder("P").hasArgs()
                .longOpt("params")
                .required()
                .argName("p1=v11,v12;p2=v21,v22")
                .desc("params list")
                .valueSeparator(';')
                .build();
        options.addOption(opt);

    }

    private static Multimap<String, String>  parseParams(String[] paramNamesAndValues){
        Multimap<String, String> paramsMultiMap = HashMultimap.create();

        if(paramNamesAndValues.length >= 2){
            paramsMultiMap.putAll(paramNamesAndValues[0],
                Splitter.on(",").trimResults().split(paramNamesAndValues[1]));

            for(int i = 2; i < paramNamesAndValues.length; i++){

                Iterator<String> paramNamesAndValue = Splitter.on("=").
                        trimResults().split(paramNamesAndValues[i]).iterator();

                paramsMultiMap.putAll(paramNamesAndValue.next(),
                        Splitter.on(",").trimResults().split(paramNamesAndValue.next()));
            }

        } else {
            throw new RuntimeException("There should have at least one param");
        }
        return paramsMultiMap;
    }


    private static List<Map<String, String>> combineAllParams(Multimap<String, String> paramsMultiMap){
        List<Map<String, String>> combinedParams = new ArrayList<Map<String, String>>();

        for(String paramName : paramsMultiMap.keySet()){
            combinedParams = combineAnotherParams(paramName, paramsMultiMap.get(paramName),
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
