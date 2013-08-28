
package com.tfltravelalerts.alerts;

import com.tfltravelalerts.R;
import com.tfltravelalerts.common.SimpleListAdapter;
import com.tfltravelalerts.model.Day;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.Time;

import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;

public class AlertsListAdapter extends SimpleListAdapter<LineStatusAlert> {

    public AlertsListAdapter(Context context) {
        super(context, R.layout.alerts_list_row);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
    @Override
    protected void initializeView(int position, View convertView) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.title = (TextView) convertView.findViewById(R.id.alerts_title);
        viewHolder.lines = (TextView) convertView.findViewById(R.id.alerts_lines);
        viewHolder.days = (TextView) convertView.findViewById(R.id.alerts_days);
        viewHolder.times = (TextView) convertView.findViewById(R.id.alerts_times);
        convertView.setTag(viewHolder);
    }

    @Override
    protected void populateView(int position, View convertView, LineStatusAlert data) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.title.setText(data.getTitle());
        viewHolder.lines.setText(Line.buildString(data.getLines()));
        viewHolder.days.setText(Day.buildShortString(data.getDays()));
        Time time = data.getTime();
        if (time != null) {
            viewHolder.times.setText(time.toString());
        }
    }

    static class ViewHolder {
        TextView title;
        TextView lines;
        TextView days;
        TextView times;
    }

}
