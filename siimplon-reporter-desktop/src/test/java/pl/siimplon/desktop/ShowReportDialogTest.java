package pl.siimplon.desktop;

import junit.framework.TestCase;
import org.junit.Test;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;

public class ShowReportDialogTest extends MainTest {

    @Test
    public void testExportMenuOption() throws Exception {

    }

    private void openReport() {
        Report report = new Report(Value.Type.LITERAL, Value.Type.LITERAL, Value.Type.LITERAL);
        report.addRecord("a", "b", "c");
        reportContext.putReport(report, "report");

        openReportsDialog();
    }

}