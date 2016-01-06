package uk.ac.ic.spark.monitor.file;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.ic.spark.monitor.config.ConstantConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static final Logger log = LogManager.getLogger(ZipUtil.class);

    private final String CSV_PATH = ConstantConfig.SPARK_USER_CSV_PATH + File.separator;

    private List<String> fileList = new ArrayList<String>();
    private String sourceFolder;
    private String outputZip;
    private String appName;


    public ZipUtil(String appName){
        this.appName = appName;
        sourceFolder = CSV_PATH + appName;
        outputZip = CSV_PATH + appName + ".zip";
    }
//    public static void main(String[] args) throws ConfigurationException {
//        ConstantConfig.init();
//
//        ZipUtil appZip = new ZipUtil("application_1450721503259_0012");
//
//        appZip.zipAppCSV();
//    }

    public void zipAppCSV() {

        generateFileList(new File(sourceFolder));

        byte[] buffer = new byte[1024];
        String source = "";
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {

            fos = new FileOutputStream(outputZip);
            zos = new ZipOutputStream(fos);

            log.info("Output to Zip : " + outputZip);
            FileInputStream in = null;

            for (String file : this.fileList) {
                log.info("File Added : " + file);
                ZipEntry ze = new ZipEntry(appName + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            log.info("Folder successfully compressed");

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void generateFileList(File node) {

        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));

        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }
}