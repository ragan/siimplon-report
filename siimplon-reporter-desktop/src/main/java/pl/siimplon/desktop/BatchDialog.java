package pl.siimplon.desktop;

import pl.siimplon.desktop.batch.Batch;
import pl.siimplon.desktop.batch.BatchEntry;

import javax.swing.*;
import java.awt.event.*;

public class BatchDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JList<String> batchJList;
    private final DefaultListModel<String> batchJListModel;

    private JButton upButton;
    private JButton downButton;
    private JButton deleteButton;

    private Batch batch;

    public BatchDialog(MainForm mainForm) {
        this.batch = mainForm.getBatch();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        batchJListModel = new DefaultListModel<>();
        updateBatchJList();
        batchJList.setModel(batchJListModel);

        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int index = batchJList.getSelectedIndex();
                if (index == -1 || index == 0) return;
                BatchEntry a = batch.getEntries().get(index);
                BatchEntry b = batch.getEntries().get(index - 1);
                batch.set(index, b);
                batch.set(index - 1, a);
                updateBatchJList();
                batchJList.setSelectedIndex(index - 1);
            }
        });
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int index = batchJList.getSelectedIndex();
                if (index == -1 || index == batch.getEntries().size() - 1) return;
                BatchEntry a = batch.getEntries().get(index);
                BatchEntry b = batch.getEntries().get(index + 1);
                batch.set(index, b);
                batch.set(index + 1, a);
                updateBatchJList();
                batchJList.setSelectedIndex(index + 1);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int index = batchJList.getSelectedIndex();
                if (index == -1) return;
                batch.remove(index);
                updateBatchJList();
            }
        });

        pack();
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void updateBatchJList() {
        batchJListModel.removeAllElements();
        for (BatchEntry batchEntry : batch.getEntries()) {
            batchJListModel.addElement(batchEntry.toString());
        }
    }
}
