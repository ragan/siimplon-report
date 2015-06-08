package pl.siimplon.desktop;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class SourcesContextTest {

    @Test
    public void testAddRemoveReport() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addReportURI("report", new File("/").toURI());
        assertNotNull(context.getReportURI("report"));
        context.delReportURI("report");
        assertNull(context.getReportURI("report"));
    }

    @Test
    public void testAddRemoveSource() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addSourceURI("source", new File("/").toURI());
        assertNotNull(context.getSourceURI("source"));
        context.delSourceURI("source");
        assertNull(context.getSourceURI("source"));
    }

    @Test
    public void testAddRemoveScheme() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addSchemeURI("scheme", new File("/").toURI());
        assertNotNull(context.getSchemeURI("scheme"));
        context.delSchemeURI("scheme");
        assertNull(context.getSchemeURI("scheme"));
    }

    @Test
    public void testReportToXML() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addReportURI("report", new File("/").toURI());

        Document document = context.makeXmlDocument();
        assertNotNull(document);
        Element element = document.getDocumentElement();
        assertNotNull(element);
        assertEquals(1, element.getChildNodes().getLength());
    }

    @Test
    public void testSchemeToXml() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addSchemeURI("scheme", new File("/").toURI());

        Document document = context.makeXmlDocument();
        assertNotNull(document);
        Element element = document.getDocumentElement();
        assertNotNull(element);
        assertEquals(1, element.getChildNodes().getLength());
        Node child = element.getFirstChild();
        assertEquals(2, child.getAttributes().getLength());
        assertEquals("scheme", child.getAttributes().getNamedItem("name").getNodeValue());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);

        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
    }

    @Test
    public void testReadXml() throws Exception {
        SourcesContext outContext = new SourcesContext();

        File file = new File("/");
        outContext.addReportURI("report", file.toURI());
        outContext.addSchemeURI("scheme", file.toURI());
        outContext.addSourceURI("source", file.toURI());

        Document document = outContext.makeXmlDocument();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);

        String s = writer.getBuffer().toString();
        System.out.println(s);

        SourcesContext inContext = new SourcesContext();
        inContext.makeFromXml(new ByteArrayInputStream(s.getBytes()));
        assertNotNull(inContext.getReportURI("report"));
        assertNotNull(inContext.getSchemeURI("scheme"));
        assertNotNull(inContext.getSourceURI("source"));


    }

    @Test
    public void testSourceToXml() throws Exception {
        SourcesContext context = new SourcesContext();
        context.addSourceURI("source", new File("/").toURI());

        Document document = context.makeXmlDocument();
        assertNotNull(document);
        Element element = document.getDocumentElement();
        assertNotNull(element);
        assertEquals(1, element.getChildNodes().getLength());
    }
}