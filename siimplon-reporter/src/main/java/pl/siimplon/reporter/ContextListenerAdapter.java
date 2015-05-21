package pl.siimplon.reporter;

import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.List;
import java.util.Set;

public abstract class ContextListenerAdapter implements ReportContextListener {
    public void reportAdded(Report report, String name) {
    }

    public void columnSchemeAdded(List<Value.Type> scheme, String name) {
    }

    public void columnSchemeRemoved(List<Value.Type> scheme, String name) {

    }

    public void reportRemoved(Report report, String name) {
    }

    public void featureAdded(List<AnalyzeItem> features, String name) {
    }

    public void featureRemoved(List<AnalyzeItem> features, String name) {
    }

    public void dictionaryAdded(Set<String> values, String name) {
    }

    public void dictionaryRemoved(Set<String> values, String name) {
    }

    public void transferAdded(List<TransferPair> transfer, String name) {
    }

    public void transferRemoved(List<TransferPair> transfer, String name) {
    }
}
