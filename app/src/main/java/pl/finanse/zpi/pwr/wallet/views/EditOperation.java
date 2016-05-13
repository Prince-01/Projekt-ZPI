package pl.finanse.zpi.pwr.wallet.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.filters.ValueInputFilter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by rober on 05.05.2016.
 */
public class EditOperation extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab;
    private View mainView;
    Operation operationToEdit;
   private EditText kwotaEdytowana;
   private EditText title;
   private Spinner categoriesSpinner;
  private  Spinner walletsSpinner;
    private Button dateButton;
    public EditOperation(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getActivity(),""+operationToEdit.id,Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.edit_operation, container, false);
        mainView = view;
        setupButtons(view);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        categoriesSpinner = (Spinner) view.findViewById(R.id.categoriesSpinner);
        walletsSpinner = (Spinner) view.findViewById(R.id.walletsSpinner);
        List<String> categoriesStrings = Category.getAllFormattedCategoriesWithDepth(getActivity());
        int chosen = 0;
        for(int i=0;i<categoriesStrings.size();i++)
            if(categoriesStrings.get(i).trim().equals(operationToEdit.category)){
                chosen = i;
                break;
            }
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                categoriesStrings);
        categoriesSpinner.setAdapter(categoryArrayAdapter);
        categoriesSpinner.setSelection(chosen);
        Wallet[] wallets = Database.GetAllWallets(getActivity());
        for(int i=0;i<wallets.length;i++)
            if(wallets[i].getName().equals(operationToEdit.wallet)){
                chosen = i;
                break;
            }
        ArrayAdapter<Wallet> walletArrayAdapter = new ArrayAdapter<Wallet>(getActivity(), android.R.layout.simple_spinner_item,
                wallets);
        walletsSpinner.setAdapter(walletArrayAdapter);
        walletsSpinner.setSelection(chosen);
        kwotaEdytowana = ((EditText) view.findViewById(R.id.kwotaEdytowanejOperacji));
        kwotaEdytowana.setFilters(new InputFilter[]{new ValueInputFilter(10, 2)});
        kwotaEdytowana.setText(Float.toString(operationToEdit.cost));
        title = ((EditText) view.findViewById(R.id.tytulEdytowanejOperacji));
        title.setText(operationToEdit.operationName);
        ((RadioButton)view.findViewById(operationToEdit.isIncome?R.id.radioButton:R.id.wydatekEdytowanejOperacji)).setChecked(true);
        dateButton = (Button)view.findViewById(R.id.datePickerBtn);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        dateButton.setText(dateFormat.format(operationToEdit.date));
        Button editBtn = ((Button)view.findViewById(R.id.editPositionBtn));
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationToEdit.cost = Float.parseFloat(kwotaEdytowana.getText().toString());
                operationToEdit.operationName = title.getText().toString();
                operationToEdit.category = categoriesSpinner.getSelectedItem().toString().trim();
                operationToEdit.wallet = walletsSpinner.getSelectedItem().toString().trim();
                operationToEdit.isIncome = !((RadioButton)getView().findViewById(R.id.wydatekEdytowanejOperacji)).isChecked();
                try {
                    operationToEdit.date = android.text.format.DateFormat.getDateFormat(getActivity()).parse(dateButton.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Database.EditPosition(getActivity(),operationToEdit);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContent, new HomePage()).commit();

            }
        });
        // Inflate the layout for this fragment
        setupUI(view);
        return view;
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.datePickerBtn).setOnClickListener(this);
        view.findViewById(R.id.editPositionBtn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datePickerBtn:
                showDatePickerDialog(v);
                break;
            case R.id.editPositionBtn:
                onEditPosition(v);
                break;
            default:
                Toast.makeText(getActivity(), "Nieobsługiwany przycisk! Somethings wrong! ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        fab.setVisibility(View.VISIBLE);
        super.onDestroyView();
    }

    /*
    To powinno być w jakimś utils czy coś
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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

    /*
    Edycja operacji
     */
    public void onEditPosition(View v) {

    }
}
