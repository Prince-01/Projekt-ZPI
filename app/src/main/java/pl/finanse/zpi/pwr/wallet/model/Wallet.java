package pl.finanse.zpi.pwr.wallet.model;

/**
 * Created by rober on 21.04.2016.
 */
public class Wallet {
    private String name;
    private float state; // TO DO: money

    public Wallet(String name) {
        this.name = name;
        state = 0;
    }
}
