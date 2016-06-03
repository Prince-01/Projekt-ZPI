package pl.finanse.zpi.pwr.wallet.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.sql.DataSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;
import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.ShoppingItemAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.ShoppingItem;
import pl.finanse.zpi.pwr.wallet.model.ShoppingList;

/**
 * Created by sebastiankotarski on 27.05.16.
 */
public class ShoppingListView extends Fragment {
    FloatingActionButton fab;

    private Unbinder unbinder;
    public ShoppingList selectedShoppingList;
    @BindView(R.id.ShoppingListView)
    ListView shoppingListView;
    @BindView(R.id.newItemEditText)
    EditText newItemEditText;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //long temp = System.currentTimeMillis();
        View view = inflater.inflate(R.layout.shopping_list_view, container, false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        unbinder = ButterKnife.bind(this, view);
        getActivity().setTitle(selectedShoppingList.name);
        reloadData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * wywolywanie przycisku dodaj
     */
    @OnClick(R.id.addShoppingItemButton)
    public void addShoppingItemButton() {
        ShoppingItem newItem = new ShoppingItem(newItemEditText.getText().toString(),false);
        if(newItem.name.equals(""))
            return;//pustych nie przyjmujemy
        newItemEditText.setText("");
        selectedShoppingList.items.add(newItem);
        Database.UpdateShoppingList(getActivity(),selectedShoppingList);
        reloadData();
    }

    /**
     * dlugie przycisniecie na liscie, sluzy do usuwania
     * @param pos
     * @return
     */
    @OnItemLongClick(R.id.ShoppingListView)
    public boolean removeItem(int pos) {
        final ShoppingItem op = selectedShoppingList.items.get(pos);
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setMessage("Czy na pewno chcesz usunąć?");
        b.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedShoppingList.items.remove(op);
                Database.UpdateShoppingList(getActivity(), selectedShoppingList);
                reloadData();
            }
        });
        b.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        b.create().show();
        return true;
    }

    /**
     * krotkie przycisniecie na liscie, check/uncheck napisu
     * @param pos
     */
    @OnItemClick(R.id.ShoppingListView)
    public void crossItem(int pos) {
        selectedShoppingList.items.get(pos).isChecked = !selectedShoppingList.items.get(pos).isChecked;
        Database.UpdateShoppingList(getActivity(),selectedShoppingList);
        reloadData();
    }

    /**
     * odswierzanie danych wyswietlanych na ekranie, nie obejmuje update w bazie danych
     */
    public void reloadData() {
        Collections.sort(selectedShoppingList.items, new Comparator<ShoppingItem>() {
            @Override
            public int compare(ShoppingItem lhs, ShoppingItem rhs) {
                if(lhs.isChecked == rhs.isChecked) return lhs.name.compareTo(rhs.name);
                if(lhs.isChecked) return 1;
                else return -1;
            }
        });
        ShoppingItemAdapter adapter = new ShoppingItemAdapter(getActivity(), selectedShoppingList.items, R.layout.shopping_item_row);
        shoppingListView.setAdapter(adapter);
    }
}
