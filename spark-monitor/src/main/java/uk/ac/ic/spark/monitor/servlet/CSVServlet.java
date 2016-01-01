package uk.ac.ic.spark.monitor.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ic.spark.monitor.config.ConstantConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CSVServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CSVServlet.class);

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String appName = request.getParameter("appName");

        if(appName == null){
            response.getWriter().println("appName is required");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String filePath = ConstantConfig.SPARK_USER_CSV_PATH +
                File.separator + appName + ".zip";

        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);

        String relativePath = getServletContext().getRealPath("");
        log.info("relativePath = " + relativePath);

        ServletContext context = getServletContext();

        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        log.info("MIME type: " + mimeType);

        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inStream.close();
        outStream.close();
    }
}
