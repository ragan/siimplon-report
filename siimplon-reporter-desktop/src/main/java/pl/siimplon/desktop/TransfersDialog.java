package pl.siimplon.desktop;

import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransfersDialog extends MapEditorDialog<List<TransferPair>> {

    public TransfersDialog(MainForm mainForm, Map<String, List<TransferPair>> map) {
        super(mainForm, map);
    }

    @Override
    protected void onDelButton(String name) {
        getReportContext().delTransfer(name);
    }

    @Override
    protected void onDoubleClick(int rowNum, String at) {
        openTransferListEditor(getReportContext().getTransfer(at), at);
    }

    @Override
    protected JMenuBar makeJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu mFile = new JMenu("File");
        JMenuItem miLoad = new JMenuItem("Load...");
        miLoad.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = mainForm.getTransferListFileChooser();
                int result = jFileChooser.showOpenDialog(TransfersDialog.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    for (File file : jFileChooser.getSelectedFiles()) {
                        try {
                            mainForm.addTransferList(file);
                            populateTableData(getTableModel());
                        } catch (IOException | XMLStreamException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mFile.add(miLoad);
        bar.add(mFile);
        return bar;
    }

    @Override
    public void onAddButton(JFrame frame, Map<String, List<TransferPair>> map) {
        openTransferListEditor(new ArrayList<TransferPair>(), "");
    }

    private void openTransferListEditor(List<TransferPair> pairList, String transferPairName) {
        TransferListEditor transferListEditor;
        if (transferPairName.isEmpty()) {
            transferListEditor = new TransferListEditor(getReportContext(), pairList, mainForm);
        } else {
            transferListEditor = new TransferListEditor(getReportContext(), pairList, transferPairName, mainForm);
        }
        transferListEditor.setVisible(true);
        int status = transferListEditor.getStatus();

        if (status == JOptionPane.OK_OPTION) {
            mainForm.addTransferList(transferListEditor.getTransferPairList(), transferListEditor.getTransferName());
        }
    }
}
