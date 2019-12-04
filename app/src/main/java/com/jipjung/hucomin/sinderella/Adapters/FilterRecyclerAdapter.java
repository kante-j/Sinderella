package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jipjung.hucomin.sinderella.R;
//import com.jipjung.hucomin.sinderella.item.Data;

import java.util.ArrayList;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.ViewHolder> {

    private ArrayList<String> arrayfilter; //= new ArrayList<>();
    private Context context;



    public FilterRecyclerAdapter(Context context, ArrayList<String> arrayfilter){
        this.context=context;
        this.arrayfilter= arrayfilter;


    }

    @Override
    public FilterRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.filter_item_feeds,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public  void onBindViewHolder(ViewHolder holder, int position){
        String item = arrayfilter.get(position);

        holder.filter_item.setText(item);

    }

    @Override
    public int getItemCount() {
        return arrayfilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView filter_item;

        public ViewHolder(View itemView) {
            super(itemView);

            filter_item = itemView.findViewById(R.id.filter_item);
        }
    }


//    @NonNull
//    @Override
//    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(context()).inflate(R.layout.filter_item_feeds, parent, true);
//        return new ItemViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//
//        holder.onBind(arrayfilter.get(position));
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayfilter.size();
//    }
//
//    public void addItem(Data data){
//
//        arrayfilter.add(data);
//    }
//
//
//    class ItemViewHolder extends  RecyclerView.ViewHolder{
//
//        private TextView filter_item;
//
//        ItemViewHolder(View itemView) {
//            super(itemView);
//
//            filter_item = itemView.findViewById(R.id.filter_item);
//        }
//
//        void onBind(Data data) {
//            filter_item.setText(data.getFilterItem());
//
//        }
//    }
}