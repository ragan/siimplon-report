package pl.siimplon.desktop;

import pl.siimplon.reporter.report.Report;

import javax.swing.*;
import java.awt.event.*;
import java.util.Map;
import java.util.ResourceBundle;

public class MergeToolDialog extends JDialog {
    private JPanel contentPane;
    private JComboBox<String> comboBoxFirst;
    private JComboBox<String> comboBoxSecond;
    private JButton buttonMerge;
    private JButton buttonOK;
    private JButton buttonCancel;

    public MergeToolDialog(Map<String, Report> reportMap) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.mergetool"));

        for (Map.Entry<String, Report> entry : reportMap.entrySet()) {
            comboBoxFirst.addItem(entry.getKey());
            comboBoxSecond.addItem(entry.getKey());
        }

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
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
