package pl.finanse.zpi.pwr.wallet.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pl.finanse.zpi.pwr.wallet.R;

/**
 * Created by rober on 12.04.2016.
 */
/*
Adapter do ListView w kategoriach
 */
public class CategoriesAdapter extends ArrayAdapter<Category> {
    Context context;
    int layoutResourceId;
    Category data[] = null;

    public CategoriesAdapter(Context context, int layoutResourceId, Category[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowBeanHolder holder = null;
        //Dodawanie do wiersza tekstu i ikony
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RowBeanHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.categoryIcon);
            holder.txtTitle = (TextView) row.findViewById(R.id.categoryText);

            row.setTag(holder);
        }
        else
        {
            holder = (RowBeanHolder)row.getTag();
        }

        Category object = data[position];
        holder.txtTitle.setText(object.categoryName);
        holder.imgIcon.setImageResource(object.icon);

        return row;
    }

    static class RowBeanHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }

}
