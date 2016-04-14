package pl.finanse.zpi.pwr.wallet.view;

import java.util.Date;

/**
 * Created by sebastiankotarski on 14.04.16.
 * Klasa operacji
 *
 */
public class Operation {
    public String operationName;
    public Category category;
    public Date date;
    public float cost; //// TODO: 14.04.16 Money money 

    public Operation(String operationName, float cost, Date date) {
        this.operationName = operationName;
        this.cost = cost;
        this.date = date;
    }



}
