package pl.finanse.zpi.pwr.wallet.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;

/**
 * Dodawanie nowego stalego zlecenia
 * Created by sebastiankotarski on 21.04.16.
 */
public class NewStandingOperation extends Fragment {
    FloatingActionButton fab;

    public NewStandingOperation() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_standing_operation, container, false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        Spinner categoriesSpinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity(), android.R.layout.simple_spinner_item, Database.GetAllCategories(getActivity()));
        categoriesSpinner.setAdapter(adapter);

        /*TextView text = (TextView) view.findViewById(R.id.NewOperationLabel);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.otf");
        text.setTypeface(tf);*/
        Button startDateButton = (Button)view.findViewById(R.id.startDatePickerBtn);
        Button endDateButton = (Button)view.findViewById(R.id.endDatePickerBtn);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        startDateButton.setText(dateFormat.format(date));
        endDateButton.setText(dateFormat.format(date));
        // Inflate the layout for this fragment
        setupUI(view);
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
}
