package pl.siimplon.reporter.scheme.transfer;

public enum Transfer {
    /**
     * Get attribute from main feature. <br />
     * [0] = attribute name.
     * <p/>
     */
    MAIN_ATTRIBUTE,
    /**
     * Get attribute from other feature. <br />
     * [0] = attribute name.
     * <p/>
     */
    OTHER_ATTRIBUTE,
    /**
     * Empty value.
     */
    EMPTY,
    /**
     * Inserts value into column. <br />
     * [0] = value to be inserted.
     */
    VALUE,
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
    MAIN_PART_LENGTH,
    /**
     * Get value from a part column where row values are equal to indicated values. <br />
     * [0] = part name <br />
     * [1] = column where wanted value should be<br />
     * [2] = expected values in row as list of strings<br />
     * [3] = columns where expected values are supposed to be as list of Integers.<br />
     */
    GET_PART_VAL,
    /**
     * Inserts value of other feature length.
     */
    OTHER_PART_LENGTH,
    /**
     * Calculate length of union of both main and other feature.
     * TODO: formatting like PART_LENGTH (decimal round)
     */
    UNION_LENGTH,

    GET_PART_NUM_AND_SUM,

    PERCENT_CONDITION,
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
    PERCENT_FROM_RECORD_COUNT,

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
    PERCENT_FROM_DISTINCT_VALUES,

    COUNT_DISTINCT_VALUES,

    /**
     * Counts distinct column values from found records.
     * <ul>
     * <li>[0] - report</li>
     * <li>[1] - distinct value column</li>
     * <li>[2] - list of values (conditions)</li>
     * <li>[3] - list of column indices (conditions)</li>
     * </ul>
     */
    COUNT_DISTINCT_VALUES_CONDITIONAL
}