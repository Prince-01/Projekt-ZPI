package pl.finanse.zpi.pwr.wallet;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.FabState;
import pl.finanse.zpi.pwr.wallet.model.Wallet;
import pl.finanse.zpi.pwr.wallet.views.CategoriesView;
import pl.finanse.zpi.pwr.wallet.views.DefaultPage;
import pl.finanse.zpi.pwr.wallet.views.HistoryView;
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
        if(Wallet.GetActiveWallet(this) == null)
            Wallet.SetActiveWallet(this, Database.GetAllWallets(this)[0].getName());
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
            case R.id.nav_history:
                toolbar.setTitle("Historia");
           //     fabState = FabState.NEW_OPERATION;
                fragment = new HistoryView();
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

    /**
     * sluzy do pobierania sharedpreferences, zeby wszystko bylo w 1 miejscu
     * @param context
     * @return
     */
    public static final SharedPreferences GetGlobalSharedPreferences(Context context){
         return context.getSharedPreferences("ustawienia", Context.MODE_PRIVATE);
    }

    private void onHandleClick() {
        switch (fabState) {
            case NEW_OPERATION:
                addNewOperation();
                break;
            case NEW_STANDING_OPERATION:
                addNewStandingOperation();
                break;
            case NEW_CATEGORY:
                addNewCategory();
                break;
            case NEW_WALLET:
                addNewWallet();
                break;
        }
    }

    public void addNewOperation() {
        toolbar.setTitle(R.string.txt_new_operation);
        Fragment fragment = new NewOperation();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
    }

    public void addNewStandingOperation() {
        toolbar.setTitle(R.string.txt_new_standing_operation);
        Fragment fragment = new NewStandingOperation();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
    }

    public void addNewCategory() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_category);
        dialog.setTitle(R.string.new_cat_string);

        // set the custom dialog components - text, image and button
        Spinner parentCategorySpinner = (Spinner)dialog.findViewById(R.id.parentCategory);
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item,
                Database.GetAllCategories(this));
        parentCategorySpinner.setAdapter(categoryArrayAdapter);

        Spinner spinner2 = (Spinner)dialog.findViewById(R.id.iconForCategory);
//        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        Button dialogButton = (Button) dialog.findViewById(R.id.addCategory);
        // if button is clicked, close the custom dialog

        /*
        Dodawanie kategori do bazy
         */
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void addNewWallet() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_wallet);
        dialog.setTitle(R.string.new_wallet_string);

        // set the custom dialog components - text, image and button
        TextView walletName = (TextView) dialog.findViewById(R.id.newWalletName);
        Spinner currencySpinner = (Spinner)dialog.findViewById(R.id.currency_spinner);
        /*
        wypisanie walut z bazy w spinerze
         */
        Button dialogButton = (Button) dialog.findViewById(R.id.addWallet);
        // if button is clicked, close the custom dialog
        /*
        Dodawanie portfela do bazy
         */
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /*
    Dodawanie zlecenia stałego do bazy
     */
    public void onAddPosition(View view) {


    }
}