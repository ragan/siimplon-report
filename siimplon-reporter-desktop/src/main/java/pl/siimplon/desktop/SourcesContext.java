package pl.siimplon.desktop;

import pl.siimplon.reporter.ContextListenerAdapter;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.ReportContextListener;
import pl.siimplon.reporter.report.Report;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SourcesContext {

    private Map<String, URI> reportMap = new HashMap<String, URI>();
    private Map<String, URI> sourceMap = new HashMap<String, URI>();
    private Map<String, URI> schemeMap = new HashMap<String, URI>();

    public void addReportURI(String name, URI uri) {
        reportMap.put(name, uri);
    }

    public void delReportURI(String name) {
        reportMap.remove(name);
    }

    public URI getReportURI(String name) {
        return reportMap.get(name);
    }

    public void addSourceURI(String name, URI uri) {
        sourceMap.put(name, uri);
    }

    public URI getSourceURI(String name) {
        return sourceMap.get(name);
    }

    public void delSourceURI(String name) {
        sourceMap.remove(name);
    }

    public void addSchemeURI(String name, URI uri) {
        schemeMap.put(name, uri);
    }

    public URI getSchemeURI(String scheme) {
        return schemeMap.get(scheme);
    }

    public void delSchemeURI(String scheme) {
        schemeMap.remove(scheme);
    }
}
