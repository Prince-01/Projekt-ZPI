package pl.finanse.zpi.pwr.wallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.model.ShoppingItem;
import pl.finanse.zpi.pwr.wallet.model.ShoppingList;

/**
 * Created by sebastiankotarski on 01.06.16.
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {
    Context context;
    int layoutResourceId;
    ArrayList<ShoppingList> data = null;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public ShoppingListAdapter(Context context, ArrayList<ShoppingList> data, int layoutResourceId) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RowBeanHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RowBeanHolder();
            holder.shoppingListName = (TextView) row.findViewById(R.id.shoppingListName);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        ShoppingList object = data.get(position);
        int kupione = 0;
        for (ShoppingItem si:object.items) {
            if(si.isChecked)
                kupione++;
        }
        holder.shoppingListName.setText(object.name);
        ((TextView)row.findViewById(R.id.shoppingListInfo)).setText("Ilość kupionych:"+kupione+"/"+object.items.size());
        //TODO
        //set items
        return row;
        //saa
    }

    public static class RowBeanHolder {
        public TextView shoppingListName;
    }
}
