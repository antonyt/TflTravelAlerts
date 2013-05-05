
package com.tfltravelalerts.debug;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.TextView;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ExceptionViewerFragment extends Fragment {

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTextView = new TextView(getActivity());
        mTextView.setHorizontallyScrolling(true);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mTextView.setTextSize(12f);
        showExceptions();
        return mTextView;
    }
    
    private void showExceptions() {
        String exceptions = ExceptionViewerUtils.getExceptions();
        if(exceptions != null && exceptions.length() > 0) {
            mTextView.setText(exceptions);
        } else {
            mTextView.setText("No Crashes. Hurray!");
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setHorizontallyScrolling(false);
        }
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem clearLog = menu.add("Clear log");
        clearLog.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        MenuItem emailLog = menu.add("Email log");
        emailLog.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Clear log")) {
            ExceptionViewerUtils.clearLog();
            showExceptions();
            return true;
        } else if(item.getTitle().equals("Email log")) {
            ExceptionViewerUtils.sendEmail(getActivity());
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

}
