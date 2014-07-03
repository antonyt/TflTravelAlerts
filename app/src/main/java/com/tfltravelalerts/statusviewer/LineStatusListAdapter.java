
package com.tfltravelalerts.statusviewer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.widget.TextView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.common.base.Joiner;
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
        String nameOfLine = row.getResources().getString(line.getNameResId());
        String lineStatusDescription = row.getResources().getString(getLineStatusDescriptionId(lineStatusUpdate));

        int resId = getLineStatusDrawableId(lineStatusUpdate);
        row.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        row.setContentDescription(Joiner.on(": ").join(nameOfLine, lineStatusDescription));

        return row;
    }

    private int getLineStatusDrawableId(LineStatusUpdate lineStatusUpdate) {
        int resId;
        if (lineStatusUpdate != null) {
            resId = R.drawable.line_status_unknown;
            LineStatus lineStatus = lineStatusUpdate.getLineStatus();
            if (lineStatus != null) {
                resId = lineStatus.getStatusIcon();
            }
        } else {
            // if no update yet, show nothing (loading updates?)
            // this may happen when we fail to parse data from the server
            resId = R.drawable.line_status_unknown;
        }
        return resId;
    }

    private int getLineStatusDescriptionId(LineStatusUpdate lineStatusUpdate) {
        int resId;
        if (lineStatusUpdate != null) {
            resId = R.string.line_status_unknown;
            LineStatus lineStatus = lineStatusUpdate.getLineStatus();
            if (lineStatus != null) {
                resId = lineStatus.getStatusResId();
            }
        } else {
            // if no update yet, show nothing (loading updates?)
            // this may happen when we fail to parse data from the server
            resId = R.string.line_status_unknown;
        }
        return resId;
    }
}
