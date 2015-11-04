package test.uk.ac.ic.spark.monitor.service;

import org.junit.Test;
import uk.ac.ic.spark.monitor.services.FigureDisplayService;

import static org.junit.Assert.assertEquals;

public class TestFigureDisplayService {

    @Test
    public void testFigureDisplayService() {
        FigureDisplayService figureDisplayService = new FigureDisplayService();
        assertEquals(1, figureDisplayService.figureDisplay());

    }

}
