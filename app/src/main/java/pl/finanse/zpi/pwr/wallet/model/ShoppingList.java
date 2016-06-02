package pl.finanse.zpi.pwr.wallet.model;

import java.util.ArrayList;

/**
 * Created by sebastiankotarski on 01.06.16.
 */
public class ShoppingList {
   public String name;
   public ArrayList<ShoppingItem> items;

    public ShoppingList(ArrayList<ShoppingItem> items, String name) {
        this.items = items;
        this.name = name;
    }


}
