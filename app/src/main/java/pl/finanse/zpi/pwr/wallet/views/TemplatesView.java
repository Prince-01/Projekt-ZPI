package pl.finanse.zpi.pwr.wallet.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.DecimalFormat;

import pl.finanse.zpi.pwr.wallet.R;
import pl.finanse.zpi.pwr.wallet.adapters.TemplateAdapter;
import pl.finanse.zpi.pwr.wallet.model.Template;

/**
 * Created by sebastiankotarski on 05.05.16.
 */
public class TemplatesView extends Fragment {
    private ListView templatesListView;
    //TO DO
    Template templatesData[] = new Template[2];
    public final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_templates, container, false);
        templatesListView = (ListView)view.findViewById(R.id.templatesListView);
        makeData();
        TemplateAdapter adapter = new TemplateAdapter(getActivity(), R.layout.template_row, templatesData);
        templatesListView.setAdapter(adapter);
        return view;
    }
    /*
    Wypełniamy listę dla wybranej daty operacje
     */
    private void makeData() {
//        templatesData = Database.GetAllPositions(getActivity().getApplicationContext());
    }
}
