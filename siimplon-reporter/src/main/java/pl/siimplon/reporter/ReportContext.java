package pl.siimplon.reporter;

import pl.siimplon.reporter.analyzer.AnalyzeCallback;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.analyzer.Analyzer;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.scheme.RowScheme;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ReportContext {

    private final Map<String, Report> reportMap;

    private final Map<String, List<AnalyzeItem>> featuresMap;

    private final Map<String, List<TransferPair>> transferMap;

    public ReportContext() {
        reportMap = new HashMap<String, Report>();
        featuresMap = new HashMap<String, List<AnalyzeItem>>();
        transferMap = new HashMap<String, List<TransferPair>>();
    }

    public void make(String reportName, String mainFeatureName, String otherFeatureName, String mainTransferName,
                     String otherTransferName, AnalyzeCallback callback) {
        Analyzer a = new Analyzer(getReport(reportName));
        RowScheme mainScheme = null;
        if (!mainTransferName.isEmpty()) {
            mainScheme = new RowScheme(transform(getTransfer(mainTransferName)));
        }
        RowScheme otherScheme = null;
        if (!otherTransferName.isEmpty()) {
            otherScheme = new RowScheme(transform(getTransfer(otherTransferName)));
        }
        List<AnalyzeItem> mainFeatures = getFeature(mainFeatureName);
        List<AnalyzeItem> otherFeatures = getFeature(otherFeatureName);
        List<RowScheme> mainSchemeList = new Vector<RowScheme>();
        if (mainScheme != null) {
            mainSchemeList.add(mainScheme);
        }
        List<RowScheme> otherSchemeList = new Vector<RowScheme>();
        if (otherScheme != null) {
            otherSchemeList.add(otherScheme);
        }
        if (mainFeatureName.isEmpty() && otherFeatureName.isEmpty()) {
            if (!mainSchemeList.isEmpty())
                a.putRecord(mainSchemeList.get(0));
            else if (!otherSchemeList.isEmpty())
                a.putRecord(otherSchemeList.get(0));
        } else {
            if (otherFeatureName.isEmpty() && otherTransferName.isEmpty() &&
                    !mainFeatureName.isEmpty() && !mainTransferName.isEmpty())
                a.analyze(mainFeatures, mainSchemeList);
            else
                a.analyze(mainFeatures, otherFeatures, mainSchemeList, otherSchemeList, callback);
        }
    }

    private List<TransferPair> transform(List<TransferPair> transfer) {
        List<TransferPair> ret = new Vector<TransferPair>();
        for (TransferPair pair : transfer) {
            Object[] attrs = pair.getAttributes();
            switch (pair.getSource()) {
                case PERCENT_CONDITION:
                case GET_PART_NUM_AND_SUM:
                case GET_PART_VAL:
                case COUNT_DISTINCT_VALUES:
                case COUNT_DISTINCT_VALUES_CONDITIONAL:
                case PERCENT_FROM_DISTINCT_VALUES:
                    pair.getAttributes()[0] = getReport(((String) attrs[0]));
                    break;
                default:
                    break;
            }
            ret.add(pair);
        }
        return ret;
    }

    public void merge(String... reports) {
        Report report = getReport(reports[0]);
        for (int i = 1; i < reports.length; i++) {
            report.add(getReport(reports[i]));
        }
    }

    public Report getReport(String name) {
        return reportMap.get(name);
    }

    public void putReport(Report report, String name) {
        reportMap.put(name, report);
    }

    public List<TransferPair> getTransfer(String name) {
        return transferMap.get(name);
    }

    public void putTransfer(List<TransferPair> transfer, String name) {
        transferMap.put(name, transfer);
    }

    public void putFeature(List<AnalyzeItem> features, String name) {
        featuresMap.put(name, features);
    }

    public List<AnalyzeItem> getFeature(String name) {
        return featuresMap.get(name);
    }
}
