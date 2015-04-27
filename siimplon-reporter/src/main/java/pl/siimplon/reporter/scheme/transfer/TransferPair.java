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
}
