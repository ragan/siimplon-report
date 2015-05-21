package pl.siimplon.desktop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.google.common.io.Resources;
import org.fest.swing.data.TableCell;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.*;
import org.junit.*;
import org.mockito.Mockito;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class MainFormTest {

    private static final String FEATURES_ENTRY_0 = "Features entry 0";
    private static final String FEATURES_ENTRY_1 = "Features entry 1";
    private static final String TRANSFER_0 = "Transfer 0";
    private static final String TRANSFER_1 = "Transfer 1";

    private static ResourceBundle names;

    private FrameFixture window;

    private ReportContext reportContext;

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

        reportContext.putColumnScheme(Collections.<Value.Type>emptyList(), "column-scheme");

        reportContext.putTransfer(Collections.<TransferPair>emptyList(), TRANSFER_0);
        reportContext.putTransfer(Collections.<TransferPair>emptyList(), TRANSFER_1);
    }

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

    @After
    public void tearDown() throws Exception {
        window.cleanUp();
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
        addButton.click();
        window.fileChooser().requireVisible();
        window.fileChooser().selectFile(new File(Resources.getResource("ew/ew.shp").getFile()));
        window.fileChooser().approve();
        table.requireRowCount(reportContext.getFeaturesMap().size());
        // ~~~
    }

    private void openMapSourcesDialog() {
        clickMenuItem("form.main.menuItem.context");
        clickMenuItem("form.main.menuItem.sources");
    }

    private void clickMenuItem(String name) {
        window.menuItem(get(name)).click();
    }

    private void requireDialog(String name) {
        window.dialog(get(name)).requireVisible();
    }

    private void requireMenuItem(String name) {
        window.menuItem(get(name)).requireVisible();
    }

    private void requireComboBox(String name) {
        window.comboBox(get(name)).requireVisible();
    }

    private void requireTextBox(String name) {
        window.textBox(get(name)).requireVisible();
    }

    private void requireButton(String name) {
        window.button(get(name)).requireVisible();
    }

    protected String get(String key) {
        return names.getString(key);
    }
}