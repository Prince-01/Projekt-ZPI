package pl.finanse.zpi.pwr.wallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.model.Operation;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sebastiankotarski on 14.04.16.
 */
public class OperationsAdapter extends ArrayAdapter<Operation> {
    Context context;
    int layoutResourceId;
    Operation data[] = null;
    final DecimalFormat df = new DecimalFormat("#0.00");

    public OperationsAdapter(Context context, int layoutResourceId, Operation[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RowBeanHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RowBeanHolder();
            holder.nameLabel = (TextView) row.findViewById(R.id.operationName);
            holder.categoryName = (TextView) row.findViewById(R.id.operationTitle);
            holder.cost = (TextView) row.findViewById(R.id.operationCost);
            holder.date = (TextView) row.findViewById(R.id.operationDate);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
       // Toast.makeText(row.getContext(),row.toString(),Toast.LENGTH_SHORT).show();
        Operation object = data[position];
//        SimpleDateFormat sdf = new SimpleDateFormat(String.valueOf(DateFormat.getDateInstance()), Locale.ENGLISH);
//        holder.nameLabel.setText(sdf.format(object.date));
//        holder.nameLabel.setText("Nazwa");
        holder.nameLabel.setText(object.category);
        holder.categoryName.setText(object.operationName);
        holder.cost.setText(df.format(object.isIncome ? object.cost : -object.cost));
        holder.cost.setTextColor(object.isIncome ? Color.rgb(46, 204, 113) : Color.rgb(217, 30, 24));

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        holder.date.setText(dateFormat.format(object.date));

        return row;
        //saa
    }

    public static class RowBeanHolder {
        public TextView nameLabel;
        public TextView categoryName;
        public TextView cost;
        public TextView date;
    }
}
