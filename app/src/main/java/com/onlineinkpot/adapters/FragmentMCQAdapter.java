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

public class FragmentMCQAdapter extends RecyclerView.Adapter<FragmentMCQAdapter.MyViewHolder> {
    private List<MyCourse> courseList;
    public Context context;

    public FragmentMCQAdapter(Context context, List<MyCourse> courseList) {
        this.courseList = courseList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mcq_fragment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FragmentMCQAdapter.MyViewHolder holder, int position) {
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
            tvName = (TextView) view.findViewById(R.id.tv_mcq_subject_name);
            tvStatus = (TextView) view.findViewById(R.id.tv_sem);
        }
    }
}
