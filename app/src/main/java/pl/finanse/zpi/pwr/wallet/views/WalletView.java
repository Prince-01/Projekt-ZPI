package pl.finanse.zpi.pwr.wallet.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.WalletAdapter;
import pl.finanse.zpi.pwr.wallet.helpers.Database;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by rober on 21.04.2016.
 */
public class WalletView extends Fragment {
    private ListView walletListView;
    Wallet[] walletData;
    int p;

    public WalletView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        walletListView = (ListView) view.findViewById(R.id.listViewWallets);

        makeData();
        WalletAdapter adapter = new WalletAdapter(getActivity(), R.layout.wallet_row, walletData);
        walletListView.setAdapter(adapter);

        registerForContextMenu(walletListView);

        walletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Wallet wallet = walletData[position];
                Wallet.SetActiveWallet(getActivity(), wallet.getName());
                WalletAdapter adapter = new WalletAdapter(getActivity(), R.layout.wallet_row, walletData);
                walletListView.setAdapter(adapter);
            }
        });

        walletListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Wallet wallet = walletData[position];
                if(wallet.getName().equals(Wallet.GetActiveWallet(getActivity()).getName()) || walletData.length < 2 || Database.GetNumberOfPositionsForWallet(getActivity(), wallet) != 0) {
                    Toast.makeText(getActivity(), "Zeby usunac portfel musi byc on pusty, musi byc przynajmniej 2 portfele i nie moze byc on portfelem aktywnym", Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Toast.makeText(getActivity(), "Kliknieto dlugo", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setMessage("Czy na pewno chcesz usunąć ten portfel?");
                b.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database.RemoveWallet(getActivity(), wallet);
                        makeData();
                        WalletAdapter adapter = new WalletAdapter(getActivity(), R.layout.wallet_row, walletData);
                        walletListView.setAdapter(adapter);
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

        return view;
    }
    private void makeData() {
        walletData = Database.GetAllWallets(getActivity());
    }
}
