package pl.siimplon.desktop;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.siimplon.reporter.ReportContext;

public class TransferEditorTest extends EditorTest {

    @Test
    public void testEmpty() throws Exception {
        openNewTransferListEditor();

    }
}