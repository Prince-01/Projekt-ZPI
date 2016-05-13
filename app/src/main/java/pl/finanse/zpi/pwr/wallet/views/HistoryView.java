package pl.finanse.zpi.pwr.wallet.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.OperationsAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Operation;

/**
 * Created by sebastiankotarski on 05.05.16.
 */
public class HistoryView extends Fragment implements View.OnClickListener {
    private static ListView operationsListView;
    private Button dateFromButton;
    private Button dateToButton;
    public static Date fromDate;
    public static Date toDate;
    private FloatingActionButton fab;
    public static boolean isFrom = true;

    //TO DO
    static Operation operationsData[];
    public final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        operationsListView = (ListView)view.findViewById(R.id.history_operations);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        toDate = Calendar.getInstance().getTime();
        Calendar fC = Calendar.getInstance();
        fC.add(Calendar.MONTH, -1);
        fromDate = fC.getTime();
        dateFromButton = (Button)view.findViewById(R.id.dateFromButton);
        System.out.println("Btn" + dateFromButton);
        dateToButton = (Button)view.findViewById(R.id.dateToButton);
        System.out.println("Btn" + dateToButton);

        makeData(getActivity(),fromDate, toDate);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        dateFromButton.setText(dateFormat.format(fromDate));
        dateToButton.setText(dateFormat.format(toDate));
        dateFromButton.setOnClickListener(this);
        dateToButton.setOnClickListener(this);
        return view;
    }
/*
Wypełniamy listę dla wybranego przedzialu daty operacje
 */
    public static void makeData(Context context, Date from, Date to) {
        operationsData = Database.GetChoosenPositions(context, from, to);
        OperationsAdapter adapter = new OperationsAdapter(context, R.layout.operation_row, operationsData);
        operationsListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.dateFromButton:
                isFrom = true;
                showDatePickerDialog(v);
                break;
            case R.id.dateToButton:
                isFrom = false;
                showDatePickerDialog(v);
                break;
        }

    }

    @Override
    public void onDestroyView() {
        fab.setVisibility(View.VISIBLE);
        super.onDestroyView();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Button from = (Button)getActivity().findViewById(R.id.dateFromButton);
            Button to = (Button)getActivity().findViewById(R.id.dateToButton);
            String date;
            if(HistoryView.isFrom) {
                date = from.getText().toString();
            } else {
                date = to.getText().toString();
            }
            final Calendar c = Calendar.getInstance();
            int year = Integer.parseInt(date.substring(6));
            int month = Integer.parseInt(date.substring(3,5));// c.get(Calendar.MONTH);
            int day = Integer.parseInt(date.substring(0,2));
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month-1, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Button from = (Button)getActivity().findViewById(R.id.dateFromButton);
            Button to = (Button)getActivity().findViewById(R.id.dateToButton);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
            if(HistoryView.isFrom) {
                from.setText(dateFormat.format(calendar.getTime()));
                fromDate = calendar.getTime();
            } else {
                to.setText(dateFormat.format(calendar.getTime()));
                toDate = calendar.getTime();
            }
            makeData(getActivity(),fromDate,toDate);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");


    }

}
