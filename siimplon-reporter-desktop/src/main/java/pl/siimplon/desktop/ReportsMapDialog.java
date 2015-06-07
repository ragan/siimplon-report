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

    public ReportsMapDialog(JFrame frame, Map<String, Report> map, ReportContext reportContext) {
        super(frame, map, reportContext);
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
        JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("csv file", "csv");
        jFileChooser.setMultiSelectionEnabled(true);
        jFileChooser.addChoosableFileFilter(csvFilter);
        jFileChooser.setFileFilter(csvFilter);
        int result = jFileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                for (File file : jFileChooser.getSelectedFiles()) {
                    Report report = parseCSV(file);
                    getReportContext().putReport(report, Files.getNameWithoutExtension(file.getName()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Report parseCSV(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        CSVParser csvRecords = new CSVParser(fileReader, CSVFormat.EXCEL);
        Report report = null;
        List<String> values;

        for (CSVRecord record : csvRecords) {
            values = new ArrayList<String>();
            for (int i = 0; i < record.size(); i++) {
                if (report == null) {
                    report = new Report(record.size());
                }
                values.add(record.get(i));
            }
            if (report != null) report.addRecord(values);
            values.clear();
        }
        return report;
    }

}
