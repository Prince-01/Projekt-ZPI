package pl.finanse.zpi.pwr.wallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.model.StandingOperation;

/**
 * Created by sebastiankotarski on 21.04.16.
 */
public class StandingOperationsAdapter  extends ArrayAdapter<StandingOperation> {
    Context context;
    int layoutResourceId;
    StandingOperation data[] = null;

    public StandingOperationsAdapter(Context context, int layoutResourceId, StandingOperation[] data) {
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
            holder.nameLabel = (TextView) row.findViewById(R.id.standingOperationTitle);
            holder.categoryName = (TextView) row.findViewById(R.id.standingOperationcategoryText);
            holder.cost = (TextView) row.findViewById(R.id.standingOperationCost);
            holder.endDateLabel = (TextView) row.findViewById(R.id.standingOperationEndDate);
            holder.startDateLabel = (TextView) row.findViewById(R.id.standingOperationStartDate);
            holder.timeInterval = (TextView)row.findViewById(R.id.standingOperationTimeInterval);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        StandingOperation object = data[position];
        holder.nameLabel.setText("Nazwa");
        holder.categoryName.setText("Kategoria");
        holder.cost.setText(String.valueOf(527) + " zł");
        holder.startDateLabel.setText("Data rozpoczęcia: 22.04.2016");
        holder.endDateLabel.setText("Data zakończenia: 22.04.2017");
        holder.timeInterval.setText("Co miesiąc");
        return row;
    }

    public static class RowBeanHolder {
        public TextView nameLabel;
        public TextView categoryName;
        public TextView cost;
        public TextView startDateLabel;
        public TextView endDateLabel;
        public TextView timeInterval;
    }
}
