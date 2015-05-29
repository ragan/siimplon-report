package pl.siimplon.desktop;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MainTest {

    public static final String FEATURES_ENTRY_0 = "Features entry 0";
    public static final String FEATURES_ENTRY_1 = "Features entry 1";
    public static final String TRANSFER_0 = "Transfer 0";
    private static final String TRANSFER_1 = "Transfer 1";
    private static final String COLUMN_SCHEME = "column-scheme";
    public static final String TEST_REPORT_0 = "test-report-0";
    public static final String TEST_REPORT_1 = "test-report-1";

    protected static ResourceBundle names;

    protected FrameFixture window;

    protected ReportContext reportContext;
    private Object reportsDialogHandle;

    @BeforeClass
    public static void setUpBefore() {
        names = ResourceBundle.getBundle("names");
    }

    @Before
    public void setUp() throws Exception {
        reportContext = new ReportContext();
        MainForm mainForm = GuiActionRunner.execute(new GuiQuery<MainForm>() {
            @Override
            protected MainForm executeInEDT() throws Throwable {
                return new MainForm(reportContext);
            }
        });

        window = new FrameFixture(mainForm);
        window.show();

        AnalyzeItem analyzeItem = Mockito.mock(AnalyzeItem.class);

        ArrayList<AnalyzeItem> analyzeItems = new ArrayList<AnalyzeItem>();
        analyzeItems.add(analyzeItem);
        analyzeItems.add(analyzeItem);

        reportContext.putFeature(analyzeItems, FEATURES_ENTRY_0);
        reportContext.putFeature(analyzeItems, FEATURES_ENTRY_1);

        reportContext.putColumnScheme(Collections.<Value.Type>emptyList(), COLUMN_SCHEME);

        reportContext.putTransfer(Collections.<TransferPair>emptyList(), TRANSFER_0);
        reportContext.putTransfer(Collections.<TransferPair>emptyList(), TRANSFER_1);
    }

    protected void openMergeTool() {
        window.menuItem(get("form.main.menuItem.tools")).click();
        window.menuItem(get("form.main.menuItem.mergetool")).click();
    }

    protected void closeMergeTool() {
        window.dialog(get("form.main.dialog.mergetool")).close();
    }

    protected void expectDialogVisible(String name) {
        window.dialog(get(name)).requireVisible();
    }

    protected void clickMakeReportButton() {
        window.button(get("form.main.button.makeReport")).click();
    }

    protected void selectFirstTransfer(String name) {
        window.comboBox(get("form.main.comboBox.firstScheme")).selectItem(name);
    }

    protected void selectSecondSource(String name) {
        window.comboBox(get("form.main.comboBox.secondSource")).selectItem(name);
    }

    protected void selectFirstSource(String name) {
        window.comboBox(get("form.main.comboBox.firstSource")).selectItem(name);
    }

    protected void enterReportAlias(String alias) {
        window.textBox(get("form.main.textBox.reportAlias")).enterText(alias);
    }

    protected void closeMapSourcesDialog() {
        window.dialog(get("form.main.dialog.sourceDialog")).close();
    }

    protected void addFeaturesWhenInMapSourcesDialog(String fileName) {
        DialogFixture dialog = window.dialog(get("form.main.dialog.sourceDialog"));
        dialog.button(get("form.main.dialog.button.add")).click();
        window.fileChooser().requireVisible();
        window.fileChooser().selectFile(new File(fileName));
        window.fileChooser().approve();
    }

    protected void openMapSourcesDialog() {
        clickMenuItem("form.main.menuItem.context");
        clickMenuItem("form.main.menuItem.sources");
    }

    protected void openReportsDialog() {
        clickMenuItem("form.main.menuItem.context");
        clickMenuItem("form.main.menuItem.reports");
    }

    protected DialogFixture getMapEditorDialog() {
        return window.dialog(get("form.main.dialog.sourceDialog"));
    }

    protected void clickMenuItem(String name) {
        window.menuItem(get(name)).click();
    }

    protected void requireDialog(String name) {
        window.dialog(get(name)).requireVisible();
    }

    protected void requireMenuItem(String name) {
        window.menuItem(get(name)).requireVisible();
    }

    protected void requireComboBox(String name) {
        window.comboBox(get(name)).requireVisible();
    }

    protected void requireTextBox(String name) {
        window.textBox(get(name)).requireVisible();
    }

    protected void requireButton(String name) {
        window.button(get(name)).requireVisible();
    }

    protected String get(String key) {
        return names.getString(key);
    }

    @After
    public void tearDown() throws Exception {
        window.cleanUp();
    }
}
