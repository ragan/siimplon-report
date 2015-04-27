package pl.siimplon.reporttool;

import com.vividsolutions.jts.geom.TopologyException;
import pl.siimplon.reporter.analyzer.AnalyzeCallback;
import pl.siimplon.reporter.analyzer.AnalyzeItem;

public class MyCallback implements AnalyzeCallback {

    @Override
    public boolean isRelation(AnalyzeItem main, AnalyzeItem other) {
        boolean result = false;
        try {
            result = main.crosses(other) || other.crosses(main) || main.contains(other) || other.contains(main) ||
                    other.covers(main) || main.coveredBy(other) || main.intersects(other) || other.intersects(main);
        } catch (TopologyException e) {
            System.out.printf("main: %s, other: %s", main.getAttribute("Layer"), other.getAttribute("Layer"));
            e.printStackTrace();
        }
        return result;
    }
}
