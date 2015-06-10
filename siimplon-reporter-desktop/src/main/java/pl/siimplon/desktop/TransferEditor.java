package pl.siimplon.desktop;

import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class TransferEditor extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panelMain;

    private final JTextField[] textFields;

    private String[] params;

    private Transfer currentTransfer;

    private int status = JOptionPane.CANCEL_OPTION;

    public TransferEditor(TransferPair transferPair) {
        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);
        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.transferEditor"));

        currentTransfer = transferPair.getSource();
        Transfer transfer = transferPair.getSource();

        textFields = new JTextField[transfer.getAttrSize()];
        params = new String[transfer.getAttrSize()];

        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        for (int i = 0; i < transfer.getAttrSize(); i++) {
            JTextField textField = new JTextField();
            textFields[i] = textField;
            textField.setName("textField_" + String.valueOf(i));
            panelMain.add(textField);
            String t = String.valueOf(transferPair.getAttributes()[i]);
            if (transfer.getDescriptors()[i] == Transfer.Descriptor.INTEGER_VECTOR ||
                    transfer.getDescriptors()[i] == Transfer.Descriptor.STRING_VECTOR) {
                if (t.contains("[") && t.contains("]"))
                    t = t.substring(1, t.length() - 1);
            }
            textField.setText(t);
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
//        contentPane.registerKeyboardAction(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onOK() {
        status = JOptionPane.OK_OPTION;
        dispose();
    }

    private void onCancel() {
        status = JOptionPane.CANCEL_OPTION;
        dispose();
    }

    public int getStatus() {
        return status;
    }

    public TransferPair getResultTransfer() {

        Object[] attributes = new Object[currentTransfer.getAttrSize()];
        for (int i = 0; i < currentTransfer.getAttrSize(); i++) {
            attributes[i] = parseParam(currentTransfer, i);
        }

        return new TransferPair(currentTransfer, attributes);
    }

    private Object parseParam(Transfer transfer, int ix) {
        switch (transfer.getDescriptors()[ix]) {
            case STRING:
                return String.valueOf(textFields[ix].getText());
            case INTEGER:
                return Integer.valueOf(textFields[ix].getText());
            case INTEGER_VECTOR:
                ArrayList<Integer> ints = new ArrayList<Integer>();
                String[] integerSplit = textFields[ix].getText().split(",");
                if (integerSplit.length == 1 && integerSplit[0].isEmpty()) return Collections.emptyList();
                for (int i = 0; i < integerSplit.length; i++) {
                    integerSplit[i] = integerSplit[i].trim();
                    ints.add(Integer.valueOf(integerSplit[i]));
                }
                return ints;
            case STRING_VECTOR:
                ArrayList<String> strings = new ArrayList<String>();
                String[] stringSplit = textFields[ix].getText().split(",");
                if (stringSplit.length == 1 && stringSplit[0].isEmpty()) return Collections.emptyList();
                for (int i = 0; i < stringSplit.length; i++) {
                    stringSplit[i] = stringSplit[i].trim();
                    strings.add(String.valueOf(stringSplit[i]));
                }
                return strings;
        }
        return null;
    }
}
