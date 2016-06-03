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
import pl.finanse.zpi.pwr.wallet.views.HomePage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sebastiankotarski on 21.04.16.
 */
public class StandingOperationsAdapter  extends ArrayAdapter<StandingOperation> {
    Context context;
    int layoutResourceId;
    ArrayList<StandingOperation> data = null;

    public StandingOperationsAdapter(Context context, int layoutResourceId, ArrayList<StandingOperation> data) {
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
            holder.wallet = (TextView) row.findViewById(R.id.standingOperationWallet);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        StandingOperation object = data.get(position);
        holder.nameLabel.setText(object.operationName);
        holder.categoryName.setText(object.category);
        holder.cost.setText(HomePage.decimalFormat.format(object.cost) + " zł");
        holder.startDateLabel.setText("Data rozpoczęcia: " + SimpleDateFormat.getInstance().format(object.begin));
        if(object.end != null) holder.endDateLabel.setText("Data zakończenia: " + SimpleDateFormat.getInstance().format(object.end));
        holder.timeInterval.setText("Co miesiąc");
        if(object.interval == StandingOperation.INTERVAL.week)
            holder.timeInterval.setText("Co tydzień");
        else if(object.interval == StandingOperation.INTERVAL.twoweeks)
            holder.timeInterval.setText("Co dwa tygodnie");
        else if(object.interval == StandingOperation.INTERVAL.month)
            holder.timeInterval.setText("Co miesiąc");
        else if(object.interval == StandingOperation.INTERVAL.twomonths)
            holder.timeInterval.setText("Co dwa miesiące");
        else if(object.interval == StandingOperation.INTERVAL.quarter)
            holder.timeInterval.setText("Co kwartał");
        else if(object.interval == StandingOperation.INTERVAL.halfyear)
            holder.timeInterval.setText("Co pół roku");
        else
            holder.timeInterval.setText("Co rok");
        holder.wallet.setText(object.wallet);
        return row;
    }

    public static class RowBeanHolder {
        public TextView nameLabel;
        public TextView categoryName;
        public TextView cost;
        public TextView startDateLabel;
        public TextView endDateLabel;
        public TextView timeInterval;
        public TextView wallet;
    }
}
