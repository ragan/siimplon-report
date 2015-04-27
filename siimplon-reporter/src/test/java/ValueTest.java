import org.junit.Test;
import pl.siimplon.reporter.report.value.Value;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class ValueTest {

    public static final String DEFAULT_STRING_VALUE = "";
    public static final double DEFAULT_NUMBER_VALUE = 0.0;

    @Test
    public void testCreateLiteral() throws Exception {
        Value value = new Value(Value.Type.LITERAL);
        assertIsLiteral(value);
        assertEquals(value.getValue(), DEFAULT_STRING_VALUE);
    }

    @Test
    public void testDictionaryCreate() throws Exception {
        HashSet<String> set = makeHashSet("A", "B", "C");
        Value value = new Value(Value.Type.DICTIONARY, set);
        assertEquals("", value.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyDictionary() throws Exception {
        new Value(Value.Type.DICTIONARY, new HashSet<String>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullDictionary() throws Exception {
        new Value(Value.Type.DICTIONARY, null);
    }

    private HashSet<String> makeHashSet(String... values) {
        HashSet<String> set = new HashSet<String>();
        Collections.addAll(set, values);
        return set;
    }

    @Test
    public void testCreateNumber() throws Exception {
        Value value = new Value(Value.Type.NUMBER);
        assertIsNumber(value);
        assertEquals((Double) value.getValue(), DEFAULT_NUMBER_VALUE, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWrongDictionaryValue() throws Exception {
        Value value = new Value(Value.Type.DICTIONARY, makeHashSet("A", "B", "C"));
        value.setValue("A");
        assertEquals("A", value.getValue());
        value.setValue("D");
        assertEquals("A", value.getValue());
    }

    @Test
    public void testSetValueByDictionary() throws Exception {
        Value value = new Value(Value.Type.DICTIONARY, makeHashSet("A", "B", "C"));
        value.setValue("A");
        assertEquals("A", value.getValue());
        value.setValue("B");
        assertEquals("B", value.getValue());
    }

    @Test
    public void testCreateWithNumberValue() throws Exception {
        double num = 10.0;
        Value value = new Value(num);
        assertIsNumber(value);
        assertEquals(num, value.getValue());
    }

    @Test
    public void testSetStringValue() throws Exception {
        String s = "test";
        Value value = new Value(Value.Type.LITERAL);
        value.setValue(s);
        assertEquals(s, value.getValue());
    }

    @Test
    public void testInputNumberAsString() throws Exception {
        Value value = new Value(Value.Type.NUMBER);
        value.setValue("10.1234");
        assertEquals(10.1234, value.getValue());
    }

    @Test
    public void testWithStringValue() throws Exception {
        String s = "test";
        Value value = new Value(s);
        assertIsLiteral(value);
        assertEquals(s, value.getValue());
    }

    @Test
    public void testNumberToString() throws Exception {
        Value value = new Value(10.0);
        assertEquals(String.valueOf(10.0), value.toString());
    }

    private void assertIsLiteral(Value value) {
        assertEquals(Value.Type.LITERAL, value.getType());
    }

    private void assertIsNumber(Value value) {
        assertEquals(Value.Type.NUMBER, value.getType());
    }
}