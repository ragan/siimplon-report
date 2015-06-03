package pl.siimplon.desktop;

import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JComboBoxFixture;
import org.fest.swing.fixture.JListFixture;
import org.junit.Test;
import pl.siimplon.reporter.scheme.transfer.Transfer;

import java.util.EnumSet;

import static org.junit.Assert.*;

public class TransferListEditorSelectionTest extends EditorTest {

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