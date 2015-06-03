package pl.siimplon.desktop;

import pl.siimplon.reporter.ContextListenerAdapter;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;
import pl.siimplon.reporttool.MyCallback;
import pl.siimplon.reporttool.TransferRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainForm extends JFrame {

    private final ResourceBundle names;

    private final ReportContext context;

    private JPanel panelMain;

    private JTextField textFieldReportAlias;

    private JComboBox<String> comboBoxFirstSource;
    private JComboBox<String> comboBoxSecondSource;
    private JComboBox<String> comboBoxFirstScheme;
    private JComboBox<String> comboBoxSecondScheme;
    private JComboBox<String> comboBoxColumnScheme;

    private JButton buttonMake;

    public MainForm(ReportContext context) {
        super();

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
                        getContext().make(reportAlias, firstSourceName, secondSourceName, firstSchemeName, secondSchemeName, new MyCallback());
                    }
                }).run();

//                SwingWorker<Report, Void> worker = new SwingWorker<Report, Void>() {
//                    @Override
//                    protected Report doInBackground() throws Exception {
//                        Report report;
//                        try {
//                            report = getContext().getReport(reportAlias);
//                        } catch (IllegalArgumentException e) {
//                            report = new Report(getContext().getColumnScheme(((String) comboBoxColumnScheme.getSelectedItem())));
//                            getContext().putReport(report, reportAlias);
//                        }
//                        getContext().make(reportAlias, firstSourceName, secondSourceName, firstSchemeName, secondSchemeName, new MyCallback());
//                        return report;
//                    }
//                };
//                worker.execute();
            }
        });
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
