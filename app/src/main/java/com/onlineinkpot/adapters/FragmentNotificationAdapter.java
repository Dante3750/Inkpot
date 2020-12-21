package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlineinkpot.R;
import com.onlineinkpot.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 9/27/2017.
 */

public class FragmentNotificationAdapter extends RecyclerView.Adapter<FragmentNotificationAdapter.MyViewHolder> {
    public ImageView imageView;
    private List<Course> courseList;
    public Context context;
    public String item;
    public ArrayList<String> header, topic, unit, subject, course, date, flag;
    public int j = 0, k = 0;

    public FragmentNotificationAdapter(
            Context context2,
            ArrayList<String> header,
            ArrayList<String> flag
    ) {
        this.context = context2;
        this.header = header;
        this.flag = flag;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notificationtext;

        public MyViewHolder(View view) {
            super(view);
            notificationtext = (TextView) view.findViewById(R.id.tv_notification_text);

        }
    }

    public FragmentNotificationAdapter(Context context, List courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public FragmentNotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification_adapter, parent, false);
        return new FragmentNotificationAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(FragmentNotificationAdapter.MyViewHolder holder, int position) {
        holder.notificationtext.setText(header.get(position));
    }


    @Override
    public int getItemCount() {
        return header.size();

    }

}

