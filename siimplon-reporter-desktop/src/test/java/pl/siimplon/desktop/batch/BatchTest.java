package pl.siimplon.desktop.batch;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

public class BatchTest {
    @Test
    public void testToXml() throws Exception {
        Batch batch = new Batch();
        batch.add(new BatchEntry(new String[]{"a", "b", "c", "d"}, BatchEntry.Type.MAKE));

        Document xml = batch.getXml();

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(xml);

        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
    }

    @Test
    public void testFromXml() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<batch>\n" +
                "  <entry type=\"MAKE\">\n" +
                "    <value>a</value>\n" +
                "    <value>b</value>\n" +
                "    <value>c</value>\n" +
                "    <value>d</value>\n" +
                "    <value>e</value>\n" +
                "  </entry>\n" +
                "</batch>\n";
        Batch batch = new Batch();
        ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
        batch.makeFromXml(stream);

        BatchEntry enty = batch.getEntries().get(0);
        assertNotNull(enty);

        assertEquals(BatchEntry.Type.MAKE, enty.getType());
    }
}