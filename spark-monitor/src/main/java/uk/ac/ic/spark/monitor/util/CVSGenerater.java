package uk.ac.ic.spark.monitor.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;

public class CVSGenerater {

    public static void main(String[] args) throws IOException {
        final Appendable out = new StringBuffer();



        final CSVPrinter printer = CSVFormat.DEFAULT.withHeader("H1", "H2").print(out);
        out.append("a,").append("b,").append("a,").append("b");
//        printer.println();

        System.out.println(out);


    }


}
