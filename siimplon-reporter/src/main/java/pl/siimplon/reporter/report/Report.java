package pl.siimplon.reporter.report;

import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.style.Style;
import pl.siimplon.reporter.report.value.Value;

import java.util.*;

public class Report {

    private List<Record> records = new ArrayList<Record>();

    private List<Value.Type> types;

    private List<Style> styles;

    private List<String> header = new Vector<String>();

    private List<Set<String>> dictionaries;

    public Report(List<Value.Type> types, List<Style> styles) {
        if (types == null)
            throw new IllegalArgumentException("Types list should not be null!");
        this.types = types;
        if (styles == null || styles.size() != types.size()) {
            styles = new ArrayList<Style>();
            for (Value.Type ignored : types) styles.add(new Style(Style.Color.DEFAULT));
        }
        this.styles = styles;
        makeDictionaries(types);
    }

    private void makeDictionaries(List<Value.Type> types) {
        dictionaries = new ArrayList<Set<String>>();
        for (Value.Type ignored : types) dictionaries.add(null);
    }

    public Report(List<Value.Type> types) {
        this(types, Collections.<Style>emptyList());
    }

    public Report(Value.Type... types) {
        this(Arrays.asList(types));
    }

    /**
     * Creates a report with set length. All value types will be Type.LITERAL.
     *
     * @param size Size of report.
     */
    public Report(int size) {
        this(new ArrayList<Value.Type>(), new ArrayList<Style>());
        for (int i = 0; i < size; i++) {
            types.add(Value.Type.LITERAL);
            styles.add(new Style(Style.Color.DEFAULT));
        }
        makeDictionaries(types);
    }

    public int getLength() {
        if (header.size() == 0)
            return types.size();
        else
            return header.size();
    }

    public void setColumnStyle(int column, Style style) {
        styles.set(column, style);
    }

    public List<String> getStrings(int recordIx) {
        return getRecords().get(recordIx).getStringValues();
    }

    public List<Record> getValuesByContent(List<String> expValues, List<Integer> expCols) {
        List<Record> values = new ArrayList<Record>();
        for (Record record : getRecords()) {
            if (record.hasValues(expValues, expCols)) values.add(record);
        }
        return values;
    }

    public void setDict(Set<String> strings, int i) {
        dictionaries.set(i, strings);
    }

    public Set<String> getDict(int i) {
        return dictionaries.get(i);
    }

    public int getColumnIxByName(String s) {
        for (int i = 0; i < header.size(); i++) {
            if (s.equalsIgnoreCase(header.get(i))) return i;
        }
        return -1;
    }

    public List<String> getStringsByContent(List<String> expValues, List<Integer> expCols) {
        for (Record record : getRecords()) {
            if (record.hasValues(expValues, expCols)) return record.getStringValues();
        }
        return Collections.emptyList();
    }

    public int getCount() {
        return records.size();
    }

    public void addRecord(List<String> values) {
        List<Object> vls = new ArrayList<Object>();
        for (String value : values) {
            vls.add(value);
        }
        add(vls);
    }

    public List<Record> getRecords() {
        return records;
    }

    public Style getColumnStyle(int column) {
        return styles.get(column);
    }

    public void add(List<Object> values) {
        List<Value> vls = new ArrayList<Value>();
        for (int i = 0; i < values.size(); i++) {
            Value v = new Value(types.get(i), getDict(i));
            v.setValue(values.get(i));
            vls.add(v);
        }
        add(new Record(vls));
    }

    public void add(Record e) {
        if (!recordFits(e))
            throw new IllegalArgumentException("Incompatible value type.");
        records.add(e);
    }

    public Value.Type getType(int i) {
        return types.get(i);
    }

    private boolean recordFits(Record record) {
        for (int i = 0; i < record.getLength(); i++) {
            if (record.getValue(i).getType() != types.get(i)) return false;
        }
        return true;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<String> getHeader() {
        return header;
    }

    public void addRecord(String... values) {
        addRecord(Arrays.asList(values));
    }

    public void add(Report report) {
        for (Record record : report.getRecords()) {
            add(record);
        }
    }
}
