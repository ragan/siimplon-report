package pl.siimplon.desktop;

import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        getRootPane().setDefaultButton(buttonOK);
        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.transferEditor"));

        currentTransfer = transferPair.getSource();
        Transfer transfer = transferPair.getSource();

        textFields = new JTextField[transfer.getAttrSize()];
        params = new String[transfer.getAttrSize()];

        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        for (int i = 0; i < transfer.getAttrSize(); i++) {
            JTextField textField = new JTextField();
            textFields[i] = textField;
            panelMain.add(textField);
            textField.setText(String.valueOf(transferPair.getAttributes()[i]));
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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

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
        return null;
    }
}
