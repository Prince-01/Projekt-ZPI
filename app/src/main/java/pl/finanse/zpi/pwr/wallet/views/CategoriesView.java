package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.CategoriesAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;

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
//        categoriesData = Database.GetCategories(getActivity(),null);//tworzenie aktualnej listy kategorii, na poczatku z glownymi
//
//        /*
//        Tutaj był błąd  android.R.layout.list_content ---> R.layout.caregories_row (To moje! :P)
//         */
//
//        CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.layout.caregories_row, categoriesData);
//        categoriesListView.setAdapter(adapter);
        //tutaj nadpsywanie funkcjonalnosci plusa
        return view;
    }
public void onStart(){
    super.onStart();
    setNewList(null);//w tej metodzie nadajemy listenery dla wszystkich clickow!!!!
}


    /*
    OnLongClickItem
    Usuwa z pytaniem czy usunać (dialog box), uwaga usuwa tez podkategorie
     */

    public void setNewList(final String newCategory){
        Category[] categoriesData = Database.GetCategories(getActivity(),newCategory);//tworzenie aktualnej listy kategorii, na poczatku z glownymi
        if(categoriesData.length == 0) //gdy kategoria nie ma subkategorii
            return;
        //ustawianie gornego napis
        final TextView tv = (TextView) getView().findViewById(R.id.categoryHeadText);
        tv.setText(newCategory == null ? "Kategorie" : newCategory);
        //TODO setonclick wracajace do nadkategorii
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewList(Database.GetParentCategory(getActivity(), tv.getText().toString()));
            }
        });
        //koniec ustawiania napisu
        CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.layout.categories_row, categoriesData);
        categoriesListView.setAdapter(adapter);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catName = ((CategoriesAdapter.RowBeanHolder) view.getTag()).txtTitle.getText().toString();
                setNewList(catName);
               // Toast.makeText(getActivity(),((CategoriesAdapter.RowBeanHolder)view.getTag()).txtTitle.getText(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
