package pl.finanse.zpi.pwr.wallet.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pl.finanse.zpi.pwr.wallet.R;

/**
 * Created by sebastiankotarski on 14.04.16.
 */
public class OperationsAdapter extends ArrayAdapter<Operation> {
    Context context;
    int layoutResourceId;
    Operation data[] = null;

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
            holder.dateLabel = (TextView) row.findViewById(R.id.operationDate);
            holder.categoryName = (TextView) row.findViewById(R.id.operationTitle);
            holder.cost = (TextView) row.findViewById(R.id.operationCost);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        Operation object = data[position];
//        SimpleDateFormat sdf = new SimpleDateFormat(String.valueOf(DateFormat.getDateInstance()), Locale.ENGLISH);
//        holder.dateLabel.setText(sdf.format(object.date));
        holder.dateLabel.setText("8 marca 2016");
        holder.categoryName.setText(object.operationName);
        holder.cost.setText(String.valueOf(object.cost));
        return row;
    }

    static class RowBeanHolder {
        TextView dateLabel;
        TextView categoryName;
        TextView cost;
    }
}
