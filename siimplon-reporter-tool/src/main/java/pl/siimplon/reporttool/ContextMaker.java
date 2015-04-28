package pl.siimplon.reporttool;

import com.google.common.io.Resources;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.analyzer.AnalyzeItem;
import pl.siimplon.reporter.report.Report;
import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.RowScheme;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

    private static List<TransferPair> plotListCable = Arrays.asList(
            new TransferPair(NUM_PART, ""),
            new TransferPair(GET_PART_VAL,
                    "zero-cable-report",
                    3,
                    Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":NR"),
                    Arrays.asList(3)
            ),
            new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE")
    );

    private static List<TransferPair> plotListSweep = Arrays.asList(
            new TransferPair(NUM_PART, ""),
            new TransferPair(GET_PART_VAL,
                    "zero-sweep-report",
                    3,
                    Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":NR"),
                    Arrays.asList(3)
            ),
            new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE")
    );

    private static List<TransferPair> plotListTmpRd = Arrays.asList(
            new TransferPair(NUM_PART, ""),
            new TransferPair(GET_PART_VAL,
                    "zero-tmpRd-report",
                    3,
                    Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":NR"),
                    Arrays.asList(3)
            ),
            new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE")
    );

    private static List<TransferPair> plotListFinRd = Arrays.asList(
            new TransferPair(NUM_PART, ""),
            new TransferPair(GET_PART_VAL,
                    "zero-finRd-report",
                    3,
                    Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":NR"),
                    Arrays.asList(3)
            ),
            new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE")
    );

    private static List<TransferPair> plotListTurbine = Arrays.asList(
            new TransferPair(NUM_PART, ""),
            new TransferPair(GET_PART_VAL,
                    "zero-turbine-report",
                    3,
                    Arrays.asList(RowScheme.MCR_PARAM_MAIN + ":NR"),
                    Arrays.asList(3)
            ),
            new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE")
    );

    // ~~~~~~~~~~ SUMMARY BEGIN ~~~~~~~~~~

    private static List<Value.Type> summaryAllColumns = Arrays.asList(
            LITERAL,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER,
            NUMBER
    );

    private static List<TransferPair> summaryAllSLU = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "służebność przesyłu"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("służebność przesyłu"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllUDZ = Arrays.asList(
            // ~~~~~~~~~~~ UMOWA DZIERŻAWY
            new TransferPair(VALUE, "umowa dzierżawy"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("umowa dzierżawy"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllUNA = Arrays.asList(
            // ~~~~~~~~~~~ umowa najmu
            new TransferPair(VALUE, "umowa najmu"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("umowa najmu"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllUPR = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "umowa przedwstępna"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("umowa przedwstępna"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllSLG = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "służebność gruntowa"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("służebność gruntowa"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllDAD = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "Decyzja adm."),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("Decyzja adm."), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllPOR = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "porozumienie"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("porozumienie"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllUZG = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "uzgodnienie"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("uzgodnienie"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllWSZ = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "Wstępna zgoda"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("Wstępna zgoda"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllWKN = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "w trakcie końcowych negocjacji"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllWPR = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "w trakcie negocjacji/procedury"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllWTN = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "w trakcie negocjacji"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllBRU = Arrays.asList(
            // ~~~~~~~~~~~ umowa przedwstępna
            new TransferPair(VALUE, "brak umowy"),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-turbine-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-turbine-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-sweep-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-sweep-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-cable-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-cable-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-finRd-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-finRd-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(GET_PART_NUM_AND_SUM, "zero-tmpRd-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_CONDITION, "zero-tmpRd-report", 3, Arrays.asList("brak umowy"), Arrays.asList(9))
    );

    private static List<TransferPair> summaryAllHeader = Arrays.asList(
            new TransferPair(VALUE, ""),
            new TransferPair(VALUE, "Wartość"),
            new TransferPair(VALUE, "[%]"),
            new TransferPair(VALUE, "Wartość"),
            new TransferPair(VALUE, "[%]"),
            new TransferPair(VALUE, "Wartość"),
            new TransferPair(VALUE, "[%]"),
            new TransferPair(VALUE, "Wartość"),
            new TransferPair(VALUE, "[%]"),
            new TransferPair(VALUE, "Wartość"),
            new TransferPair(VALUE, "[%]")
    );

    private static List<Value.Type> summaryAllHeaderColumns = Arrays.asList(
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL,
            LITERAL
    );

    // ~~~~~~~~~~ SUMMARY END ~~~~~~~~~~

    private static List<Value.Type> plotListColumns = Arrays.asList(
            LITERAL,
            LITERAL,
            LITERAL
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

    //TODO: plot count summary report
    private static List<TransferPair> plotCountAllSLU = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "służebność przesyłu"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("służebność przesyłu"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllUDZ = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "umowa dzierżawy"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("umowa dzierżawy"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllUNA = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "umowa najmu"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("umowa najmu"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllUPR = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "umowa przedwstępna"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("umowa przedwstępna"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllSLG = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "służebność gruntowa"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("służebność gruntowa"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllDAD = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "Decyzja adm."),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("Decyzja adm."), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllPOR = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "porozumienie"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("porozumienie"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllUZG = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "uzgodnienie"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("uzgodnienie"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllWSZ = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "Wstępna zgoda"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("Wstępna zgoda"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllWKN = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "w trakcie końcowych negocjacji"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("w trakcie końcowych negocjacji"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllWPR = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "w trakcie negocjacji/procedury"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("w trakcie negocjacji/procedury"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllWTN = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "w trakcie negocjacji"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("w trakcie negocjacji"), Arrays.asList(9))
    );
    private static List<TransferPair> plotCountAllBRU = Arrays.asList(
            // ~~~~~~~~~~~ SŁUŻEBNOŚĆ PRZESYŁU
            new TransferPair(VALUE, "brak umowy"),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-turbine-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-turbine-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-sweep-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-sweep-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-cable-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-cable-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-finRd-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-finRd-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(COUNT_DISTINCT_VALUES_CONDITIONAL, "zero-tmpRd-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9)),
            new TransferPair(PERCENT_FROM_DISTINCT_VALUES, "zero-tmpRd-report", 4, Arrays.asList("brak umowy"), Arrays.asList(9))
    );


    public static void main(String[] args) throws IOException {
        doStuff(false);
    }

    private static void doStuff(boolean importInputData) throws IOException {
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

        context.putReport(new Report(plotListColumns), "plotList-cable-report");
        context.putReport(new Report(plotListColumns), "plotList-sweep-report");
        context.putReport(new Report(plotListColumns), "plotList-tmpRd-report");
        context.putReport(new Report(plotListColumns), "plotList-finRd-report");
        context.putReport(new Report(plotListColumns), "plotList-turbine-report");

        context.putReport(new Report(summaryAllHeaderColumns), "summary-all-report-HEAD");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-SLU");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-UDZ");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-UNA");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-UPR");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-SLG");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-DAD");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-POR");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-UZG");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-WSZ");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-WKN");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-WPR");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-WTN");
        context.putReport(new Report(summaryAllColumns), "summary-all-report-BRU");

        context.putReport(new Report(summaryAllColumns), "summary-all-merge");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-merge");

        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-SLU");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-UDZ");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-UNA");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-UPR");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-SLG");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-DAD");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-POR");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-UZG");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-WSZ");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-WKN");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-WPR");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-WTN");
        context.putReport(new Report(summaryAllColumns), "plotCount-all-report-BRU");

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

        context.putTransfer(plotListCable, "plotList-cable-transfer");
        context.putTransfer(plotListSweep, "plotList-sweep-transfer");
        context.putTransfer(plotListTmpRd, "plotList-tmpRd-transfer");
        context.putTransfer(plotListFinRd, "plotList-finRd-transfer");
        context.putTransfer(plotListTurbine, "plotList-turbine-transfer");

        context.putTransfer(summaryAllHeader, "summary-all-transfer-HEAD");
        context.putTransfer(summaryAllSLU, "summary-all-transfer-SLU");
        context.putTransfer(summaryAllUDZ, "summary-all-transfer-UDZ");
        context.putTransfer(summaryAllUNA, "summary-all-transfer-UNA");
        context.putTransfer(summaryAllUPR, "summary-all-transfer-UPR");
        context.putTransfer(summaryAllSLG, "summary-all-transfer-SLG");
        context.putTransfer(summaryAllDAD, "summary-all-transfer-DAD");
        context.putTransfer(summaryAllPOR, "summary-all-transfer-POR");
        context.putTransfer(summaryAllUZG, "summary-all-transfer-UZG");
        context.putTransfer(summaryAllWSZ, "summary-all-transfer-WSZ");
        context.putTransfer(summaryAllWKN, "summary-all-transfer-WKN");
        context.putTransfer(summaryAllWPR, "summary-all-transfer-WPR");
        context.putTransfer(summaryAllWTN, "summary-all-transfer-WTN");
        context.putTransfer(summaryAllBRU, "summary-all-transfer-BRU");

        context.putTransfer(summaryAllHeader, "plotCount-all-transfer-HEAD");
        context.putTransfer(plotCountAllSLU, "plotCount-all-transfer-SLU");
        context.putTransfer(plotCountAllUDZ, "plotCount-all-transfer-UDZ");
        context.putTransfer(plotCountAllUNA, "plotCount-all-transfer-UNA");
        context.putTransfer(plotCountAllUPR, "plotCount-all-transfer-UPR");
        context.putTransfer(plotCountAllSLG, "plotCount-all-transfer-SLG");
        context.putTransfer(plotCountAllDAD, "plotCount-all-transfer-DAD");
        context.putTransfer(plotCountAllPOR, "plotCount-all-transfer-POR");
        context.putTransfer(plotCountAllUZG, "plotCount-all-transfer-UZG");
        context.putTransfer(plotCountAllWSZ, "plotCount-all-transfer-WSZ");
        context.putTransfer(plotCountAllWKN, "plotCount-all-transfer-WKN");
        context.putTransfer(plotCountAllWPR, "plotCount-all-transfer-WPR");
        context.putTransfer(plotCountAllWTN, "plotCount-all-transfer-WTN");
        context.putTransfer(plotCountAllBRU, "plotCount-all-transfer-BRU");


        MyCallback cb = new MyCallback();

        if (!importInputData) {
            context.make("zero-cable-report", "plot-source", "cable-source", "zero-cable-transfer", "", cb);
            context.make("zero-sweep-report", "plot-source", "sweep-source", "zero-sweep-transfer", "", cb);
            context.make("zero-turbine-report", "plot-source", "turbine-source", "zero-turbine-transfer", "", cb);
            context.make("zero-tmpRd-report", "plot-source", "tmpRd-source", "zero-tmpRd-transfer", "", cb);
            context.make("zero-finRd-report", "plot-source", "finRd-source", "zero-finRd-transfer", "", cb);

            context.merge("zero-final", "zero-cable-report", "zero-sweep-report", "zero-turbine-report",
                    "zero-tmpRd-report", "zero-finRd-report");
        } else {
            //import
        }

        context.make("summary-cable-report", "cable-source", "plot-source", "summary-cable-transfer", "", cb);
        context.make("summary-sweep-report", "sweep-source", "plot-source", "summary-sweep-transfer", "", cb);
        context.make("summary-tmpRd-report", "tmpRd-source", "plot-source", "summary-tmpRd-transfer", "", cb);
        context.make("summary-finRd-report", "finRd-source", "plot-source", "summary-finRd-transfer", "", cb);
        context.make("summary-turbine-report", "turbine-source", "plot-source", "summary-turbine-transfer", "", cb);

        context.make("count-cable-report", "", "", "count-cable-transfer", "", cb);
        context.make("count-sweep-report", "", "", "count-sweep-transfer", "", cb);
        context.make("count-tmpRd-report", "", "", "count-tmpRd-transfer", "", cb);
        context.make("count-finRd-report", "", "", "count-finRd-transfer", "", cb);
        context.make("count-turbine-report", "", "", "count-turbine-transfer", "", cb);

        context.make("plotList-cable-report", "plot-source", "", "plotList-cable-transfer", "", cb);
        context.make("plotList-sweep-report", "plot-source", "", "plotList-sweep-transfer", "", cb);
        context.make("plotList-tmpRd-report", "plot-source", "", "plotList-tmpRd-transfer", "", cb);
        context.make("plotList-finRd-report", "plot-source", "", "plotList-finRd-transfer", "", cb);
        context.make("plotList-turbine-report", "plot-source", "", "plotList-turbine-transfer", "", cb);

        context.make("summary-all-report-HEAD", "", "", "summary-all-transfer-HEAD", "", cb);
        context.make("summary-all-report-SLU", "", "", "summary-all-transfer-SLU", "", cb);
        context.make("summary-all-report-UDZ", "", "", "summary-all-transfer-UDZ", "", cb);
        context.make("summary-all-report-UNA", "", "", "summary-all-transfer-UNA", "", cb);
        context.make("summary-all-report-UPR", "", "", "summary-all-transfer-UPR", "", cb);
        context.make("summary-all-report-SLG", "", "", "summary-all-transfer-SLG", "", cb);
        context.make("summary-all-report-DAD", "", "", "summary-all-transfer-DAD", "", cb);
        context.make("summary-all-report-POR", "", "", "summary-all-transfer-POR", "", cb);
        context.make("summary-all-report-UZG", "", "", "summary-all-transfer-UZG", "", cb);
        context.make("summary-all-report-WSZ", "", "", "summary-all-transfer-WSZ", "", cb);
        context.make("summary-all-report-WKN", "", "", "summary-all-transfer-WKN", "", cb);
        context.make("summary-all-report-WPR", "", "", "summary-all-transfer-WPR", "", cb);
        context.make("summary-all-report-WTN", "", "", "summary-all-transfer-WTN", "", cb);
        context.make("summary-all-report-BRU", "", "", "summary-all-transfer-BRU", "", cb);

        context.make("plotCount-all-report-SLU", "", "", "plotCount-all-transfer-SLU", "", cb);
        context.make("plotCount-all-report-UDZ", "", "", "plotCount-all-transfer-UDZ", "", cb);
        context.make("plotCount-all-report-UNA", "", "", "plotCount-all-transfer-UNA", "", cb);
        context.make("plotCount-all-report-UPR", "", "", "plotCount-all-transfer-UPR", "", cb);
        context.make("plotCount-all-report-SLG", "", "", "plotCount-all-transfer-SLG", "", cb);
        context.make("plotCount-all-report-DAD", "", "", "plotCount-all-transfer-DAD", "", cb);
        context.make("plotCount-all-report-POR", "", "", "plotCount-all-transfer-POR", "", cb);
        context.make("plotCount-all-report-UZG", "", "", "plotCount-all-transfer-UZG", "", cb);
        context.make("plotCount-all-report-WSZ", "", "", "plotCount-all-transfer-WSZ", "", cb);
        context.make("plotCount-all-report-WKN", "", "", "plotCount-all-transfer-WKN", "", cb);
        context.make("plotCount-all-report-WPR", "", "", "plotCount-all-transfer-WPR", "", cb);
        context.make("plotCount-all-report-WTN", "", "", "plotCount-all-transfer-WTN", "", cb);
        context.make("plotCount-all-report-BRU", "", "", "plotCount-all-transfer-BRU", "", cb);

        context.merge("summary-all-merge", "summary-all-report-SLU", "summary-all-report-UDZ", "summary-all-report-UNA",
                "summary-all-report-UPR", "summary-all-report-SLG", "summary-all-report-DAD", "summary-all-report-POR",
                "summary-all-report-UZG", "summary-all-report-WSZ", "summary-all-report-WKN", "summary-all-report-WPR",
                "summary-all-report-WTN", "summary-all-report-BRU");

        context.merge("plotCount-all-merge", "plotCount-all-report-SLU", "plotCount-all-report-UDZ",
                "plotCount-all-report-UNA", "plotCount-all-report-UPR", "plotCount-all-report-SLG",
                "plotCount-all-report-DAD", "plotCount-all-report-POR", "plotCount-all-report-UZG",
                "plotCount-all-report-WSZ", "plotCount-all-report-WKN", "plotCount-all-report-WPR",
                "plotCount-all-report-WTN", "plotCount-all-report-BRU");

        assert (context != null);

        ContextExporter exporter = new ContextExporter(context);

        String outPath = System.getProperty("user.home") + File.separatorChar + "report-out-new-DW-v3.xls";
        File file = new File(outPath);
        exporter.export(Arrays.asList("zero-final"), "DANE WSADOWE", file);
        exporter.export(Arrays.asList("summary-cable-report", "count-cable-report", "plotList-cable-report"), "KABLE SN", file);
        exporter.export(Arrays.asList("summary-sweep-report", "count-sweep-report", "plotList-sweep-report"), "OMIATANIE", file);
        exporter.export(Arrays.asList("summary-tmpRd-report", "count-tmpRd-report", "plotList-tmpRd-report"), "DROGI TYMCZASOWE", file);
        exporter.export(Arrays.asList("summary-finRd-report", "count-finRd-report", "plotList-finRd-report"), "DROGI DOCELOWE", file);
        exporter.export(Arrays.asList("summary-turbine-report", "count-turbine-report", "plotList-turbine-report"), "TURBINY", file);

        exporter.export(Arrays.asList(
                "summary-all-report-HEAD",
                "summary-all-merge",
                "summary-all-report-HEAD",
                "plotCount-all-merge"
        ), "ZESTAWIENIE", file);

        Desktop.getDesktop().open(file);
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

    public Report importReport(HSSFSheet sheet, int beginRow) {
        int count = 0;
        for (Cell ignored : sheet.getRow(beginRow)) count++;
        java.util.List<Value.Type> types = new ArrayList<Value.Type>();
        for (Cell cell : sheet.getRow(beginRow)) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    types.add(Value.Type.NUMBER);
                    break;
                default:
                    types.add(Value.Type.LITERAL);
                    break;
            }
        }
        Report report = new Report(types);
        for (int i = beginRow; i < sheet.getLastRowNum() + beginRow; i++) {
            HSSFRow row = sheet.getRow(i);
            java.util.List<String> values = new ArrayList<String>();
            for (int j = 0; j < count; j++) {
                HSSFCell cell = row.getCell(j);
                switch (report.getType(j)) {
                    case NUMBER:
                        values.add(String.valueOf(cell == null ? "" : cell.getNumericCellValue()));
                        break;
                    default:
                        values.add(cell == null ? "" : cell.getStringCellValue());
                        break;
                }
            }
            report.addRecord(values);
        }
        return report;
    }

}
