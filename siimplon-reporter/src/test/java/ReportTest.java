import org.junit.Test;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.style.Style;
import pl.siimplon.reporter.report.value.Value;

import java.util.*;

import static org.junit.Assert.*;

public class ReportTest {

    @Test
    public void testCreateWithHeader() throws Exception {
        Report report = createReport();
        report.setHeader(Arrays.asList("TEXT", "NUMBER"));
        List<String> header = report.getHeader();
        assertEquals("TEXT", header.get(0));
        assertEquals("NUMBER", header.get(1));
        assertEquals(2, report.getLength());

        report.add(new Record(Arrays.asList(new Value(10.0), new Value("test"))));
        assertEquals(1, report.getCount());
    }

    @Test
    public void testConditionalStyles() throws Exception {
        Report report = createReport();

        Map<String, Style.Color> conditions = new HashMap<String, Style.Color>();
        conditions.put("VALUE A", Style.Color.BLUE);
        conditions.put("VALUE B", Style.Color.RED);
        conditions.put("VALUE C", Style.Color.YELLOW);

        report.setColumnStyle(1, new Style(Style.Color.WHITE, conditions));

        report.add(new Record(Arrays.asList(new Value(0.0), new Value("VALUE A"))));
        report.add(new Record(Arrays.asList(new Value(0.0), new Value("VALUE B"))));
        report.add(new Record(Arrays.asList(new Value(0.0), new Value("VALUE C"))));

        assertEquals(Style.Color.BLUE, report.getColumnStyle(1).getBGColor("VALUE A"));
        assertEquals(Style.Color.RED, report.getColumnStyle(1).getBGColor("VALUE B"));
        assertEquals(Style.Color.YELLOW, report.getColumnStyle(1).getBGColor("VALUE C"));
    }

    @Test
    public void testColumnStyles() throws Exception {
        Report report = createReport();
        assertEquals(Style.Color.DEFAULT, report.getColumnStyle(0).getBGColor());
        assertEquals(Style.Color.DEFAULT, report.getColumnStyle(1).getBGColor());
    }

    @Test
    public void testSetColumnStyle() throws Exception {
        Report report = createReport();
        report.setColumnStyle(0, new Style(Style.Color.BLUE));
        assertEquals(Style.Color.BLUE, report.getColumnStyle(0).getBGColor());
    }

    @Test
    public void testCreationWithTypes() throws Exception {
        Report report = createReport();
        assertEquals(report.getType(0), Value.Type.NUMBER);
        assertEquals(report.getType(1), Value.Type.LITERAL);
    }

    @Test
    public void testDictionaryByColumn() throws Exception {
        Report report = new Report(Arrays.asList(Value.Type.LITERAL, Value.Type.DICTIONARY));
        report.setDict(makeDict("A", "B", "C"), 1);
        assertNull(report.getDict(0));
        Set<String> dict = report.getDict(1);
        assertTrue(dict.contains("A"));
        assertTrue(dict.contains("B"));
        assertTrue(dict.contains("C"));

        assertFalse(dict.contains("D"));
    }

    @Test
    public void testFindColumnByHeaderName() throws Exception {
        Report report = new Report(Arrays.asList(Value.Type.LITERAL, Value.Type.LITERAL));
        report.setHeader(Arrays.asList("COLUMN A", "COLUMN B"));
        assertEquals(0, report.getColumnIxByName("COLUMN A"));
        assertEquals(0, report.getColumnIxByName("column a"));
        assertEquals(1, report.getColumnIxByName("COLUMN B"));
        assertEquals(1, report.getColumnIxByName("column b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongValueType() throws Exception {
        Report report = createReport();
        report.add(new Record(Arrays.asList(
                new Value(Value.Type.LITERAL),
                new Value(Value.Type.NUMBER)
        )));
    }

    @Test
    public void testGetStringValues() throws Exception {
        Report report = createReport();
        report.add(Arrays.<Object>asList(10.0, "test"));
        List<String> strings = report.getStrings(0);
        assertEquals(strings.get(0), "10.0");
        assertEquals(strings.get(1), "test");
    }

    @Test
    public void testAddByListOfStrings() throws Exception {
        Report report = createReport();
        report.addRecord(Arrays.asList("10.0", "test"));
        assertEquals(1, report.getCount());
    }

    @Test
    public void testCreateLiteralBySize() throws Exception {
        Report simpleReport = new Report(3);
        for (int i = 0; i < 3; i++) {
            assertEquals(Value.Type.LITERAL, simpleReport.getType(i));
        }
    }

    @Test
    public void testGetRecordsByValues() throws Exception {
        Report report = createReport();
        List<Object> a = Arrays.<Object>asList(10.0, "test");
        report.add(a);
        List<Object> b = Arrays.<Object>asList(12.34, "test");
        report.add(b);
        List<Record> content = report.getValuesByContent(Arrays.asList("10.0", "test"), Arrays.asList(0, 1));
        assertEquals(1, content.size());
        assertEquals(content.get(0).getValue(0).getValue(), a.get(0));
        assertEquals(content.get(0).getValue(1).getValue(), a.get(1));
    }

    @Test
    public void testAddRecordByValuesOnly() throws Exception {
        Report report = createReport();
        report.add(Arrays.<Object>asList(10.0, "test"));
        assertEquals(1, report.getCount());
    }

    @Test
    public void testMergeReports() throws Exception {
        Report a = createReport();
        a.addRecord("1.0", "value a");
        Report b = createReport();
        b.addRecord("2.0", "value b");

        b.add(a);
        assertEquals(1, a.getCount());
        assertEquals(2, b.getCount());
    }

    private Report createReport() {
        return new Report(Arrays.asList(Value.Type.NUMBER, Value.Type.LITERAL));
    }

    private Set<String> makeDict(String... values) {
        Set<String> strings = new HashSet<String>();
        Collections.addAll(strings, values);
        return strings;
    }
}