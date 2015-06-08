package pl.siimplon.desktop;

import com.google.common.io.Files;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.report.Report;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportsMapDialog extends MapEditorDialog<Report> {

    public ReportsMapDialog(MainForm mainForm, Map<String, Report> map) {
        super(mainForm, map);
    }

    @Override
    protected void onDelButton(String name) {
        getReportContext().delReport(name);
    }

    @Override
    protected void onDoubleClick(int rowNum, String at) {
        new ShowReportDialog(map.get(at)).setVisible(true);
    }

    @Override
    public void onAddButton(JFrame frame, Map<String, Report> map) {

        JFileChooser jFileChooser = mainForm.getReportFileChooser();
        int result = jFileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                for (File file : jFileChooser.getSelectedFiles()) {
                    mainForm.addReportFile(file);
                }
                MainForm.setLastDir(jFileChooser.getSelectedFiles()[0].getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
