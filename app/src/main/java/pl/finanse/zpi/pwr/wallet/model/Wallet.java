package pl.finanse.zpi.pwr.wallet.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import pl.finanse.zpi.pwr.wallet.MainActivity;

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

    /**
     * ustawia nazwe aktualnego portfela
     * @param context
     * @param nazwa
     */
    public static final void SetActiveWallet(Context context, String nazwa){
        SharedPreferences.Editor spe = MainActivity.GetGlobalSharedPreferences(context).edit();
        spe.putString("activeWallet",nazwa);
        spe.commit();
    }

    /**
     * pobiera nazwe aktualnego portfela
     * @param context
     * @return
     */
    public static final String GetActiveWallet(Context context){
        SharedPreferences sp = MainActivity.GetGlobalSharedPreferences(context);
        return sp.getString("activeWallet","");
    }
}
