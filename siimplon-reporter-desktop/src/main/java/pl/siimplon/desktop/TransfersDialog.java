package pl.siimplon.desktop;

import com.google.common.io.Resources;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TransfersDialog extends MapEditorDialog<List<TransferPair>> {

    public TransfersDialog(JFrame frame, Map<String, List<TransferPair>> map, ReportContext reportContext) {
        super(frame, map, reportContext);

//        setName(names.getString("form.main.dialog.transfers"));
    }

    @Override
    protected void onDelButton(String name) {
    }

    @Override
    protected void onDoubleClick(int rowNum, String at) {
        new TransferEditor(getReportContext().getTransfer(at)).setVisible(true);
    }

    @Override
    public void onAddButton(JFrame frame, Map<String, List<TransferPair>> map) {
        new TransferEditor().setVisible(true);
    }
}
