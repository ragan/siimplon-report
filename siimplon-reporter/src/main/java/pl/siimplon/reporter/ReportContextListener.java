package pl.siimplon.reporter;

import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.List;
import java.util.Set;

public interface ReportContextListener {

    void reportAdded(Report report, String name);

    void reportRemoved(Report report, String name);

    void featureAdded(List<AnalyzeItem> features, String name);

    void featureRemoved(List<AnalyzeItem> features, String name);

    void dictionaryAdded(Set<String> values, String name);

    void dictionaryRemoved(Set<String> values, String name);

    void transferAdded(List<TransferPair> transfer, String name);

    void transferRemoved(List<TransferPair> transfer, String name);

    void columnSchemeAdded(List<Value.Type> scheme, String name);

    void columnSchemeRemoved(List<Value.Type> scheme, String name);

    void makeFinished(Report report);
}
