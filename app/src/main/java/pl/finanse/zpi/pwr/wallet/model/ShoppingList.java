package pl.finanse.zpi.pwr.wallet.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sebastiankotarski on 01.06.16.
 */
public class ShoppingList {
    public final int id;
   public String name;
   public List<ShoppingItem> items;

    public ShoppingList(int listId,String name, LinkedList<ShoppingItem> items) {
        id=listId;
        this.items = items;
        this.name = name;
    }


}
