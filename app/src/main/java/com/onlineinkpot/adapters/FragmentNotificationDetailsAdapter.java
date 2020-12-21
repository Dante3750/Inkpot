package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlineinkpot.R;
import com.onlineinkpot.models.NotificationDetailModel;

import java.util.List;

/**
 * Created by USER on 10/5/2017.
 */

public class FragmentNotificationDetailsAdapter extends RecyclerView.Adapter<FragmentNotificationDetailsAdapter.MyViewHolder> {
    private List<NotificationDetailModel> orderHistories;
    public Context context;

    public FragmentNotificationDetailsAdapter(Context context, List<NotificationDetailModel> orderHistories) {
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationDetailModel order = orderHistories.get(position);
        holder.tvSemester.setText(order.getChapter());
        holder.tvExpireDate.setText(order.getUniversity());
        holder.tvSubjectName.setText(order.getSubject());
        holder.tvPrice.setText(order.getDate());
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