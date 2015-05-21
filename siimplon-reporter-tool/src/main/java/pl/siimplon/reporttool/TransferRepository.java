package pl.siimplon.reporttool;

import pl.siimplon.reporter.report.value.Value;
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


}
