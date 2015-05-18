package pl.siimplon.reporttool;

import org.apache.commons.cli.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeCallback;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class ZeroMaker {

    private static final String OPT_SWEEP_S = "sweepsource";
    private static final String OPT_SWEEP_L = "sweep-source";

    private static final String OPT_TURBINE_S = "turbinesource";
    private static final String OPT_TURBINE_L = "turbine-source";

    private static final String OPT_FINAL_RD_S = "finalroadsource";
    private static final String OPT_FINAL_RD_L = "final-road-source";

    private static final String OPT_TEMP_RD_S = "temproadsource";
    private static final String OPT_TEMP_RD_L = "temp-road-source";

    private final String OPT_PLOT_SOURCE_S = "plotsource";
    private final String OPT_PLOT_SOURCE_L = "plot-source";

    private final String OPT_CABLE_SN_S = "cablesnsource";
    private final String OPT_CABLE_SN_L = "cable-sn-source";

    private final String OPT_CABLE_WN_S = "cablewnsource";
    private final String OPT_CABLE_WN_L = "cable-wn-source";

    private final String OPT_OUTPUT_FILE_S = "outputfile";
    private final String OPT_OUTPUT_FILE_L = "output-file";


    private Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder(OPT_OUTPUT_FILE_S)
                .longOpt(OPT_OUTPUT_FILE_L)
                .hasArg()
                .required()
                .build());
        options.addOption(Option.builder(OPT_PLOT_SOURCE_S)
                .longOpt(OPT_PLOT_SOURCE_L)
                .hasArg()
                .required()
                .build());
        options.addOption(Option.builder(OPT_CABLE_SN_S)
                .longOpt(OPT_CABLE_SN_L)
                .hasArg()
                .build());
        options.addOption(Option.builder(OPT_CABLE_WN_S)
                .longOpt(OPT_CABLE_WN_L)
                .hasArg()
                .build());
        options.addOption(Option.builder(OPT_SWEEP_S)
                .longOpt(OPT_SWEEP_L)
                .hasArg()
                .build());
        options.addOption(Option.builder(OPT_TURBINE_S)
                .longOpt(OPT_TURBINE_L)
                .hasArg()
                .build());
        options.addOption(Option.builder(OPT_FINAL_RD_S)
                .longOpt(OPT_FINAL_RD_L)
                .hasArg()
                .build());
        options.addOption(Option.builder(OPT_TEMP_RD_S)
                .longOpt(OPT_TEMP_RD_L)
                .hasArg()
                .build());

        return options;
    }

    public void make(String[] args) throws ParseException, IOException {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(getOptions(), args);

        String outputFilePath = cmd.getOptionValue(OPT_OUTPUT_FILE_S);

        String plotSourcePath = cmd.getOptionValue(OPT_PLOT_SOURCE_S);

        ReportContext context = new ReportContext();

        // ~~~ DICT AGREEMENTS
        context.createDictionary("dict-agreements",
                "", "służebność przesyłu", "umowa dzierżawy", "umowa najmu", "umowa przedwstępna", "służebność gruntowa",
                "Decyzja adm.", "porozumienie", "uzgodnienie", "Wstępna zgoda", "w trakcie końcowych negocjacji",
                "w trakcie negocjacji/procedury", "w trakcie negocjacji", "brak umowy");
        // ~~~

        // ~~~ DICT PAYMENTS
        context.createDictionary("dict-payments",
                "", "Raty", "Całość", "Roczny");
        // ~~~

        context.putFeature(getFeatures(plotSourcePath), "plot-source");

        Report zeroFinal = new Report(TransferRepository.zeroColumnScheme);
        zeroFinal.setDict(context.getDictionary("dict-agreements"), 10);
        zeroFinal.setDict(context.getDictionary("dict-payments"), 11);
        context.putReport(zeroFinal, "zero-report-final");


        MyCallback cb = new MyCallback();
        if (cmd.hasOption(OPT_CABLE_SN_S)) {
            makeSingleReport(cmd.getOptionValue(OPT_CABLE_SN_S), context, "cable-sn-source",
                    TransferRepository.zeroCableTransfer, "cable-sn-transfer", TransferRepository.zeroColumnScheme,
                    "cable-sn-report", cb);
            context.merge("zero-report-final", "cable-sn-report");
        }
        if (cmd.hasOption(OPT_CABLE_WN_S)) {
            makeSingleReport(cmd.getOptionValue(OPT_CABLE_WN_S), context, "cable-wn-source",
                    TransferRepository.zeroCableWnTransfer, "cable-wn-transfer", TransferRepository.zeroColumnScheme,
                    "cable-wn-report", cb);
            context.merge("zero-report-final", "cable-wn-report");
        }
        if (cmd.hasOption(OPT_SWEEP_S)) {
            makeSingleReport(cmd.getOptionValue(OPT_SWEEP_S), context, "sweep-souce",
                    TransferRepository.zeroSweepTransfer, "sweep-transfer", TransferRepository.zeroColumnScheme,
                    "sweep-report", cb);
            context.merge("zero-report-final", "sweep-report");
        }
        if (cmd.hasOption(OPT_TURBINE_S)) {
            makeSingleReport(cmd.getOptionValue(OPT_TURBINE_S), context, "turbine-souce",
                    TransferRepository.zeroTurbineTransfer, "turbine-transfer", TransferRepository.zeroColumnScheme,
                    "turbine-report", cb);
            context.merge("zero-report-final", "turbine-report");
        }
        if (cmd.hasOption(OPT_FINAL_RD_S)) {
            makeSingleReport(cmd.getOptionValue(OPT_FINAL_RD_S), context, "final-road-souce",
                    TransferRepository.zeroFinalRdTransfer, "final-road-transfer", TransferRepository.zeroColumnScheme,
                    "final-road-report", cb);
            context.merge("zero-report-final", "final-road-report");
        }
        if (cmd.hasOption(OPT_TEMP_RD_S)) {
            makeSingleReport(cmd.getOptionValue(OPT_TEMP_RD_S), context, "temp-road-souce",
                    TransferRepository.zeroTempRdTransfer, "temp-road-transfer", TransferRepository.zeroColumnScheme,
                    "temp-road-report", cb);
            context.merge("zero-report-final", "temp-road-report");
        }


        System.out.println("done");

        System.out.println("exporting...");
        ContextExporter exporter = new ContextExporter(context);
        exporter.export(Arrays.asList("zero-report-final"), "Dane wsadowe", new File(cmd.getOptionValue(OPT_OUTPUT_FILE_S)));

        Desktop.getDesktop().open(new File(cmd.getOptionValue(OPT_OUTPUT_FILE_S)));
    }

    private List<AnalyzeItem> getFeatures(String fileName) throws IOException {
        return getFeatures(new File(fileName));
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

    private void makeSingleReport(String filePath, ReportContext context, String sourceAlias,
                                  List<TransferPair> reportTransfer, String transferAlias,
                                  List<Value.Type> reportScheme, String reportAlias, AnalyzeCallback callback)
            throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("File does not exist (%s)", filePath));
        }
        context.putFeature(getFeatures(file), sourceAlias);
        context.putTransfer(reportTransfer, transferAlias);

        Report report = new Report(reportScheme);
        report.setDict(context.getDictionary("dict-agreements"), 10);
        report.setDict(context.getDictionary("dict-payments"), 11);
        context.putReport(report, reportAlias);

        context.make(reportAlias, "plot-source", sourceAlias, transferAlias, "", callback);
        /*
        if (cmd.hasOption(OPT_CABLE_SN_S)) {
            File file = new File(cmd.getOptionValue(OPT_CABLE_SN_S));
            if (!file.exists()) {
                throw new IllegalArgumentException(String.format("File does not exist (%s)", cmd.getOptionValue(OPT_CABLE_SN_S)));
            }
            context.putFeature(getFeatures(file), "cable-sn-source");
            context.putTransfer(TransferRepository.zeroCableTransfer, "zero-cable-transfer");

            Report report = new Report(TransferRepository.zeroColumnScheme);
            report.setDict(context.getDictionary("dict-agreements"), 10);
            report.setDict(context.getDictionary("dict-payments"), 11);
            context.putReport(report, "zero-cable-report");

            context.make("zero-cable-report", "plot-source", "cable-sn-source", "zero-cable-transfer", "", cb);

            context.merge("zero-report-final", "zero-cable-report");
        }
         */
    }


}
