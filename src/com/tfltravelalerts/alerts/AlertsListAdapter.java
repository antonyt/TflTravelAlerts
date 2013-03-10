
package com.tfltravelalerts.alerts;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tfltravelalerts.R;
import com.tfltravelalerts.model.Day;
import com.tfltravelalerts.model.Line;
import com.tfltravelalerts.model.LineStatusAlert;
import com.tfltravelalerts.model.Time;

public class AlertsListAdapter extends BaseAdapter {

    private List<LineStatusAlert> mAlerts = new ArrayList<LineStatusAlert>();

    private LayoutInflater mLayoutInflater;

    public AlertsListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateAlerts(List<LineStatusAlert> alerts) {
        mAlerts.clear();
        mAlerts.addAll(alerts);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAlerts.size();
    }

    @Override
    public LineStatusAlert getItem(int position) {
        return mAlerts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.alerts_list_row, parent, false);
            convertView.setTag(viewHolder);
            
            viewHolder.title = (TextView) convertView.findViewById(R.id.alerts_title);
            viewHolder.lines = (TextView) convertView.findViewById(R.id.alerts_lines);
            viewHolder.days = (TextView) convertView.findViewById(R.id.alerts_days);
            viewHolder.times = (TextView) convertView.findViewById(R.id.alerts_times);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        LineStatusAlert alert = getItem(position);
        
        viewHolder.title.setText(alert.getTitle());
        viewHolder.lines.setText(Line.buildString(alert.getLines()));
        viewHolder.days.setText(Day.buildShortString(alert.getDays()));
        viewHolder.times.setText(Time.buildString(alert.getTimes()));
        
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView lines;
        TextView days;
        TextView times;
    }
    
}
