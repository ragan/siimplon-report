package pl.siimplon.reporttool;

import pl.siimplon.reporter.report.value.Value;
import pl.siimplon.reporter.scheme.RowScheme;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import java.util.Arrays;
import java.util.List;

import static pl.siimplon.reporter.report.value.Value.Type.DICTIONARY;
import static pl.siimplon.reporter.report.value.Value.Type.LITERAL;
import static pl.siimplon.reporter.report.value.Value.Type.NUMBER;
import static pl.siimplon.reporter.scheme.transfer.Transfer.*;
import static pl.siimplon.reporter.scheme.transfer.Transfer.EMPTY;

public class TransferRepository {

    public static final List<Value.Type> zeroColumnScheme = Arrays.asList(
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

    public static final List<TransferPair> zeroCableTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_dzialki"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_KW"),
                    new TransferPair(MAIN_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(MAIN_ATTRIBUTE, "ADRES"),
                    new TransferPair(UNION_LENGTH_REVERSED, ""),
//                    new TransferPair(VALUE, "000.00"),
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

    public static List<TransferPair> zeroCableWnTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_dzialki"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_KW"),
                    new TransferPair(MAIN_ATTRIBUTE, "WLASCICIEL"),
                    new TransferPair(MAIN_ATTRIBUTE, "ADRES"),
                    new TransferPair(UNION_LENGTH_REVERSED, ""),
//                    new TransferPair(VALUE, "000.00"),
                    new TransferPair(VALUE, "Kabel WN"),
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

    public static final List<TransferPair> zeroSweepTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_dzialki"),
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

    public final static List<TransferPair> zeroTurbineTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_dzialki"),
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

    public final static List<TransferPair> zeroFinalRdTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_dzialki"),
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

    public final static List<TransferPair> zeroTempRdTransfer =
            Arrays.asList(
                    new TransferPair(MAIN_ATTRIBUTE, "GMINA"),
                    new TransferPair(MAIN_ATTRIBUTE, "NAZWA_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NUMER_OBRE"),
                    new TransferPair(MAIN_ATTRIBUTE, "NR_dzialki"),
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

        public static List<TransferPair> summaryTransferSweep =
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


}
