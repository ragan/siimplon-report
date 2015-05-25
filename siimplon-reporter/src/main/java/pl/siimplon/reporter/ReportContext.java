package pl.siimplon.reporter;

import pl.siimplon.reporter.analyzer.AnalyzeCallback;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.analyzer.Analyzer;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.RowScheme;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.*;

public class ReportContext {

    private final Map<String, Report> reportMap;

    private final Map<String, List<AnalyzeItem>> featuresMap;

    private final Map<String, List<TransferPair>> transferMap;

    private final Map<String, Set<String>> dictionaryMap;

    private final Map<String, List<Value.Type>> columnSchemeMap;

    private final List<ReportContextListener> contextListeners;


    public ReportContext() {
        reportMap = new HashMap<String, Report>();
        featuresMap = new HashMap<String, List<AnalyzeItem>>();
        transferMap = new HashMap<String, List<TransferPair>>();
        dictionaryMap = new HashMap<String, Set<String>>();
        columnSchemeMap = new HashMap<String, List<Value.Type>>();

        contextListeners = new ArrayList<ReportContextListener>();
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
        for (ReportContextListener listener : contextListeners) {
            listener.makeFinished(getReport(reportName));
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
        Report report = reportMap.get(name);
        if (report == null) {
            throw new IllegalArgumentException(String.format("Report '%s' not found.", name));
        }
        return report;
    }

    public void putReport(Report report, String name) {
        reportMap.put(name, report);
        for (ReportContextListener listener : contextListeners) {
            listener.reportAdded(report, name);
        }
    }

    public List<TransferPair> getTransfer(String name) throws IllegalArgumentException {
        List<TransferPair> transfer = transferMap.get(name);
        if (transfer == null) {
            throw new IllegalArgumentException(String.format("Transfer %s was not found.", name));
        }
        return transfer;
    }

    public void putTransfer(List<TransferPair> transfer, String name) {
        transferMap.put(name, transfer);
        for (ReportContextListener listener : contextListeners) {
            listener.transferAdded(transfer, name);
        }
    }

    public void putFeature(List<AnalyzeItem> features, String name) {
        featuresMap.put(name, features);
        for (ReportContextListener listener : contextListeners) {
            listener.featureAdded(features, name);
        }
    }

    public List<AnalyzeItem> getFeature(String name) {
        List<AnalyzeItem> items = featuresMap.get(name);
        if (items == null) {
            throw new IllegalArgumentException(String.format("Featues %s not found.", name));
        }
        return items;
    }

    public void putDictionary(String name, Set<String> values) {
        dictionaryMap.put(name, values);
        for (ReportContextListener listener : contextListeners) {
            listener.dictionaryAdded(values, name);
        }
    }

    public void createDictionary(String name, String... values) {
        putDictionary(name, new HashSet<String>(Arrays.asList(values)));
    }

    public Set<String> getDictionary(String s) {
        return dictionaryMap.get(s);
    }

    public Map<String, Report> getReportMap() {
        return Collections.unmodifiableMap(reportMap);
    }

    public Map<String, List<AnalyzeItem>> getFeaturesMap() {
        return Collections.unmodifiableMap(featuresMap);
    }

    public Map<String, List<TransferPair>> getTransferMap() {
        return Collections.unmodifiableMap(transferMap);
    }

    public Map<String, Set<String>> getDictionaryMap() {
        return Collections.unmodifiableMap(dictionaryMap);
    }

    public void addContextListener(ReportContextListener contextListener) {
        this.contextListeners.add(contextListener);
    }

    public void delReport(String name) {
        Report item = reportMap.remove(name);
        for (ReportContextListener listener : contextListeners) {
            listener.reportRemoved(item, name);
        }
    }

    public void delFeature(String name) {
        List<AnalyzeItem> item = featuresMap.remove(name);

        for (ReportContextListener listener : contextListeners) {
            listener.featureRemoved(item, name);
        }
    }

    public void delDictionary(String name) {
        Set<String> item = dictionaryMap.remove(name);
        for (ReportContextListener listener : contextListeners) {
            listener.dictionaryRemoved(item, name);
        }
    }

    public void delTransfer(String name) {
        List<TransferPair> item = transferMap.remove(name);
        for (ReportContextListener listener : contextListeners) {
            listener.transferRemoved(item, name);
        }
    }

    public void putColumnScheme(List<Value.Type> scheme, String name) {
        columnSchemeMap.put(name, scheme);
        for (ReportContextListener listener : contextListeners) {
            listener.columnSchemeAdded(scheme, name);
        }
    }

    public void delColumnScheme(String name) {
        List<Value.Type> item = columnSchemeMap.remove(name);
        for (ReportContextListener listener : contextListeners) {
            listener.columnSchemeRemoved(item, name);
        }
    }

    public Map<String, List<Value.Type>> getColumnSchemesMap() {
        return columnSchemeMap;
    }

    public List<Value.Type> getColumnScheme(String name) {
        List<Value.Type> columnScheme = columnSchemeMap.get(name);
        if (columnScheme == null)
            throw new IllegalArgumentException(String.format("Column scheme %s not found.", name));
        return columnScheme;
    }
}
