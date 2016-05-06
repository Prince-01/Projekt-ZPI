package pl.finanse.zpi.pwr.wallet.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import android.provider.ContactsContract;
import pl.finanse.zpi.pwr.wallet.MainActivity;
import pl.finanse.zpi.pwr.wallet.helpers.Database;

/**
 * Created by rober on 21.04.2016.
 */
public class Wallet implements Comparable<Wallet> {
    private String name;
    private float value; // TO DO: money
    private String currency;
    private ArrayList<Operation> operations;
    private static Wallet currentWallet = null;

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
    public void UpdateValueBy(float money) {
        value += money;
    }

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
        if(currentWallet != null && currentWallet.getName().equals(nazwa)) return;
        SharedPreferences.Editor spe = MainActivity.GetGlobalSharedPreferences(context).edit();
        spe.putString("activeWallet",nazwa);
        spe.commit();
        currentWallet = Database.GetWallet(context, nazwa);
    }

    /**
     * pobiera nazwe aktualnego portfela
     * @param context
     * @return
     */
    public static final Wallet GetActiveWallet(Context context){
        if(currentWallet != null) return currentWallet;
        SharedPreferences sp = MainActivity.GetGlobalSharedPreferences(context);
        String wname = sp.getString("activeWallet","");
        currentWallet = wname == "" ? null : Database.GetWallet(context, wname);
        return currentWallet;
    }

    @Override
    public int compareTo(Wallet another) {
        return name.compareTo(another.name);
    }
}
