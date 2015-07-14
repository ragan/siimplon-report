package pl.siimplon.desktop;

import pl.siimplon.reporter.analyzer.AnalyzeItem;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapSourcesEditor extends MapEditorDialog<List<AnalyzeItem>> {

    public MapSourcesEditor(MainForm mainForm, Map<String, List<AnalyzeItem>> map) {
        super(mainForm, map);
    }

    @Override
    protected void onDelButton(String name) {
        getReportContext().delFeature(name);
    }

    @Override
    protected void onDoubleClick(int rowNum, String at) {
    }

    @Override
    public void onAddButton(JFrame frame, Map<String, List<AnalyzeItem>> map) {
        JFileChooser jFileChooser = mainForm.getMapSourceFileChooser();
        int result = jFileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                for (File file : jFileChooser.getSelectedFiles()) {
                    mainForm.addMapSource(file);
                    populateTableData(getTableModel());
                }
                MainForm.setLastDir(jFileChooser.getSelectedFiles()[0].getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
