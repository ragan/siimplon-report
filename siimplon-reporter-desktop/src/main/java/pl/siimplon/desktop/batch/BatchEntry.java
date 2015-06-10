package pl.siimplon.desktop.batch;

import java.util.Arrays;
import java.util.List;

public class BatchEntry {

    public enum Type {
        MAKE, MERGE
    }

    private Type type;

    protected String[] values;

    protected BatchEntry(Type type) {
        this(new String[0]);
        int size = 0;
        if (type == Type.MAKE) size = 5;
        else if (type == Type.MERGE) size = 3;
        values = new String[size];
        if (size == 3) this.type = Type.MERGE;
    }

    public BatchEntry(String[] values) {
        this(values, Type.MAKE);
    }

    public BatchEntry(String[] values, Type type) {
        this.values = values;
        this.type = type;
    }

    public List<String> getValues() {
        return Arrays.asList(values);
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.type.name()).append(": ");
        sb.append("[ ");
        for (String value : values) {
            sb.append(value + " ");
        }
        sb.append("]");

        return sb.toString();
    }
}