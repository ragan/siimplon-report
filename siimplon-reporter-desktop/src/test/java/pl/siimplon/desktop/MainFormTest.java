package pl.siimplon.desktop;

import com.google.common.collect.Table;
import com.google.common.io.Resources;
import org.fest.swing.data.TableCell;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.*;
import org.junit.*;
import org.mockito.Mockito;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.ReportContextListener;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class MainFormTest extends MainTest {

    @Test
    public void testBasicMainFormComponents() throws Exception {
        requireTextBox("form.main.textBox.reportAlias");
        requireComboBox("form.main.comboBox.firstSource");
        requireComboBox("form.main.comboBox.secondSource");
        requireComboBox("form.main.comboBox.firstScheme");
        requireComboBox("form.main.comboBox.secondScheme");
        requireComboBox("form.main.comboBox.columnScheme");

        requireButton("form.main.button.makeReport");

        requireMenuItem("form.main.menuItem.context");
    }

    @Test
    public void testSaveAndLoad() throws Exception {
        window.menuItem("menu.file").click();
        window.menuItem("menu.file.new").click();

        JFileChooserFixture fileChooser = window.fileChooser();
        URL resource = Resources.getResource("");
//        fileChooser.selectFile(new File(resource.getFile()));
        fileChooser.setCurrentDirectory(new File(resource.getFile()));
        fileChooser.fileNameTextBox().enterText("testProject.xml");
        fileChooser.approve();
        openMapSourcesDialog();
        DialogFixture dialog = window.dialog(get("form.main.dialog.sourceDialog"));
        JButtonFixture button = dialog.button(get("form.main.dialog.button.add"));
        button.click();
        fileChooser = window.fileChooser();
        fileChooser.setCurrentDirectory(new File(Resources.getResource("/ew/ew.shp").getFile()));
        dialog.close();

        window.menuItem("menu.file").click();
        window.menuItem("menu.file.save").click();

        window.menuItem("menu.file").click();
        window.menuItem("menu.file.open").click();

        fileChooser = window.fileChooser();
        fileChooser.setCurrentDirectory(new File(resource.getFile()));
//        fileChooser.selectFile(new File(Resources.getResource("/testProject.xml")))

    }

    @Test
    public void testFirstSourceComboBox() throws Exception {
        JComboBoxFixture firstSourceComboBox = window.comboBox(get("form.main.comboBox.firstSource"));
        JComboBoxFixture secondSourceComboBox = window.comboBox(get("form.main.comboBox.secondSource"));
        firstSourceComboBox.requireItemCount(reportContext.getFeaturesMap().size() + 1);
        secondSourceComboBox.requireItemCount(reportContext.getFeaturesMap().size() + 1);
    }

    @Test
    public void testColumnSchemesComboBox() throws Exception {
        JComboBoxFixture columnSchemeComboBox = window.comboBox(get("form.main.comboBox.columnScheme"));
        columnSchemeComboBox.requireItemCount(reportContext.getColumnSchemesMap().size());
    }

    @Test
    public void testTransferComboBoxes() throws Exception {
        JComboBoxFixture firstCombo = window.comboBox(get("form.main.comboBox.firstScheme"));
        firstCombo.requireItemCount(reportContext.getTransferMap().size() + 1);
        JComboBoxFixture secondCombo = window.comboBox(get("form.main.comboBox.secondScheme"));
        secondCombo.requireItemCount(reportContext.getTransferMap().size() + 1);
    }

    @Test
    public void testPlotSourcesDialog() throws Exception {
        openMapSourcesDialog();
        requireDialog("form.main.dialog.sourceDialog");

        DialogFixture dialog = window.dialog(get("form.main.dialog.sourceDialog"));
        // add source button
        JButtonFixture addButton = dialog.button(get("form.main.dialog.button.add"));
        // del source button
        JButtonFixture delButton = dialog.button(get("form.main.dialog.button.del"));

        addButton.requireVisible();
        addButton.requireEnabled();

        delButton.requireVisible();
        delButton.requireDisabled();

        //main table
        JTableFixture table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.requireVisible();
        table.requireColumnCount(2);
        table.requireRowCount(reportContext.getFeaturesMap().size());

        table.requireRowCount(reportContext.getFeaturesMap().size());
        table.requireCellValue(TableCell.row(0).column(0), FEATURES_ENTRY_1);
        table.requireCellValue(TableCell.row(1).column(0), FEATURES_ENTRY_0);

        table.selectRows(0);
        delButton.requireEnabled();
        delButton.click();
        assertEquals(1, reportContext.getFeaturesMap().size());
        table.requireRowCount(1);

        // ~~~ Add new analyze item list from file on disk
        addFeaturesWhenInMapSourcesDialog(Resources.getResource("ew/ew.shp").getFile());
        table.requireRowCount(reportContext.getFeaturesMap().size());
        // ~~~
    }

    @Test
    public void testReportDialog() throws Exception {
        Report report = new Report(Arrays.asList(Value.Type.LITERAL, Value.Type.LITERAL, Value.Type.LITERAL));
        reportContext.putReport(report, TEST_REPORT_0);

        openReportsDialog();

        DialogFixture dialog = window.dialog(get("form.main.dialog.sourceDialog"));
        JTableFixture table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.requireRowCount(1);

        dialog.close();
        reportContext.putReport(report, TEST_REPORT_1);

        openReportsDialog();

        dialog = window.dialog(get("form.main.dialog.sourceDialog"));
        table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.requireRowCount(2);

        table.cell(TableCell.row(0).column(0)).doubleClick();
        expectDialogVisible("form.main.dialog.Reports");
    }

    @Test
    public void testCreateSimpleReport() throws Exception {
        openMapSourcesDialog();
        addFeaturesWhenInMapSourcesDialog(Resources.getResource("ew/ew.shp").getFile());
        closeMapSourcesDialog();
        enterReportAlias("test-report-alias");
        selectFirstSource("ew.shp");
        selectSecondSource("ew.shp");
        selectFirstTransfer(TRANSFER_0);

        clickMakeReportButton();

        expectDialogVisible("form.main.dialog.Reports");
    }

    @Test
    public void testDisplayDialog() throws Exception {
        Report report = new Report(Arrays.asList(Value.Type.LITERAL, Value.Type.LITERAL, Value.Type.LITERAL));
        report.addRecord(Arrays.asList("a", "b", "c"));

        reportContext.putReport(report, TEST_REPORT_0);
        openReportsDialog();
        DialogFixture dialog = window.dialog(get("form.main.dialog.sourceDialog"));
        JTableFixture table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.cell(TableCell.row(0).column(0)).doubleClick();

        dialog = window.dialog(get("form.main.dialog.Reports"));
        dialog.requireVisible();
        table = dialog.table(get("form.main.dialog.reports.table"));

        table.requireColumnCount(3);
        table.cell(TableCell.row(0).column(0)).requireValue("a");
        table.cell(TableCell.row(0).column(1)).requireValue("b");
        table.cell(TableCell.row(0).column(2)).requireValue("c");
    }
}