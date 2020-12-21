package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.onlineinkpot.R;
import com.onlineinkpot.models.ModTopic;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> {

    private List<ModTopic> topList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView topName, topNameUnit;
        public ImageView topIcon;

        public MyViewHolder(View view) {
            super(view);
            topName = (TextView) view.findViewById(R.id.tv_unit_name);
        }
    }

    public TopicAdapter(Context context, List<ModTopic> tList) {
        this.context = context;
        this.topList = tList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_unit, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModTopic topic = topList.get(position);
        holder.topName.setText(topic.gettName());
    }

    @Override
    public int getItemCount() {
        return topList.size();
    }
}

