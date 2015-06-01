package pl.siimplon.reporter.scheme.transfer;

import java.util.Vector;

public enum Transfer {
    /**
     * Get attribute from main feature. <br />
     * [0] = attribute name.
     * <p/>
     */
    MAIN_ATTRIBUTE(1),
    /**
     * Get attribute from other feature. <br />
     * [0] = attribute name.
     * <p/>
     */
    OTHER_ATTRIBUTE(1),
    /**
     * Empty value.
     */
    EMPTY,
    /**
     * Inserts value into column. <br />
     * [0] = value to be inserted.
     */
    VALUE(1),
    /**
     * Inserts number according to current part row number.
     */
    NUM_PART,
    /**
     * Inserts value of main feature area.
     */
    MAIN_PART_AREA,
    /**
     * Inserts value of other feature area. <br />
     * [0] = decimal number round
     */
    OTHER_PART_AREA,
    /**
     * Inserts value of main feature length. <br />
     * [0] = decimal number round
     */
    MAIN_PART_LENGTH(1, new Class[]{Integer.class}),
    /**
     * Get value from a part column where row values are equal to indicated values. <br />
     * [0] = part name <br />
     * [1] = column where wanted value should be<br />
     * [2] = expected values in row as list of strings<br />
     * [3] = columns where expected values are supposed to be as list of Integers.<br />
     */
    GET_PART_VAL(4, new Class[]{String.class, Integer.class, Vector.class, Vector.class}),
    /**
     * Inserts value of other feature length.
     */
    OTHER_PART_LENGTH(1, new Class[]{Integer.class}),
    /**
     * Calculate length of union of both main and other feature.
     * TODO: formatting like PART_LENGTH (decimal round)
     */
    UNION_LENGTH,

    GET_PART_NUM_AND_SUM(4, new Class[]{String.class, Integer.class, Vector.class, Vector.class}),

    PERCENT_CONDITION(4, new Class[]{String.class, Integer.class, Vector.class, Vector.class}),
    /**
     * Calculate area of union of both main and other feature. If Feature is not a
     * polygon result will be 0.0.
     */
    UNION_AREA,

    UNION_LENGTH_REVERSED,

    /**
     * Finds records with given values at given column indices
     * and returns percentage (recordsFound / allReportRecords) <br />
     * <ul>
     * <li>[0] - report</li>
     * <li>[1] - list of values expected</li>
     * <li>[2] - list of column indices</li>
     * </ul>
     */
    PERCENT_FROM_RECORD_COUNT(3, new Class[]{String.class, Vector.class, Vector.class}),

    /**
     * Finds records with given values at given column indices.
     * Counts distinct values from indicated column. <br />
     * Returns percentage (value found / distinct values count).
     * <ul>
     * <li>[0] - report</li>
     * <li>[1] - distinct values column index</li>
     * <li>[2] - list of searched valued</li>
     * <li>[3] - list of column indices</li>
     * </ul>
     */
    PERCENT_FROM_DISTINCT_VALUES(4, new Class[]{String.class, Integer.class, Vector.class, Vector.class}),

    COUNT_DISTINCT_VALUES(2, new Class[]{String.class, Integer.class}),

    /**
     * Counts distinct column values from found records.
     * <ul>
     * <li>[0] - report</li>
     * <li>[1] - distinct value column</li>
     * <li>[2] - list of values (conditions)</li>
     * <li>[3] - list of column indices (conditions)</li>
     * </ul>
     */
    COUNT_DISTINCT_VALUES_CONDITIONAL(4, new Class[]{String.class, Integer.class, Vector.class, Vector.class});

    private int attrSize;
    private Class[] descriptors;

    Transfer() {
        this(0, new Class[]{});
    }

    Transfer(int attrSize) {
        Class[] desc = new Class[attrSize];
        for (int i = 0; i < desc.length; i++) {
            desc[i] = String.class;
        }
        this.descriptors = desc;
    }

    Transfer(int attrSize, Class[] descriptors) {
        this.attrSize = attrSize;
        this.descriptors = descriptors;
    }
}