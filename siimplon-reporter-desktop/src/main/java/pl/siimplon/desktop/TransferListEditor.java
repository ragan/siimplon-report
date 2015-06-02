package pl.siimplon.desktop;

import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;

public class TransferListEditor extends JDialog {

    private JPanel contentPane;

    private JButton buttonOK;
    private JButton buttonCancel;

    private JList<String> listTransfers;

    private JComboBox<String> comboBoxSelect;

    private JButton buttonAdd;
    private JButton buttonDelete;

    private final List<TransferPair> transfer;

    private int result;
    private DefaultListModel<String> listModel;

    public TransferListEditor() {
        this(new ArrayList<TransferPair>());
    }

    public TransferListEditor(List<TransferPair> transfer) {
        this.transfer = new ArrayList<TransferPair>();
        this.transfer.addAll(transfer);

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
        listTransfers.setModel(listModel);

        updateTransfersList();

        listTransfers.setSelectedIndex(-1);
        buttonDelete.setEnabled(false);
        buttonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                getTransfer().remove(listTransfers.getSelectedIndex());
                updateTransfersList();
            }
        });

        this.result = JOptionPane.CANCEL_OPTION;
        pack();
    }

    private void updateTransfersList() {
        listModel.removeAllElements();
        for (TransferPair pair : transfer) {
            listModel.addElement(pair.toString());
        }
    }

    private void onCancel() {
        this.result = JOptionPane.CANCEL_OPTION;
        dispose();
    }

    private void onOK() {
        this.result = JOptionPane.OK_OPTION;
        dispose();
    }

    public List<TransferPair> getTransfer() {
        return transfer;
    }
}
