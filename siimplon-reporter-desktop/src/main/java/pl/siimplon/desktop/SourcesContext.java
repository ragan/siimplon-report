package pl.siimplon.desktop;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.siimplon.reporter.ContextListenerAdapter;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.ReportContextListener;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourcesContext {

    private Map<String, URI> reportMap = new HashMap<String, URI>();
    private Map<String, URI> sourceMap = new HashMap<String, URI>();
    private Map<String, URI> schemeMap = new HashMap<String, URI>();

    public void addReportURI(String name, URI uri) {
        reportMap.put(name, uri);
    }

    public void delReportURI(String name) {
        reportMap.remove(name);
    }

    public URI getReportURI(String name) {
        return reportMap.get(name);
    }

    public void addSourceURI(String name, URI uri) {
        sourceMap.put(name, uri);
    }

    public URI getSourceURI(String name) {
        return sourceMap.get(name);
    }

    public void delSourceURI(String name) {
        sourceMap.remove(name);
    }

    public void addSchemeURI(String name, URI uri) {
        schemeMap.put(name, uri);
    }

    public URI getSchemeURI(String scheme) {
        return schemeMap.get(scheme);
    }

    public void delSchemeURI(String scheme) {
        schemeMap.remove(scheme);
    }

    public Document makeXmlDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.newDocument();

        Element root = document.createElement("sources");
        document.appendChild(root);
        for (Map.Entry<String, URI> entry : reportMap.entrySet()) {
            Element report = makeNameValueElement(document, "report", entry);
            root.appendChild(report);
        }

        for (Map.Entry<String, URI> entry : sourceMap.entrySet()) {
            Element source = makeNameValueElement(document, "source", entry);
            root.appendChild(source);
        }

        for (Map.Entry<String, URI> entry : schemeMap.entrySet()) {
            Element scheme = makeNameValueElement(document, "scheme", entry);
            root.appendChild(scheme);
        }

        return document;
    }

    private Element makeNameValueElement(Document document, String nodeName, Map.Entry<String, URI> element) {
        Element elem = document.createElement(nodeName);
        elem.setAttribute("name", element.getKey());
        elem.setAttribute("value", String.valueOf(element.getValue()));
        return elem;
    }

    public void makeFromXml(InputStream stream) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(stream);

        while (reader.hasNext()) {
            if (reader.isStartElement()) {
                switch (reader.getLocalName()) {
                    case "report":
                        putToMap(reportMap, reader);
                        break;
                    case "scheme":
                        putToMap(schemeMap, reader);
                        break;
                    case "source":
                        putToMap(sourceMap, reader);
                        break;
                }
            }
            reader.next();
        }
    }

    private void putToMap(Map<String, URI> map, XMLStreamReader reader) {
        String name = reader.getAttributeValue(0);
        URI uri = URI.create(reader.getAttributeValue(1));
        map.put(name, uri);
    }
}
