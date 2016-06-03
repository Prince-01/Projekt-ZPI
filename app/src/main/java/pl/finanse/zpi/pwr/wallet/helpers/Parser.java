package pl.finanse.zpi.pwr.wallet.helpers;

import java.util.Calendar;

/**
 * Created by Kamil on 01.04.2016.
 */
public class Parser {
    private Parser() {}
    public static final char DELIMETER = '\n';
    /**
    zamienia string na tablice, potrzeba na utworzenia listy zakupow
     */
    public static String[] Split(String toSplit) {
        return toSplit.split(String.valueOf(DELIMETER));
    }

    /**
     * zamienia checki dla listy zakupow, zeby bylo wiadomo ktore pola mozna zaznaczyc
     * @param toSplit
     * @return
     */
    public static boolean[] SplitToBoolean(String toSplit) {
        String[] splitted = Split(toSplit);
        boolean[] splittedBoolean = new boolean[splitted.length];

        for(int i = 0; i < splitted.length; i++)
            if(splitted[i].equals("0"))
                splittedBoolean[i] = false;
            else
                splittedBoolean[i] = true;

        return splittedBoolean;
    }

    /**
     * zamienia tablice z przedmiotami z listy zakupow, na stringa do bazy danych
     * @param toJoin
     * @return
     */
    public static String Join(String... toJoin) {
        StringBuffer sb = new StringBuffer();

        for(String str : toJoin)
            sb.append(str).append(DELIMETER);

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * zamienia liste zakupow na ciag znakow, zeby bylo co juz zaznaczyl klient
     * @param toJoin
     * @return
     */
    public static String Join(boolean... toJoin) {
        StringBuffer sb = new StringBuffer();

        for(boolean b : toJoin)
            sb.append(b ? "1" : "0").append(DELIMETER);

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * zwraca nazwe miesiaca po polsku dla podanego numerka
     * @param number
     * @return
     */
    public static String GetNameOfMonth(int number){
        switch(number){
            case Calendar.JANUARY:
                return "Styczeń";
            case Calendar.FEBRUARY:
                return "Luty";
            case Calendar.MARCH:
                return "Marzec";
            case Calendar.APRIL:
                return "Kwiecień";
            case Calendar.MAY:
                return "Maj";
            case Calendar.JUNE:
                return "Czerwiec";
            case Calendar.JULY:
                return "Lipiec";
            case Calendar.AUGUST:
                return "Sierpień";
            case Calendar.SEPTEMBER:
                return "Wrzesień";
            case Calendar.OCTOBER:
                return "Piździernik";
            case Calendar.NOVEMBER:
                return "Listopad";
            case Calendar.DECEMBER:
                return "Grudzień";
        }
        throw new RuntimeException("Nie ma miesiąca o numerze "+number);
    }
}
