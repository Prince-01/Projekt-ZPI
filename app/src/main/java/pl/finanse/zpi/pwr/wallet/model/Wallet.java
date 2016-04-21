package pl.finanse.zpi.pwr.wallet.model;

import java.util.ArrayList;

/**
 * Created by rober on 21.04.2016.
 */
public class Wallet {
    private String name;
    private float value; // TO DO: money
    private ArrayList<Operation> operations;

    public Wallet(String name) {
        this.name = name;
        value = 0;
    }

    public String getName() {
        return name;
    }
}
