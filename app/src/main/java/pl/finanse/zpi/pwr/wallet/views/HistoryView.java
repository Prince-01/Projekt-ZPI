package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.Calendar;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.OperationsAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Operation;

/**
 * Created by sebastiankotarski on 05.05.16.
 */
public class HistoryView extends Fragment{
    private ListView operationsListView;
    private CalendarView calendarView;
    //TO DO
    Operation operationsData[];
    public final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        operationsListView = (ListView)view.findViewById(R.id.history_operations);
        makeData(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        OperationsAdapter adapter = new OperationsAdapter(getActivity(), R.layout.operation_row, operationsData);
        operationsListView.setAdapter(adapter);
        calendarView=(CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                makeData(year,month,dayOfMonth);
            }
        });
        return view;
    }
/*
Wypełniamy listę dla wybranej daty operacje
 */
    private void makeData(int year, int month, int dayOfMonth) {
        operationsData = Database.GetAllPositions(getActivity().getApplicationContext());
    }
}
