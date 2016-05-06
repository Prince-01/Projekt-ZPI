package pl.finanse.zpi.pwr.wallet.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
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
    private ListView operationsListView;
    private Button dateFromButton;
    private Button dateToButton;
    private Date fromDate;
    private Date toDate;
    private FloatingActionButton fab;

    //TO DO
    Operation operationsData[];
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

        makeData(fromDate, toDate);
        OperationsAdapter adapter = new OperationsAdapter(getActivity(), R.layout.operation_row, operationsData);
        operationsListView.setAdapter(adapter);
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
    private void makeData(Date from, Date to) {
        operationsData = Database.GetAllPositions(getActivity().getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        showDatePickerDialog(v);
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
            // Use the current date as the default date in the picker
            //TO DO
            //WYŚWIETLIC DATĘ Z DATE
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
/*
Ustawiamy tekst na danym przycisku i wywołujemy makeData

 */
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
//            Button button = (Button)getActivity().findViewById(R.id.datePickerBtn);
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(year, month, day);
//            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
//            button.setText(dateFormat.format(calendar.getTime()));
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

}
