package pl.finanse.zpi.pwr.wallet.model;

/**
 * Created by sebastiankotarski on 27.05.16.
 */
public class ShoppingItem {
    public boolean isChecked;
    public String name;

    public ShoppingItem(String name,boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
    }
}