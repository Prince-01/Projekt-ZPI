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

import pl.finanse.zpi.pwr.wallet.R;

/**
 * Created by rober on 12.04.2016.
 */
public class CategoriesView extends Fragment {
    private ListView categoriesListView;
    Category categoriesData[];

    public CategoriesView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        //przypisawnie liscie adaptera i danych
        categoriesListView = (ListView) view.findViewById(R.id.listViewCategories);
        makeData();
        CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.layout.caregories_row, categoriesData);
        categoriesListView.setAdapter(adapter);


        return view;
    }

    /*
    Tutaj tworzę kateogie na sztywno, to ma być wczytywane z bazy danych
     */
    private void makeData() {
        categoriesData = new Category[] {
              new Category("Spożywka", R.drawable.ic_cake_24dp),
              new Category("Samochód", R.drawable.ic_cake_24dp),
              new Category("Inne", R.drawable.ic_menu_send),
              new Category("nazwa1", R.drawable.ic_menu_send),
              new Category("nazwa2", R.drawable.ic_cake_24dp),
              new Category("nazwa3", R.drawable.ic_cake_24dp),
              new Category("nazwa4", R.drawable.ic_cake_24dp),
              new Category("nazwa5", R.drawable.ic_cake_24dp),
              new Category("nazwa6", R.drawable.ic_cake_24dp),
              new Category("nazwa7", R.drawable.ic_cake_24dp),
              new Category("nazwa8", R.drawable.ic_cake_24dp)
        };
    }

    /*
    OnLongClickItem
    Usuwa z pytaniem czy usunać (dialog box), uwaga usuwa tez podkategorie
     */

}
