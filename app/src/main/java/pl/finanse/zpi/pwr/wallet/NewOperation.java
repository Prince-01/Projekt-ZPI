package pl.finanse.zpi.pwr.wallet;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by sebastiankotarski on 07.04.16.
 */
public class NewOperation extends Fragment {
    public NewOperation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getView();
        TextView text = (TextView) getActivity().findViewById(R.id.newOperationLabel);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.otf");
        text.setTypeface(tf);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_operation, container, false);
    }
}