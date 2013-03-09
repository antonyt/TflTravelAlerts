
package com.tfltravelalerts.alerts;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class LineStatusAlertsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = new View(getActivity());
        root.setBackgroundColor(Color.RED);
        return root;
    }

}
