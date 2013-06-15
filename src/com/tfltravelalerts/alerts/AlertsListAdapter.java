
package com.tfltravelalerts.alerts;

import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;
import butterknife.InjectView;
import butterknife.Views;

import com.tfltravelalerts.R;
import com.tfltravelalerts.common.SimpleListAdapter;
import com.tfltravelalerts.model.Day;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.Time;

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
        Views.inject(viewHolder, convertView);
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
        @InjectView(R.id.alerts_title) TextView title;
        @InjectView(R.id.alerts_lines) TextView lines;
        @InjectView(R.id.alerts_days) TextView days;
        @InjectView(R.id.alerts_times) TextView times;
    }

}
