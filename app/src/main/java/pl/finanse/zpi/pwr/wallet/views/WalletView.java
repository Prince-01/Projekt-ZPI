package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.WalletAdapter;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

/**
 * Created by rober on 21.04.2016.
 */
public class WalletView extends Fragment {
    private ListView walletListView;
    Wallet[] walletData;

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
        return view;
    }

    private void makeData() {
        walletData = new Wallet[] {
                new Wallet("Podstawowy"),
                new Wallet("Domowy")
        };
    }
}
