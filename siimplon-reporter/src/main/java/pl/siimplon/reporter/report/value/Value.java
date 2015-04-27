package pl.siimplon.reporter.report.value;

import java.util.Set;

public class Value {

    public enum Type {NUMBER, LITERAL, DICTIONARY}

    private Type type;

    private Set<String> dict;

    private Object value;

    public Value(Type type) {
        this(type, null);
    }

    public Value(Type type, Set<String> dict) {
        this.type = type;

        switch (type) {
            case LITERAL:
                this.value = "";
                break;
            case NUMBER:
                this.value = 0.0;
                break;
            case DICTIONARY:
                if (dict == null || dict.isEmpty())
                    throw new IllegalArgumentException(String.format("Dictionary values must have non empty dictionaries."));
                this.dict = dict;
                this.value = "";
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        switch (type) {
            case DICTIONARY:
            case LITERAL:
                return (String) value;
            case NUMBER:
                return String.valueOf(value);
            default:
                return null;
        }
    }

    public Value(double value) {
        this(Type.NUMBER);
        this.value = value;
    }

    public Value(String value) {
        this(Type.LITERAL);
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        switch (type) {
            case LITERAL:
                this.value = value;
                break;
            case NUMBER:
                try {
                    this.value = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    this.value = 0.0;
                }
                break;
            case DICTIONARY:
                if (!dict.contains(value))
                    throw new IllegalArgumentException(String.format("Unknown dictionary value: %s.", value));
                else this.value = value;
                break;
        }
    }

    public void setValue(Object o) {
        setValue(String.valueOf(o));
    }

}
