package pl.siimplon.desktop;

import com.google.common.io.Files;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.w3c.dom.Document;
import pl.siimplon.desktop.batch.Batch;
import pl.siimplon.desktop.batch.BatchEntry;
import pl.siimplon.reporter.ContextListenerAdapter;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.ReportContextListener;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;
import pl.siimplon.reporttool.MyCallback;
import pl.siimplon.reporttool.SimpleFeatureAnalyzeItem;
import pl.siimplon.reporttool.TransferRepository;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;

//TODO: save all that are not saved (if context have not saved files)
//TODO: raport niech się sam podcina (niech nie jest taki szeroki) + ew. rozszerza jak potrzeba
//TODO: jw. + nie potrzeba wyboru schematu skoro wszystko to stringi

//TODO: buttony przenieść do swoich funkcji
//TODO: append extension to files when needed
public class MainForm extends JFrame implements ReportContextListener {

    public static final String PREF_LAST_DIR = "last_open_directory";

    private final ResourceBundle names;

    private ReportContext context;
    public static final Preferences preferences;

    private static String sourcesContextSource = "";

    private static SourcesContext sourcesContext;

    private JPanel panelMain;

    private JTextField textFieldReportAlias;

    private Batch batch;

    private JComboBox<String> comboBoxFirstSource;
    private JComboBox<String> comboBoxSecondSource;
    private JComboBox<String> comboBoxFirstScheme;
    private JComboBox<String> comboBoxSecondScheme;
    private JComboBox<String> comboBoxColumnScheme;

    private JButton buttonMake;
    private JButton buttonAddToBatch;

    static {
        preferences = Preferences.systemNodeForPackage(MainForm.class);
    }

    public static String getLastDir() {
        return preferences.get(PREF_LAST_DIR, System.getProperty("user.home"));
    }

    public static void setLastDir(String dir) {
        preferences.put(PREF_LAST_DIR, dir);
    }

