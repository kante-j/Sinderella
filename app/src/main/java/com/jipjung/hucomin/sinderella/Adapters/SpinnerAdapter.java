package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    private ArrayList<String> array_filter;

    private SpinnerAdapter.ViewHolder mViewHolder;


    public SpinnerAdapter(ArrayList<String> array_filter) {
        this.array_filter = array_filter;
    }

    @Override
    public int getCount() {
        return array_filter.size();
    }

    @Override
    public Object getItem(int position) {
        return array_filter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }



    public class ViewHolder {

    }

}