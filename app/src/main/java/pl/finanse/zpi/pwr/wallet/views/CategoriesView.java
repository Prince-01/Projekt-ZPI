package pl.finanse.zpi.pwr.wallet.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.CategoriesAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;

/**
 * Created by rober on 12.04.2016.
 */
public class CategoriesView extends Fragment {
    private ListView categoriesListView;
    private  Category[] categoriesData;

    public CategoriesView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        //przypisawnie liscie adaptera i danych
        categoriesListView = (ListView) view.findViewById(R.id.listViewCategories);
        //  categoriesData = Database.GetCategories(getActivity(),null);//tworzenie aktualnej listy kategorii, na poczatku z glownymi

        //  CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), R.layout.caregories_row, categoriesData);
        //  categoriesListView.setAdapter(adapter);

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
        Category[] catTab = Database.GetCategories(getActivity(),newCategory);//tworzenie aktualnej listy kategorii, na poczatku z glownymi
        if(catTab.length == 0) //gdy kategoria nie ma subkategorii
            if(newCategory !=null) {
               setNewList(Database.GetParentCategory(getActivity(), newCategory));//na wypadek usuwania, ostatniej kategorii
                return;
            }
            else
                return;//wywplywane gdy mamy wyswietlic
        categoriesData = catTab;//tutaj zapamietywanie danych, zeby nie bylo, ze trzyammy sobie gowniana pusta tablice
        //ustawianie gornego napis
        final TextView tv = (TextView) getView().findViewById(R.id.categoryHeadText);
        tv.setText(newCategory == null ? "Kategorie" : newCategory);

        //ustawianie widocznej strzlki, jesli jestesmy nie w glownej kategorii
        final ImageView iv = (ImageView) getView().findViewById(R.id.categoryHeadIcon);
        iv.setVisibility(newCategory == null?View.INVISIBLE:View.VISIBLE);
        final RelativeLayout gora =  (RelativeLayout) getView().findViewById(R.id.catCofnij);
        gora.setOnClickListener(new View.OnClickListener() {
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
        if(newCategory!=null)//dodajemy usuwanie, tylko jak wyswietlamy kategoire ktore nie sa glowne
        categoriesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Category cat = categoriesData[position];
               // Toast.makeText(getActivity(), "Kliknieto dlugo", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setMessage("Czy na pewno chcesz usunąć tą kategorię?");
                b.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database.RemoveCategory(getActivity(), cat);
                        ReloadList();
                    }
                });
                b.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                b.create().show();
                return true;
            }
        });
    }
    public void ReloadList(){
        setNewList(((TextView) getView().findViewById(R.id.categoryHeadText)).getText().toString());
    }
}
