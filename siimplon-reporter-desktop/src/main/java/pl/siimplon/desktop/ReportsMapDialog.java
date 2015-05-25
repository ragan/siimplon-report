package pl.siimplon.desktop;

import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.report.Report;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ReportsMapDialog extends MapEditorDialog<Report> {

    public ReportsMapDialog(JFrame frame, Map<String, Report> map, ReportContext reportContext) {
        super(frame, map, reportContext);
    }

    @Override
    protected void onDelButton(String name) {

    }

    @Override
    protected void onDoubleClick(int rowNum, String at) {
        new ShowReportDialog(map.get(at)).setVisible(true);
    }

    @Override
    public void onAddButton(JFrame frame, Map<String, Report> map) {

    }
}
