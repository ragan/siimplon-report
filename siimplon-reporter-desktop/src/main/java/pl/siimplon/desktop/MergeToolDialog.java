package pl.siimplon.desktop;

import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.report.Report;

import javax.swing.*;
import java.awt.event.*;
import java.util.Map;
import java.util.ResourceBundle;

public class MergeToolDialog extends JDialog implements ItemListener {

    private JPanel contentPane;
    private JComboBox<String> comboBoxFirst;
    private JComboBox<String> comboBoxSecond;
    private JButton buttonMerge;
    private JTextField textFieldAlias;

    private Map<String, Report> reportMap;

    private ReportContext reportContext;

    public MergeToolDialog(ReportContext reportContext) {
        this(reportContext.getReportMap(), reportContext);
    }

    public MergeToolDialog(final Map<String, Report> reportMap, final ReportContext reportContext) {
        this.reportMap = reportMap;
        this.reportContext = reportContext;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonMerge);

        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.mergetool"));

        for (Map.Entry<String, Report> entry : reportMap.entrySet()) {
            comboBoxFirst.addItem(entry.getKey());
            comboBoxSecond.addItem(entry.getKey());
        }

        comboBoxFirst.addItemListener(this);
        comboBoxSecond.addItemListener(this);
        textFieldAlias.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {
                setButtonEnableState();
            }

            public void keyPressed(KeyEvent keyEvent) {
                setButtonEnableState();
            }

            public void keyReleased(KeyEvent keyEvent) {
                setButtonEnableState();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setButtonEnableState();

        buttonMerge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Report first = reportMap.get(((String) comboBoxFirst.getSelectedItem()));
                Report second = reportMap.get(((String) comboBoxSecond.getSelectedItem()));

                Report finalReport = new Report(first.getTypes());
                finalReport.add(first);
                finalReport.add(second);
                reportContext.putReport(finalReport, textFieldAlias.getText());
                dispose();
            }
        });

        pack();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void setButtonEnableState() {
        buttonMerge.setEnabled(comboBoxFirst.getSelectedIndex() != -1 &&
                comboBoxSecond.getSelectedIndex() != -1 && !
                textFieldAlias.getText().isEmpty());
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            setButtonEnableState();
        }
    }

//    private boolean canMerge(String reportA, String reportB) {
//        Report first = reportMap.get(reportA);
//        Report second = reportMap.get(reportB);
//        return false;
//    }
}
