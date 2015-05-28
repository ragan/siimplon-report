package pl.siimplon.desktop;

import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JComboBoxFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.junit.Assert;
import org.junit.Test;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;

import java.util.Arrays;

public class MergeToolDialogTest extends MainTest {

    private static final String REPORT_A = "report-a";
    private static final String REPORT_B = "report-b";
    private static final String REPORT_C = "report-c";

    @Test
    public void testMergeTool() throws Exception {
        openMergeTool();
        DialogFixture dialog = window.dialog(get("form.main.dialog.mergetool"));
        dialog.requireVisible();
        dialog.requireModal();

        JComboBoxFixture cbLeft = dialog.comboBox(get("form.main.dialog.mergetool.leftComboBox"));
        JComboBoxFixture cbRight = dialog.comboBox(get("form.main.dialog.mergetool.rightComboBox"));
        cbLeft.requireVisible();
        cbLeft.requireItemCount(reportContext.getReportMap().size());
        cbLeft.requireNoSelection();
        cbRight.requireVisible();
        cbRight.requireItemCount(reportContext.getReportMap().size());
        cbRight.requireNoSelection();

        JButtonFixture button = dialog.button(get("form.main.dialog.mergetool.mergeButton"));
        button.requireVisible();
        button.requireDisabled();

        JTextComponentFixture textBox = dialog.textBox(get("form.main.dialog.mergetool.textBox"));

        textBox.requireEmpty();
        textBox.requireEditable();
    }

    @Test
    public void testNoReports() throws Exception {
        openMergeTool();
        JButtonFixture mergeButton = getMergeButton();
        getLeft().requireItemCount(0);
        getRight().requireItemCount(0);
        mergeButton.requireDisabled();

    }

    @Test
    public void testTextFieldIsAlwaysEmpty() throws Exception {
        openMergeTool();
        getTextField().enterText("test");
        getMergeToolDialog().close();
        openMergeTool();
        getTextField().requireEmpty();
    }

    @Test
    public void testMergeReports() throws Exception {
        addReports();
        openMergeTool();
        getLeft().selectItem(REPORT_A);
        getRight().selectItem(REPORT_B);
        getTextField().enterText("a");
        getMergeButton().requireEnabled(); //as reports are compatible

        //TODO: validate if possible
//        getRight().selectItem(REPORT_C);
//        getMergeButton().requireDisabled(); //as reports are incompatible
    }

    @Test
    public void testMerging() throws Exception {
        addReports();
        addReportTestValues();
        openMergeTool();
        getLeft().selectItem(REPORT_A);
        getRight().selectItem(REPORT_B);
        getTextField().enterText("a");
        getMergeButton().click();

        Assert.assertNotNull(reportContext.getReport("a"));
    }

    @Test
    public void testReportsArePresent() throws Exception {
        openMergeTool();

        JComboBoxFixture left = getLeft();
        left.requireItemCount(reportContext.getReportMap().size());
        JComboBoxFixture right = getRight();
        right.requireItemCount(reportContext.getReportMap().size());

        closeMergeTool();

        addReports();
        openMergeTool();
        getLeft().requireItemCount(reportContext.getReportMap().size());
        getRight().requireItemCount(reportContext.getReportMap().size());

        getMergeButton().requireDisabled();
        getTextField().enterText("a");
        getMergeButton().requireEnabled();
        getTextField().deleteText();
        getMergeButton().requireDisabled();
    }

    private JComboBoxFixture getLeft() {
        return getMergeToolDialog().comboBox(get("form.main.dialog.mergetool.leftComboBox"));
    }

    private JComboBoxFixture getRight() {
        return getMergeToolDialog().comboBox(get("form.main.dialog.mergetool.rightComboBox"));
    }

    private JButtonFixture getMergeButton() {
        return getMergeToolDialog().button(get("form.main.dialog.mergetool.mergeButton"));
    }

    private JTextComponentFixture getTextField() {
        return getMergeToolDialog().textBox(get("form.main.dialog.mergetool.textBox"));
    }

    private DialogFixture getMergeToolDialog() {
        return window.dialog(get("form.main.dialog.mergetool"));
    }

    private void addReports() {
        Report reportA = new Report(Arrays.asList(Value.Type.LITERAL, Value.Type.LITERAL, Value.Type.LITERAL));
        Report reportB = new Report(Arrays.asList(Value.Type.LITERAL, Value.Type.LITERAL, Value.Type.LITERAL));
        Report reportC = new Report(Arrays.asList(Value.Type.NUMBER, Value.Type.LITERAL, Value.Type.NUMBER));

        reportContext.putReport(reportA, REPORT_A);
        reportContext.putReport(reportB, REPORT_B);
        reportContext.putReport(reportC, REPORT_C);
    }

    private void addReportTestValues() {
        Report report;
        report = reportContext.getReport(REPORT_A);
        report.addRecord("a", "b", "c");
        report = reportContext.getReport(REPORT_B);
        report.addRecord("d", "e", "f");
        report = reportContext.getReport(REPORT_C);
        report.addRecord("1.0", "g", "2.0");
    }
}