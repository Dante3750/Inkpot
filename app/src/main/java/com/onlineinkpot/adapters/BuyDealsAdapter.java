package com.onlineinkpot.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.onlineinkpot.R;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.models.Coupon;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by USER on 9/14/2017.
 */

public class BuyDealsAdapter extends RecyclerView.Adapter<BuyDealsAdapter.MyViewHolder> {
    private static final String TAG = "BuyDealsAdapter";
    public ImageView imageview;
    public SparseBooleanArray mCheckStates;
    private List<Coupon> couponList;
    public Context context;
    public String arr[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    public ArrayAdapter<String> arrayadapter;
    public String item, spinnervalue, totalprice;
    public static int allRowTotal = 0;
    public View itemView;
    public static String wherenew;
    public static List<String> where = new ArrayList<String>();
    public ArrayList<String> names = new ArrayList<String>();
    public int allRowTotalNews;
    int allRowTotalNew = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {
        TextView tvCouponName, tvcouponid, tvCouponSdes, tvpricetext, totalpricetextvalue, spinnertextvaluesecond;
        private CheckBox checkBox;
        Spinner spinner;

        public MyViewHolder(View view) {
            super(view);
            tvCouponName = (TextView) view.findViewById(R.id.text1);
            tvCouponSdes = (TextView) view.findViewById(R.id.text2);
            spinner = (Spinner) view.findViewById(R.id.spinner);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            imageview = (ImageView) view.findViewById(R.id.imageview);
            tvpricetext = (TextView) view.findViewById(R.id.pricetextvalue);
            totalpricetextvalue = (TextView) view.findViewById(R.id.totalpricetextvaluesecond);
            spinnertextvaluesecond = (TextView) view.findViewById(R.id.spinnertextvalue);
            tvcouponid = (TextView) view.findViewById(R.id.couponid);
            arrayadapter = new ArrayAdapter<String>(context, R.layout.spinner_one, R.id.text1, arr);
            spinner.setAdapter(arrayadapter);
            spinner.setOnItemSelectedListener(this);

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            item = parent.getItemAtPosition(position).toString();
            String price = tvpricetext.getText().toString();
            int itemnew = Integer.parseInt(item);
            int pricenew = Integer.parseInt(price);
            int pricenews = pricenew * itemnew;
            totalprice = String.valueOf(pricenews);
            totalpricetextvalue.setText("" + totalprice);
            spinnertextvaluesecond.setText("" + item);
            Log.d(TAG, "onItemSelected: " + totalprice);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    public BuyDealsAdapter(Context context, List<Coupon> couponList) {
        this.context = context;
        this.couponList = couponList;
        mCheckStates = new SparseBooleanArray(couponList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_buy_deals, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Coupon couponnew = couponList.get(position);
        holder.tvCouponName.setText(couponnew.getCouponName());
        holder.tvCouponSdes.setText(couponnew.getCouponSdes());
        holder.tvpricetext.setText(couponnew.getCouponPrice());
        holder.tvcouponid.setText(couponnew.getCouponId());
        holder.checkBox.setChecked(couponnew.isSelected());
        holder.checkBox.setTag(couponList.get(position));
        JSONArray arrForA = new JSONArray();
        JSONObject list1 = new JSONObject();
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                JSONArray arrForA = new JSONArray();
                JSONObject list1 = new JSONObject();
                if (holder.checkBox.isChecked()) {
                    Coupon contact = (Coupon) holder.checkBox.getTag();
                    contact.setSelected(holder.checkBox.isChecked());
                    String couponid = holder.tvcouponid.getText().toString();
                    spinnervalue = holder.spinnertextvaluesecond.getText().toString();
                    allRowTotal = allRowTotal + Integer.parseInt(holder.totalpricetextvalue.getText().toString());
                    try {
                        list1.put("couponid", couponid);
                        list1.put("quantity", spinnervalue);
                        where.add(String.valueOf(arrForA.put(list1)));
                        wherenew = where.toString();
                        Log.d(TAG, "onClickAd: " + wherenew);
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    holder.spinner.getSelectedView().setEnabled(false);
                    holder.spinner.setEnabled(false);
                } else {
                    where.remove(0);
                    if (allRowTotalNew == 0) {
                        allRowTotalNew = allRowTotal;
                        allRowTotalNews = allRowTotalNew - Integer.parseInt(holder.totalpricetextvalue.getText().toString());
                        allRowTotal = allRowTotalNews;
                    } else {

                        allRowTotal = allRowTotal - Integer.parseInt(holder.totalpricetextvalue.getText().toString());
                    }

                    holder.spinner.getSelectedView().setEnabled(true);
                    holder.spinner.setEnabled(true);
                    where.remove(String.valueOf(arrForA.put(list1)));
                    wherenew = where.toString();
                    couponnew.setSelected(false);
                    Log.d(TAG, "onClickRv: " + wherenew);
                }
            }
        });

        String image = couponnew.getCouponImage();
        String imageurl = Cons.URL_IMAGE + image;
        Picasso.with(context).load(imageurl).into(imageview);
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }
}