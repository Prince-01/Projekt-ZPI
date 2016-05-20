package pl.finanse.zpi.pwr.wallet.model;

import java.util.Date;

/**
 * Klasa odpowiedzialna za stale zlecenie.
 * Created by sebastiankotarski on 21.04.16.
 */
public class StandingOperation extends Operation {
    public StandingOperation(int id, String wallet, String title, float cost, Date beginning, Date ending, int progress, boolean income, String category) {
        super(id,wallet,title,cost,null, income, category);
        this.begin = beginning;
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
    public Date begin;
    public Date end; // null means no end
    public INTERVAL interval;

}
