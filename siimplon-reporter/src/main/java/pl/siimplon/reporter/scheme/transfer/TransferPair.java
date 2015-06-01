package pl.siimplon.reporter.scheme.transfer;

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
        sb.append(source.name()).append(" : ");
        for (int i = 0; i < source.getAttrSize(); i++) {
            sb.append(source.getDescriptors()[i].toString());
        }

        return super.toString();
    }
}
