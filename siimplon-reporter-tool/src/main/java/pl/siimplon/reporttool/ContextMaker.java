package pl.siimplon.reporttool;

import com.google.common.io.Resources;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.RowScheme;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static pl.siimplon.reporter.report.value.Value.Type.LITERAL;
import static pl.siimplon.reporter.report.value.Value.Type.NUMBER;
import static pl.siimplon.reporter.scheme.transfer.Transfer.*;

public class ContextMaker {

    private static List<TransferPair> zeroCableTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_KW"),
                    new TransferPair(MAIN_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(MAIN_ATTRIBUTE, "ADRES"),
                    new TransferPair(UNION_LENGTH_REVERSED, ""),
                    new TransferPair(VALUE, "Kabel SN"),
                    new TransferPair(OTHER_ATTRIBUTE, "ID"),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, "")
            );

    private static List<TransferPair> zeroSweepTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_KW"),
                    new TransferPair(MAIN_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(MAIN_ATTRIBUTE, "ADRES"),
                    new TransferPair(UNION_AREA, ""),
                    new TransferPair(VALUE, "Służebność łopatowa"),
                    new TransferPair(OTHER_ATTRIBUTE, "ID"),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, "")
            );

    private static List<TransferPair> zeroTurbineTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_KW"),
                    new TransferPair(MAIN_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(MAIN_ATTRIBUTE, "ADRES"),
                    new TransferPair(UNION_AREA, ""),
                    new TransferPair(VALUE, "Turbina"),
                    new TransferPair(OTHER_ATTRIBUTE, "ID"),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, "")
            );

    private static List<TransferPair> zeroTempRdTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_KW"),
                    new TransferPair(MAIN_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(MAIN_ATTRIBUTE, "ADRES"),
                    new TransferPair(UNION_AREA, ""),
                    new TransferPair(VALUE, "Drogi Tymczasowe"),
                    new TransferPair(OTHER_ATTRIBUTE, "ID"),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, "")
            );

    private static List<TransferPair> zeroFinalRdTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_KW"),
                    new TransferPair(MAIN_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(MAIN_ATTRIBUTE, "ADRES"),
                    new TransferPair(UNION_AREA, ""),
                    new TransferPair(VALUE, "Drogi docelowe"),
                    new TransferPair(OTHER_ATTRIBUTE, "ID"),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, ""),
                    new TransferPair(EMPTY, "")
            );

    private static List<TransferPair> summaryTransferCable =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "ID"),
                    new TransferPair(VALUE, ""),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            10, // "Tytuł prawny do gruntu"
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Kabel SN", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,
                                    8, 9)), //8 = "Typ infrastruktury", 9 = "Identyfikator infrastruktury"
                    new TransferPair(UNION_LENGTH),
                    new TransferPair(OTHER_ATTRIBUTE, "NR"),
                    new TransferPair(OTHER_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(OTHER_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(OTHER_ATTRIBUTE, "ADRES"),
                    new TransferPair(OTHER_ATTRIBUTE, "NR_KW"),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            11, //zeroReport.getColumnIxByName(HEAD_PAYMENT_METHOD),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Kabel SN", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)), //zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            12,//zeroReport.getColumnIxByName(HEAD_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Kabel SN", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            13, //zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Kabel SN", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            14,//zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_BUILD_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Kabel SN", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(VALUE, "NIE WIEM"),
                    new TransferPair(VALUE, "NIE WIEM")
            );

    private static List<TransferPair> summaryTransferSweep =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "ID"),
                    new TransferPair(VALUE, ""),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            10, // "Tytuł prawny do gruntu"
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Służebność łopatowa", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,
                                    8, 9)), //8 = "Typ infrastruktury", 9 = "Identyfikator infrastruktury"
                    new TransferPair(UNION_AREA),
                    new TransferPair(OTHER_ATTRIBUTE, "NR"),
                    new TransferPair(OTHER_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(OTHER_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(OTHER_ATTRIBUTE, "ADRES"),
                    new TransferPair(OTHER_ATTRIBUTE, "NR_KW"),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            11, //zeroReport.getColumnIxByName(HEAD_PAYMENT_METHOD),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Służebność łopatowa", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)), //zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            12,//zeroReport.getColumnIxByName(HEAD_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Służebność łopatowa", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            13, //zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Służebność łopatowa", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            14,//zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_BUILD_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Służebność łopatowa", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(VALUE, "NIE WIEM"),
                    new TransferPair(VALUE, "NIE WIEM")
            );

    private static List<TransferPair> summaryTransferTempRd =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "ID"),
                    new TransferPair(VALUE, ""),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            10, // "Tytuł prawny do gruntu"
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi Tymczasowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,
                                    8, 9)), //8 = "Typ infrastruktury", 9 = "Identyfikator infrastruktury"
                    new TransferPair(UNION_AREA),
                    new TransferPair(OTHER_ATTRIBUTE, "NR"),
                    new TransferPair(OTHER_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(OTHER_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(OTHER_ATTRIBUTE, "ADRES"),
                    new TransferPair(OTHER_ATTRIBUTE, "NR_KW"),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            11, //zeroReport.getColumnIxByName(HEAD_PAYMENT_METHOD),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi Tymczasowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)), //zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            12,//zeroReport.getColumnIxByName(HEAD_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi Tymczasowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            13, //zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi Tymczasowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            14,//zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_BUILD_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi Tymczasowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(VALUE, "NIE WIEM"),
                    new TransferPair(VALUE, "NIE WIEM")
            );

    private static List<TransferPair> summaryTransferFinalRd =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "ID"),
                    new TransferPair(VALUE, ""),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            10, // "Tytuł prawny do gruntu"
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi docelowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,
                                    8, 9)), //8 = "Typ infrastruktury", 9 = "Identyfikator infrastruktury"
                    new TransferPair(UNION_AREA),
                    new TransferPair(OTHER_ATTRIBUTE, "NR"),
                    new TransferPair(OTHER_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(OTHER_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(OTHER_ATTRIBUTE, "ADRES"),
                    new TransferPair(OTHER_ATTRIBUTE, "NR_KW"),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            11, //zeroReport.getColumnIxByName(HEAD_PAYMENT_METHOD),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi docelowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)), //zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            12,//zeroReport.getColumnIxByName(HEAD_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi docelowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            13, //zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi docelowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            14,//zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_BUILD_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Drogi docelowe", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(VALUE, "NIE WIEM"),
                    new TransferPair(VALUE, "NIE WIEM")
            );

    private static List<TransferPair> summaryTransferTurbine =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "ID"),
                    new TransferPair(VALUE, ""),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            10, // "Tytuł prawny do gruntu"
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Turbina", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,
                                    8, 9)), //8 = "Typ infrastruktury", 9 = "Identyfikator infrastruktury"
                    new TransferPair(UNION_AREA),
                    new TransferPair(OTHER_ATTRIBUTE, "NR"),
                    new TransferPair(OTHER_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(OTHER_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(OTHER_ATTRIBUTE, "ADRES"),
                    new TransferPair(OTHER_ATTRIBUTE, "NR_KW"),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            11, //zeroReport.getColumnIxByName(HEAD_PAYMENT_METHOD),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Turbina", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)), //zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            12,//zeroReport.getColumnIxByName(HEAD_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Turbina", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            13, //zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_DEV_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Turbina", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(GET_PART_VAL,
                            "zero-final",
                            14,//zeroReport.getColumnIxByName(HEAD_TO_BE_PAID_BUILD_PHASE),
                            Arrays.asList(RowScheme.MCR_PARAM_OTHER + ":NR",
                                    "Turbina", RowScheme.MCR_PARAM_MAIN + ":ID"),
                            Arrays.asList(3,//zeroReport.getColumnIxByName(HEAD_PLOT_NR),
                                    8, 9)),//zeroReport.getColumnIxByName(HEAD_INF_TYPE), zeroReport.getColumnIxByName(HEAD_INF_ID))),
                    new TransferPair(VALUE, "NIE WIEM"),
                    new TransferPair(VALUE, "NIE WIEM")
            );

    private static List<TransferPair> transferPlotCountCable = Arrays.asList(
            new TransferPair(VALUE, "Liczba działek:"),
            new TransferPair(COUNT_DISTINCT_VALUES, "summary-cable-report", 3)
    );

    private static List<TransferPair> transferPlotCountSweep = Arrays.asList(
            new TransferPair(VALUE, "Liczba działek:"),
            new TransferPair(COUNT_DISTINCT_VALUES, "summary-sweep-report", 3)
    );

    private static List<TransferPair> transferPlotCountTempRd = Arrays.asList(
            new TransferPair(VALUE, "Liczba działek:"),
            new TransferPair(COUNT_DISTINCT_VALUES, "summary-tmpRd-report", 3)
    );

    private static List<TransferPair> transferPlotCountFinalRd = Arrays.asList(
            new TransferPair(VALUE, "Liczba działek:"),
            new TransferPair(COUNT_DISTINCT_VALUES, "summary-finRd-report", 3)
    );

    private static List<TransferPair> transferPlotCountTurbine = Arrays.asList(
            new TransferPair(VALUE, "Liczba działek:"),
            new TransferPair(COUNT_DISTINCT_VALUES, "summary-turbine-report", 3)
    );


    private static List<Value.Type> summaryColumnScheme = Arrays.asList(
            LITERAL,
            LITERAL,
            LITERAL,
            NUMBER,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            NUMBER,
            NUMBER,
            NUMBER,
            LITERAL,
            LITERAL
    );

    private static List<Value.Type> zeroColumnScheme = Arrays.asList(
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            NUMBER,
            LITERAL,
            LITERAL,
            LITERAL,    //was DICTIONARY
            LITERAL,    //was DICTIONARY
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER
    );


    public static void main(String[] args) throws IOException {
        ReportContext context = new ReportContext();

        context.putFeature(getFeatures(Resources.getResource("ew/ew.shp").getPath()), "plot-source");
        context.putFeature(getFeatures(Resources.getResource("ksn/ksn.shp").getPath()), "cable-source");
        context.putFeature(getFeatures(Resources.getResource("lop/lop_poly.shp").getPath()), "sweep-source");
        context.putFeature(getFeatures(Resources.getResource("trb/trb_poly.shp").getPath()), "turbine-source");
        context.putFeature(getFeatures(Resources.getResource("sakowko-drogi-tymczasowe/sakowko-drogi-tymczasowe-polygon-singlepart.shp").getPath()), "tmpRd-source");
        context.putFeature(getFeatures(Resources.getResource("sakowko-drogi-docelowe/sakowko-drogi-docelowe-polygon.shp").getPath()), "finRd-source");

        context.putReport(new Report(zeroColumnScheme), "zero-cable-report");
        context.putReport(new Report(zeroColumnScheme), "zero-sweep-report");
        context.putReport(new Report(zeroColumnScheme), "zero-turbine-report");
        context.putReport(new Report(zeroColumnScheme), "zero-tmpRd-report");
        context.putReport(new Report(zeroColumnScheme), "zero-finRd-report");
        context.putReport(new Report(zeroColumnScheme), "zero-final");

        context.putReport(new Report(summaryColumnScheme), "summary-cable-report");
        context.putReport(new Report(summaryColumnScheme), "summary-sweep-report");
        context.putReport(new Report(summaryColumnScheme), "summary-tmpRd-report");
        context.putReport(new Report(summaryColumnScheme), "summary-finRd-report");
        context.putReport(new Report(summaryColumnScheme), "summary-turbine-report");

        context.putReport(new Report(LITERAL, NUMBER), "count-cable-report");
        context.putReport(new Report(LITERAL, NUMBER), "count-sweep-report");
        context.putReport(new Report(LITERAL, NUMBER), "count-tmpRd-report");
        context.putReport(new Report(LITERAL, NUMBER), "count-finRd-report");
        context.putReport(new Report(LITERAL, NUMBER), "count-turbine-report");

        context.putTransfer(zeroCableTransfer, "zero-cable-transfer");
        context.putTransfer(zeroSweepTransfer, "zero-sweep-transfer");
        context.putTransfer(zeroTurbineTransfer, "zero-turbine-transfer");
        context.putTransfer(zeroTempRdTransfer, "zero-tmpRd-transfer");
        context.putTransfer(zeroFinalRdTransfer, "zero-finRd-transfer");

        context.putTransfer(summaryTransferCable, "summary-cable-transfer");
        context.putTransfer(summaryTransferSweep, "summary-sweep-transfer");
        context.putTransfer(summaryTransferTempRd, "summary-tmpRd-transfer");
        context.putTransfer(summaryTransferFinalRd, "summary-finRd-transfer");
        context.putTransfer(summaryTransferTurbine, "summary-turbine-transfer");

        context.putTransfer(transferPlotCountCable, "count-cable-transfer");
        context.putTransfer(transferPlotCountSweep, "count-sweep-transfer");
        context.putTransfer(transferPlotCountTempRd, "count-tmpRd-transfer");
        context.putTransfer(transferPlotCountFinalRd, "count-finRd-transfer");
        context.putTransfer(transferPlotCountTurbine, "count-turbine-transfer");

        MyCallback acb = new MyCallback();
        context.make("zero-cable-report", "plot-source", "cable-source", "zero-cable-transfer", "", acb);
        context.make("zero-sweep-report", "plot-source", "sweep-source", "zero-sweep-transfer", "", acb);
        context.make("zero-turbine-report", "plot-source", "turbine-source", "zero-turbine-transfer", "", acb);
        context.make("zero-tmpRd-report", "plot-source", "tmpRd-source", "zero-tmpRd-transfer", "", acb);
        context.make("zero-finRd-report", "plot-source", "finRd-source", "zero-finRd-transfer", "", acb);

        context.merge("zero-final", "zero-cable-report", "zero-sweep-report", "zero-turbine-report",
                "zero-tmpRd-report", "zero-finRd-report");

        context.make("summary-cable-report", "cable-source", "plot-source", "summary-cable-transfer", "", acb);
        context.make("summary-sweep-report", "sweep-source", "plot-source", "summary-sweep-transfer", "", acb);
        context.make("summary-tmpRd-report", "tmpRd-source", "plot-source", "summary-tmpRd-transfer", "", acb);
        context.make("summary-finRd-report", "finRd-source", "plot-source", "summary-finRd-transfer", "", acb);
        context.make("summary-turbine-report", "turbine-source", "plot-source", "summary-turbine-transfer", "", acb);

        context.make("count-cable-report", "", "", "count-cable-transfer", "", acb);
        context.make("count-sweep-report", "", "", "count-sweep-transfer", "", acb);
        context.make("count-tmpRd-report", "", "", "count-tmpRd-transfer", "", acb);
        context.make("count-finRd-report", "", "", "count-finRd-transfer", "", acb);
        context.make("count-turbine-report", "", "", "count-turbine-transfer", "", acb);

        assert (context != null);
    }

    private static List<AnalyzeItem> getFeatures(String fileName) throws IOException {
        ShapefileDataStore store = new ShapefileDataStore(new File(fileName).toURI().toURL());
        store.setCharset(Charset.forName("UTF-8"));
        SimpleFeatureIterator features = store.getFeatureSource().getFeatures().features();
        List<AnalyzeItem> featureList = new Vector<AnalyzeItem>();
        while (features.hasNext()) {
            featureList.add(new SFAnalyzeItem(features.next()));
        }
        features.close();
        return featureList;
    }

}
