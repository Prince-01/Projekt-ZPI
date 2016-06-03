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
import java.util.List;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.model.ShoppingItem;

/**
 * Created by sebastiankotarski on 27.05.16.
 */
public class ShoppingItemAdapter extends ArrayAdapter<ShoppingItem> {
    Context context;
    int layoutResourceId;
    List<ShoppingItem>data = null;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    public ShoppingItemAdapter(Context context, List<ShoppingItem> data, int layoutResourceId) {
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
            holder.shoppingItemName = (TextView) row.findViewById(R.id.itemName);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        ShoppingItem object = data.get(position);
        holder.shoppingItemName.setText(object.name);
        if(data.get(position).isChecked) {
            holder.shoppingItemName.setText(object.name, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) holder.shoppingItemName.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, object.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.shoppingItemName.setTextColor(getContext().getResources().getColor(R.color.grey));
        }
        else {
            holder.shoppingItemName.setText(object.name, TextView.BufferType.NORMAL);
            holder.shoppingItemName.setTextColor(getContext().getResources().getColor(R.color.white));
        }
        return row;
        //saa
    }

    public static class RowBeanHolder {
        public TextView shoppingItemName;
    }
}
