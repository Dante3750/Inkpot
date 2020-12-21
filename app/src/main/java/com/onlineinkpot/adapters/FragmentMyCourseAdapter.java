package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.onlineinkpot.R;
import com.onlineinkpot.models.MyCourse;

import java.util.List;

/**
 * Created by Akshay on 9/6/2017.
 */

public class FragmentMyCourseAdapter extends RecyclerView.Adapter<FragmentMyCourseAdapter.MyViewHolder> {
    private List<MyCourse> courseList;
    public Context context;

    public FragmentMyCourseAdapter(Context context, List<MyCourse> courseList) {
        this.courseList = courseList;
        this.context = context;
    }

    @Override
    public FragmentMyCourseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mycourse_fragment, parent, false);
        return new FragmentMyCourseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FragmentMyCourseAdapter.MyViewHolder holder, int position) {
        MyCourse myCourse = courseList.get(position);
        holder.tvName.setText(myCourse.getMySubject_name());
        holder.tvStatus.setText(myCourse.getMySemester_name());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvStatus;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_subject_name);
            tvStatus = (TextView) view.findViewById(R.id.tv_semester);
        }
    }
}
