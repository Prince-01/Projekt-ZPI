package pl.finanse.zpi.pwr.wallet.model;

import java.util.Date;

import pl.finanse.zpi.pwr.wallet.model.Category;

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
    public boolean isIncome;

    public Operation(String operationName, float cost, Date date, boolean isIncome, Category category) {
        this.operationName = operationName;
        this.cost = cost;
        this.date = date;
        this.isIncome = isIncome;
        this.category = category;
    }
    public Operation(String operationName, float cost, Date date) {
        this.operationName = operationName;
        this.cost = cost;
        this.date = date;
    }



}
