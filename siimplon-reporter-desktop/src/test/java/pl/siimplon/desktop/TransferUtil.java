package pl.siimplon.desktop;

import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;

public class TransferUtil extends EditorTest {

    public static JButtonFixture getOkButton() {
        return getTransferEditor().button(get("form.main.dialog.transferEditor.button.ok"));
    }

    public static DialogFixture getTransferEditor() {
        return window.dialog(get("form.main.dialog.transferEditor"));
    }
}
