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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.OperationsAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by sebastiankotarski on 05.05.16.
 */
public class HistoryView extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;

    private static ListView operationsListView;
    public static Date fromDate;
    public static Date toDate;
    private FloatingActionButton fab;
    public static boolean isFrom = true;

    private Calendar arrowCalendar = Calendar.getInstance();

    // UI components
    @BindView(R.id.dateFromButton)
    Button dateFromButton;
    @BindView(R.id.dateToButton)
    Button dateToButton;
    @BindView(R.id.walletName)
    TextView walletName;
    @BindView(R.id.timeRange)
    TextView timeRange;

    static TextView balance;

    //TO DO
    static Operation operationsData[];
    public final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        balance = (TextView) view.findViewById(R.id.balance);
        operationsListView = (ListView)view.findViewById(R.id.history_operations);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        toDate = Calendar.getInstance().getTime();
        Calendar fC = Calendar.getInstance();
        fC.add(Calendar.MONTH, -1);
        fromDate = fC.getTime();

        walletName.setText(Wallet.GetActiveWallet(getActivity()).getName());
        // Do wypełniania miesiącami

        makeData(getActivity(),fromDate, toDate);
        updateBalance(balance);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        dateFromButton.setText(dateFormat.format(fromDate));
        dateToButton.setText(dateFormat.format(toDate));
        dateFromButton.setOnClickListener(this);
        dateToButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /*
    Wypełniamy listę dla wybranego przedzialu daty operacje
     */
    public static void makeData(Context context, Date from, Date to) {
        operationsData = Database.GetChoosenPositions(context, from, to);
        OperationsAdapter adapter = new OperationsAdapter(context, R.layout.operation_row, operationsData);
        operationsListView.setAdapter(adapter);
    }

    private static void updateBalance(TextView balance) {
        double sum = 0;
        for(Operation o : operationsData) {
            sum += o.isIncome ? o.cost : -o.cost;
        }
        balance.setText(sum + "");
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
        updateBalance(balance);
        timeRange.setText("");
        arrowCalendar = Calendar.getInstance();
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
            updateBalance(HistoryView.balance);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.rightArrow)
    public void setMonthRight() {
        arrowCalendar.add(Calendar.MONTH, 1);
        Date tFromDate = arrowCalendar.getTime();
        tFromDate.setDate(1);
        Date tToDate = arrowCalendar.getTime();
        tToDate.setDate(arrowCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        makeData(getActivity(), tFromDate, tToDate);
        timeRange.setText(arrowCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " " +
                arrowCalendar.get(Calendar.YEAR));
        Toast.makeText(getActivity(), "W PRAWO", Toast.LENGTH_SHORT).show();
        updateBalance(balance);
    }

    @OnClick(R.id.leftArrow)
    public void setMonthLeft() {
        arrowCalendar.add(Calendar.MONTH, -1);
        Date tFromDate = arrowCalendar.getTime();
        tFromDate.setDate(1);
        Date tToDate = arrowCalendar.getTime();
        tToDate.setDate(arrowCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        makeData(getActivity(), tFromDate, tToDate);
        timeRange.setText(arrowCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " " +
                arrowCalendar.get(Calendar.YEAR));
        Toast.makeText(getActivity(), "W LEWO", Toast.LENGTH_SHORT).show();
        updateBalance(balance);
    }
}
