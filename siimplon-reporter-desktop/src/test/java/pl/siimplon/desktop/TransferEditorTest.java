package pl.siimplon.desktop;

import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JTableFixture;
import org.junit.Test;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.Arrays;
import java.util.List;

public class TransferEditorTest extends TransfersDialogTest {

    @Test
    public void testComponentsArePresent() throws Exception {
        reportContext.putTransfer(getThreeEmptyTransfer(), "transfer");
        openTransfersDialog();
        JTableFixture mapTable = getMapTable();
        int row = mapTable.cell("transfer").row;
        int column = mapTable.cell("transfer").column;
        mapTable.cell(TableCell.row(row).column(column)).doubleClick();
        DialogFixture editor = getTransferEditor();
        editor.requireVisible();
        editor.requireModal();
    }

    private List<TransferPair> getThreeEmptyTransfer() {
        return Arrays.asList(
                new TransferPair(Transfer.EMPTY, ""),
                new TransferPair(Transfer.EMPTY, ""),
                new TransferPair(Transfer.EMPTY, "")
        );
    }

    protected JTableFixture getMapTable() {
        return getTransfersDialog().table(get("form.main.dialog.table.mainTable"));
    }

    protected DialogFixture getTransferEditor() {
        return window.dialog(get("form.main.dialog.transferEditor"));
    }
}