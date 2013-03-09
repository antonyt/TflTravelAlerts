
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.line_status_list_row, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);

            viewHolder.title = (TextView) convertView.findViewById(R.id.line_name);
            viewHolder.description = (TextView) convertView
                    .findViewById(R.id.line_status_description);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Line line = getItem(position);
        LineStatusUpdate lineStatusUpdate = getLineStatusUpdate(position);

        viewHolder.title.setText(line.getNameResId());
        convertView.setBackgroundResource(line.getColorResId());

        if (lineStatusUpdate != null) {
            viewHolder.description.setText(lineStatusUpdate.getLineStatus().getStatusResId());
        } else {
            viewHolder.description.setText("");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView description;
    }

}
