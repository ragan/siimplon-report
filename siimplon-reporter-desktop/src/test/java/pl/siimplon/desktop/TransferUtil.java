package pl.siimplon.desktop;

import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JComboBoxFixture;
import org.fest.swing.fixture.JListFixture;

public class TransferUtil extends EditorTest {

    public static void openTransferListEditor() {
        TransfersListUtil.openTransfersEditor();
        TransfersListUtil.getTransfersDialog().button(get("form.main.dialog.button.add")).click();
    }

    public static DialogFixture getTransferListEditor() {
        return window.dialog(get("form.main.dialog.transferListEditor"));
    }

    public static DialogFixture getTransferEditor() {
        return window.dialog(get("form.main.dialog.transferEditor"));
    }

    public static JComboBoxFixture getComboBox() {
        return getTransferListEditor().comboBox(get("form.main.dialog.transferListEditor.comboBox"));
    }

    public static JButtonFixture getAddButton() {
        return getTransferListEditor().button(get("form.main.dialog.transferListEditor.button.add"));
    }

    public static JListFixture getList() {
        return getTransferListEditor().list(get("form.main.dialog.transferListEditor.list"));
    }


}
