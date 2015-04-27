package pl.siimplon.reporter.analyzer;

public interface AnalyzeCallback {

    boolean isRelation(AnalyzeItem main, AnalyzeItem other);

}
