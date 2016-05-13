package pl.finanse.zpi.pwr.wallet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.model.Template;


/**
 * Created by sebastiankotarski on 12.05.16.
 */
public class TemplateAdapter extends ArrayAdapter<Template> {
    Context context;
    int layoutResourceId;
    Template data[] = null;

    public TemplateAdapter(Context context, int layoutResourceId, Template[] data) {
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
            holder.nameLabel = (TextView) row.findViewById(R.id.templateTitle);
            holder.categoryName = (TextView) row.findViewById(R.id.templateCategoryText);
            holder.cost = (TextView) row.findViewById(R.id.templateCost);
            holder.title = (TextView) row.findViewById(R.id.templateName);
            holder.wallet = (TextView)row.findViewById(R.id.templateWallet);
            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }
        Template object = data[position];
        holder.nameLabel.setText("Nazwa szablonu");
        holder.title.setText("Nazwa operacji");
        holder.categoryName.setText("Kategoria");
        holder.cost.setText(String.valueOf(527) + " zł");
        holder.wallet.setText("nazwa portfela");
        return row;
    }

    public static class RowBeanHolder {
        public TextView nameLabel;
        public TextView categoryName;
        public TextView cost;
        public TextView title;
        public TextView wallet;
    }
}