    public MainForm(ReportContext context) {
        super();

        sourcesContext = new SourcesContext();

        this.context = context;

        setContentPane(panelMain);

        batch = new Batch();

        names = ResourceBundle.getBundle("names");

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menuContext = new JMenu("Context");
        menuContext.setName(names.getString("form.main.menuItem.context"));
        JMenuItem menuItemSources = new JMenuItem("Sources");
        menuItemSources.setName(names.getString("form.main.menuItem.sources"));
        menuItemSources.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new MapSourcesEditor(MainForm.this, getContext().getFeaturesMap());
            }
        });
        menuContext.add(menuItemSources);
        JMenuItem menuItemReports = new JMenuItem("Reports");
        menuItemReports.setName(names.getString("form.main.menuItem.reports"));
        menuItemReports.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new ReportsMapDialog(MainForm.this, getContext().getReportMap());
            }
        });
        menuContext.add(menuItemReports);
        JMenuItem menuItemTransfers = new JMenuItem("Transfers");
        menuItemTransfers.setName(names.getString("form.main.menuItem.context.transfers"));
        menuItemTransfers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new TransfersDialog(MainForm.this, getContext().getTransferMap());
            }
        });
        menuContext.add(menuItemTransfers);

        JMenu menuTools = new JMenu("Tools");
        menuTools.setName(names.getString("form.main.menuItem.tools"));

        JMenuItem miMakeBatch = new JMenuItem("Make Batch");
        miMakeBatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int result = JOptionPane.showConfirmDialog(MainForm.this, "RLY?");
                if (result == JOptionPane.YES_OPTION) {
                    for (BatchEntry entry : getBatch().getEntries()) {
                        switch (entry.getType()) {
                            case MAKE:
                                //TODO: matko bosko...
                                make(
                                        entry.getValues().get(0), entry.getValues().get(1), entry.getValues().get(2), entry.getValues().get(3), entry.getValues().get(4)
                                );
                                break;
                            case MERGE:
                                merge(
                                        entry.getValues().get(0),
                                        getContext().getReport(entry.getValues().get(1)),
                                        getContext().getReport(entry.getValues().get(2))
                                );
                                break;
                        }
                    }
                }
            }
        });
        menuTools.add(miMakeBatch);
        //TODO: refresh all elements when necessary
        //TODO: "please make new project" before file chooser
        JMenuItem menuItemMerge = new JMenuItem("Merge");
        menuItemMerge.setName(names.getString("form.main.menuItem.mergetool"));
        menuItemMerge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new MergeToolDialog(getContext(), MainForm.this).setVisible(true);
            }
        });
        menuTools.add(menuItemMerge);
        JMenuItem exportXLS = new JMenuItem("Export XLS");
        exportXLS.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
                int result = jFileChooser.showSaveDialog(MainForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        HSSFWorkbook workbook = new HSSFWorkbook();
                        for (Map.Entry<String, Report> e : getContext().getReportMap().entrySet()) {
                            Report report = e.getValue();
                            HSSFSheet sheet = workbook.createSheet(e.getKey());
                            for (int i = 0; i < report.getRecords().size(); i++) {
                                Record record = report.getRecords().get(i);
                                HSSFRow row = sheet.createRow(i);
                                for (int ii = 0; ii < record.getStringValues().size(); ii++) {
                                    row.createCell(ii).setCellValue(record.getStringValues().get(ii));
                                }
                            }
                        }
                        FileOutputStream stream = new FileOutputStream(jFileChooser.getSelectedFile());
                        workbook.write(stream);
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        menuTools.add(exportXLS);

        JMenuItem miBatch = new JMenuItem("Batch");
        miBatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new BatchDialog(MainForm.this).setVisible(true);
            }
        });
        menuTools.add(miBatch);

        JMenu menuFile = new JMenu("File");
        menuFile.setName("menu.file");
        JMenuItem miNew = new JMenuItem("New");
        miNew.setName("menu.file.new");
        miNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(MainForm.getLastDir());
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Project file", "xml");
                jFileChooser.addChoosableFileFilter(filter);
                jFileChooser.setFileFilter(filter);
                int result = jFileChooser.showSaveDialog(MainForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    sourcesContextSource = jFileChooser.getSelectedFile().getAbsolutePath();
                }
                sourcesContext = new SourcesContext();
                setContext(new ReportContext());
                refreshColumnSchemes();
                refreshSources();
                refreshTransfers();
            }
        });
        JMenuItem miOpen = new JMenuItem("Open...");
        miOpen.setName("menu.file.open");
        miOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
                FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("Project file", "xml");
                jFileChooser.addChoosableFileFilter(xmlFilter);
                jFileChooser.setFileFilter(xmlFilter);
                int result = jFileChooser.showOpenDialog(MainForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    sourcesContext = new SourcesContext();
                    setContext(new ReportContext());

                    File file = jFileChooser.getSelectedFile();
                    try {
                        sourcesContext.makeFromXml(new FileInputStream(file));

                        for (Map.Entry<String, URI> entry : sourcesContext.getReportMap().entrySet()) {
                            Report report = parseCSV(new File(entry.getValue()));
                            getContext().putReport(report, entry.getKey());
                        }

                        for (Map.Entry<String, URI> entry : sourcesContext.getSchemeMap().entrySet()) {
                            File xmlFile = new File(entry.getValue());
                            getContext().parseXMLTransferList(new FileInputStream(xmlFile),
                                    entry.getKey());
                        }

                        for (Map.Entry<String, URI> entry : sourcesContext.getSourceMap().entrySet()) {
                            getContext().putFeature(getFeatures(new File(entry.getValue())), entry.getKey());
                        }

                        sourcesContextSource = jFileChooser.getSelectedFile().getAbsolutePath();
                        refreshColumnSchemes();
                        refreshSources();
                        refreshTransfers();

                    } catch (XMLStreamException | IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        JMenuItem miSave = new JMenuItem("Save");
        miSave.setName("menu.file.save");
        miSave.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: check if everything is saved
                if (sourcesContextSource.isEmpty()) {
                    JOptionPane.showMessageDialog(MainForm.this, "Please make or open a project context.");
                    return;
                }

                try {
                    Document document = sourcesContext.makeXmlDocument();
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    DOMSource source = new DOMSource(document);
                    StreamResult xmlResult = new StreamResult(new File(sourcesContextSource));
                    transformer.transform(source, xmlResult);
                } catch (ParserConfigurationException | TransformerException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JMenuItem miLoadBatch = new JMenuItem("Load Batch...");
        miLoadBatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = getBatchFileChooser();
                int result = fileChooser.showOpenDialog(MainForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    batch = new Batch();
                    try {
                        FileInputStream stream = new FileInputStream(fileChooser.getSelectedFile());
                        batch.makeFromXml(stream);
                        stream.close();
                    } catch (XMLStreamException | FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        JMenuItem miSaveBatch = new JMenuItem("Save Batch...");
        miSaveBatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = getBatchFileChooser();
                int result = fileChooser.showSaveDialog(MainForm.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    Document document = null;
                    try {
                        document = batch.getXml();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = null;
                    try {
                        transformer = transformerFactory.newTransformer();
                    } catch (TransformerConfigurationException e) {
                        e.printStackTrace();
                    }
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    DOMSource source = new DOMSource(document);
                    StreamResult xmlResult = new StreamResult(fileChooser.getSelectedFile());
                    try {
                        transformer.transform(source, xmlResult);
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        menuFile.add(miNew);
        menuFile.add(miOpen);
        menuFile.add(miSave);
        menuFile.addSeparator();
        menuFile.add(miLoadBatch);
        menuFile.add(miSaveBatch);

        jMenuBar.add(menuFile);
        jMenuBar.add(menuContext);
        jMenuBar.add(menuTools);
        setJMenuBar(jMenuBar);

        getContext().addContextListener(this);

//        getContext().addContextListener(new ContextListenerAdapter() {
//            @Override
//            public void featureAdded(List<AnalyzeItem> features, String name) {
//                refreshSources();
//            }
//
//            @Override
//            public void featureRemoved(List<AnalyzeItem> features, String name) {
//                refreshSources();
//            }
//
//            @Override
//            public void columnSchemeRemoved(List<Value.Type> scheme, String name) {
//                refreshColumnSchemes();
//            }
//
//            @Override
//            public void makeFinished(Report report) {
//                new ShowReportDialog(report).setVisible(true);
//            }
//
//            @Override
//            public void columnSchemeAdded(List<Value.Type> scheme, String name) {
//                refreshColumnSchemes();
//            }
//
//            @Override
//            public void transferAdded(List<TransferPair> transfer, String name) {
//                refreshTransfers();
//            }
//
//            @Override
//            public void transferRemoved(List<TransferPair> transfer, String name) {
//                refreshTransfers();
//            }
//        });

        refreshSources();
        refreshColumnSchemes();
        refreshTransfers();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        buttonMake.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                final String reportAlias = textFieldReportAlias.getText();
                final String firstSourceName = (String) comboBoxFirstSource.getSelectedItem();
                final String secondSourceName = ((String) comboBoxSecondSource.getSelectedItem());
                final String firstSchemeName = (String) comboBoxFirstScheme.getSelectedItem();
                final String secondSchemeName = ((String) comboBoxSecondScheme.getSelectedItem());

                Report report;
                try {
                    report = getContext().getReport(reportAlias);
                } catch (IllegalArgumentException e) {
                    report = new Report(getContext().getColumnScheme(((String) comboBoxColumnScheme.getSelectedItem())));
                    getContext().putReport(report, reportAlias);
                }

                new Thread(new Runnable() {
                    public void run() {
                        getContext().make(reportAlias, firstSourceName, secondSourceName,
                                firstSchemeName, secondSchemeName, new MyCallback());
                    }
                }).run();
            }
        });

        buttonAddToBatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onAddToBatch();
            }
        });
    }

    private void make(String reportAlias, String firstSourceName, String secondSourceName, String firstSchemeName, String secondSchemeName) {
        Report report;
        try {
            report = getContext().getReport(reportAlias);
        } catch (IllegalArgumentException e) {
            report = new Report(getContext().getColumnScheme(((String) comboBoxColumnScheme.getSelectedItem())));
            getContext().putReport(report, reportAlias);
        }

        getContext().make(reportAlias, firstSourceName, secondSourceName,
                firstSchemeName, secondSchemeName, new MyCallback());

    }

    private void onAddToBatch() {
        String reportAlias = textFieldReportAlias.getText();
        String firstSource = (String) comboBoxFirstSource.getSelectedItem();
        String secondSource = (String) comboBoxSecondSource.getSelectedItem();
        String firstScheme = (String) comboBoxFirstScheme.getSelectedItem();
        String secondScheme = (String) comboBoxSecondScheme.getSelectedItem();

        BatchEntry batchEntry = new BatchEntry(new String[]{reportAlias, firstSource, secondSource, firstScheme, secondScheme});
        getBatch().add(batchEntry);
    }

    public Batch getBatch() {
        return batch;
    }

    private void setContext(ReportContext context) {
        this.context = context;
        context.putColumnScheme(TransferRepository.zeroColumnScheme, "zero-column-scheme");
        context.addContextListener(this);
    }

    public ReportContext getContext() {
        return context;
    }

    private void refreshSources() {
        comboBoxFirstSource.removeAllItems();
        comboBoxFirstSource.addItem("");
        comboBoxSecondSource.removeAllItems();
        comboBoxSecondSource.addItem("");
        for (Map.Entry<String, List<AnalyzeItem>> e : getContext().getFeaturesMap().entrySet()) {
            comboBoxFirstSource.addItem(e.getKey());
            comboBoxSecondSource.addItem(e.getKey());
        }
    }

    private void refreshColumnSchemes() {
        comboBoxColumnScheme.removeAllItems();
        for (Map.Entry<String, List<Value.Type>> e : getContext().getColumnSchemesMap().entrySet()) {
            comboBoxColumnScheme.addItem(e.getKey());
        }
    }

    private void refreshTransfers() {
        comboBoxFirstScheme.removeAllItems();
        comboBoxFirstScheme.addItem("");
        comboBoxSecondScheme.removeAllItems();
        comboBoxSecondScheme.addItem("");
        for (Map.Entry<String, List<TransferPair>> e : getContext().getTransferMap().entrySet()) {
            comboBoxFirstScheme.addItem(e.getKey());
            comboBoxSecondScheme.addItem(e.getKey());
        }
    }

    public void addReportFile(File file) throws IOException {
        if (sourcesContextSource.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please make new project context.");
            return;
        }
        Report report = parseCSV(file);
        String fileName = Files.getNameWithoutExtension(file.getAbsolutePath());
        getContext().putReport(report, fileName);
        sourcesContext.addReportURI(fileName, file.toURI());
    }

    private Report parseCSV(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        CSVParser csvRecords = new CSVParser(fileReader, CSVFormat.EXCEL);
        Report report = null;
        List<String> values;

        for (CSVRecord record : csvRecords) {
            values = new ArrayList<>();
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

    public void addTransferList(File file) throws IOException, XMLStreamException {
        if (sourcesContextSource.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please make new project context.");
            return;
        }
        String fileName = Files.getNameWithoutExtension(file.getAbsolutePath());
        sourcesContext.addSchemeURI(fileName, file.toURI());
        FileInputStream stream = new FileInputStream(file);
        getContext().parseXMLTransferList(stream, fileName);
        stream.close();
        setLastDir(file.getAbsolutePath());
    }

    public void addTransferList(List<TransferPair> list, String name) {
        getContext().putTransfer(list, name);
    }

    private List<AnalyzeItem> getFeatures(File file) throws IOException {
        ShapefileDataStore store = new ShapefileDataStore(file.toURI().toURL());
        store.setCharset(Charset.forName("UTF-8"));
        SimpleFeatureIterator features = store.getFeatureSource().getFeatures().features();
        List<AnalyzeItem> featureList = new Vector<>();
        while (features.hasNext()) {
            featureList.add(new SimpleFeatureAnalyzeItem(features.next()));
        }
        features.close();
        return featureList;
    }

    public void addMapSource(File file) throws IOException {
        if (sourcesContextSource.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please make new project context.");
            return;
        }
        List<AnalyzeItem> features = getFeatures(file);
        String fileName = Files.getNameWithoutExtension(file.getAbsolutePath());
        getContext().putFeature(features, fileName);
        sourcesContext.addSourceURI(fileName, file.toURI());
    }

    private JFileChooser getFileChooser(boolean multiSelection, String desc, String ext) {
        final JFileChooser fc = new JFileChooser(getLastDir());
        fc.removeChoosableFileFilter(fc.getFileFilter());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, ext);
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);

        fc.setMultiSelectionEnabled(multiSelection);
        fc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    setLastDir(fc.getCurrentDirectory().getAbsolutePath());

                }
            }
        });

        return fc;
    }

    public JFileChooser getBatchFileChooser() {
        return getFileChooser(false, "XML", "xml");
    }

    public JFileChooser getReportFileChooser() {
        return getReportFileChooser(true);
    }

    public JFileChooser getReportFileChooser(boolean multiSelection) {
        return getFileChooser(multiSelection, "CSV File", "csv");
    }

    public JFileChooser getTransferListFileChooser() {
        return getTransferListFileChooser(true);
    }

    public JFileChooser getTransferListFileChooser(boolean multiSelection) {
        return getFileChooser(multiSelection, "Transfer File", "xml");
    }

    public JFileChooser getMapSourceFileChooser() {
        return getMapSourceFileChooser(true);
    }

    public JFileChooser getMapSourceFileChooser(boolean multiSelection) {
        return getFileChooser(multiSelection, "SHP File", "shp");
    }

    public void merge(String alias, Report first, Report second) {
        Report finalReport = new Report(first.getTypes());
        finalReport.add(first);
        finalReport.add(second);
        getContext().putReport(finalReport, alias);
    }

    public void addBatchEntry(BatchEntry.Type type, String... values) {
        getBatch().add(new BatchEntry(values, type));
    }

    @Override
    public void reportAdded(Report report, String name) {

    }

    @Override
    public void reportRemoved(Report report, String name) {

    }

    @Override
    public void featureAdded(List<AnalyzeItem> features, String name) {
        refreshSources();
    }

    @Override
    public void featureRemoved(List<AnalyzeItem> features, String name) {
        refreshSources();
    }

    @Override
    public void dictionaryAdded(Set<String> values, String name) {

    }

    @Override
    public void dictionaryRemoved(Set<String> values, String name) {
        refreshTransfers();
    }

    @Override
    public void transferAdded(List<TransferPair> transfer, String name) {
        refreshTransfers();
    }

    @Override
    public void transferRemoved(List<TransferPair> transfer, String name) {

    }

    @Override
    public void columnSchemeAdded(List<Value.Type> scheme, String name) {
        refreshColumnSchemes();
    }

    @Override
    public void columnSchemeRemoved(List<Value.Type> scheme, String name) {
        refreshColumnSchemes();
    }

    @Override
    public void makeFinished(Report report) {
        //new ShowReportDialog(report).setVisible(true);
    }
}
