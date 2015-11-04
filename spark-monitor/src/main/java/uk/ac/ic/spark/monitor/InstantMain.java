package uk.ac.ic.spark.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class InstantMain {

    private static final Logger log = LogManager.getLogger(InstantMain.class);


    public static void main(final String[] args) {
        log.info("Get args: " + Arrays.toString(args));

    }

}
