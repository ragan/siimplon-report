package pl.siimplon.reporter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReportContextTest {

    private ReportContext reportContext;
    private ReportContextListener contextListener;

    @Before
    public void setUp() throws Exception {
        reportContext = new ReportContext();

        contextListener = mock(ReportContextListener.class);
    }

    // ~~~ report listener
    @Test
    public void testReportAdded() throws Exception {
        reportContext.addContextListener(contextListener);
        Report report = new Report(2);
        reportContext.putReport(report, "report");
        verify(contextListener, times(1)).reportAdded(report, "report");
    }

    @Test
    public void testReportRemoved() throws Exception {
        reportContext.addContextListener(contextListener);
        Report report = new Report(1);
        reportContext.putReport(report, "report");
        reportContext.delReport("report");
        verify(contextListener, times(1)).reportRemoved(report, "report");
    }

    // ~~~
    // ~~~ map data source listener
    @Test
    public void testSourceAdded() throws Exception {
        reportContext.addContextListener(contextListener);
        List<AnalyzeItem> features = Collections.emptyList();
        reportContext.putFeature(features, "empty");
        verify(contextListener, times(1)).featureAdded(features, "empty");
    }

    @Test
    public void testSourceRemoved() throws Exception {
        reportContext.addContextListener(contextListener);
        List<AnalyzeItem> features = Collections.emptyList();
        reportContext.putFeature(features, "empty");
        reportContext.delFeature("empty");
        verify(contextListener, times(1)).featureRemoved(features, "empty");
    }
    // ~~~
    // ~~~ dictionary listener

    @Test
    public void testDictionaryAdded() throws Exception {
        reportContext.addContextListener(contextListener);
        Set<String> values = Collections.emptySet();
        reportContext.putDictionary("empty", values);
        verify(contextListener, times(1)).dictionaryAdded(values, "empty");
    }

    @Test
    public void testDictionaryRemoved() throws Exception {
        reportContext.addContextListener(contextListener);
        Set<String> values = Collections.emptySet();
        reportContext.putDictionary("empty", values);
        reportContext.delDictionary("empty");
        verify(contextListener, times(1)).dictionaryRemoved(values, "empty");
    }

    // ~~~
    // ~~~ transfer listeners
    @Test
    public void testTransferAdded() throws Exception {
        reportContext.addContextListener(contextListener);
        List<TransferPair> transfer = Collections.emptyList();
        reportContext.putTransfer(transfer, "empty");
        verify(contextListener, times(1)).transferAdded(transfer, "empty");
    }

    @Test
    public void testTransferRemoved() throws Exception {
        reportContext.addContextListener(contextListener);
        List<TransferPair> transfer = Collections.emptyList();
        reportContext.putTransfer(transfer, "empty");
        reportContext.delTransfer("empty");
        verify(contextListener, times(1)).transferRemoved(transfer, "empty");
    }
    // ~~~
    // ~~~ column scheme listeners

    @Test
    public void testColumnSchemeAdded() throws Exception {
        reportContext.addContextListener(contextListener);
        List<Value.Type> scheme = Collections.emptyList();
        reportContext.putColumnScheme(scheme, "empty");
        verify(contextListener, times(1)).columnSchemeAdded(scheme, "empty");
    }

    @Test
    public void testColumnSchemeRemoved() throws Exception {
        reportContext.addContextListener(contextListener);
        List<Value.Type> scheme = Collections.emptyList();
        reportContext.putColumnScheme(scheme, "empty");
        reportContext.delColumnScheme("empty");
        verify(contextListener, times(1)).columnSchemeRemoved(scheme, "empty");
    }

    // ~~~
}