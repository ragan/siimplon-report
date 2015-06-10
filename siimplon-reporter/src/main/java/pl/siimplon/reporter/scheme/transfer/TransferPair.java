package pl.siimplon.reporter.scheme.transfer;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class TransferPair {

    public final Transfer source;

    public final Object[] attributes;

    public TransferPair(Transfer source, Object... attributes) {
        this.source = source;
        this.attributes = attributes;
    }

    public Transfer getSource() {
        return source;
    }

    public Object[] getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(source.name());
        if (source.getAttrSize() > 0) sb.append(":");
        for (int i = 0; i < source.getAttrSize(); i++) {
            sb.append(String.valueOf(attributes[i]));
            if (i != source.getAttrSize() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public String getAttributeString(int i) {
        Transfer.Descriptor descriptor = getSource().getDescriptors()[i];
        switch (descriptor) {
            case INTEGER:
            case STRING:
                return String.valueOf(getAttributes()[i]);
            case INTEGER_VECTOR:
            case STRING_VECTOR:
                List o = (List) getAttributes()[i];
                StringBuilder sb = new StringBuilder();
                for (Object val : o) {
                    sb.append(String.valueOf(val)).append(",");
                }
                if (!sb.toString().isEmpty())
                    sb.deleteCharAt(sb.length() - 1);
                return sb.toString();
        }
        return "";
    }

    public void setAttribute(String attributeValue, int attrIx) {
        Transfer.Descriptor descriptor = getSource().getDescriptors()[attrIx];
        switch (descriptor) {
            case INTEGER:
                attributes[attrIx] = Integer.valueOf(attributeValue);
                break;
            case STRING:
                attributes[attrIx] = String.valueOf(attributeValue);
                break;
            case INTEGER_VECTOR:
                ArrayList<Integer> vals = new ArrayList<>();
                String[] split = attributeValue.split(",");
                if (split.length == 1 && split[0].isEmpty()) {
                    attributes[attrIx] = Collections.emptyList();
                    return;
                }
                for (int i = 0; i < split.length; i++) {
                    split[i] = split[i].trim();
                    vals.add(Integer.valueOf(split[i]));
                }
                attributes[attrIx] = vals;
                break;
            case STRING_VECTOR:
                ArrayList<String> stringVals = new ArrayList<>();
                String[] stringSplit = attributeValue.split(",");
                if (stringSplit.length == 1 && stringSplit[0].isEmpty()) {
                    attributes[attrIx] = Collections.emptyList();
                    return;
                }
                for (int i = 0; i < stringSplit.length; i++) {
                    stringSplit[i] = stringSplit[i].trim();
                    stringVals.add(stringSplit[i]);
                }
                attributes[attrIx] = stringVals;
                break;
        }
    }
}
