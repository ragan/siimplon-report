package pl.siimplon.desktop;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import pl.siimplon.reporter.ContextListenerAdapter;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;
import pl.siimplon.reporttool.MyCallback;
import pl.siimplon.reporttool.TransferRepository;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainForm extends JFrame {

    public static final String PREF_LAST_DIR = "last_open_directory";

    private final ResourceBundle names;

    private ReportContext context;
    public static final Preferences preferences;

    private static String sourcesContextSource;

    private static SourcesContext sourcesContext;

    private JPanel panelMain;

    private JTextField textFieldReportAlias;

    private JComboBox<String> comboBoxFirstSource;
    private JComboBox<String> comboBoxSecondSource;
    private JComboBox<String> comboBoxFirstScheme;
    private JComboBox<String> comboBoxSecondScheme;
    private JComboBox<String> comboBoxColumnScheme;

    private JButton buttonMake;

    static {
        preferences = Preferences.systemNodeForPackage(MainForm.class);
    }

    public static JFileChooser getFileDialog(String description, String extension) {
        JFileChooser jFileChooser = new JFileChooser(MainForm.getLastDir());
        jFileChooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
        jFileChooser.addChoosableFileFilter(filter);
        jFileChooser.setFileFilter(filter);
        return jFileChooser;
    }

    public static String getLastDir() {
        return preferences.get(PREF_LAST_DIR, System.getProperty("user.home"));
    }

    public static void setLastDir(String dir) {
        preferences.put(PREF_LAST_DIR, dir);
    }

    public static void addReportSource(String name, File file) {
        sourcesContext.addReportURI(name, file.toURI());
    }

    public static void delReportSource(String name) {
        sourcesContext.delReportURI(name);
    }

    public static void addSchemeSource(String name, File file) {
        sourcesContext.addSchemeURI(name, file.toURI());
    }

    public static void delSchemeSource(String name) {
        sourcesContext.delSchemeURI(name);
    }

    public static void addSourceSource(String name, File file) {
        sourcesContext.addSourceURI(name, file.toURI());
    }

    public static void delSourceSource(String name) {
        sourcesContext.delSourceURI(name);
    }

    public MainForm(ReportContext context) {
        super();

        sourcesContext = new SourcesContext();

        this.context = context;

        setContentPane(panelMain);

        names = ResourceBundle.getBundle("names");

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menuContext = new JMenu("Context");
        menuContext.setName(names.getString("form.main.menuItem.context"));
        JMenuItem menuItemSources = new JMenuItem("Sources");
        menuItemSources.setName(names.getString("form.main.menuItem.sources"));
        menuItemSources.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new MapSourcesEditor(MainForm.this, getContext().getFeaturesMap(), getContext());
            }
        });
        menuContext.add(menuItemSources);
        JMenuItem menuItemReports = new JMenuItem("Reports");
        menuItemReports.setName(names.getString("form.main.menuItem.reports"));
        menuItemReports.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new ReportsMapDialog(MainForm.this, getContext().getReportMap(), getContext());
            }
        });
        menuContext.add(menuItemReports);
        JMenuItem menuItemTransfers = new JMenuItem("Transfers");
        menuItemTransfers.setName(names.getString("form.main.menuItem.context.transfers"));
        menuItemTransfers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new TransfersDialog(MainForm.this, getContext().getTransferMap(), getContext());
            }
        });
        menuContext.add(menuItemTransfers);

        JMenu menuTools = new JMenu("Tools");
        menuTools.setName(names.getString("form.main.menuItem.tools"));
        JMenuItem menuItemMerge = new JMenuItem("Merge");
        menuItemMerge.setName(names.getString("form.main.menuItem.mergetool"));
        menuItemMerge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new MergeToolDialog(getContext()).setVisible(true);
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

        JMenu menuFile = new JMenu("File");
        JMenuItem miNew = new JMenuItem("New");
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
            }
        });
        JMenuItem miOpen = new JMenuItem("Open...");
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
                    } catch (XMLStreamException e1) {
                        e1.printStackTrace();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
        menuFile.add(miNew);


        jMenuBar.add(menuFile);
        jMenuBar.add(menuContext);
        jMenuBar.add(menuTools);
        setJMenuBar(jMenuBar);

        getContext().addContextListener(new ContextListenerAdapter() {
            @Override
            public void featureAdded(List<AnalyzeItem> features, String name) {
                refreshSources();
            }

            @Override
            public void featureRemoved(List<AnalyzeItem> features, String name) {
                refreshSources();
            }

            @Override
            public void columnSchemeRemoved(List<Value.Type> scheme, String name) {
                refreshColumnSchemes();
            }

            @Override
            public void makeFinished(Report report) {
                new ShowReportDialog(report).setVisible(true);
            }

            @Override
            public void columnSchemeAdded(List<Value.Type> scheme, String name) {
                refreshColumnSchemes();
            }

            @Override
            public void transferAdded(List<TransferPair> transfer, String name) {
                refreshTransfers();
            }

            @Override
            public void transferRemoved(List<TransferPair> transfer, String name) {
                refreshTransfers();
            }
        });

        refreshSources();
        refreshColumnSchemes();
        refreshTransfers();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
    }

    private void setContext(ReportContext context) {
        this.context = context;
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
}
