package pl.siimplon.desktop;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class EditorTest {

    protected static ResourceBundle names;
    protected static ReportContext reportContext;
    protected static FrameFixture window;

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
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                return new MainForm(reportContext);
            }
        });

        window = new FrameFixture(mainForm);
        window.show();
    }

    @After
    public void tearDown() throws Exception {
        window.cleanUp();
    }

    public static String get(String key) {
        return names.getString(key);
    }

//    public static void openNewTransferListEditor() {
//        window.menuItem(get("form.main.menuItem.context")).click();
//        window.menuItem(get("form.main.menuItem.context.transfers")).click();
//        DialogFixture transfersDialog = getTransfersDialog();
//        transfersDialog.button(get("form.main.dialog.button.add")).click();
//        getTransferListEditor().button(get("form.main.dialog.transferListEditor.button.add"));
//    }

    public static DialogFixture getTransferListEditor() {
        return window.dialog(get("form.main.dialog.transferListEditor"));
    }

    public static DialogFixture getTransfersDialog() {
        return window.dialog(get("form.main.dialog.sourceDialog"));
    }
}
