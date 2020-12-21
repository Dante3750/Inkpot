package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.onlineinkpot.R;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.helper.PicassoClient;
import com.onlineinkpot.models.Offer;

import java.util.List;

/**
 * Created by john on 9/3/2017.
 */

public class FragmentOfferAdapter extends RecyclerView.Adapter<FragmentOfferAdapter.MyViewHolder> {
    private List<Offer> offerList;
    public Context context;

    public FragmentOfferAdapter(Context context, List<Offer> offerList) {
        this.offerList = offerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_fragment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.offerDes.setText(offer.getCoupon_des());
        PicassoClient.downloadImage(context, Cons.URL_IMAGE + offer.getCoupon_front_img(), holder.offerImage);
        holder.offerCode.setText(" User PromoCodeFragment " + offer.getCoupon_code() + " to Get, " + offer.getCoupon_des());
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView offerImage;
        public TextView offerCode, offerDes;

        public MyViewHolder(View view) {
            super(view);
            offerCode = (TextView) view.findViewById(R.id.tv_offer_code);
            offerDes = (TextView) view.findViewById(R.id.tv_offer_des);
            offerImage = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }
}
