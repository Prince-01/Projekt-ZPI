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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;
import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.ShoppingItemAdapter;
import pl.finanse.zpi.pwr.wallet.model.ShoppingItem;

/**
 * Created by sebastiankotarski on 27.05.16.
 */
public class ShoppingListView extends Fragment {
    FloatingActionButton fab;

    private Unbinder unbinder;
    private ArrayList<ShoppingItem>data;
    @BindView(R.id.ShoppingListView)
    ListView shoppingListView;
    @BindView(R.id.newItemEditText)
    EditText newItemEditText;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //long temp = System.currentTimeMillis();
        data = new ArrayList<>();
        View view = inflater.inflate(R.layout.shopping_list_view, container, false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.addShoppingItemButton)
    public void addShoppingItemButton() {
        ShoppingItem newItem = new ShoppingItem(newItemEditText.getText().toString());
        data.add(newItem);
        reloadData();
    }

    @OnItemLongClick(R.id.ShoppingListView)
    public boolean removeItem(int pos) {
        final ShoppingItem op = data.get(pos);
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

    @OnItemClick(R.id.ShoppingListView)
    public void crossItem(int pos) {
        data.get(pos).isChecked = !data.get(pos).isChecked;
        reloadData();
    }

    public void reloadData() {
        Collections.sort(data, new Comparator<ShoppingItem>() {
            @Override
            public int compare(ShoppingItem lhs, ShoppingItem rhs) {
                if(lhs.isChecked == rhs.isChecked )return 0;
                if(lhs.isChecked) return 1;
                else return -1;
            }
        });
        ShoppingItemAdapter adapter = new ShoppingItemAdapter(getActivity(), data, R.layout.shopping_item_row);
        shoppingListView.setAdapter(adapter);
    }
}
