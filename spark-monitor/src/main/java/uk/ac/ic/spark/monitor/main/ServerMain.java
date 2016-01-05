package uk.ac.ic.spark.monitor.main;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import uk.ac.ic.spark.monitor.config.ConstantConfig;
import uk.ac.ic.spark.monitor.servlet.CSVServlet;
import uk.ac.ic.spark.monitor.servlet.SubmitDebugServlet;
import uk.ac.ic.spark.monitor.servlet.SubmitServlet;

public class ServerMain {



    public static void main(String[] args) throws Exception {


        ConstantConfig.init();

        // Create a basic jetty server object that will listen on port 8080.
        // Note that if you set this to port 0 then a randomly available port
        // will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
        Server server = new Server(8080);

        // The ServletHandler is a dead simple way to create a context handler
        // that is backed by an instance of a Servlet.
        // This handler then needs to be registered with the Server object.
        ServletHandler servlet_handler = new ServletHandler();
//        server.setHandler(servlet_handler);

        // Passing in the class for the Servlet allows jetty to instantiate an
        // instance of that Servlet and mount it on a given context path.

        // IMPORTANT:
        // This is a raw Servlet, not a Servlet that has been configured
        // through a web.xml @WebServlet annotation, or anything similar.
        servlet_handler.addServletWithMapping(SubmitServlet.class, "/submit");
        servlet_handler.addServletWithMapping(CSVServlet.class, "/csv");
        servlet_handler.addServletWithMapping(SubmitDebugServlet.class, "/submit-debug");

        // Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
        // a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
        ResourceHandler resource_handler = new ResourceHandler();
        // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        resource_handler.setResourceBase("./web/");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, servlet_handler, new DefaultHandler() });
        server.setHandler(handlers);


        // Start things up!
        server.start();

        // The use of server.join() the will make the current thread join and
        // wait until the server is done executing.
        // See
        // http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
        server.join();
    }


}