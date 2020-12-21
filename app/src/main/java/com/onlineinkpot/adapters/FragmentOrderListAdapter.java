package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.onlineinkpot.R;
import com.onlineinkpot.models.Order;

import java.util.List;

/**
 * Created by USER on 9/3/2017.
 */

public class FragmentOrderListAdapter extends RecyclerView.Adapter<FragmentOrderListAdapter.MyViewHolder> {
    private List<Order> orderList;
    public Context context;

    public FragmentOrderListAdapter(Context context, List<Order> orderList) {
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvName.setText(order.getCourse_name());
        holder.tvStatus.setText(order.getPayment_status());
        holder.tvAmount.setText(" \u20B9 " + order.getOrder_price());
        holder.tvDate.setText(order.getPurchase_date());
        holder.tvNo.setText(" Order No: " + order.getOrder_number());
        holder.tvPayment_mode.setText(order.getPayment_mode());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvStatus, tvDate, tvNo, tvAmount, tvPayment_mode;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_orderCourse_name);
            tvStatus = (TextView) view.findViewById(R.id.tv_order_status);
            tvAmount = (TextView) view.findViewById(R.id.tv_order_amount);
            tvDate = (TextView) view.findViewById(R.id.tv_order_date);
            tvNo = (TextView) view.findViewById(R.id.tv_order_no);
            tvPayment_mode = (TextView) view.findViewById(R.id.tv_payment_mode);
        }
    }
}
