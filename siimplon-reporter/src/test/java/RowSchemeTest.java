import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeCallback;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.record.Record;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.RowScheme;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static pl.siimplon.reporter.report.value.Value.Type.LITERAL;
import static pl.siimplon.reporter.report.value.Value.Type.NUMBER;
import static pl.siimplon.reporter.scheme.transfer.Transfer.*;

public class RowSchemeTest {

    private static final Object VALUE = "VALUE";

    @Mock
    Report report;

    @Test
    public void testValueCommand() throws Exception {
        RowScheme scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.VALUE, VALUE)));
        AnalyzeItem item = Mockito.mock(AnalyzeItem.class);
        String[] values = scheme.getRowValues(item, report);
        assertEquals(1, values.length);
        assertEquals(values[0], VALUE);
    }

    @Test
    public void testPercentDistinctValuesFromRecordsFound() throws Exception {
        Report report = new Report(4);

        report.addRecord("233", "DROGA", "UMOWA A");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("213", "DROGA", "UMOWA B");
        report.addRecord("203", "DROGA", "UMOWA D");
        report.addRecord("233", "TURBINA", "UMOWA A");
        report.addRecord("213", "TURBINA", "UMOWA B");

        RowScheme rowScheme;
        rowScheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        PERCENT_DISTINCT_VALUES_FROM_RECORDS_FOUND,
                        report,
                        0,
                        Arrays.asList("DROGA"),
                        Arrays.asList(1),
                        Arrays.asList("DROGA", "UMOWA B"),
                        Arrays.asList(1, 2)
                )));
        String[] rowValues = rowScheme.getRowValues(null, null);
        assertEquals("50.00", rowValues[0]);
        rowScheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        PERCENT_DISTINCT_VALUES_FROM_RECORDS_FOUND,
                        report,
                        0,
                        Arrays.asList("DROGA"),
                        Arrays.asList(1),
                        Arrays.asList("DROGA", "UMOWA D"),
                        Arrays.asList(1, 2)
                )));
        rowValues = rowScheme.getRowValues(null, null);
        assertEquals("25.00", rowValues[0]);

        report = new Report(3);

        report.addRecord("233", "DROGA", "UMOWA A");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("203", "DROGA", "UMOWA D");

        rowScheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        PERCENT_DISTINCT_VALUES_FROM_RECORDS_FOUND,
                        report,
                        0,
                        Arrays.asList("DROGA"),
                        Arrays.asList(1),
                        Arrays.asList("DROGA", "UMOWA B"),
                        Arrays.asList(1, 2)
                )));
        rowValues = rowScheme.getRowValues(null, null);
        assertEquals("50.00", rowValues[0]);

        report.getRecords().clear();

        report.addRecord("233", "DROGA", "UMOWA A");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("223", "DROGA", "UMOWA B");
        report.addRecord("203", "DROGA", "UMOWA D");

        rowValues = rowScheme.getRowValues(null, null);
        assertEquals("75.00", rowValues[0]);

        report = new Report(3);

        report.addRecord("62/3", "BRAK UMOWY");
        report.addRecord("167/2", "BRAK UMOWY");
        report.addRecord("167/1", "BRAK UMOWY");
        report.addRecord("309", "BRAK UMOWY");
        report.addRecord("114", "BRAK UMOWY");
        report.addRecord("73", "BRAK UMOWY");
        report.addRecord("69", "BRAK UMOWY");
        report.addRecord("68", "BRAK UMOWY");
        report.addRecord("18", "BRAK UMOWY");
        report.addRecord("169", "BRAK UMOWY");
        report.addRecord("441/2", "BRAK UMOWY");
        report.addRecord("291/2", "BRAK UMOWY");
        report.addRecord("11/3", "BRAK UMOWY");

        report.addRecord("60/2", "DECYZJA ADM.");
        report.addRecord("113/2", "DECYZJA ADM.");
        report.addRecord("104/1", "DECYZJA ADM.");
        report.addRecord("165", "DECYZJA ADM.");
        report.addRecord("64", "DECYZJA ADM.");
        report.addRecord("58", "DECYZJA ADM.");
        report.addRecord("366", "DECYZJA ADM.");

        report.addRecord("58/5", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("21/2", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("489", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("434", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("212", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("209", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("79", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("77", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("6", "UMOWA PRZEDWSTĘPNA");
        report.addRecord("432", "UMOWA PRZEDWSTĘPNA");

        report.addRecord("53/9", "UMOWA DZIERŻAWY");
        report.addRecord("52/3", "UMOWA DZIERŻAWY");
        report.addRecord("46/5", "UMOWA DZIERŻAWY");
        report.addRecord("51/2", "UMOWA DZIERŻAWY");
        report.addRecord("431/5", "UMOWA DZIERŻAWY");
        report.addRecord("33/1", "UMOWA DZIERŻAWY");
        report.addRecord("22/6", "UMOWA DZIERŻAWY");
        report.addRecord("21/4", "UMOWA DZIERŻAWY");
        report.addRecord("177", "UMOWA DZIERŻAWY");
        report.addRecord("60", "UMOWA DZIERŻAWY");
        report.addRecord("22", "UMOWA DZIERŻAWY");
        report.addRecord("8", "UMOWA DZIERŻAWY");
        report.addRecord("5", "UMOWA DZIERŻAWY");
        report.addRecord("134/5", "UMOWA DZIERŻAWY");
        report.addRecord("510", "UMOWA DZIERŻAWY");
        report.addRecord("134/2", "UMOWA DZIERŻAWY");
        report.addRecord("132/1", "UMOWA DZIERŻAWY");
        report.addRecord("436", "UMOWA DZIERŻAWY");
        report.addRecord("11/8", "UMOWA DZIERŻAWY");

        report.addRecord("441/1", "UZGODNIENIE");
        report.addRecord("50/2", "UZGODNIENIE");
        report.addRecord("141/1", "UZGODNIENIE");
        report.addRecord("292", "UZGODNIENIE");
        report.addRecord("399", "UZGODNIENIE");
        report.addRecord("364", "UZGODNIENIE");
        report.addRecord("355", "UZGODNIENIE");
        report.addRecord("53", "UZGODNIENIE");
        report.addRecord("52", "UZGODNIENIE");
        report.addRecord("51", "UZGODNIENIE");
        report.addRecord("49", "UZGODNIENIE");
        report.addRecord("45", "UZGODNIENIE");
        report.addRecord("38", "UZGODNIENIE");

        report.addRecord("51/1", "W TRAKCIE NEGOCJACJI");
        report.addRecord("368/1", "W TRAKCIE NEGOCJACJI");
        report.addRecord("217/1", "W TRAKCIE NEGOCJACJI");
        report.addRecord("226", "W TRAKCIE NEGOCJACJI");

        report.addRecord("45/1", "POROZUMIENIE");
        report.addRecord("354/1", "POROZUMIENIE");
        report.addRecord("294/23", "POROZUMIENIE");
        report.addRecord("290/1", "POROZUMIENIE");
        report.addRecord("176/4", "POROZUMIENIE");
        report.addRecord("157/1", "POROZUMIENIE");
        report.addRecord("287", "POROZUMIENIE");
        report.addRecord("362", "POROZUMIENIE");
        report.addRecord("359", "POROZUMIENIE");
        report.addRecord("225", "POROZUMIENIE");
        report.addRecord("115", "POROZUMIENIE");
        report.addRecord("72", "POROZUMIENIE");
        report.addRecord("61", "POROZUMIENIE");
        report.addRecord("58", "POROZUMIENIE");
        report.addRecord("54", "POROZUMIENIE");
        report.addRecord("52", "POROZUMIENIE");
        report.addRecord("49", "POROZUMIENIE");
        report.addRecord("47", "POROZUMIENIE");
        report.addRecord("36", "POROZUMIENIE");
        report.addRecord("5", "POROZUMIENIE");


        List<Record> brakUmowy = report.getValuesByContent(Arrays.asList("BRAK UMOWY"), Arrays.asList(1));
        System.out.println("brakUmowy " + brakUmowy.size());
        List<Record> wTrakcieNegocjacji = report.getValuesByContent(Arrays.asList("W TRAKCIE NEGOCJACJI"), Arrays.asList(1));
        System.out.println("wTrakcieNegocjacji " + wTrakcieNegocjacji.size());
        List<Record> uzgodnienie = report.getValuesByContent(Arrays.asList("UZGODNIENIE"), Arrays.asList(1));
        System.out.println("uzgodnienie " + uzgodnienie.size());
        List<Record> porozumienie = report.getValuesByContent(Arrays.asList("POROZUMIENIE"), Arrays.asList(1));
        System.out.println("porozumienie " + porozumienie.size());
        List<Record> umowaPrzedwstepna = report.getValuesByContent(Arrays.asList("UMOWA PRZEDWSTĘPNA"), Arrays.asList(1));
        System.out.println("umowaPrzedwstepna " + umowaPrzedwstepna.size());
        List<Record> umowaDzierzawy = report.getValuesByContent(Arrays.asList("UMOWA DZIERŻAWY"), Arrays.asList(1));
        System.out.println("umowaDzierzawy " + umowaDzierzawy.size());

        System.out.println("------");
        List<Record> sluzebnoscPrzesylu = report.getValuesByContent(Arrays.asList("SŁUŻEBNOŚĆ PRZESYŁU"), Arrays.asList(1));
        System.out.println("sluzebnoscPrzesylu " + sluzebnoscPrzesylu.size());
        List<Record> umowaNajmu = report.getValuesByContent(Arrays.asList("UMOWA NAJMU"), Arrays.asList(1));
        System.out.println("umowaNajmu " + umowaNajmu.size());
        List<Record> decyzjaAdm = report.getValuesByContent(Arrays.asList("DECYZJA ADM."), Arrays.asList(1));
        System.out.println("decyzjaAdm " + decyzjaAdm.size());
        List<Record> wstepnaZgoda = report.getValuesByContent(Arrays.asList("WSTĘPNA ZGODA"), Arrays.asList(1));
        System.out.println("wstepnaZgoda " + wstepnaZgoda.size());
        List<Record> wTrakcieKoncowychNeg = report.getValuesByContent(Arrays.asList("W TRAKCIE KOŃCOWYCH NEG."), Arrays.asList(1));
        System.out.println("wTrakcieKoncowychNeg " + wTrakcieKoncowychNeg.size());
        List<Record> wTrakcieKoncowychNegProc = report.getValuesByContent(Arrays.asList("W TRAKCIE NEGOCJACJI / PROCEDURY"), Arrays.asList(1));
        System.out.println("wTrakcieKoncowychNegProc " + wTrakcieKoncowychNegProc.size());

        System.out.println("report.getCount(): " + report.getCount());

        assertEquals(brakUmowy.size() +
                wTrakcieNegocjacji.size() +
                uzgodnienie.size() +
                porozumienie.size() +
                umowaPrzedwstepna.size() +
                umowaDzierzawy.size() +
                sluzebnoscPrzesylu.size() +
                umowaNajmu.size() +
                decyzjaAdm.size() +
                wstepnaZgoda.size() +
                wTrakcieKoncowychNeg.size() +
                wTrakcieKoncowychNegProc.size(), report.getRecords().size());


    }

    @Test
    public void testAreaCommand() throws Exception {
        AnalyzeItem f = Mockito.mock(AnalyzeItem.class);
        double area = 10.1234567890;
        Mockito.when(f.getArea()).thenReturn(area);
        //main part without rounding value
        RowScheme scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.MAIN_PART_AREA, "")));
        String[] values = scheme.getRowValues(f, report);
        assertEquals(String.valueOf(area), values[0]);
        //main part with rounding value
        scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.MAIN_PART_AREA, "2")));
        values = scheme.getRowValues(f, report);
        assertEquals(String.valueOf(10.12), values[0]);
        //other part without rounding value
        scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.OTHER_PART_AREA, "")));
        values = scheme.getRowValues(null, f, report);
        //other part with rounding value
        assertEquals(String.valueOf(area), values[0]);
        scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.OTHER_PART_AREA, "2")));
        values = scheme.getRowValues(null, f, report);
        assertEquals(String.valueOf(10.12), values[0]);
    }

    @Test
    public void testNumPartCommand() throws Exception {
        Report report = Mockito.mock(Report.class);
        Mockito.when(report.getCount()).thenReturn(123);
        RowScheme scheme = new RowScheme(Arrays.asList(new TransferPair(NUM_PART, "")));
        String[] values = scheme.getRowValues(Mockito.mock(AnalyzeItem.class), report);
        assertEquals(1, values.length);
        assertEquals(String.valueOf(123), values[0]);
    }

    @Test
    public void testOtherAttributeCommand() throws Exception {
        AnalyzeItem f = Mockito.mock(AnalyzeItem.class);
        Mockito.when(f.getAttribute("a")).thenReturn("xxx");

        RowScheme scheme = new RowScheme(Arrays.asList(new TransferPair(OTHER_ATTRIBUTE, "a")
        ));
        String[] values = scheme.getRowValues(null, f, report);
        assertEquals(1, values.length);
        assertEquals(values[0], "xxx");
    }

    @Test
    public void testMainAttributeCommand() throws Exception {
        AnalyzeItem f = Mockito.mock(AnalyzeItem.class);
        Mockito.when(f.getAttribute("a")).thenReturn("xxx");

        RowScheme scheme = new RowScheme(Arrays.asList(new TransferPair(MAIN_ATTRIBUTE, "a")));
        String[] values = scheme.getRowValues(f, report);
        assertEquals(1, values.length);
        assertEquals(values[0], "xxx");
    }

    @Test
    public void testLengthCommand() throws Exception {
        AnalyzeItem f = Mockito.mock(AnalyzeItem.class);
//        Geometry geom = Mockito.mock(Geometry.class);
        double length = 10.1234567890;
//        Mockito.when(geom.getLength()).thenReturn(length);
        Mockito.when(f.getLength()).thenReturn(length);
        //main part length without rounding value
        RowScheme scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.MAIN_PART_LENGTH, "")));
        String[] values = scheme.getRowValues(f, null);
        assertEquals(1, values.length);
        assertEquals(String.valueOf(length), values[0]);
        //main part length with rounding value
        scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.MAIN_PART_LENGTH, "2")));
        values = scheme.getRowValues(f, null);
        assertEquals(1, values.length);
        assertEquals(String.valueOf(10.12), values[0]);
        //other part length without rounding value
        scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.OTHER_PART_LENGTH, "")));
        values = scheme.getRowValues(null, f, null);
        assertEquals(1, values.length);
        assertEquals(String.valueOf(length), values[0]);
        //other part length with rounding value
        scheme = new RowScheme(Arrays.asList(new TransferPair(Transfer.OTHER_PART_LENGTH, "2")));
        values = scheme.getRowValues(null, f, null);
        assertEquals(1, values.length);
        assertEquals(String.valueOf(10.12), values[0]);
    }

    @Test
    public void testGetValuesFromOtherReport() throws Exception {
        Report report = new Report(Arrays.asList(LITERAL, LITERAL, LITERAL));
        report.addRecord("a", "b", "c");
        Report report2 = new Report(Arrays.asList(LITERAL, LITERAL, LITERAL));
        report2.addRecord("d", "e", "f");
        RowScheme scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        GET_PART_VAL, //command
                        report,
                        2, //column to take value from (zero indexed)
                        Arrays.asList("a", "b"),
                        Arrays.asList(0, 1))
        ));
        String[] values = scheme.getRowValues(null, report2);
        assertEquals(1, values.length);
        assertEquals("c", values[0]);
    }

    @Test
    public void testGetPercentByCondition() throws Exception {
        Report report = new Report(Arrays.asList(NUMBER, LITERAL));
        report.add(Arrays.<Object>asList(10.0, "A"));
        report.add(Arrays.<Object>asList(10.0, "B"));
        report.add(Arrays.<Object>asList(10.0, "B"));
        report.add(Arrays.<Object>asList(10.0, "C"));
        RowScheme scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.PERCENT_CONDITION, report, 0, Arrays.asList("A"),
                        Arrays.asList(1))
        ));
        String[] values = scheme.getRowValues(null, report);
        assertEquals(1, values.length);
        assertEquals("25.00", values[0]);
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.PERCENT_CONDITION, report, 0, Arrays.asList("B"), Arrays.asList(1))
        ));
        values = scheme.getRowValues(null, report);
        assertEquals(1, values.length);
        assertEquals("50.00", values[0]);
    }

    @Test
    public void testGetValueFromReportPartWithMacroCommand() throws Exception {
        //mcr_param_main:param = getValue value from main feature
        //mcr_param_other:param = getValue value from other feature
        List<List<String>> values = new ArrayList<List<String>>();
        values.add(Arrays.asList("120", "b", "c"));

        Report reportImpl = new Report(Arrays.asList(LITERAL, LITERAL, LITERAL));
        String partName = "part name";
        reportImpl.addRecord(values.get(0));

        AnalyzeItem f = Mockito.mock(AnalyzeItem.class);
        Mockito.when(f.getAttribute("fsparam")).thenReturn("120");

        RowScheme scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        Transfer.GET_PART_VAL, //command
                        partName, //name of part
                        2, //column to take value from (zero indexed)
                        Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":fsparam", "b"),
                        Arrays.asList(0, 1))
        ));
        String[] schemeRowValues = scheme.getRowValues(f, reportImpl);
        assertEquals(1, schemeRowValues.length);
        assertEquals("c", schemeRowValues[0]);
    }

    @Test
    public void testCountDistinctValues() throws Exception {
        Report report = new Report(LITERAL, NUMBER, LITERAL);
        report.setHeader(Arrays.asList("first", "second", "third"));
        report.addRecord(Arrays.asList("a", "10.0", "c"));
        report.addRecord(Arrays.asList("c", "10.0", "c"));
        report.addRecord(Arrays.asList("b", "12.0", "c"));

        assertDistinctValueCount(report, 1, "third");
        assertDistinctValueCount(report, 2, "second");
        assertDistinctValueCount(report, 3, "first");
    }

    private void assertDistinctValueCount(Report report, int expectedCount, String columnName) {
        RowScheme scheme = getGetCountDistinctValueScheme(report, columnName);
        String[] values = scheme.getRowValues(null, null);
        assertEquals(String.valueOf(expectedCount), values[0]);
    }

    private RowScheme getGetCountDistinctValueScheme(Report report, String columnName) {
        return new RowScheme(Arrays.asList(
                new TransferPair(Transfer.COUNT_DISTINCT_VALUES, report, report.getColumnIxByName(columnName))
        ));
    }

    @Test
    public void testCountDistinctValuesConditional() throws Exception {
        Report testReport = new Report(LITERAL, LITERAL, LITERAL);
        testReport.addRecord("102", "UDZ", "TRB");
        testReport.addRecord("120", "UDZ", "TRB");
        testReport.addRecord("102", "UNA", "TRB");
        testReport.addRecord("120", "UDZ", "TRB");

        RowScheme scheme;
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        Transfer.COUNT_DISTINCT_VALUES_CONDITIONAL,
                        testReport,
                        0,
                        Arrays.asList("UDZ", "TRB"),
                        Arrays.asList(1, 2)
                )
        ));
        String[] values;
        values = scheme.getRowValues(null, null);
        assertEquals("2", values[0]);
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        Transfer.COUNT_DISTINCT_VALUES_CONDITIONAL,
                        testReport,
                        0,
                        Arrays.asList("102", "TRB"),
                        Arrays.asList(0, 2)
                )
        ));
        values = scheme.getRowValues(null, null);
        assertEquals("1", values[0]);
    }

    @Test
    public void testPercentFromDistinctValues() throws Exception {
        Report testReport = new Report(LITERAL, LITERAL, LITERAL);
        testReport.addRecord("102", "UDZ", "TRB");
        testReport.addRecord("120", "UDZ", "TRB");
        testReport.addRecord("102", "UNA", "TRB");
        RowScheme scheme;
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        Transfer.PERCENT_FROM_DISTINCT_VALUES,
                        testReport,
                        0,
                        Arrays.asList("102", "UDZ"),
                        Arrays.asList(0, 1)
                )
        ));
        String[] values;
        values = scheme.getRowValues(null, null);
        assertEquals("50.00", values[0]);

        scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        Transfer.PERCENT_FROM_DISTINCT_VALUES,
                        testReport,
                        0,
                        Arrays.asList("102", "TRB"),
                        Arrays.asList(0, 2)
                )
        ));
        values = scheme.getRowValues(null, null);
        assertEquals("50.00", values[0]);
    }

    @Test
    public void testPercentageRecordCount() throws Exception {
        Report testReport = new Report(LITERAL, LITERAL, LITERAL);
        testReport.addRecord("a", "b", "c");
        testReport.addRecord("a", "d", "e");
        testReport.addRecord("b", "d", "a");
        testReport.addRecord("b", "d", "a");
        RowScheme scheme;
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.PERCENT_FROM_RECORD_COUNT,
                        testReport,
                        Arrays.asList("a"),
                        Arrays.asList(0)
                )
        ));
        String[] values;
        values = scheme.getRowValues(null, null);
        assertEquals("50.00", values[0]);
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.PERCENT_FROM_RECORD_COUNT,
                        testReport,
                        Arrays.asList("d"),
                        Arrays.asList(1)
                )
        ));
        values = scheme.getRowValues(null, null);
        assertEquals("75.00", values[0]);
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.PERCENT_FROM_RECORD_COUNT,
                        testReport,
                        Arrays.asList("c"),
                        Arrays.asList(2)
                )
        ));
        values = scheme.getRowValues(null, null);
        assertEquals("25.00", values[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPartNumAndSumException() throws Exception {
        Report report = new Report(LITERAL, NUMBER);
        report.addRecord("A", "1.0");
        report.addRecord("B", "1.0");

        RowScheme rowScheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.GET_PART_NUM_AND_SUM, report, 1, Arrays.asList("C"), Arrays.asList(0))
        ));
        rowScheme.getRowValues(null, null);
    }

    @Test
    public void testInsertValueIntoReport() throws Exception {
        ReportContext context = new ReportContext();
        Report reportA = new Report(3);
        reportA.addRecord("A", "B", "C");
        Report reportB = new Report(4);
        reportB.addRecord("D", "", "", "G");

        context.putReport(reportA, "report-a");
        context.putReport(reportB, "report-b");

        context.putTransfer(
                Arrays.asList(new TransferPair(
                        INSERT_INTO_REPORT,
                        "report-a",
                        1,
                        Arrays.asList("A", "C"),
                        Arrays.asList(0, 2),
                        "report-b",
                        1,
                        Arrays.asList("D", "G"),
                        Arrays.asList(0, 3)
                )), "scheme"
        );

        context.putReport(new Report(1), "dump-report");

        context.make("dump-report", "", "", "scheme", "", new AnalyzeCallback() {
            @Override
            public boolean isRelation(AnalyzeItem main, AnalyzeItem other) {
                return true;
            }
        });

        Report report = context.getReport("report-b");
        assertEquals("D", report.getRecords().get(0).getValue(0).toString());
        assertEquals("B", report.getRecords().get(0).getValue(1).toString());
        assertEquals("", report.getRecords().get(0).getValue(2).toString());
        assertEquals("G", report.getRecords().get(0).getValue(3).toString());


    }

    @Test
    public void testGetPartNumAndSumNoConditions() throws Exception {
        Report report = new Report(LITERAL, NUMBER);
        report.addRecord("A", "1.0");
        report.addRecord("B", "1.0");

        RowScheme rowScheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.GET_PART_NUM_AND_SUM_ZERO, report, 1, Collections.emptyList(), Collections.emptyList())
        ));
        String[] rowValues = rowScheme.getRowValues(null, null);
        assertEquals("2.00", rowValues[0]);

        report = new Report(LITERAL, LITERAL);
        report.addRecord("A", "10");
        report.addRecord("B", "10");

        rowScheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.GET_PART_NUM_AND_SUM_ZERO, report, 1, Collections.emptyList(), Collections.emptyList())
        ));
        rowValues = rowScheme.getRowValues(null, null);
        assertEquals("20.00", rowValues[0]);

        report = new Report(LITERAL, LITERAL);
        report.addRecord("A", "10");
        report.addRecord("B", "10");
        report.addRecord("A", "10");

        rowScheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.GET_PART_NUM_AND_SUM_ZERO, report, 1, Arrays.asList("A"), Arrays.asList(0))
        ));
        rowValues = rowScheme.getRowValues(null, null);
        assertEquals("20.00", rowValues[0]);
    }

    @Test
    public void testFindAndSumValues() throws Exception {
        Report report;
        report = new Report(LITERAL, NUMBER, LITERAL);
        String headA = "HEAD A";
        String tailA = "TAIL A";
        String headB = "HEAD B";
        String tailB = "TAIL B";
        String headC = "HEAD C";
        String tailC = "TAIL C";
        report.add(new Record(Arrays.asList(new Value(headA), new Value(10.0), new Value(tailA))));
        report.add(new Record(Arrays.asList(new Value(headB), new Value(12.3), new Value(tailB))));
        report.add(new Record(Arrays.asList(new Value(headA), new Value(45.6), new Value(tailC))));
        report.add(new Record(Arrays.asList(new Value("value"), new Value(45.6), new Value("value"))));
        report.add(new Record(Arrays.asList(new Value(headC), new Value(45.6), new Value(tailC))));

        RowScheme scheme;
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.GET_PART_NUM_AND_SUM,
                        report,
                        1,
                        Arrays.asList(headA),
                        Arrays.asList(0)
                )
        ));
        String[] values;
        values = scheme.getRowValues(null, null);
        assertEquals(1, values.length);
        assertEquals("55.60", values[0]);

        scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.GET_PART_NUM_AND_SUM,
                        report,
                        1,
                        Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":fsparam",
                                RowScheme.MCR_PARAM_MAIN + ":fsparam"),
                        Arrays.asList(0, 2)
                )
        ));
        AnalyzeItem feature = Mockito.mock(AnalyzeItem.class);
        Mockito.when(feature.getAttribute("fsparam")).thenReturn("value");
        values = scheme.getRowValues(feature, null);
        assertEquals(1, values.length);
        assertEquals("45.60", values[0]);

        report.add(new Record(Arrays.asList(new Value("value"), new Value(0.1), new Value("value"))));

        values = scheme.getRowValues(feature, null);
        assertEquals(1, values.length);
        assertEquals("45.70", values[0]);
        report = new Report(Arrays.asList(LITERAL, LITERAL, LITERAL));
        report.add(new Record(Arrays.asList(new Value("value"), new Value("45.6"), new Value("value"))));
        scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.GET_PART_NUM_AND_SUM,
                        report,
                        1,
                        Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":fsparam", RowScheme.MCR_PARAM_MAIN + ":fsparam"),
                        Arrays.asList(0, 2)
                )
        ));
        values = scheme.getRowValues(feature, null);
        assertEquals(1, values.length);
        assertEquals("45.60", values[0]);
    }

    /**
     * Get value from a column in report part where values are
     * equal to a list of given values.
     *
     * @throws Exception
     */
    @Test
    public void testGetValueFromReportPartCommand() throws Exception {
        //params = part name; column number; list of expected values; list of column numbers
        List<List<String>> reportValues = new ArrayList<List<String>>();
        reportValues.add(Arrays.asList("a", "b", "c", "d", "e"));
        reportValues.add(Arrays.asList("f", "g", "h", "i", "j"));

        Report report = new Report(Arrays.asList(LITERAL, LITERAL, LITERAL, LITERAL, LITERAL));
        report.addRecord(reportValues.get(0));
        report.addRecord(reportValues.get(1));
        RowScheme scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        Transfer.GET_PART_VAL, //command
                        "", //name of part
                        1, //column to take value from (zero indexed)
                        Arrays.asList("a", "c", "e"), //expected values
                        // according column numbers
                        // (ex. in col 0 value must be "a")
                        // (in col 2 value must be "c")
                        Arrays.asList(0, 2, 4))
        ));
        String[] values = scheme.getRowValues(null, report);
        assertEquals(1, values.length);
        assertEquals("b", values[0]);

        scheme = new RowScheme(Arrays.asList(
                new TransferPair(
                        Transfer.GET_PART_VAL, //command
                        "", //name of part
                        1, //column to take value from (zero indexed)
                        Arrays.asList("f", "h", "j"), //expected values
                        // according column numbers
                        // (ex. in col 0 value must be "a")
                        // (in col 2 value must be "c")
                        Arrays.asList(0, 2, 4))
        ));
        values = scheme.getRowValues(null, report);
        assertEquals(1, values.length);
        assertEquals("g", values[0]);
    }

    @Test
    public void testEmptyCommand() throws Exception {
        RowScheme scheme = new RowScheme(Arrays.asList(
                new TransferPair(Transfer.EMPTY, "nothing")
        ));
        String[] values = scheme.getRowValues(null, report);
        assertEquals(1, values.length);
        Assert.assertTrue(values[0].isEmpty());
    }
}