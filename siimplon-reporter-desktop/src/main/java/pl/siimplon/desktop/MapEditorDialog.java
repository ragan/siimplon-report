package pl.siimplon.desktop;

import pl.siimplon.reporter.ReportContext;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

public abstract class MapEditorDialog<T> extends JDialog {
    protected final ResourceBundle names;
    private final DefaultTableModel tableModel;
    private JPanel contentPane;
    private JTable table1;

    private JButton addButton;
    private JButton delButton;

    protected final Map<String, T> map;

    private ReportContext reportContext;

    public MapEditorDialog(final JFrame frame, final Map<String, T> map, ReportContext reportContext) {
        this.map = map;
        this.reportContext = reportContext;
        setContentPane(contentPane);
        setModal(true);

        names = ResourceBundle.getBundle("names");
        setName(names.getString("form.main.dialog.sourceDialog"));

        tableModel = new DefaultTableModel();
        tableModel.setColumnCount(2);
        tableModel.setColumnIdentifiers(new Object[]{"Key", "Value"});
        populateTableData(tableModel);
        table1.setModel(tableModel);

        table1.setCellSelectionEnabled(false);
        final ListSelectionModel selectionModel = table1.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                delButton.setEnabled(listSelectionEvent.getFirstIndex() != -1);
            }
        });

        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedRow = table1.getSelectedRow();
                String valueAt = ((String) tableModel.getValueAt(selectedRow, 0));
                tableModel.removeRow(selectedRow);
                onDelButton(valueAt);
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                onAddButton(frame, map);
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point pt = e.getPoint();
                int rowNum = table.rowAtPoint(pt);
                if (e.getClickCount() == 2) {
                    onDoubleClick(rowNum, ((String) tableModel.getValueAt(rowNum, 0)));
                }
            }
        });

        setLocationRelativeTo(frame);
        pack();
        setVisible(true);
    }

    protected abstract void onDelButton(String name);

    protected abstract void onDoubleClick(int rowNum, String at);

    public abstract void onAddButton(JFrame frame, Map<String, T> map);

    private void populateTableData(DefaultTableModel myTableModel) {
        for (Map.Entry<String, T> e : map.entrySet()) {
            myTableModel.addRow(new Object[]{e.getKey(), e.getValue().toString()});
        }
    }

    protected DefaultTableModel getTableModel() {
        return tableModel;
    }

    public ReportContext getReportContext() {
        return reportContext;
    }
}
