package pl.siimplon.desktop;

import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.*;
import org.junit.Assert;
import org.junit.Test;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransferListEditorSelectionTest extends EditorTest {

    @Test
    public void testDoubleClickOnList() throws Exception {
        reportContext.putTransfer(Arrays.asList(new TransferPair(Transfer.VALUE, "value a"),
                new TransferPair(Transfer.VALUE, "value b")), "transfer");
        TransfersListUtil.openTransfersEditor();
        DialogFixture dialog = TransfersListUtil.getTransfersDialog();
        JTableFixture table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.cell(TableCell.row(0).column(0)).doubleClick();

        TransferListUtil.getList().item(0).doubleClick();
        TransferListUtil.getTransferEditor().requireVisible();
    }

    @Test
    public void testSingleValue() throws Exception {
        reportContext.putTransfer(Arrays.asList(new TransferPair(Transfer.VALUE, "a")), "transfer");
        TransfersListUtil.openTransfersEditor();
        DialogFixture dialog = TransfersListUtil.getTransfersDialog();
        JTableFixture table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.cell(TableCell.row(0).column(0)).doubleClick();

        TransferListUtil.getList().item(0).doubleClick();
        DialogFixture transferEditor = TransferListUtil.getTransferEditor();
        JTextComponentFixture textField_0 = transferEditor.textBox("textField_0");
        textField_0.requireVisible();
        textField_0.requireText("a");
        textField_0.deleteText();
        textField_0.enterText("new value");

        TransferUtil.getOkButton().click();

        TransferListUtil.getList().requireItemCount(1);
        String[] contents = TransferListUtil.getList().contents();
        Assert.assertEquals("VALUE:new value", contents[0]);
        TransferListUtil.getOkButton().click();
    }

    @Test
    public void testCreateNewTransferList() throws Exception {
        TransferListUtil.openTransferListEditor();
        TransferListUtil.getComboBox().selectItem(Transfer.VALUE.name());
        TransferListUtil.getAddButton().click();

        JTextComponentFixture textBox = TransferUtil.getTransferEditor().textBox();
        textBox.requireText("null");
        textBox.deleteText();
        textBox.enterText("some value");
        TransferUtil.getOkButton().click();

        TransferListUtil.getList().requireItemCount(1);
        String[] contents = TransferListUtil.getList().contents();
        assertEquals("VALUE:some value", contents[0]);
    }

    @Test
    public void testSelectEmpty() throws Exception {
        testTransfer(Transfer.EMPTY);
    }

    @Test
    public void testCountDistinctValues() throws Exception {
        testTransfer(Transfer.COUNT_DISTINCT_VALUES);
    }

    @Test
    public void testCountDistinctValuesConditional() throws Exception {
        testTransfer(Transfer.COUNT_DISTINCT_VALUES_CONDITIONAL);
    }

    private void testTransfer(Transfer transfer) {
        TransferListUtil.openTransferListEditor();
        TransferListUtil.getComboBox().selectItem(transfer.name());
        TransferListUtil.getList().requireItemCount(0);
        TransferListUtil.getAddButton().click();
        if (transfer.getAttrSize() == 0) {
            TransferListUtil.getList().requireItemCount(1);
        } else {
            DialogFixture transferEditor = TransferListUtil.getTransferEditor();
            transferEditor.requireVisible();

            for (int i = 0; i < transfer.getAttrSize(); i++) {

            }
        }

    }


}