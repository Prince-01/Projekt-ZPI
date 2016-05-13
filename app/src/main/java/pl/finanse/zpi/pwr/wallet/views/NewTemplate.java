package pl.finanse.zpi.pwr.wallet.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.filters.ValueInputFilter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by sebastiankotarski on 12.05.16.
 */
public class NewTemplate extends Fragment {
    FloatingActionButton fab;
    Spinner categoriesSpinner;
    Spinner walletsSpinner;
    EditText nazwa,koszt,tytul;
    RadioButton czyWydatek;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_template, container, false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        nazwa = (EditText) view.findViewById(R.id.templateName);
        koszt = (EditText) view.findViewById(R.id.templateCost);
        koszt.setFilters(new InputFilter[] { new ValueInputFilter(10,2) });
        tytul = (EditText) view.findViewById(R.id.tytulNowegoszablonu);
        categoriesSpinner = (Spinner) view.findViewById(R.id.spinner);
        walletsSpinner = (Spinner) view.findViewById(R.id.walletsSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,Category.getAllFormattedCategoriesWithDepth(getActivity()));
        categoriesSpinner.setAdapter(adapter);
        ArrayAdapter<Wallet> adapt = new ArrayAdapter<Wallet>(getActivity(), android.R.layout.simple_spinner_item,Database.GetAllWallets(getActivity()));
        walletsSpinner.setAdapter(adapt);
        czyWydatek = (RadioButton) view.findViewById(R.id.wydatekNowegoSzablonu);
        Button btn = (Button)view.findViewById(R.id.newTemplateButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Klikneto w nowy szablon , kurwa",Toast.LENGTH_SHORT).show();
            }
        });

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

}
