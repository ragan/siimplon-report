package pl.siimplon.reporter;

import org.junit.Test;
import org.w3c.dom.*;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ReportContextXMLTest {

    @Test
    public void testSimpleTransferList() throws Exception {
        ReportContext context = new ReportContext();
        context.putTransfer(Arrays.asList(new TransferPair(Transfer.VALUE, "test"),
                new TransferPair(Transfer.GET_PART_VAL, "some-report", 1, Arrays.asList("blah", "blah"), Arrays.asList(1, 2))), "test-transfer");

        Document xml = context.getTransferXML("test-transfer");

        Element documentElement = xml.getDocumentElement();
        assertNotNull(documentElement);
        assertEquals(ReportContext.TRANSFER_PAIR_LIST, documentElement.getTagName());

        NodeList pairElements = documentElement.getElementsByTagName(ReportContext.TRANSFER_PAIR_LIST_TRANSFERPAIR);
        assertEquals(2, pairElements.getLength());
        Node item = pairElements.item(0);
        NamedNodeMap attributes = item.getAttributes();
        assertEquals(Transfer.VALUE.name(),
                attributes.getNamedItem(ReportContext.TRANSFER_PAIR_LIST_TRANSFERPAIR_ATTR_TYPE).getNodeValue());
//        Node stringElement = item.getFirstChild();
//        assertEquals(Transfer.Descriptor.STRING.name(), stringElement.getNodeName());

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(xml);

        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
        // Output to console for testing
    }

    @Test
    public void testParseTransferListXML() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<list name=\"test-transfer\">\n" +
                "  <pair type=\"VALUE\">\n" +
                "    <entry type=\"STRING\" value=\"test\"/>\n" +
                "  </pair>\n" +
                "  <pair type=\"GET_PART_VAL\">\n" +
                "    <entry type=\"STRING\" value=\"some-report\"/>\n" +
                "    <entry type=\"INTEGER\" value=\"1\"/>\n" +
                "    <entry type=\"STRING_VECTOR\" value=\"blah,bllah\"/>\n" +
                "    <entry type=\"INTEGER_VECTOR\" value=\"1,2\"/>\n" +
                "  </pair>\n" +
                "  <pair type=\"GET_PART_VAL\">\n" +
                "    <entry type=\"STRING\" value=\"some-report 2\"/>\n" +
                "    <entry type=\"INTEGER\" value=\"1\"/>\n" +
                "    <entry type=\"STRING_VECTOR\" value=\"blah,bllah,srah\"/>\n" +
                "    <entry type=\"INTEGER_VECTOR\" value=\"1,2\"/>\n" +
                "  </pair>\n" +
                "  <pair type=\"VALUE\">\n" +
                "    <entry type=\"STRING\" value=\"test 2\"/>\n" +
                "  </pair>\n" +
                "</list>";
        ReportContext context = new ReportContext();
        context.parseXMLTransferList(new ByteArrayInputStream(xml.getBytes()));

        List<TransferPair> transfer = context.getTransfer("test-transfer");
    }
}