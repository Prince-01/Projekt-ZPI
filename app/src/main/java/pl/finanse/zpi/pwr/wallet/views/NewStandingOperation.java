package pl.finanse.zpi.pwr.wallet.views;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.StandingOperation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Dodawanie nowego stalego zlecenia
 * Created by sebastiankotarski on 21.04.16.
 */
public class NewStandingOperation extends Fragment implements View.OnClickListener {
    FloatingActionButton fab;
    View mainView;

    public NewStandingOperation() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_standing_operation, container, false);
        mainView = view;
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        Spinner categoriesSpinner = (Spinner) view.findViewById(R.id.spinnerCategoryNewStandingOperation);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Category.getAllFormattedCategoriesWithDepth(getActivity()));
        categoriesSpinner.setAdapter(adapter);

        Spinner walletsSpinner = (Spinner) view.findViewById(R.id.spinnerWalletNewStandingOperation);

        ArrayAdapter<Wallet> walletArrayAdapter = new ArrayAdapter<Wallet>(getActivity(), android.R.layout.simple_spinner_item,
                Database.GetAllWallets(getActivity()));
        walletsSpinner.setAdapter(walletArrayAdapter);

        int walletIndex = 0;
        Wallet[] temp = Database.GetAllWallets(getActivity());
        for(int i = 0; i < temp.length ; i++) {
            if(temp[i].getName().equals(Wallet.GetActiveWallet(getActivity()).getName()))
                walletIndex = i;
        }
        walletsSpinner.setSelection(walletIndex,true);

        /*TextView text = (TextView) view.findViewById(R.id.NewOperationLabel);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.otf");
        text.setTypeface(tf);*/
        final Button startDateButton = (Button)view.findViewById(R.id.startDatePickerBtn);
        final Button endDateButton = (Button)view.findViewById(R.id.endDatePickerBtn);
        startDateButton.setOnClickListener(this);
        endDateButton.setOnClickListener(this);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        startDateButton.setText(dateFormat.format(date));
        endDateButton.setText(dateFormat.format(date));
        endDateButton.setVisibility(View.INVISIBLE);
        (view.findViewById(R.id.textView11)).setVisibility(View.INVISIBLE);
        // Inflate the layout for this fragment
        setupUI(view);
        CheckBox checkbox = (CheckBox)view.findViewById(R.id.checkBox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            endDateButton.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                (getView().findViewById(R.id.textView11)).setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });

        final TextView til = (TextView) view.findViewById(R.id.timeIntervalLabel);
        SeekBar tisb = (SeekBar) view.findViewById(R.id.timeInterval);

        ((Button) view.findViewById(R.id.button2)).setOnClickListener(this);

        tisb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress < 14)
                    til.setText("Co tydzień");
                else if(progress < 28)
                    til.setText("Co dwa tygodnie");
                else if(progress < 42)
                    til.setText("Co miesiąc");
                else if(progress < 56)
                    til.setText("Co dwa miesiące");
                else if(progress < 70)
                    til.setText("Co kwartał");
                else if(progress < 84)
                    til.setText("Co pół roku");
                else
                    til.setText("Co rok");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
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

    public void chooseDateClicked(View view) {
        Toast.makeText(getActivity(), "date picker dialog", Toast.LENGTH_SHORT).show();
    }

    public void onAddStandingOperation(View v) {
        EditText kwota = (EditText) mainView.findViewById(R.id.standingOperationCost);
        EditText tytul = (EditText) mainView.findViewById(R.id.standingOperationTitleTemp);
        Button start = (Button) mainView.findViewById(R.id.startDatePickerBtn);
        Button end = (Button) mainView.findViewById(R.id.endDatePickerBtn);
        CheckBox czyDataZak = (CheckBox) mainView.findViewById(R.id.checkBox);
        Spinner categories = (Spinner) mainView.findViewById(R.id.spinnerCategoryNewStandingOperation);
        Spinner wallets = (Spinner) mainView.findViewById(R.id.spinnerWalletNewStandingOperation);
        RadioButton wydatek = (RadioButton) mainView.findViewById(R.id.wydatekNowejOperacji);
        SeekBar tisb = (SeekBar) mainView.findViewById(R.id.timeInterval);
        String cat = (String) categories.getSelectedItem();
        Wallet wal = (Wallet) wallets.getSelectedItem();
        String kw = kwota.getText().toString();
        String ty = tytul.getText().toString();

        Wallet.SetActiveWallet(getActivity(), Database.GetAllWallets(getActivity())[(int)wallets.getSelectedItemPosition()].getName());

        Date beg;
        Date ending = null;
        try {
            beg = android.text.format.DateFormat.getDateFormat(getActivity()).parse(start.getText().toString());
        } catch (ParseException e) {
            Log.d("ERR", "Couldn't parse date.");
            beg = new Date();
            e.printStackTrace();
        }
        if(czyDataZak.isChecked()) {
            try {
                ending = android.text.format.DateFormat.getDateFormat(getActivity()).parse(end.getText().toString());
            } catch (ParseException e) {
                Log.d("ERR", "Couldn't parse date.");
                ending = new Date();
                e.printStackTrace();
            }
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
        StandingOperation standingOperation = new StandingOperation(-1, wal.getName(), ty, fkw, beg, ending, tisb.getProgress(), wp, cat.trim());

        Database.CheckIfStandingOperationsUpdateIsNeededAndUpdate(getActivity());

        Database.AddNewStandingOperation(getActivity(), standingOperation);
        Wallet.SetActiveWallet(getActivity(), standingOperation.wallet);

        Calendar from = Calendar.getInstance();
        from.setTimeInMillis(beg.getTime());
        Calendar to = Calendar.getInstance();
        Database.UpdateStandingOperationFromDateToDate(getActivity(), standingOperation, from, to);

        HomePage newFragment = new HomePage();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, newFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button2:
                onAddStandingOperation(v);
                break;
            case R.id.startDatePickerBtn:
                Button startDateButton = (Button)v.findViewById(R.id.startDatePickerBtn);
                showDatePickerDialog(v, startDateButton);
                break;
            case R.id.endDatePickerBtn:
                Button endDateButton = (Button)v.findViewById(R.id.endDatePickerBtn);
                showDatePickerDialog(v, endDateButton);
                break;

        }
    }
    public void showDatePickerDialog(View v, Button b) {
        NewOperation.DatePickerFragment newFragment = new NewOperation.DatePickerFragment();
        newFragment.butt = b;
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
