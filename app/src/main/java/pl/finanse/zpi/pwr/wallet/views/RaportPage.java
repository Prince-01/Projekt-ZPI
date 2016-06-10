package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Operation;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.*;

/**
 * Created by Robert on 2016-04-01.
 */
public class RaportPage extends Fragment {

    FloatingActionButton fab;

    public RaportPage() {
        // Required empty public constructor
    }

    public float getSumOfOperationsForCategory(Operation[] operations, String category) {
        float result = 0.0f;

        for (Operation o : operations) {
            if(o.category.equals(category))
                result += o.isIncome ? 0 : o.cost;
        }
        return result;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        // Inflate the layout for this fragment
        List<Category> categoriesWithDepth = Category.getAllCategoriesWithDepth(getActivity());
        Operation[] operations = Database.GetAllPositions(getActivity());
        List<String> categoriesNames = new ArrayList<>();
        List<Float> categoriesValues = new ArrayList<>();

        for (Category c : categoriesWithDepth) {
            if(c.depth == 0)
                categoriesNames.add(c.categoryName);
        }

        float sum = 0.0f;
        int index = categoriesNames.size() - 1;
        ListIterator<Category> iterator = categoriesWithDepth.listIterator(categoriesWithDepth.size());
        while(iterator.hasPrevious()) {
            Category c = iterator.previous();

            sum += getSumOfOperationsForCategory(operations, c.categoryName);
            if(c.depth == 0) {
                if(sum != 0)
                    categoriesValues.add(sum);
                else
                    categoriesNames.remove(index);
                sum = 0.0f;
                index--;
            }
        }

        PieChart wykresKolowy = (PieChart) view.findViewById(R.id.wykresKolowy);
        ArrayList<Entry> pieEntries = new ArrayList<>();
        for(int i = categoriesValues.size() - 1; i >= 0; i--)
            pieEntries.add(new Entry(categoriesValues.get(i), categoriesValues.size() - 1 - i));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "# of Calls");

        PieData pieData = new PieData(categoriesNames, pieDataSet); // initialize Piedata
        wykresKolowy.setData(pieData); // set data into chart
        wykresKolowy.setDescription("Description");  // set the description
        wykresKolowy.getData().setValueTextSize(13);
        wykresKolowy.getLegend().setTextSize(13);
        wykresKolowy.getLegend().setTextColor(Color.WHITE);
        wykresKolowy.setDrawSliceText(false);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        LineChart wykresLiniowy = (LineChart) view.findViewById(R.id.wykresLiniowy);
        ArrayList<Entry> lineEntries = new ArrayList<>();
        //for()

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        float walletState = 0;
        List<Float> walletStates = new ArrayList<>();
        List<String> walletDatesStrings = new ArrayList<>();

        for(int i = 0; i < operations.length && operations[i].date.getMonth() + 1 <= month; i++) {
            walletState += operations[i].isIncome ? operations[i].cost : -operations[i].cost;

            if(operations[i].date.getMonth() + 1 == month) {
                walletStates.add(walletState);
                walletDatesStrings.add(android.text.format.DateFormat.getDateFormat(getActivity()).format(operations[i].date));
            }
        }

        for (int i = 0; i < walletStates.size(); i++)
            lineEntries.add(new Entry(walletStates.get(i), i));

        LineDataSet lds = new LineDataSet(lineEntries, " # of cells");
        LineData ld = new LineData(walletDatesStrings, lds);
        wykresLiniowy.setData(ld);

        /*
        Wykres Liniowy
         */

        return view;
    }

    @Override
    public void onDestroy() {
        fab.setVisibility(View.VISIBLE);
        super.onDestroy();
    }
}
