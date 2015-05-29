package pl.siimplon.desktop;

import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ResourceBundle;

public class TransferEditor extends JDialog {

    private JPanel contentPane;

    private JButton buttonOK;
    private JButton buttonCancel;

    private List<TransferPair> transfer;

    private int result;

    public TransferEditor() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.transferEditor"));

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

        this.result = JOptionPane.CANCEL_OPTION;
        pack();
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
