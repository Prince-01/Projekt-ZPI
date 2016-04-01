package pl.finanse.zpi.pwr.wallet.helpers;

/**
 * Created by Kamil on 01.04.2016.
 */
public class Parser {
    private Parser() {}
    public static final char DELIMETER = '\n';
    public static String[] Split(String toSplit) {
        return toSplit.split(String.valueOf(DELIMETER));
    }
    public static boolean[] SplitToBoolean(String toSplit) {
        String[] splitted = Split(toSplit);
        boolean[] splittedBoolean = new boolean[splitted.length];

        for(int i = 0; i < splitted.length; i++)
            if(splitted[i] == "0")
                splittedBoolean[i] = false;
            else
                splittedBoolean[i] = true;

        return splittedBoolean;
    }
    public static String Join(String[] toJoin) {
        StringBuffer sb = new StringBuffer();

        for(String str : toJoin)
            sb.append(str).append('\n');

        return sb.substring(0, sb.length() - 1);
    }
    public static String Join(boolean[] toJoin) {
        StringBuffer sb = new StringBuffer();

        for(boolean b : toJoin)
            sb.append(b ? "1" : "0").append('\n');

        return sb.substring(0, sb.length() - 1);
    }
}
