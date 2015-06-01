package pl.siimplon.reporter.scheme.transfer;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TransferPairTest {
    @Test
    public void testToString() throws Exception {
        assertEquals("EMPTY", new TransferPair(Transfer.EMPTY, "").toString());
        assertEquals("COUNT_DISTINCT_VALUES:report, 3", new TransferPair(Transfer.COUNT_DISTINCT_VALUES, "report", 3)
                .toString());
        assertEquals("COUNT_DISTINCT_VALUES_CONDITIONAL:report, 12, [column], [value]",
                new TransferPair(Transfer.COUNT_DISTINCT_VALUES_CONDITIONAL, "report", 12,
                        Arrays.asList("column"), Arrays.asList("value")).toString());
        assertEquals("COUNT_DISTINCT_VALUES_CONDITIONAL:report, 12, [column_0, column_1], [1, 2]",
                new TransferPair(Transfer.COUNT_DISTINCT_VALUES_CONDITIONAL, "report", 12,
                        Arrays.asList("column_0", "column_1"), Arrays.asList(1, 2)).toString());
    }
}