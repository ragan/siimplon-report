package pl.siimplon.reporter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.siimplon.reporter.analyzer.AnalyzeCallback;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.analyzer.Analyzer;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.RowScheme;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;

public class ReportContext {

    public static final String TRANSFER_PAIR_LIST = "list";
    public static final String TRANSFER_PAIR_LIST_ATTR_NAME = "name";
    public static final String TRANSFER_PAIR_LIST_TRANSFERPAIR = "pair";
    public static final String TRANSFER_PAIR_LIST_TRANSFERPAIR_ATTR_TYPE = "type";
    public static final String TRANSFER_PAIR_LIST_TRANSFERPAIR_ATTR_VAL = "value";
    public static final String TRANSFER_PAIR_LIST_TRANSFERPAIR_ENTRY = "entry";
    public static final String TRANSFER_PAIR_LIST_TRANSFERPAIR_ENTRY_ATTR_TYPE = "type";

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
        make(reportName, mainFeatureName, otherFeatureName, mainTransferName, otherTransferName, callback, false);
    }

    public void make(String reportName, String mainFeatureName, String otherFeatureName, String mainTransferName,
                     String otherTransferName, AnalyzeCallback callback, boolean makeError) {
        Analyzer a = new Analyzer(getReport(reportName));
        RowScheme mainScheme = null;
        if (!mainTransferName.isEmpty()) {
            mainScheme = new RowScheme(transform(getTransfer(mainTransferName)));
        }
        RowScheme otherScheme = null;
        if (!otherTransferName.isEmpty()) {
            otherScheme = new RowScheme(transform(getTransfer(otherTransferName)));
        }
        List<AnalyzeItem> mainFeatures;
        try {
            mainFeatures = getFeature(mainFeatureName);
        } catch (Exception e) {
            mainFeatures = new ArrayList<>();
        }
        List<AnalyzeItem> otherFeatures;
        try {
            otherFeatures = getFeature(otherFeatureName);
        } catch (Exception e) {
            otherFeatures = new ArrayList<>();
        }
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

        Report error;
        try {
            error = getReport("ERROR");
        } catch (IllegalArgumentException e) {
            error = new Report(1);
            putReport(error, "ERROR");
        }
        if (a.getErrorReport().getRecords().size() > 0) {
            error.addRecord("REPORT NAME: " + reportName);
            error.add(a.getErrorReport());
        }

        for (ReportContextListener listener : contextListeners) {
            listener.makeFinished(getReport(reportName));
        }

    }

    private List<TransferPair> transform(List<TransferPair> transfer) {
        List<TransferPair> ret = new Vector<TransferPair>();
        for (TransferPair pair : transfer) {
            Object[] attributes = new Object[pair.getAttributes().length];
            for (int i = 0; i < attributes.length; i++) {
                attributes[i] = pair.getAttributes()[i];
            }
            TransferPair newPair = new TransferPair(pair.getSource(), attributes);
            Object[] attrs = pair.getAttributes();
            switch (pair.getSource()) {
                case PERCENT_DISTINCT_VALUES_FROM_RECORDS_FOUND:
                case PERCENT_CONDITION:
                case GET_PART_NUM_AND_SUM:
                case GET_PART_NUM_AND_SUM_ZERO:
                case GET_PART_VAL:
                case COUNT_DISTINCT_VALUES:
                case COUNT_DISTINCT_VALUES_CONDITIONAL:
                case PERCENT_FROM_DISTINCT_VALUES:
                    attributes[0] = getReport(((String) attrs[0]));
                    break;
                case INSERT_INTO_REPORT:
                    attributes[0] = getReport(((String) attrs[0]));
                    attributes[4] = getReport(((String) attrs[4]));
                    break;
                default:
                    break;
            }
            ret.add(newPair);
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

    //TODO: this should not be here.
    public Document getTransferXML(List<TransferPair> list, String transferName) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.newDocument();

        Element root = document.createElement(TRANSFER_PAIR_LIST);
        document.appendChild(root);
        root.setAttribute(TRANSFER_PAIR_LIST_ATTR_NAME, transferName);

        List<TransferPair> transfer = list;
        for (TransferPair pair : transfer) {
            Element pairElement = document.createElement(TRANSFER_PAIR_LIST_TRANSFERPAIR); //PAIR
            root.appendChild(pairElement);

            Transfer source = pair.getSource();
            pairElement.setAttribute(TRANSFER_PAIR_LIST_TRANSFERPAIR_ATTR_TYPE, source.name()); // source attribute

            for (int i = 0; i < source.getAttrSize(); i++) {
                Transfer.Descriptor descriptor = source.getDescriptors()[i];
                Element element = document.createElement(TRANSFER_PAIR_LIST_TRANSFERPAIR_ENTRY);
                pairElement.appendChild(element);
                element.setAttribute(TRANSFER_PAIR_LIST_TRANSFERPAIR_ATTR_VAL, pair.getAttributeString(i));
                element.setAttribute(TRANSFER_PAIR_LIST_TRANSFERPAIR_ENTRY_ATTR_TYPE, descriptor.name());
            }
        }

        return document;
    }

    public Document getTransferXML(String transferName) throws ParserConfigurationException {
        return getTransferXML(getTransfer(transferName), transferName);
    }

    public List<TransferPair> parseXMLList(InputStream stream) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(stream);
        String listName = "";
        List<TransferPair> list = new ArrayList<>();

        TransferPair currentTransferPair = null;
        int attrIx = 0;

        while (reader.hasNext()) {

            if (reader.isStartElement()) {
                switch (reader.getLocalName()) {
                    case TRANSFER_PAIR_LIST:
                        listName = reader.getAttributeValue(0);
                        break;
                    case TRANSFER_PAIR_LIST_TRANSFERPAIR:
                        String attributeValue1 = reader.getAttributeValue(0);
                        Transfer t = Transfer.valueOf(attributeValue1);
                        currentTransferPair = new TransferPair(t, new Object[t.getAttrSize()]);
                        break;
                    case TRANSFER_PAIR_LIST_TRANSFERPAIR_ENTRY:
                        String attributeValue = reader.getAttributeValue(1);
                        currentTransferPair.setAttribute(attributeValue, attrIx);
                        attrIx++;
                        break;
                }
            } else if (reader.isEndElement()) {
                switch (reader.getLocalName()) {
                    case TRANSFER_PAIR_LIST_TRANSFERPAIR:
                        if (currentTransferPair != null) list.add(currentTransferPair);
                        currentTransferPair = null;
                        attrIx = 0;
                        break;
                }
            }
            reader.next();
        }
        return list;
    }

    public void parseXMLTransferList(InputStream stream, String listName) throws XMLStreamException {
        List<TransferPair> transferPairs = parseXMLList(stream);
        putTransfer(transferPairs, listName);
    }
}
