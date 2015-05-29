package pl.siimplon.desktop;

import com.google.common.io.Resources;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JTableFixture;
import org.junit.Test;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class ShowReportDialogTest extends MainTest {

    private static final String FILE_NAME = "test-file";

    @Test
    public void testMenuIsPresent() throws Exception {

    }

    @Test
    public void testExportMenuOption() throws Exception {
        openReport();
        getReportsTable();
    }

    private void openReport() {
        Report report = new Report(Value.Type.LITERAL, Value.Type.LITERAL, Value.Type.LITERAL);
        report.addRecord("a", "b", "c");
        report.addRecord("d", "e", "f");
        reportContext.putReport(report, "report");

        openReportsDialog();
        getReportsTable().cell(TableCell.row(0).column(0)).doubleClick();
        DialogFixture dialog = getShowReportDialog();
        dialog.button(get("form.main.dialog.showreport.button.save")).click();
        JFileChooserFixture fileChooser = dialog.fileChooser();
        fileChooser.requireVisible();
        fileChooser.setCurrentDirectory(new File(Resources.getResource("").getFile()));
        fileChooser.fileNameTextBox().enterText(FILE_NAME);
        fileChooser.approve();
        File file = new File(Resources.getResource(FILE_NAME).getFile());
        assertTrue(file.exists());

//        file.delete();
    }

    private JTableFixture getReportsTable() {
        return getMapEditorDialog().table(get("form.main.dialog.table.mainTable"));
    }

    private DialogFixture getShowReportDialog() {
        return window.dialog(get("form.main.dialog.Reports"));
    }

}