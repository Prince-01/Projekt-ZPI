package pl.finanse.zpi.pwr.wallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by rober on 21.04.2016.
 */
public class WalletAdapter extends ArrayAdapter<Wallet> {
        Context context;
        int layoutResourceId;
        Wallet data[] = null;

    public WalletAdapter(Context context, int layoutResourceId, Wallet[] data) {
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
            holder.name = (TextView) row.findViewById(R.id.walletName);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        Wallet object = data[position];
//        SimpleDateFormat sdf = new SimpleDateFormat(String.valueOf(DateFormat.getDateInstance()), Locale.ENGLISH);
//        holder.nameLabel.setText(sdf.format(object.date));
        holder.name.setText(object.getName());
        return row;
    }

    public static class RowBeanHolder {
        public TextView name;

    }
}
