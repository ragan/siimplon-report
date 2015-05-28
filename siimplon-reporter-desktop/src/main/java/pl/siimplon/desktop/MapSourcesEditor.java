package pl.siimplon.desktop;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporttool.SimpleFeatureAnalyzeItem;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MapSourcesEditor extends MapEditorDialog<List<AnalyzeItem>> {

    public MapSourcesEditor(JFrame frame, Map<String, List<AnalyzeItem>> map, ReportContext reportContext) {
        super(frame, map, reportContext);
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
        JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
        int i = jFileChooser.showOpenDialog(frame);
        if (i == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = jFileChooser.getSelectedFile();
                List<AnalyzeItem> features = getFeatures(selectedFile);
                getReportContext().putFeature(features, selectedFile.getName());
                getTableModel().addRow(new Object[]{selectedFile.getName(), features.toString()});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<AnalyzeItem> getFeatures(File file) throws IOException {
        ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
        store.setCharset(Charset.forName("UTF-8"));
        SimpleFeatureIterator features = store.getFeatureSource().getFeatures().features();
        List<AnalyzeItem> featureList = new Vector<AnalyzeItem>();
        while (features.hasNext()) {
            featureList.add(new SimpleFeatureAnalyzeItem(features.next()));
        }
        features.close();
        return featureList;
    }
}
