package pl.siimplon.reporter.report.record;

import pl.siimplon.reporter.report.value.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Record {

    private final List<Value> values;

    /**
     * Creates simple record implementation with ready values.
     * Number of values determines length of this record.
     *
     * @param values Values to be in this record.
     */
    public Record(List<Value> values) {
        this.values = values;
    }

    /**
     * Creates simple record with new values of wanted type.
     * All values will have default content.
     *
     * @param types Values type.
     */
    public Record(Value.Type... types) {
        List<Value> values = new ArrayList<Value>();
        for (Value.Type type : types) {
            values.add(new Value(type));
        }
        this.values = values;
    }

    public Value getValue(int ix) {
        return values.get(ix);
    }

    public void setValue(int ix, Value value) {
        values.set(ix, value);
    }

    public final List<String> getStringValues() {
        List<String> result = new ArrayList<String>();
//        values.forEach(t -> result.add(valueToString(t)));
        for (Value value : values) {
            result.add(valueToString(value));
        }
        return result;
    }

    public List<Value> getValues() {
        return Collections.unmodifiableList(values);
    }

    public int getLength() {
        return values.size();
    }

    protected String valueToString(Value value) {
        return value.toString();
    }

    public boolean hasValues(List<String> expValues, List<Integer> expCols) {
        for (int i = 0; i < expCols.size(); i++) {
            if (!expValues.get(i).equals(getValues().get(expCols.get(i)).toString())) return false;
        }
        return true;
    }
}
