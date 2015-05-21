package pl.siimplon.desktop;

import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporttool.TransferRepository;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                ReportContext reportContext = new ReportContext();
                new MainForm(reportContext);
                reportContext.putTransfer(TransferRepository.zeroCableTransfer, "zero-cable-transfer");
                reportContext.putTransfer(TransferRepository.zeroSweepTransfer, "zero-sweep-transfer");
                reportContext.putTransfer(TransferRepository.zeroTurbineTransfer, "zero-turbine-transfer");
                reportContext.putTransfer(TransferRepository.zeroCableWnTransfer, "zero-cable-wn-transfer");
                reportContext.putTransfer(TransferRepository.zeroFinalRdTransfer, "zero-final-rd-transfer");
                reportContext.putTransfer(TransferRepository.zeroTempRdTransfer, "zero-temp-rd-transfer");
                
                reportContext.putColumnScheme(TransferRepository.zeroColumnScheme, "zero-column-scheme");
            }
        });
    }

}
