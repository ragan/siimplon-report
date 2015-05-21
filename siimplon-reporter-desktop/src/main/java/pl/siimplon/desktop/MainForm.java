package pl.siimplon.desktop;

import pl.siimplon.reporter.ContextListenerAdapter;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainForm extends JFrame {

    private final ResourceBundle names;

    private final ReportContext reportContext;

    private JPanel panelMain;

    private JTextField textFieldReportAlias;

    private JComboBox<String> comboBoxFirstSource;
    private JComboBox<String> comboBoxSecondSource;
    private JComboBox<String> comboBoxFirstScheme;
    private JComboBox<String> comboBoxSecondScheme;
    private JComboBox<String> comboBoxColumnScheme;

    private JButton buttonMake;

    public MainForm(ReportContext reportContext) {
        super();

        this.reportContext = reportContext;

        setContentPane(panelMain);

        names = ResourceBundle.getBundle("names");

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menuContext = new JMenu("Context");
        menuContext.setName(names.getString("form.main.menuItem.context"));
        JMenuItem menuItemSources = new JMenuItem("Sources");
        menuItemSources.setName(names.getString("form.main.menuItem.sources"));
        menuItemSources.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                new SourceMapEditor(MainForm.this, getReportContext().getFeaturesMap(), getReportContext());
            }
        });
        menuContext.add(menuItemSources);
        jMenuBar.add(menuContext);
        setJMenuBar(jMenuBar);

        getReportContext().addContextListener(new ContextListenerAdapter() {
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
    }

    public ReportContext getReportContext() {
        return reportContext;
    }

    private void refreshSources() {
        comboBoxFirstSource.removeAllItems();
        comboBoxFirstSource.addItem("");
        comboBoxSecondSource.removeAllItems();
        comboBoxSecondSource.addItem("");
        for (Map.Entry<String, List<AnalyzeItem>> e : getReportContext().getFeaturesMap().entrySet()) {
            comboBoxFirstSource.addItem(e.getKey());
            comboBoxSecondSource.addItem(e.getKey());
        }
    }

    private void refreshColumnSchemes() {
        comboBoxColumnScheme.removeAllItems();
        for (Map.Entry<String, List<Value.Type>> e : getReportContext().getColumnSchemesMap().entrySet()) {
            comboBoxColumnScheme.addItem(e.getKey());
        }
    }

    private void refreshTransfers() {
        comboBoxFirstScheme.removeAllItems();
        comboBoxFirstScheme.addItem("");
        comboBoxSecondScheme.removeAllItems();
        comboBoxSecondScheme.addItem("");
        for (Map.Entry<String, List<TransferPair>> e : getReportContext().getTransferMap().entrySet()) {
            comboBoxFirstScheme.addItem(e.getKey());
            comboBoxSecondScheme.addItem(e.getKey());
        }
    }
}
