package pl.siimplon.reporter.scheme;

import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RowScheme {

    public static final String MCR_PARAM_MAIN = "mcr_param_main";
    public static final String MCR_PARAM_OTHER = "mcr_param_other";

    private List<TransferPair> pairs;

    public RowScheme(List<TransferPair> pairs) {
        this(pairs, 0);
    }

    public RowScheme(List<TransferPair> pairs, int offset) {
        this.pairs = new Vector<TransferPair>();
        for (int i = 0; i < offset; i++) {
            this.pairs.add(new TransferPair(Transfer.EMPTY, ""));
        }
        this.pairs.addAll(pairs);
    }

    public String[] getRowValues(AnalyzeItem main, Report report) {
        return getRowValues(main, null, report);
    }

    public String[] getRowValues(AnalyzeItem main, AnalyzeItem other, Report report) {
        String[] values = new String[pairs.size()];
        for (int i = 0; i < pairs.size(); ++i) {
            TransferPair p = pairs.get(i);
            switch (p.getSource()) {
                case PERCENT_FROM_RECORD_COUNT:
                    values[i] = onPercentRecordCount(
                            ((Report) p.getAttributes()[0]),
                            ((List<String>) p.getAttributes()[1]),
                            ((List<Integer>) p.getAttributes()[2])
                    );
                    break;
                case PERCENT_FROM_DISTINCT_VALUES:
                    values[i] = onPercentFromDistinctValues(
                            ((Report) p.getAttributes()[0]),
                            ((Integer) p.getAttributes()[1]),
                            ((List<String>) p.getAttributes()[2]),
                            ((List<Integer>) p.getAttributes()[3])
                    );
                    break;
                case COUNT_DISTINCT_VALUES_CONDITIONAL:
                    values[i] = onCountDistinctValuesConditional(
                            ((Report) p.getAttributes()[0]),
                            ((Integer) p.getAttributes()[1]),
                            ((List<String>) p.getAttributes()[2]),
                            ((List<Integer>) p.getAttributes()[3])
                    );
                    break;
                case EMPTY:
                    values[i] = "";
                    break;
                case MAIN_ATTRIBUTE:
                    if (main == null)
                        throw new IllegalArgumentException("[MAIN_ATTRIBUTE] Error: Main source not present.");
                    values[i] = getAttributeStringValue(main, (String) p.getAttributes()[0]);
                    break;
                case MAIN_PART_AREA:
                    if (main == null)
                        throw new IllegalArgumentException("[MAIN_PART_AREA] Error: Other source not present.");
                    values[i] = onPartArea(main, p.getAttributes()[0]);
                    break;
                case MAIN_PART_LENGTH:
                    if (main == null)
                        throw new IllegalArgumentException("[MAIN_PART_LENGTH] Error: Main source not present.");
                    values[i] = onPartLength(main, p.getAttributes()[0]);
                    break;
                case VALUE:
                    values[i] = (String) p.getAttributes()[0];
                    break;
                case NUM_PART:
                    values[i] = String.valueOf(report.getCount());
                    break;
                case COUNT_DISTINCT_VALUES:
                    values[i] = String.valueOf(countDistinctValues(
                            ((Report) p.getAttributes()[0]),
                            ((Integer) p.getAttributes()[1])
                    ));
                    break;
                case OTHER_PART_LENGTH:
                    if (other == null)
                        throw new IllegalArgumentException("[OTHER_PART_LENGTH] Error: Other source not present.");
                    values[i] = onPartLength(other, p.getAttributes()[0]);
                    break;
                case OTHER_PART_AREA:
                    if (other == null)
                        throw new IllegalArgumentException("[OTHER_PART_AREA] Error: Other source not present.");
                    values[i] = onPartArea(other, p.getAttributes()[0]);
                    break;
                case OTHER_ATTRIBUTE:
                    if (other == null)
                        throw new IllegalArgumentException("[OTHER_ATTRIBUTE] Error: Other source not present.");
                    values[i] = getAttributeStringValue(other, (String) p.getAttributes()[0]);
                    break;
                case GET_PART_VAL:
                    if (p.getAttributes()[0] != null && p.getAttributes()[0] instanceof Report) {
                        values[i] = onGetPartVal((Report) p.getAttributes()[0],
                                changeValuesByMacro((List<String>) p.getAttributes()[2], main, other),
                                (List<Integer>) p.getAttributes()[3],
                                (Integer) p.getAttributes()[1]);
                    } else {
                        values[i] = onGetPartVal(report,
                                changeValuesByMacro((List<String>) p.getAttributes()[2], main, other),
                                (List<Integer>) p.getAttributes()[3],
                                (Integer) p.getAttributes()[1]);
                    }
                    break;
                case UNION_LENGTH:
                    values[i] = String.valueOf(getUnionLength(main, other));
                    break;
                case UNION_LENGTH_REVERSED:
                    values[i] = String.valueOf(getUnionLength(other, main));
                    break;
                case UNION_AREA:
                    values[i] = String.valueOf(getUnionArea(main, other));
                    break;
                case GET_PART_NUM_AND_SUM:
                    values[i] = onGetSum((Report) p.getAttributes()[0],
                            (Integer) p.getAttributes()[1],
                            changeValuesByMacro((List<String>) p.getAttributes()[2], main, other),
                            (List<Integer>) p.getAttributes()[3], false);
                    break;
                case GET_PART_NUM_AND_SUM_ZERO:
                    values[i] = onGetSum((Report) p.getAttributes()[0],
                            (Integer) p.getAttributes()[1],
                            changeValuesByMacro((List<String>) p.getAttributes()[2], main, other),
                            (List<Integer>) p.getAttributes()[3], true);
                    break;
                case PERCENT_CONDITION:
                    values[i] = onGetPercentage((Report) p.getAttributes()[0],
                            (Integer) p.getAttributes()[1],
                            changeValuesByMacro((List<String>) p.getAttributes()[2], main, other),
                            (List<Integer>) p.getAttributes()[3]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command.");
            }
        }
        return values;
    }

    private String onCountDistinctValuesConditional(Report report, Integer distinctColumn, List<String> values,
                                                    List<Integer> columns) {
        List<Record> content = report.getValuesByContent(values, columns);
        return String.valueOf(countDistinctValues(content, distinctColumn));
    }

    private String onPercentFromDistinctValues(Report report, Integer distinctColumn, List<String> values,
                                               List<Integer> columns) {
        List<Record> content = report.getValuesByContent(values, columns);
        int distinctValues = countDistinctValues(report, distinctColumn);
        double val;
        if (content.size() > 0)
            val = 1.0 / ((double) distinctValues);
        else
            val = 0.0;
        return new BigDecimal(val * 100.0).setScale(2, RoundingMode.HALF_UP).toString();
    }

    private String onPercentRecordCount(Report report, List<String> values, List<Integer> columns) {
        List<Record> content = report.getValuesByContent(values, columns);
        double val = (double) content.size() / ((double) report.getCount());
        return new BigDecimal(val * 100.0).setScale(2, RoundingMode.HALF_UP).toString();
    }

    private int countDistinctValues(List<Record> records, int columnIx) {
        List<String> values = new ArrayList<String>();
        for (Record record : records) {
            String v = String.valueOf(record.getValue(columnIx).getValue());
            if (!values.contains(v)) values.add(v);
        }
        return values.size();
    }

    private int countDistinctValues(Report report, int columnIx) {
//        List<String> values = new ArrayList<String>();
//        for (Record record : report.getRecords()) {
//            String v = String.valueOf(record.getValue(columnIx).getValue());
//            if (!values.contains(v)) values.add(v);
//        }
//        return values.size();
        return countDistinctValues(report.getRecords(), columnIx);
    }

    private String onGetPercentage(Report report, Integer column, List<String> values, List<Integer> cols) {
        double sum = getSum(column, report.getRecords());
        List<Record> content = report.getValuesByContent(values, cols);
        double contentSum = getSum(column, content);
        double v = contentSum / sum * 100.0;
        String s = "0.00";
        try {
            s = new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).toString();
        } catch (Exception ignored) {
            s = "0.00";
        }
        return s; //TODO: set round as param
    }

    private String onGetSum(Report report, Integer column, List<String> values, List<Integer> columns, boolean zero) {
        List<Record> content = report.getValuesByContent(values, columns);
        double sum;
        if (content.size() == 0 && !zero) throw new IllegalArgumentException("No content.");
        sum = getSum(column, content);
        return new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP).toString(); //TODO: set round as param
    }

    private double getSum(Integer column, List<Record> records) {
        double sum = 0.0;
        for (Record record : records) {
            Value val = record.getValue(column);
            try {
                if (val.getType() == Value.Type.NUMBER) sum += (Double) val.getValue();
                else sum += Double.valueOf((String) val.getValue());
            } catch (NumberFormatException ignored) {
            }
        }
        return sum;
    }

    private String onPartLength(AnalyzeItem item, Object round) {
        String len = String.valueOf(getFeatureLength(item));
        String param0 = (String) round;
        if (param0.isEmpty())
            return len;
        else {
            return new BigDecimal(len).setScale(Integer.valueOf(param0), BigDecimal.ROUND_HALF_UP)
                    .toString();
        }
    }

    private String onPartArea(AnalyzeItem item, Object round) {
        String area = String.valueOf(getFeatureArea(item));
        String param0 = (String) round;
        if (param0.isEmpty())
            return area;
        else {
            return new BigDecimal(area).setScale(Integer.valueOf(param0), BigDecimal.ROUND_HALF_UP)
                    .toString();
        }
    }

    private String onGetPartVal(Report report, List<String> expValues, List<Integer> expCols, int col) {
        List<String> valuesByContent = report.getStringsByContent(expValues, expCols);
        if (valuesByContent.size() == 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < expValues.size(); i++) {
                sb.append(String.format("%s ", expValues.get(i) + ":" + expCols.get(i)));

            }
            throw new IllegalStateException(sb.toString() + ". Value not found at:" + col);
        } else return valuesByContent.get(col);
    }

    private double getUnionArea(AnalyzeItem main, AnalyzeItem other) {
        return main.clipArea(other);
    }

    private double getUnionLength(AnalyzeItem main, AnalyzeItem other) {
        return main.clipLength(other);
    }

    private List<String> changeValuesByMacro(List<String> values, AnalyzeItem main, AnalyzeItem other) {
        List<String> result = new ArrayList<String>();
        for (String value : values) {
            if (value.startsWith(MCR_PARAM_MAIN)) {
                result.add(getAttributeStringValue(main, value.split(":")[1]));
            } else if (value.startsWith(MCR_PARAM_OTHER)) {
                result.add(getAttributeStringValue(other, value.split(":")[1]));
            } else {
                result.add(value);
            }
        }
        return result;
    }

    private String getAttributeStringValue(AnalyzeItem item, String name) {
        return String.valueOf(item.getAttribute(name));
    }

    private double getFeatureLength(AnalyzeItem item) {
        return item.getLength();
    }

    private double getFeatureArea(AnalyzeItem item) {
        return item.getArea();
    }

}
