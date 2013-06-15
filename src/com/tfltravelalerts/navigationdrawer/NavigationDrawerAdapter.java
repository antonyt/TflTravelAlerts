package com.tfltravelalerts.navigationdrawer;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.tfltravelalerts.R;

public class NavigationDrawerAdapter extends ArrayAdapter<AppScreen> {

    public NavigationDrawerAdapter(Context context) {
        super(context, R.layout.navdrawer_row, AppScreenUtil.getInitialScreenArray(context));
    }
}
