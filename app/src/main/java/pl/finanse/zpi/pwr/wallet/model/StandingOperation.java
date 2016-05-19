package pl.finanse.zpi.pwr.wallet.model;

import java.util.Date;

/**
 * Klasa odpowiedzialna za stale zlecenie.
 * Created by sebastiankotarski on 21.04.16.
 */
public class StandingOperation {
    public StandingOperation(int id, String wallet, String title, float cost, Date beg, Date ending, int progress, boolean income, String cat) {
        this.id = id;
        this.name = title;
        this.cost = cost;
        this.category = cat;
        this.wallet = wallet;
        this.isIncome = income;
        this.begin = beg;
        this.end = ending;

        if(progress < 14)
            this.interval = INTERVAL.week;
        else if(progress < 28)
            this.interval = INTERVAL.twoweeks;
        else if(progress < 42)
            this.interval = INTERVAL.month;
        else if(progress < 56)
            this.interval = INTERVAL.twomonths;
        else if(progress < 70)
            this.interval = INTERVAL.quarter;
        else if(progress < 84)
            this.interval = INTERVAL.halfyear;
        else
            this.interval = INTERVAL.year;
    }

    public enum INTERVAL {
        week,
        twoweeks,
        month,
        twomonths,
        quarter,
        halfyear,
        year
    }

    public int id;
    public String name;
    public float cost;
    public String category;
    public String wallet;
    public boolean isIncome;
    public Date begin;
    public Date end; // null means no end
    public INTERVAL interval;
    public StandingOperation() {
    }
}
