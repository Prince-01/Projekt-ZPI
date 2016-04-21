package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.adapters.OperationsAdapter;

/**
 * Created by Robert on 2016-04-01.
 */
public class HomePage extends Fragment {

    private ListView lastOperationsListView;
    //TO DO
    Operation operationsData[];
    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lastOperationsListView = (ListView)view.findViewById(R.id.lastOperationsListView);
        TextView textView = (TextView)view.findViewById(R.id.operationCost);
        makeData();
        OperationsAdapter adapter = new OperationsAdapter(getActivity(), R.layout.operation_row, operationsData);
        lastOperationsListView.setAdapter(adapter);
        return view;
    }

    /*
    Tutaj tworzę kateogie na sztywno, to ma być wczytywane z bazy danych
     */
    private void makeData() {
        operationsData = Database.GetAllPositions(getActivity().getApplicationContext());
    }


    /*
    *Do ustawienia wartości stanu konta
    *
     */
//    public void setAccountBalance(Money money) {
//        TextView operationCost = ((TextView) getView().findViewById(R.id.operationCost));
//        operationCost.setText(money.toString());
//        operationCost.setTextColor(money < 0 ? :);
//    }
}
