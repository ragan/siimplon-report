package pl.siimplon.desktop;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SourcesContextTest {

    @Test
    public void testAddRemoveReport() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addReportURI("report", new File("/").toURI());
        assertNotNull(context.getReportURI("report"));
        context.delReportURI("report");
        assertNull(context.getReportURI("report"));
    }

    @Test
    public void testAddRemoveSource() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addSourceURI("source", new File("/").toURI());
        assertNotNull(context.getSourceURI("source"));
        context.delSourceURI("source");
        assertNull(context.getSourceURI("source"));
    }

    @Test
    public void testAddRemoveScheme() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addSchemeURI("scheme", new File("/").toURI());
        assertNotNull(context.getSchemeURI("scheme"));
        context.delSchemeURI("scheme");
        assertNull(context.getSchemeURI("scheme"));
    }

    @Test
    public void testToXML() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addReportURI("report", new File("/").toURI());

//        context.getXML();

    }
}