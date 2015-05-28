package pl.siimplon.desktop;

import junit.framework.TestCase;
import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JTableFixture;
import org.junit.Test;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;

public class ReportsMapDialogTest extends MainTest {

    private static final String REPORT_A = "report-a";
    private static final String REPORT_B = "report-b";

    @Test
    public void testExportToCSV() throws Exception {
        addTwoReports();
        openReportsDialog();
        getReportsMapTable().cell(TableCell.row(0).column(0)).doubleClick();
    }

    private void addTwoReports() {
        Report report = new Report(Value.Type.LITERAL, Value.Type.LITERAL, Value.Type.LITERAL);
        report.addRecord("a", "b", "c");
        reportContext.putReport(report, REPORT_A);
        reportContext.putReport(report, REPORT_B);
    }

    private JButtonFixture getExportCSVButton() {
        return getReportsDialog().button(get("form.main.dialog.reports.csvButton"));
    }

    private JTableFixture getReportsMapTable() {
        return getReportsDialog().table(get("form.main.dialog.reports.table"));
    }

    private DialogFixture getReportsDialog() {
        return window.dialog(get("form.main.dialog.sourceDialog"));
    }
}