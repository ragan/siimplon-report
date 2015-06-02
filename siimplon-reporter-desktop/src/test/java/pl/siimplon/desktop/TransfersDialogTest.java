package pl.siimplon.desktop;

import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JTableFixture;
import org.junit.Test;

public class TransfersDialogTest extends MainTest {

    @Test
    public void testTransfersDialog() throws Exception {
        openTransfersDialog();
        DialogFixture dialog = getTransfersDialog();
        dialog.requireVisible();
        dialog.requireModal();
    }

    @Test
    public void testIfTransfersArePresent() throws Exception {
        openTransfersDialog();
        DialogFixture dialog = getTransfersDialog();
        JTableFixture table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.requireRowCount(reportContext.getTransferMap().size());
    }

    @Test
    public void testAddButton() throws Exception {
        openTransfersDialog();
        getTransfersDialog().button(get("form.main.dialog.button.add")).click();
        window.dialog(get("form.main.dialog.transferListEditor")).requireVisible();
    }

    @Test
    public void testOpenTransferEditor() throws Exception {
        openTransfersDialog();
        JTableFixture table = getTransfersDialog().table(get("form.main.dialog.table.mainTable"));
        table.cell(TableCell.row(0).column(0)).doubleClick();
        DialogFixture dialog = window.dialog(get("form.main.dialog.transferListEditor"));
        dialog.requireVisible();
    }


}