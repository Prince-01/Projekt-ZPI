package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.finanse.zpi.pwr.wallet.R;

/**
 * Created by Robert on 2016-04-01.
 */
public class DefaultPage extends Fragment {
    public DefaultPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.deafult_view, container, false);
    }
}
