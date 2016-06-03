package pl.finanse.zpi.pwr.wallet.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;
import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.ShoppingItemAdapter;
import pl.finanse.zpi.pwr.wallet.adapters.ShoppingListAdapter;
import pl.finanse.zpi.pwr.wallet.adapters.StandingOperationsAdapter;
import pl.finanse.zpi.pwr.wallet.model.ShoppingItem;
import pl.finanse.zpi.pwr.wallet.model.ShoppingList;

/**
 * Created by sebastiankotarski on 01.06.16.
 */
public class ShoppingListsView extends Fragment {
    FloatingActionButton fab;

    private ArrayList<ShoppingList>data;
    private Unbinder unbinder;
    @BindView(R.id.shoppingListsView)
    ListView shoppingListsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        unbinder = ButterKnife.bind(this, view);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        reloadData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void reloadData() {

        //TODO LOAD FROM DATABASE
        data = new ArrayList<>();
        data.add(new ShoppingList(null,"Lista1"));
        data.add(new ShoppingList(null, "Lista2"));
        ShoppingListAdapter adapter = new ShoppingListAdapter(getActivity(), data, R.layout.shopping_list_row);
        shoppingListsView.setAdapter(adapter);
    }

    @OnItemClick(R.id.shoppingListsView)
    public void navigateToShoppingList(int pos) {
        ShoppingListView fragment = new ShoppingListView();
        fragment.selectedShoppingList = data.get(pos);
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        reloadData();
    }

    @OnItemLongClick(R.id.shoppingListsView)
    public boolean removeItem(int pos) {
        final ShoppingList op = data.get(pos);
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setMessage("Czy na pewno chcesz usunąć?");
        b.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.remove(op);
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
}
