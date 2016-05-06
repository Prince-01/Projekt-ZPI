package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.w3c.dom.Text;
import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.adapters.OperationsAdapter;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by Robert on 2016-04-01.
 */
public class HomePage extends Fragment implements View.OnClickListener {
    //2ECC71
    private ListView lastOperationsListView;
    //TO DO
    Operation operationsData[];
    public final DecimalFormat decimalFormat = new DecimalFormat("#0.00");
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

        /*
        ON LONG CLICK - usuwanie operacji z bazy wraz z pytaniem czy na pewno usunąć
         */
        lastOperationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });

        /*
        on click, wyświetla informacje - layout jeszcze nie gotowy
         */
        //lastOperationsListView.setOnClickListener

        UpdateTotalBalance(view);
        return view;
    }

    private void UpdateTotalBalance(View view) {
        TextView totalBalance = (TextView) view.findViewById(R.id.TotalBalance);
        float walletValue = Wallet.GetActiveWallet(getActivity()).getValue();

        totalBalance.setText("Stan konta: " + decimalFormat.format(walletValue) + " zł");
        totalBalance.setTextColor(walletValue > 0 ? Color.rgb(46,204,113) : walletValue == 0 ? Color.WHITE : Color.rgb(217, 30, 24));
        //Color.GREEN
    }

    /*
    Tutaj tworzę operacje na sztywno, to ma być wczytywane z bazy danych
     */
    private void makeData() {
        operationsData = Database.GetAllPositions(getActivity().getApplicationContext());
        //Toast.makeText(getActivity(),"Ilosc portfeli "+Database.GetAllWallets(getActivity())[0],Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

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
