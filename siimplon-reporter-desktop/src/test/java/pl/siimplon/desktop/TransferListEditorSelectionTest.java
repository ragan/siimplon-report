package pl.siimplon.desktop;

import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.*;
import org.junit.Test;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.*;

public class TransferListEditorSelectionTest extends EditorTest {

    @Test
    public void testDoubleClickOnList() throws Exception {
        reportContext.putTransfer(Arrays.asList(new TransferPair(Transfer.VALUE, "value a"),
                new TransferPair(Transfer.VALUE, "value b")), "transfer");
        TransfersListUtil.openTransfersEditor();
        DialogFixture dialog = TransfersListUtil.getTransfersDialog();
        JTableFixture table = dialog.table(get("form.main.dialog.table.mainTable"));
        table.cell(TableCell.row(0).column(0)).doubleClick();

        TransferUtil.getList().item(0).doubleClick();
        TransferUtil.getTransferEditor().requireVisible();
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
        TransferUtil.openTransferListEditor();
        TransferUtil.getComboBox().selectItem(transfer.name());
        TransferUtil.getList().requireItemCount(0);
        TransferUtil.getAddButton().click();
        if (transfer.getAttrSize() == 0) {
            TransferUtil.getList().requireItemCount(1);
        } else {
            DialogFixture transferEditor = TransferUtil.getTransferEditor();
            transferEditor.requireVisible();

            for (int i = 0; i < transfer.getAttrSize(); i++) {

            }
        }

    }



}