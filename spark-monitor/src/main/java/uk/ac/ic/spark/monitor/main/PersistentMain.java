package uk.ac.ic.spark.monitor.main;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ic.spark.monitor.services.ClusterMonitorService;
import uk.ac.ic.spark.monitor.services.ConfigRestService;
import uk.ac.ic.spark.monitor.services.FigureDisplayService;
import uk.ac.ic.spark.monitor.services.TasksMonitorService;

import java.util.Set;



//This class is not used now.
public class PersistentMain {

    private static final Logger log = LogManager.getLogger(PersistentMain.class);

    private static final Set<Service> services =
            ImmutableSet.<Service>builder()
                    .add(new ClusterMonitorService())
                    .add(new ConfigRestService())
                    .add(new FigureDisplayService())
                    .add(new TasksMonitorService())
                    .build();

    public static void main(final String[] args) {

        log.info("I'm main and I'm starting");

        ServiceManager serviceManager = new ServiceManager(services);
        serviceManager.startAsync();
    }

}
