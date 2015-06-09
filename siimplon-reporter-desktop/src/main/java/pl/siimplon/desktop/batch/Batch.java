package pl.siimplon.desktop.batch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Batch {

    private final List<BatchEntry> entries = new ArrayList<>();

    public void set(int i, BatchEntry entry) {
        entries.set(i, entry);
    }

    public void add(BatchEntry batchEntry) {
        entries.add(batchEntry);
    }

    public void remove(BatchEntry batchEntry) {
        entries.remove(batchEntry);
    }

    public void remove(int i) {
        entries.remove(i);
    }

    public List<BatchEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public Document getXml() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.newDocument();
        Element root = document.createElement("batch");
        for (BatchEntry entry : getEntries()) {
            Element element = document.createElement("entry");
            element.setAttribute("type", entry.getType().name());
            root.appendChild(element);

            for (String s : entry.getValues()) {
                Element value = document.createElement("value");
                value.setTextContent(s);
                element.appendChild(value);
            }
        }
        document.appendChild(root);

        return document;
    }

    public void makeFromXml(InputStream stream) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(stream);

        int valueIdx = 0;
        BatchEntry batchEntry = null;
        BatchEntry.Type type;
        while (reader.hasNext()) {

            if (reader.isStartElement()) {
                switch (reader.getLocalName()) {
                    case "entry":
                        type = BatchEntry.Type.valueOf(reader.getAttributeValue(0));
                        batchEntry = new BatchEntry(type);
                        break;
                    case "value":
                        if (batchEntry != null) {
                            batchEntry.values[valueIdx] = reader.getElementText();
                            valueIdx++;
                        }
                        break;
                }
            } else if (reader.isEndElement()) {
                switch (reader.getLocalName()) {
                    case "entry":
                        add(batchEntry);
                        batchEntry = null;
                        valueIdx = 0;
                        type = null;
                        break;
                }
            }
            reader.next();
        }

    }

}
