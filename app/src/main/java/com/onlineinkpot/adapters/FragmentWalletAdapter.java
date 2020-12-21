package com.onlineinkpot.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlineinkpot.R;
import com.onlineinkpot.models.WalletHistory;

import java.util.List;

/**
 * Created by Akshay on 9/25/2017.
 */

public class FragmentWalletAdapter extends RecyclerView.Adapter<FragmentWalletAdapter.ViewHolder> {
    private List<WalletHistory> topic;
    private Context mContext;
    public Typeface face;

    public FragmentWalletAdapter(Context mContext, List<WalletHistory> topic) {
        this.topic = topic;
        this.mContext = mContext;
        this.face = Typeface.createFromAsset(mContext.getAssets(), "fonts/Chalkduster.ttf");
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vh = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fragment_wallet, parent, false);
        ViewHolder ViewHolder = new ViewHolder(vh);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            String cbtype = topic.get(position).getCbType();
            if (cbtype.equals("Dr")) {
                holder.paymentOrderTv.setText("Paid For Order");
                holder.iconIv.setBackgroundResource(R.drawable.paid_wallet);
            } else if (cbtype.equals("Cr")) {
                holder.paymentOrderTv.setText("Payment Cash Received");
                holder.iconIv.setBackgroundResource(R.drawable.received_wallet);
            }
            holder.dateTv.setText(topic.get(position).getCreatedon());
            if (topic.get(position).getCbAmt().isEmpty()) {
                holder.amountTv.setText("\u20b9 " + "0.00");

            } else {
                holder.amountTv.setText("\u20b9 " + topic.get(position).getCbAmt());
                holder.orderNOTv.setText(topic.get(position).getOrderNumber());
                holder.paymentOrderTv.setTypeface(face);
                holder.dateTv.setTypeface(face);
                holder.amountTv.setTypeface(face);
                holder.orderNOTv.setTypeface(face);
            }
        }
    }

    @Override
    public int getItemCount() {
        return topic.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView paymentOrderTv, amountTv, dateTv, orderNOTv;
        private ImageView iconIv;

        public ViewHolder(View itemView) {
            super(itemView);
            paymentOrderTv = (TextView) itemView.findViewById(R.id.custom_payment_received_tv);
            dateTv = (TextView) itemView.findViewById(R.id.custom_payment_date_tv);
            amountTv = (TextView) itemView.findViewById(R.id.custom_payment_amount_tv);
            orderNOTv = (TextView) itemView.findViewById(R.id.custom_payment_from_tv);
            iconIv = (ImageView) itemView.findViewById(R.id.custom_payment_wallet_icon_iv);
        }
    }
}