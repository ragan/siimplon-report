package pl.siimplon.desktop;

import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.*;

public class TransferListEditor extends JDialog {

    private JPanel contentPane;

    private JButton buttonOK;
    private JButton buttonCancel;

    private JList<String> listTransfers;

    private JComboBox<String> comboBoxSelect;

    private JButton buttonAdd;
    private JButton buttonDelete;
    private JTextField textFieldName;

    private final List<TransferPair> transfer;

    private int status;

    private DefaultListModel<String> listModel;

    public TransferListEditor() {
        this(new ArrayList<TransferPair>(), "new transfer");
    }

    public TransferListEditor(List<TransferPair> transfer) {
        this(transfer, "new transfer");
    }

    public TransferListEditor(final List<TransferPair> transfer, String transferName) {
        this.transfer = new ArrayList<TransferPair>();
        this.transfer.addAll(transfer);

        textFieldName.setText(transferName);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.transferListEditor"));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        for (Transfer t : EnumSet.allOf(Transfer.class)) {
            comboBoxSelect.addItem(t.name());
        }
        listModel = new DefaultListModel<String>();
        listTransfers.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                buttonDelete.setEnabled(listSelectionEvent.getFirstIndex() != -1 &&
                        listSelectionEvent.getLastIndex() != -1);
            }
        });
        listTransfers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TransferPair pair = getSelectedTransferPair();
                    if (pair.getSource().getAttrSize() > 0) {
                        TransferEditor editor = getTransferEditor(pair.getSource(), pair.getAttributes());
                        editor.setVisible(true);
                        int status = editor.getStatus();
                        if (status == JOptionPane.OK_OPTION) {
                            getTransferPairList().set(listTransfers.getSelectedIndex(), editor.getResultTransfer());
                            updateTransfersList();
                        }
                    }
                }
            }
        });
        listTransfers.setModel(listModel);

        updateTransfersList();

        listTransfers.setSelectedIndex(-1);
        buttonDelete.setEnabled(false);
        buttonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                getTransferPairList().remove(listTransfers.getSelectedIndex());
                updateTransfersList();
            }
        });
        buttonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Transfer t = getSelectedTransfer();
                if (t.getAttrSize() == 0) {
                    getTransferPairList().add(new TransferPair(t, ""));
                    updateTransfersList();
                } else {
                    TransferEditor transferEditor = getTransferEditor(t, new Object[t.getAttrSize()]);
                    transferEditor.setVisible(true);
                    int status = transferEditor.getStatus();
                    if (status == JOptionPane.OK_OPTION) {
                        getTransferPairList().add(transferEditor.getResultTransfer());
                        updateTransfersList();
                    }
                }
            }
        });

        this.status = JOptionPane.CANCEL_OPTION;
        pack();
    }

    private Transfer getSelectedTransfer() {
        return Transfer.valueOf(((String) comboBoxSelect.getSelectedItem()));
    }

    private TransferPair getSelectedTransferPair() {
        return getTransferPairList().get(listTransfers.getSelectedIndex());
    }

    //TODO: if modification then delete, if new than add
    private TransferEditor getTransferEditor(Transfer t, Object[] attributes) {
        return new TransferEditor(new TransferPair(t, attributes));
//        transferEditor.setVisible(true);
//        int status = transferEditor.getStatus();
//        if (status == JOptionPane.OK_OPTION) {
//            TransferPair resultTransfer = transferEditor.getResultTransfer();
//            if (resultTransfer != null) {
//            }
//        }
    }

    private void updateTransfersList() {
        listModel.removeAllElements();
        for (TransferPair pair : transfer) {
            listModel.addElement(pair.toString());
        }
    }

    private void onCancel() {
        this.status = JOptionPane.CANCEL_OPTION;
        dispose();
    }

    private void onOK() {
        this.status = JOptionPane.OK_OPTION;
        dispose();
    }

    public List<TransferPair> getTransferPairList() {
        return transfer;
    }

    public int getStatus() {
        return status;
    }

    public String getTransferName() {
        return textFieldName.getText();
    }
}
