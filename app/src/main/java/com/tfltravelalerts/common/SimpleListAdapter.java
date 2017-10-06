
package com.tfltravelalerts.common;

import java.util.List;

import android.view.LayoutInflater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SimpleListAdapter<T> extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResId;

    private List<T> mData;

    public SimpleListAdapter(Context context, int layoutResId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mLayoutResId = layoutResId;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mLayoutResId, parent, false);
            initializeView(position, convertView);
        }
        T data = getItem(position);
        populateView(position, convertView, data);

        return convertView;
    }

    protected void initializeView(int position, View convertView) {
    }

    protected void populateView(int position, View convertView, T data) {
    }

}
