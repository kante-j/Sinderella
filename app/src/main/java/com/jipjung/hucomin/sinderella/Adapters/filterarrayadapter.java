package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jipjung.hucomin.sinderella.R;


import java.util.ArrayList;


public class filterarrayadapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> array_filter;

    private ViewHolder mViewHolder;



    public filterarrayadapter(Context mContext, ArrayList<String> array_filter){
        this.mContext = mContext;
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

        if(convertView == null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.filter_item_feeds,parent,false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }
        mViewHolder.filter_item.setText(array_filter.get(position));

        return convertView;
    }

    private class ViewHolder {
        private TextView filter_item;

        public ViewHolder(View convertView) {
            filter_item = (TextView) convertView.findViewById(R.id.filter_item);
        }
    }
}
