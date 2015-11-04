package uk.ac.ic.spark.monitor.services;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigRestService extends AbstractExecutionThreadService {

    private static final Logger log = LogManager.getLogger(ConfigRestService.class);

    @Override
    protected void run() throws Exception {

        while (true) {
            log.info("I'm ConfigRestService and I'm running");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

    }
}
