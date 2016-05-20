package pl.finanse.zpi.pwr.wallet.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.filters.ValueInputFilter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by sebastiankotarski on 07.04.16.
 */
public class NewOperation extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab;
    private View mainView;

    public NewOperation() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_operation, container, false);
        mainView = view;
        setupButtons(view);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        Spinner categoriesSpinner = (Spinner) view.findViewById(R.id.categoriesSpinner);
        Spinner walletsSpinner = (Spinner) view.findViewById(R.id.walletsSpinner);

        List<String> categoriesStrings = Category.getAllFormattedCategoriesWithDepth(getActivity());

        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                categoriesStrings);
        categoriesSpinner.setAdapter(categoryArrayAdapter);
        ArrayAdapter<Wallet> walletArrayAdapter = new ArrayAdapter<Wallet>(getActivity(), android.R.layout.simple_spinner_item,
                Database.GetAllWallets(getActivity()));
        walletsSpinner.setAdapter(walletArrayAdapter);

        int walletIndex = 0;
        Wallet[] temp = Database.GetAllWallets(getActivity());
        for(int i = 0; i < temp.length ; i++) {
            if(temp[i].getName().equals(Wallet.GetActiveWallet(getActivity()).getName())) {
                walletIndex = i;
                break;
            }
        }
        walletsSpinner.setSelection(walletIndex,true);

        ((EditText)view.findViewById(R.id.kwotaNowejKategorii)).setFilters(new InputFilter[] { new ValueInputFilter(10,2) });
        Button button = (Button)view.findViewById(R.id.datePickerBtn);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        button.setText(dateFormat.format(date));
        // Inflate the layout for this fragment
        setupUI(view);
        return view;
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.datePickerBtn).setOnClickListener(this);
        view.findViewById(R.id.addPositionBtn).setOnClickListener(this);
        view.findViewById(R.id.useTemplateBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datePickerBtn:
                showDatePickerDialog(v);
                break;
            case R.id.addPositionBtn:
                onAddPosition(v);
                break;
            case R.id.useTemplateBtn:
                onTemplateUse(v);
                break;
            default:
                Toast.makeText(getActivity(), "Nieobsługiwany przycisk! Somethings wrong! ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void onTemplateUse(View v) {
        TemplatesView newFragment = new TemplatesView();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, newFragment).commit();
    }

    @Override
    public void onDestroyView() {
        fab.setVisibility(View.VISIBLE);
        super.onDestroyView();
    }
    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Button button = (Button)getActivity().findViewById(R.id.datePickerBtn);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
            button.setText(dateFormat.format(calendar.getTime()));
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onAddPosition(View v) {
        EditText kwota = (EditText) mainView.findViewById(R.id.kwotaNowejKategorii);
        EditText tytul = (EditText) mainView.findViewById(R.id.tytulNowejOperacji);
        Button date = (Button) mainView.findViewById(R.id.datePickerBtn);
        Spinner categories = (Spinner) mainView.findViewById(R.id.categoriesSpinner);
        Spinner wallets = (Spinner) mainView.findViewById(R.id.walletsSpinner);
        RadioButton wydatek = (RadioButton) mainView.findViewById(R.id.wydatekNowejOperacji);

        Wallet.SetActiveWallet(getActivity(), Database.GetAllWallets(getActivity())[(int)wallets.getSelectedItemId()].getName());

        String cat = (String) categories.getSelectedItem();
        Wallet wal = (Wallet) wallets.getSelectedItem();
        String kw = kwota.getText().toString();
        String ty = tytul.getText().toString();
        Date dt;
        try {
            dt = android.text.format.DateFormat.getDateFormat(getActivity()).parse(date.getText().toString());
        } catch (ParseException e) {
            Log.d("ERR", "Couldn't parse date.");
            dt = new Date();
            e.printStackTrace();
        }
        boolean wp = !wydatek.isChecked();

        if(kw .equals("")) {
            Toast.makeText(getActivity(), "Brak kwoty ", Toast.LENGTH_SHORT).show();
            return;
        }
        float fkw = Float.parseFloat(kw);
        if(fkw <= 0f) {
            Toast.makeText(getActivity(), "Kwota musi byc wieksza od 0", Toast.LENGTH_SHORT).show();
            return;
        }
        Operation operation = new Operation(-1, wal.getName(), ty, fkw, dt, wp, cat.trim());

        Database.AddQuickNewPosition(getActivity(), operation);
        Wallet.SetActiveWallet(getActivity(), operation.wallet);

        HomePage newFragment = new HomePage();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, newFragment).commit();
    }
}