
package com.tfltravelalerts.statusviewer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tfltravelalerts.R;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatus;
import com.tfltravelalerts.model.LineStatusUpdate;

/**
 * List adapter to display lines and their statuses.
 */
public class LineStatusListAdapter extends BaseAdapter {

    private List<Line> mLines;
    private Map<Line, LineStatusUpdate> mUpdates = new HashMap<Line, LineStatusUpdate>();
    private LayoutInflater mInflater;

    public LineStatusListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mLines = Arrays.asList(Line.values());
    }

    public void updateLineStatus(List<LineStatusUpdate> updates) {
        mUpdates.clear();
        for (LineStatusUpdate update : updates) {
            mUpdates.put(update.getLine(), update);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLines.size();
    }

    @Override
    public Line getItem(int position) {
        return mLines.get(position);
    }

    public LineStatusUpdate getLineStatusUpdate(int position) {
        Line line = getItem(position);
        return getLineStatusUpdate(line);
    }

    public LineStatusUpdate getLineStatusUpdate(Line line) {
        return mUpdates.get(line);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView row;
        if (convertView == null) {
            row = (TextView) mInflater.inflate(R.layout.line_status_list_row, parent, false);
        } else {
            row = (TextView) convertView;
            // clear previous state
            row.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        Line line = getItem(position);
        LineStatusUpdate lineStatusUpdate = getLineStatusUpdate(position);

        row.setText(line.getNameResId());
        row.setBackgroundResource(line.getColorResId());

        if (lineStatusUpdate != null) {
            int resId = R.drawable.line_status_unknown;
            LineStatus lineStatus = lineStatusUpdate.getLineStatus();
            if (lineStatus != null) {
                resId = lineStatus.getStatusIcon();
            }
            row.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        } else {
            // if no update yet, show nothing (loading updates?)
            // this may happen when we fail to parse data from the server
            int resId = R.drawable.line_status_unknown;
            row.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        }

        return row;
    }
}
