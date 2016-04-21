package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.StandingOperationsAdapter;
import pl.finanse.zpi.pwr.wallet.model.StandingOperation;

/**
 * Widok odpowoiedzialny za stale zlecenia.
 * Created by sebastiankotarski on 21.04.16.
 */
public class StandingOperationView extends Fragment {
    ListView standingOperationsListView;
    StandingOperation[]standingOperations;
    public StandingOperationView() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standing_operations, container, false);
        standingOperationsListView = (ListView)view.findViewById(R.id.standingOperations);
        makeData();
        StandingOperationsAdapter adapter = new StandingOperationsAdapter(getActivity(), R.layout.standing_operation_row, standingOperations);
        standingOperationsListView.setAdapter(adapter);
        return view;
    }
    private void makeData() {
        standingOperations = new StandingOperation[]{new StandingOperation(), new StandingOperation()};
    }

}
