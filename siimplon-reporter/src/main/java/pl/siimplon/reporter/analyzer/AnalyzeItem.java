package pl.siimplon.reporter.analyzer;

public interface AnalyzeItem {

    public Object getAttribute(String name);

    double getArea();

    double getLength();

    boolean crosses(AnalyzeItem item);

    boolean contains(AnalyzeItem item);

    boolean covers(AnalyzeItem item);

    boolean coveredBy(AnalyzeItem item);

    boolean intersects(AnalyzeItem item);

    double clipArea(AnalyzeItem item);

    double clipLength(AnalyzeItem item);
}
