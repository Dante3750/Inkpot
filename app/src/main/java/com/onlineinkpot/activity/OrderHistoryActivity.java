package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.FragmentOrderDetailsAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.OrderHistoryModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {
    private static final String TAG = "OrderHistoryActivity";
    public PrefManager pref;
    private RecyclerView rvOrderHis;
    public List<OrderHistoryModel> orderHistories;
    private FragmentOrderDetailsAdapter mAdapter;
    public HashMap<String, String> userdetail;
    public TextView tvPrice;
    private ProgressDialog progressDialog;
    String priceSet;
    public static boolean length = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Order Detail");
        setContentView(R.layout.order_history_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent in = getIntent();
        priceSet = in.getStringExtra("price");
        pref = new PrefManager(this);
        userdetail = pref.getUserDetail();
        initiateLayoutVariables(getIntent());
    }

    private void initiateLayoutVariables(Intent intent) {
        tvPrice = (TextView) findViewById(R.id.tv_price);
        orderHistories = new ArrayList<>();
        rvOrderHis = (RecyclerView) findViewById(R.id.rvList_order);
        mAdapter = new FragmentOrderDetailsAdapter(this, orderHistories);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvOrderHis.setLayoutManager(mLayoutManager);
        rvOrderHis.setAdapter(mAdapter);
        getHistoryData(getIntent());
    }

    private void getHistoryData(final Intent intent) {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_MY_ORDER_DETAILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                Log.d(TAG, "onResponse: " + array.length());
                                if (array.length() > 1) {
                                    length = true;
                                    tvPrice.setVisibility(View.VISIBLE);
                                    tvPrice.setText("Total Price: " + priceSet);
                                } else {
                                    length = false;
                                }
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    OrderHistoryModel orderhistorymodel = new OrderHistoryModel();
                                    orderhistorymodel.setSubject_name(object.getString("subjectname"));
                                    orderhistorymodel.setSemester(object.getString("semester"));
                                    orderhistorymodel.setPrice(object.getString("price"));
                                    orderhistorymodel.setTax(object.getString("tax"));
                                    orderhistorymodel.setDiscount(object.getString("discount"));
                                    orderhistorymodel.setExpiredate(object.getString("expiredate"));
                                    orderHistories.add(orderhistorymodel);
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.d(TAG, "Error : " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("orderid", intent.getStringExtra("orderId"));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseListRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}

