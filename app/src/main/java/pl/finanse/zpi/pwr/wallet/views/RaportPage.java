package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import pl.finanse.zpi.pwr.wallet.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Robert on 2016-04-01.
 */
public class RaportPage extends Fragment {
    public RaportPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        // Inflate the layout for this fragment
        PieChart wykresKolowy = (PieChart) view.findViewById(R.id.wykresKolowy);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(12f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
        PieDataSet dataset = new PieDataSet(entries, "# of Calls");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        PieData data = new PieData(labels, dataset); // initialize Piedata
        wykresKolowy.setData(data); // set data into chart
        wykresKolowy.setDescription("Description");  // set the description
        wykresKolowy.getData().setValueTextSize(20);

        return view;
    }
}
