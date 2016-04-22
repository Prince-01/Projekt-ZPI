package pl.finanse.zpi.pwr.wallet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.FabState;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;
import pl.finanse.zpi.pwr.wallet.views.CategoriesView;
import pl.finanse.zpi.pwr.wallet.views.DefaultPage;
import pl.finanse.zpi.pwr.wallet.views.HomePage;
import pl.finanse.zpi.pwr.wallet.views.NewOperation;
import pl.finanse.zpi.pwr.wallet.views.NewStandingOperation;
import pl.finanse.zpi.pwr.wallet.views.RaportPage;
import pl.finanse.zpi.pwr.wallet.views.StandingOperationView;
import pl.finanse.zpi.pwr.wallet.views.WalletView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    SearchView mSearchView;
    FloatingActionButton fab;
    private FabState fabState = FabState.NEW_OPERATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onHandleClick();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new HomePage();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_home:
                toolbar.setTitle("Home");
                fabState = FabState.NEW_OPERATION;
                fragment = new HomePage();
                break;
            case R.id.nav_raports:
                toolbar.setTitle("Raporty");
                fabState = FabState.NEW_OPERATION;
                fragment = new RaportPage();
                break;
            case R.id.nav_categories:
                toolbar.setTitle("Kategorie");
                fabState = FabState.NEW_CATEGORY;
                fragment = new CategoriesView();
                break;
            case R.id.nav_wallet:
                toolbar.setTitle("Portfele");
                fabState = FabState.NEW_WALLET;
                fragment = new WalletView();
                break;
            case R.id.nav_standing_operations:
                toolbar.setTitle("Zlecenia stałe");
                fabState = FabState.NEW_STANDING_OPERATION;
                fragment = new StandingOperationView();
                break;
            default:
                toolbar.setTitle("Default");
                fabState = FabState.NEW_OPERATION;
                fragment = new DefaultPage();
        }
        //Replace mainContent with specified fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();

        // Close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addNewOperation() {
        toolbar.setTitle(R.string.txt_new_operation);
        Fragment fragment = new NewOperation();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onAddPosition(View v) {
        EditText kwota = (EditText) findViewById(R.id.kwotaNowejKategorii);
        EditText tytul = (EditText) findViewById(R.id.tytulNowejOperacji);
        Button date = (Button) findViewById(R.id.datePickerBtn);
        Spinner categories = (Spinner) findViewById(R.id.categoriesSpinner);
        Spinner wallets = (Spinner) findViewById(R.id.walletsSpinner);
        RadioButton wydatek = (RadioButton) findViewById(R.id.wydatekNowejOperacji);

        Category cat = (Category) categories.getSelectedItem();
        Wallet wal = (Wallet) wallets.getSelectedItem();
        String kw = kwota.getText().toString();
        String ty = tytul.getText().toString();
        Date dt;
        try {
            dt = android.text.format.DateFormat.getDateFormat(getApplicationContext()).parse(date.getText().toString());
        } catch (ParseException e) {
            Log.d("ERR", "Couldn't parse date.");
            dt = new Date();
            e.printStackTrace();
        }
        boolean wp = !wydatek.isChecked();

        if(kw .equals("")) {
            Toast.makeText(MainActivity.this, "Brak kwoty ", Toast.LENGTH_SHORT).show();
            return;
        }
        float fkw = Float.parseFloat(kw);
        if(fkw <= 0f) {
            Toast.makeText(MainActivity.this, "Kwota musi byc wieksza od 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Operation operation = new Operation(wal.getName(), ty, fkw, dt, wp, cat.categoryName);

        Database.AddQuickNewPosition(getApplicationContext(), operation);

        HomePage newFragment = new HomePage();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, newFragment).commit();
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

    private void onHandleClick() {
        switch (fabState) {
            case NEW_OPERATION:
                addNewOperation();
                break;
            case NEW_STANDING_OPERATION:
                addNewStandingOperation();
                break;
        }
    }

    public void addNewStandingOperation() {
        toolbar.setTitle(R.string.txt_new_standing_operation);
        Fragment fragment = new NewStandingOperation();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
    }
}