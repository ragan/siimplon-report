package pl.siimplon.desktop;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JComboBoxFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.scheme.transfer.Transfer;

public class TransferEditorTest extends EditorTest {

    public static DialogFixture getTransferEditor() {
        return window.dialog(get("form.main.dialog.transferEditor"));
    }
}