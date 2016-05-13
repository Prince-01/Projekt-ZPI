package pl.finanse.zpi.pwr.wallet.model;

import java.util.Date;

import pl.finanse.zpi.pwr.wallet.model.Category;

/**
 * Created by sebastiankotarski on 14.04.16.
 * Klasa operacji
 *
 */
public class Operation {
    public long id;
    public String operationName;
    public String wallet;
    public String category;
    public Date date;
    public float cost; //// TODO: 14.04.16 Money money
    public boolean isIncome;


    public Operation(long id, String wallet, String operationName, float cost, Date date, boolean isIncome, String category) {
        this.id = id;
        this.wallet = wallet;
        this.operationName = operationName;
        this.cost = cost;
        this.date = date;
        this.isIncome = isIncome;
        this.category = category;
    }
    public Operation(int id, String operationName, float cost, Date date) {
        this.id = id;
        this.operationName = operationName;
        this.cost = cost;
        this.date = date;
    }
}
