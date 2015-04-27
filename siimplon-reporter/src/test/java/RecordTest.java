import org.junit.Test;
import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.value.Value;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecordTest {

    @Test
    public void testCreateByTypesOnly() throws Exception {
        Record record = new Record(Value.Type.NUMBER, Value.Type.LITERAL);
        assertEquals(record.getValue(0).getValue(), ValueTest.DEFAULT_NUMBER_VALUE);
        assertEquals(record.getValue(1).getValue(), ValueTest.DEFAULT_STRING_VALUE);
    }

    @Test
    public void testCreateWithValues() throws Exception {
        Record record = createDefaultRecord();
        assertEquals(10.0, record.getValue(0).getValue());
        assertEquals("test", record.getValue(1).getValue());
    }

    @Test
    public void testGetStringValues() throws Exception {
        Record record = createDefaultRecord();
        List<String> strings = record.getStringValues();
        assertEquals("10.0", strings.get(0));
        assertEquals("test", strings.get(1));
    }

    @Test
    public void testHasValues() throws Exception {
        Record record = createDefaultRecord();
        assertTrue(record.hasValues(Arrays.asList("10.0", "test"), Arrays.asList(0, 1)));
        assertTrue(record.hasValues(Arrays.asList("test", "10.0"), Arrays.asList(1, 0)));
    }

    private Record createDefaultRecord() {
        return new Record(Arrays.asList(new Value(10.0), new Value("test")));
    }
}