package pl.finanse.zpi.pwr.wallet.helpers;

/**
 * Created by Kamil on 01.04.2016.
 */
public class Parser {
    private Parser() {}
    public static final char DELIMETER = '\n';
    public static String[] Split(String toSplit) {
        toSplit.split(DELIMETER);
    }
}
