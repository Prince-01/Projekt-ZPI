package pl.finanse.zpi.pwr.wallet.views;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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
    private Wallet[] wallets;
    private Wallet activeWallet;
    private int activeWalletIndex;
    private Operation operationsData[];
    public static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    private Unbinder unbinder;
    // UI components
    @BindView(R.id.lastOperationsListView)
    ListView lastOperationsListView;
    @BindView(R.id.TotalBalance)
    TextView totalBalance;
    @BindView(R.id.walletNameInHome)
    TextView walletName;
    @BindView(R.id.leftArrow)
    ImageView leftArrow;
    @BindView(R.id.rightArrow)
    ImageView rightArrow;

    // Required empty public constructor
    public HomePage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //long temp = System.currentTimeMillis();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        getActivity().setTitle("Home");

        walletName.setText(Wallet.GetActiveWallet(getActivity()).getName());
        checkWalletIndex();

        ReloadList();

        /*long time = System.currentTimeMillis();
        time = time - temp;
        Toast.makeText(getActivity(), " " + time, Toast.LENGTH_SHORT).show();*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void checkWalletIndex() {
        wallets = Database.GetAllWallets(getActivity());
        activeWallet = Wallet.GetActiveWallet(getActivity());
        for(int i = 0; i < wallets.length ; i++) {
            if(activeWallet.getName().equals(wallets[i].getName())) {
                activeWalletIndex = i;
                break;
            }
        }
    }

    /**
     * updatuje nam liste, np po wykasowaniu operacji, oraz sluzy do ladowania samej srony
     */
    public void ReloadList() {
        makeData();
        UpdateTotalBalance();
        operationsData = Arrays.copyOfRange(operationsData,0,30<operationsData.length?30:operationsData.length);
        OperationsAdapter adapter = new OperationsAdapter(getActivity(), R.layout.operation_row, operationsData);
        lastOperationsListView.setAdapter(adapter);
        lastOperationsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Operation op = operationsData[position];
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
                FragmentManager fragmentManager = getFragmentManager();
                EditOperation eop = null;
                fragmentManager.beginTransaction().replace(R.id.mainContent, eop = new EditOperation()).commit();
                eop.operationToEdit = operationsData[position];
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void UpdateTotalBalance() {
        float walletValue = Wallet.GetActiveWallet(getActivity()).getValue();
        float sum = walletValue;

        for (Operation operation : operationsData)
        sum += operation.isIncome ? operation.cost : -operation.cost;

        totalBalance.setText(decimalFormat.format(sum) + " zł");
        totalBalance.setTextColor(sum > 0 ? Color.rgb(46, 204, 113) : sum == 0 ? Color.WHITE : Color.rgb(217, 30, 24));
    }

    /*
    Tutaj tworzę operacje na sztywno, to ma być wczytywane z bazy danych
     */
    private void makeData() {
        operationsData = Database.GetAllPositions(getActivity());
        //Toast.makeText(getActivity(),"Ilosc portfeli "+Database.GetAllWallets(getActivity())[0],Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rightArrow)
    public void setWalletRight() {
        setActiveWallet(1);
        //Toast.makeText(getActivity(), "W PRAWO", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.leftArrow)
    public void setWalletLeft() {
        setActiveWallet(-1);
        //Toast.makeText(getActivity(), "W LEWO", Toast.LENGTH_SHORT).show();
    }
    /*
    Zmienia aktywny porfetl i wyświetla go na stronie głównej po naciśnięciu strzałki
     */
    private void setActiveWallet(int plus){
        activeWalletIndex = (activeWalletIndex+plus+wallets.length)%wallets.length;
        Wallet.SetActiveWallet(getActivity(),wallets[activeWalletIndex].getName());
        walletName.setText(Wallet.GetActiveWallet(getActivity()).getName());
        ReloadList();
    }
}
