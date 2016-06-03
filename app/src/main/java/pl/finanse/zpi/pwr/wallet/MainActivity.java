package pl.finanse.zpi.pwr.wallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.FabState;
import pl.finanse.zpi.pwr.wallet.model.ShoppingList;
import pl.finanse.zpi.pwr.wallet.model.Wallet;
import pl.finanse.zpi.pwr.wallet.views.CategoriesView;
import pl.finanse.zpi.pwr.wallet.views.DefaultPage;
import pl.finanse.zpi.pwr.wallet.views.HistoryView;
import pl.finanse.zpi.pwr.wallet.views.HomePage;
import pl.finanse.zpi.pwr.wallet.views.NewOperation;
import pl.finanse.zpi.pwr.wallet.views.NewStandingOperation;
import pl.finanse.zpi.pwr.wallet.views.NewTemplate;
import pl.finanse.zpi.pwr.wallet.views.RaportPage;
import pl.finanse.zpi.pwr.wallet.views.ShoppingListView;
import pl.finanse.zpi.pwr.wallet.views.ShoppingListsView;
import pl.finanse.zpi.pwr.wallet.views.StandingOperationView;
import pl.finanse.zpi.pwr.wallet.views.TemplatesView;
import pl.finanse.zpi.pwr.wallet.views.WalletView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    SearchView mSearchView;
    FloatingActionButton fab;
    public static FabState fabState = FabState.NEW_OPERATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        System.out.println("APPLICATION RUNNING");
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Montserrat-Regular.otf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Montserrat-Regular.otf");
        FontsOverride.setDefaultFont(this, "MONTSERRAT", "fonts/Montserrat-Regular.otf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Montserrat-Regular.otf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Montserrat-Regular.otf");*/

        if(Wallet.GetActiveWallet(this) == null)
            Wallet.SetActiveWallet(this, Database.GetAllWallets(this)[0].getName());

        Database.CheckIfStandingOperationsUpdateIsNeededAndUpdate(this);

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
            Fragment f = this.getFragmentManager().findFragmentById(R.id.mainContent);
            if(!(f instanceof HomePage)) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContent, new HomePage()).commit();
                fabState = FabState.NEW_OPERATION;
                toolbar.setTitle("Home");
            } else {
                super.onBackPressed();
            }
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
            case R.id.nav_template:
                toolbar.setTitle("Szablony");
                fabState = FabState.NEW_TEMPLATE;
                fragment = new TemplatesView();
                break;
            case R.id.nav_history:
                toolbar.setTitle("Historia");
                fragment = new HistoryView();
                break;
            case R.id.nav_shop_list:
                toolbar.setTitle("Listy zakupów");
                fabState = FabState.NEW_SHOPPING_LIST;
                fragment = new ShoppingListsView();
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
            case NEW_TEMPLATE:
                addNewTemplate();
                break;
            case NEW_SHOPPING_LIST:
                addNewShoppingList();
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

    public void addNewTemplate() {
        toolbar.setTitle(R.string.new_template);
        Fragment fragment = new NewTemplate();
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
        Object[] catTemp = Category.getAllFormattedCategoriesWithDepth(this).toArray();
        String[] cats = new String[catTemp.length];
        for(int i=0;i<cats.length;i++)
            cats[i] = (String) catTemp[i];
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                cats );
        parentCategorySpinner.setAdapter(categoryArrayAdapter);
        //ustawianie zeby bylo domyslnie na inne
        for(int i=0;i<cats.length;i++)
            if(cats[i].equals("Inne")){
                parentCategorySpinner.setSelection(i);
                break;
            }

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
               String nazwa = ((EditText)dialog.findViewById(R.id.newCategoryName)).getText().toString();
                String superCat = ((Spinner)dialog.findViewById(R.id.parentCategory)).getSelectedItem().toString().trim();
               Category cat = new Category(nazwa,0,superCat);
                Database.AddNewCategory(v.getContext(), cat);
                dialog.dismiss();
               // Toast.makeText(v.getContext(), getFragmentManager().findFragmentById(R.id.mainContent).toString(), Toast.LENGTH_SHORT).show();
                ((CategoriesView)getFragmentManager().findFragmentById(R.id.mainContent)).setNewList(null);
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
        final TextView walletName = (TextView) dialog.findViewById(R.id.newWalletName);
        final Spinner currencySpinner = (Spinner)dialog.findViewById(R.id.currency_spinner);
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
                Wallet w = new Wallet(walletName.getText().toString(), 0, currencySpinner.getSelectedItem().toString());
                Database.AddNewWallet(v.getContext(), w);
                dialog.dismiss();
                Fragment fragment = new WalletView();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
            }
        });
        dialog.show();
    }

    public void addNewShoppingList() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Nowa lista zakupów");
        alertDialog.setMessage("Podaj tytuł nowej listy zakupów");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_add_shopping_cart_24dp);

        alertDialog.setPositiveButton("Dodaj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO ADD TO DATABASE
                        String newShoppingListTitle = input.getText().toString();
                        ShoppingListView fragment = new ShoppingListView();
                        fragment.selectedShoppingList = new ShoppingList(null, newShoppingListTitle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainContent, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        //SHOW SHOPPING LIST
                    }
                });

        alertDialog.setNegativeButton("Cofnij",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    /*
    Dodawanie zlecenia stałego do bazy
     */
    public void onAddPosition(View view) {


    }

    static final class FontsOverride {

        public static void setDefaultFont(Context context,
                                          String staticTypefaceFieldName, String fontAssetName) {
            final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                    fontAssetName);
            replaceFont(staticTypefaceFieldName, regular);
        }

        protected static void replaceFont(String staticTypefaceFieldName,
                                          final Typeface newTypeface) {
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
