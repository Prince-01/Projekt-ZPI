package pl.finanse.zpi.pwr.wallet.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.adapters.OperationsAdapter;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by Robert on 2016-04-01.
 */
public class HomePage extends Fragment {
    //2ECC71
    private ListView lastOperationsListView;
    //TO DO
    Operation operationsData[];
    TextView totalBalance;
    public final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        totalBalance = (TextView) view.findViewById(R.id.TotalBalance);
        lastOperationsListView = (ListView) view.findViewById(R.id.lastOperationsListView);
        ReloadList();//robi nam liste
        return view;
    }

    /**
     * updatuje nam liste, np po wykasowaniu operacji, oraz sluzy do ladowania samej srony
     */
    public void ReloadList() {
     //   Toast.makeText(getActivity(), "Dziala", Toast.LENGTH_SHORT).show();
        makeData();
        OperationsAdapter adapter = new OperationsAdapter(getActivity(), R.layout.operation_row, operationsData);
        lastOperationsListView.setAdapter(adapter);
        lastOperationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Operation op = operationsData[position];
              //  Toast.makeText(getActivity(), "Kliknieto dlugo", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setMessage("Czy na pewno chcesz usunąć tą operację?");
                b.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database.RemoveOperation(getActivity(), op);
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

        /*
        on click, wyświetla informacje - layout jeszcze nie gotowy
         */
        lastOperationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        UpdateTotalBalance();
    }

    /**
     * prawdopodobnie oblicza stan konta, spytac kamila
     *
     * @param view
     */
    private void UpdateTotalBalance() {
        float walletValue = Wallet.GetActiveWallet(getActivity()).getValue();
        float sum = walletValue;

        for (Operation operation : operationsData)
        sum += operation.isIncome ? operation.cost : -operation.cost;

        totalBalance.setText("Stan konta: " + decimalFormat.format(sum) + " zł");
        totalBalance.setTextColor(sum > 0 ? Color.rgb(46, 204, 113) : sum == 0 ? Color.WHITE : Color.rgb(217, 30, 24));
        //Color.GREEN
    }

    /*
    Tutaj tworzę operacje na sztywno, to ma być wczytywane z bazy danych
     */
    private void makeData() {
        operationsData = Database.GetAllPositions(getActivity().getApplicationContext());
        //Toast.makeText(getActivity(),"Ilosc portfeli "+Database.GetAllWallets(getActivity())[0],Toast.LENGTH_SHORT).show();
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
