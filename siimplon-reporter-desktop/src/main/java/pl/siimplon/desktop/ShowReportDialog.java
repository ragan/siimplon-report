package pl.siimplon.desktop;

import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.util.Vector;

public class ShowReportDialog extends JDialog {
    private JPanel contentPane;
    private JTable tableReport;
    private JButton buttonOK;
    private JButton buttonCancel;

    public ShowReportDialog(Report report) {
        setContentPane(contentPane);
        setModal(true);

        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.Reports"));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnCount(report.getLength());
        tableReport.setModel(tableModel);
        for (Record record : report.getRecords()) {
            tableModel.addRow(new Vector<String>(record.getStringValues()));
        }
        tableReport.setModel(tableModel);

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onCancel() {
        dispose();
    }
}
