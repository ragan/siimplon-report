package pl.siimplon.reporter.analyzer;

import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.scheme.RowScheme;

import java.util.Arrays;
import java.util.List;

public class Analyzer {

    private final Report report;

    private final Report errorReport;

    public Analyzer(Report report) {
        this.report = report;

        errorReport = new Report(1);
    }

    public Report getErrorReport() {
        return errorReport;
    }

    public Report getReport() {
        return report;
    }

    public void putValues(List<String> values) {
        report.addRecord(values);
    }

    public void putRecord(RowScheme scheme) {
        try {
            String[] values = scheme.getRowValues(null, null, report);
            report.addRecord(Arrays.asList(values));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void analyze(List<AnalyzeItem> mains, List<AnalyzeItem> others, List<RowScheme> mainSchemes, List<RowScheme> otherSchemes,
                        AnalyzeCallback callback) {
        for (AnalyzeItem main : mains) {
            for (AnalyzeItem other : others) {
                if (callback.isRelation(main, other)) {
                    for (RowScheme mainScheme : mainSchemes) {
                        try {
                            String[] rowValues = mainScheme.getRowValues(main, other, report);
                            report.addRecord(Arrays.asList(rowValues));
                        } catch (Exception ignored) {
                            errorReport.addRecord(ignored.getMessage());
                        }
                    }
                }
            }
            for (RowScheme otherScheme : otherSchemes) {
                try {
                    String[] rowValues = otherScheme.getRowValues(main, report);
                    report.addRecord(Arrays.asList(rowValues));
                } catch (Exception ignored) {
                    errorReport.addRecord(ignored.getMessage());
                }
            }
        }
    }

    public void analyze(List<AnalyzeItem> mains, List<RowScheme> schemes) {
        for (AnalyzeItem main : mains) {
            for (RowScheme scheme : schemes) {
                try {
                    String[] values = scheme.getRowValues(main, report);
                    report.addRecord(Arrays.asList(values));
                } catch (IllegalStateException ignored) {
                    errorReport.addRecord(ignored.toString());
                }
            }
        }
    }
}
