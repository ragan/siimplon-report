package pl.siimplon.desktop;

import org.fest.swing.data.TableCell;
import org.fest.swing.fixture.*;
import org.junit.Test;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class TransferEditorTest extends TransfersDialogTest {

    @Test
    public void testComponentsArePresent() throws Exception {
        openDialog(getThreeEmptyTransfer());

        DialogFixture editor = getTransferEditor();
        editor.requireVisible();
        editor.requireModal();
    }

    @Test
    public void testSelectionComboBox() throws Exception {
        openDialog(getThreeEmptyTransfer());
        JComboBoxFixture comboBoxSelection = getSelectionComboBox();
        comboBoxSelection.requireVisible();
        comboBoxSelection.requireEnabled();

        EnumSet<Transfer> transfers = EnumSet.allOf(Transfer.class);
        comboBoxSelection.requireItemCount(transfers.size());
        for (Transfer t : transfers) {
            comboBoxSelection.selectItem(t.name());
        }
    }

    @Test
    public void testDelButton() throws Exception {
        List<TransferPair> transfer = getThreeEmptyTransfer();
        openDialog(transfer);

        JListFixture list = getTransfersList();
        list.requireNoSelection();
        JButtonFixture delButton = getDelButton();
        delButton.requireDisabled();

        int size = transfer.size();
        list.requireItemCount(size);

        list.selectItem(0);
        delButton.requireEnabled();
        delButton.click();
        list.requireItemCount(size - 1);
    }

    private JButtonFixture getDelButton() {
        return getTransferEditor().button(get("form.main.dialog.transferEditor.button.del"));
    }

    @Test
    public void testTransfersList() throws Exception {
        List<TransferPair> transfer = getThreeEmptyTransfer();
        openDialog(transfer);
        JListFixture list = getTransfersList();
        list.requireItemCount(transfer.size());
    }

    private JListFixture getTransfersList() {
        return getTransferEditor().list(get("form.main.dialog.transferEditor.list.transfers"));
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

    private void openDialog(List<TransferPair> transfer) {
        reportContext.putTransfer(transfer, "transfer");
        openTransfersDialog();
        JTableFixture mapTable = getMapTable();
        int row = mapTable.cell("transfer").row;
        int column = mapTable.cell("transfer").column;
        mapTable.cell(TableCell.row(row).column(column)).doubleClick();
    }

    private JComboBoxFixture getSelectionComboBox() {
        return getTransferEditor().comboBox(get("form.main.dialog.transferEditor.comboBox.select"));
    }
}