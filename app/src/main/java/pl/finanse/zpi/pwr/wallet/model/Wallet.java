package pl.finanse.zpi.pwr.wallet.model;

import java.util.ArrayList;

/**
 * Created by rober on 21.04.2016.
 */
public class Wallet {
    private String name;
    private float value; // TO DO: money
    private String currency;
    private ArrayList<Operation> operations;

    public Wallet(String name) {
        this.name = name;
        value = 0;
        currency = "PLN";
    }
    public Wallet(String name, float value, String currency) {
        this.name = name;
        this.value = value;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }
    public String getCurrency() { return currency; }
    public float getValue() { return value; }

    @Override
    public String toString() {
        return name;
    }
}
