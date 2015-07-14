package pl.siimplon.desktop;

import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.report.value.Value;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static pl.siimplon.reporter.report.value.Value.Type.LITERAL;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException |
                        IllegalAccessException e) {
                    e.printStackTrace();
                }
                ReportContext reportContext = new ReportContext();
                reportContext.putColumnScheme(zeroColumnScheme, "zero-column-scheme");
                new MainForm(reportContext);

            }
        });
    }

    public static final List<Value.Type> zeroColumnScheme = Arrays.asList(
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,    //was DICTIONARY
            LITERAL,    //was DICTIONARY
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,    //was DICTIONARY
            LITERAL,    //was DICTIONARY
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,    //was DICTIONARY
            LITERAL,    //was DICTIONARY
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,    //was DICTIONARY
            LITERAL,    //was DICTIONARY
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,    //was DICTIONARY
            LITERAL,    //was DICTIONARY
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,    //was DICTIONARY
            LITERAL,    //was DICTIONARY
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL
    );


}
