package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.onlineinkpot.R;
import com.onlineinkpot.activity.OrderHistoryActivity;
import com.onlineinkpot.models.OrderHistoryModel;

import java.util.List;

/**
 * Created by Akshay on 9/6/2017.
 */

public class FragmentOrderDetailsAdapter extends RecyclerView.Adapter<FragmentOrderDetailsAdapter.MyViewHolder> {
    private List<OrderHistoryModel> orderHistories;
    public Context context;

    public FragmentOrderDetailsAdapter(Context context, List<OrderHistoryModel> orderHistories) {
        this.orderHistories = orderHistories;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FragmentOrderDetailsAdapter.MyViewHolder holder, int position) {
        OrderHistoryModel order = orderHistories.get(position);
        holder.tvSemester.setText(order.getSemester());
        holder.tvExpireDate.setText(order.getExpiredate());
        holder.tvSubjectName.setText(order.getSubject_name());
        holder.tvPrice.setText(" \u20B9 " + order.getPrice());
        if (OrderHistoryActivity.length == true) {
            holder.tvExpireDate.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvOrder_expire.setVisibility(View.GONE);
        } else if (OrderHistoryActivity.length == false) {
            holder.tvExpireDate.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvOrder_expire.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSemester, tvExpireDate, tvSubjectName, tvPrice, tvOrder_expire;

        public MyViewHolder(View view) {
            super(view);
            tvSemester = (TextView) view.findViewById(R.id.tv_semester_name);
            tvExpireDate = (TextView) view.findViewById(R.id.tv_order_expire_date);
            tvSubjectName = (TextView) view.findViewById(R.id.tv_order_subject_name);
            tvPrice = (TextView) view.findViewById(R.id.tv_order_total_price);
            tvOrder_expire = (TextView) view.findViewById(R.id.tv_order_expire);
        }
    }
}