package pl.finanse.zpi.pwr.wallet.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;
import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.StandingOperationsAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.ShoppingList;
import pl.finanse.zpi.pwr.wallet.model.StandingOperation;

/**
 * Widok odpowoiedzialny za stale zlecenia.
 * Created by sebastiankotarski on 21.04.16.
 */
public class StandingOperationView extends Fragment {
    @BindView(R.id.standingOperations)
    ListView standingOperationsListView;
    ArrayList<StandingOperation> standingOperations;

    private Unbinder unbinder;


    public StandingOperationView() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standing_operations, container, false);
        unbinder = ButterKnife.bind(this, view);
        makeData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void makeData() {
        standingOperations = new ArrayList<>(Arrays.asList(Database.GetAllStandingOperations(getActivity())));
        StandingOperationsAdapter adapter = new StandingOperationsAdapter(getActivity(), R.layout.standing_operation_row, standingOperations);
        standingOperationsListView.setAdapter(adapter);
    }

//    @OnItemClick(R.id.standingOperations)
//    public void navigateToStandingOperation(int pos) {
//        ShoppingListView fragment = new ShoppingListView();
//        fragment.selectedShoppingList = data.get(pos);
//        FragmentManager fragmentManager = getActivity().getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.mainContent, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//        reloadData();
//    }

    @OnItemLongClick(R.id.standingOperations)
    public boolean removeItem(int pos) {
        final StandingOperation op = standingOperations.get(pos);
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setMessage("Czy na pewno chcesz usunąć?");
        b.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //todo remove standing operation
                Database.RemoveStandingOperation(getActivity(), op);
                // standingOperations.remove(op);
                makeData();
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

}
