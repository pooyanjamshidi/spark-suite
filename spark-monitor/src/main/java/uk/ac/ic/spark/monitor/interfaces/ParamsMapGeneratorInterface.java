package uk.ac.ic.spark.monitor.interfaces;

import java.util.List;
import java.util.Map;

public interface ParamsMapGeneratorInterface {
    public List<Map<String, String>> generateParamsMap(String[] args);
}
