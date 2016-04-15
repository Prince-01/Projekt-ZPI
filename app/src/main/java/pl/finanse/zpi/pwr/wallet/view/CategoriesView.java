package pl.finanse.zpi.pwr.wallet.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.helpers.Database;

/**
 * Created by rober on 12.04.2016.
 */
public class CategoriesView extends Fragment {
    private ListView categoriesListView;
    Category[] categoriesData;

    public CategoriesView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        //przypisawnie liscie adaptera i danych
        categoriesListView = (ListView) view.findViewById(R.id.listViewCategories);
        categoriesData = Database.GetCategories(getActivity(),null);//tworzenie aktualnej listy kategorii, na poczatku z glownymi

        /*
        Tutaj był błąd  android.R.layout.list_content ---> R.layout.caregories_row (To moje! :P)
         */

        CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.layout.categories_row, categoriesData);
        categoriesListView.setAdapter(adapter);
        //tutaj nadpsywanie funkcjonalnosci plusa
        return view;
    }


    /*
    OnLongClickItem
    Usuwa z pytaniem czy usunać (dialog box), uwaga usuwa tez podkategorie
     */

}
