package com.tfltravelalerts.navigationdrawer;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.tfltravelalerts.R;
import com.tfltravelalerts.navigationdrawer.AppScreen.Screen;

public class NavigationDrawerAdapter extends ArrayAdapter<AppScreen> {

    public NavigationDrawerAdapter(Context context) {
        super(context, R.layout.navdrawer_row, AppScreenUtil.getInitialScreenArray(context));
    }
    
    public int getScreenPosition(Screen screen) {
        for (int i = 0; i < getCount() ; i++) {
            if (getItem(i).screen == screen) {
                return i;
            }
        }
        return -1;
    }
}
