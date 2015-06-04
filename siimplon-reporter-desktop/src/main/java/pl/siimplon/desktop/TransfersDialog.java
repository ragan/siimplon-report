package pl.siimplon.desktop;

import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransfersDialog extends MapEditorDialog<List<TransferPair>> {

    public TransfersDialog(JFrame frame, Map<String, List<TransferPair>> map, ReportContext reportContext) {
        super(frame, map, reportContext);
    }

    @Override
    protected void onDelButton(String name) {
    }

    @Override
    protected void onDoubleClick(int rowNum, String at) {
        openTransferListEditor(getReportContext().getTransfer(at), at);
    }

    @Override
    public void onAddButton(JFrame frame, Map<String, List<TransferPair>> map) {
        openTransferListEditor(new ArrayList<TransferPair>(), "");
    }

    private void openTransferListEditor(List<TransferPair> pairList, String transferPairName) {
        TransferListEditor transferListEditor;
        if (transferPairName.isEmpty()) {
            transferListEditor = new TransferListEditor(getReportContext(), pairList);
        } else {
            transferListEditor = new TransferListEditor(getReportContext(), pairList, transferPairName);
        }
        transferListEditor.setVisible(true);
        int status = transferListEditor.getStatus();

        if (status == JOptionPane.OK_OPTION) {
            getReportContext().putTransfer(transferListEditor.getTransferPairList(), transferListEditor.getTransferName());
        }
    }
}
