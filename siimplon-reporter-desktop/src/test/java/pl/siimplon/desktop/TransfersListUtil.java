package pl.siimplon.desktop;

import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JButtonFixture;

public class TransfersListUtil extends EditorTest {

    public static void openTransfersEditor() {
        window.menuItem(get("form.main.menuItem.context")).click();
        window.menuItem(get("form.main.menuItem.context.transfers")).click();
    }

    public static DialogFixture getTransfersEditor() {
        return window.dialog(get("form.main.dialog.sourceDialog"));
    }
}
