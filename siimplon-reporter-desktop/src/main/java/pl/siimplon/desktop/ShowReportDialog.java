package pl.siimplon.desktop;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Vector;

public class ShowReportDialog extends JDialog {

    private JPanel contentPane;
    private JTable tableReport;

    private JButton buttonCSVExport;

    public ShowReportDialog(final Report report) {
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

        buttonCSVExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser();
                int result = jFileChooser.showSaveDialog(getParent());
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    try {
                        CSVPrinter csvPrinter = new CSVPrinter(new BufferedWriter(new FileWriter(file)), CSVFormat.EXCEL);
                        for (Record record : report.getRecords()) {
                            for (String s : record.getStringValues()) {
                                csvPrinter.print(s);
                            }
                            csvPrinter.println();
                        }
                        csvPrinter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        pack();
    }

    private void onCancel() {
        dispose();
    }
}
