package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.BuyDealsAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.Coupon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 9/14/2017.
 */

public class BuyDealsActivity extends AppCompatActivity implements View.OnClickListener {
    public List<Coupon> couponList;
    private static final String TAG = "BuyDealsActivity";
    private RecyclerView rv;
    public BuyDealsAdapter mAdapter;
    public String subjectid,courseid,semesterid;
    public TextView textview;
    public String totalvalue;
    public Button button;
    public PrefManager pref;
    private ProgressDialog progressDialog;
    public static ArrayList<String> semesteridarr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Offers");
        setContentView(R.layout.buy_deals_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(BuyDealsActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        pref=new PrefManager(this);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyDealsAdapter.allRowTotal=0;
                finish();
            }
        });


       Intent in = getIntent();
    /*    subjectid = in.getStringExtra("subjectId");
        courseid=in.getStringExtra("courseid");
        semesterid=in.getStringExtra("semesterid");*/



        Bundle b = getIntent().getExtras();
        subjectid = b.getString("subjectId");
        courseid = b.getString("courseid");
        //subjectId = b.getString("semesterId");



        semesteridarr = b.getStringArrayList("semesterid");








        textview = (TextView) findViewById(R.id.textvalue);
        button = (Button) findViewById(R.id.continuebtn);
        totalvalue=in.getStringExtra("totalvalue");
        textview.setText(""+totalvalue);
        rv.setLayoutManager(new LinearLayoutManager(this));
        hitApiForOffer();
        button.setOnClickListener(this);
         }




    private void hitApiForOffer() {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest getPrice = new StringRequest(Request.Method.POST, Cons.OFFERS_LIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(" Price Rqst", "= " + response);
                    try {
                        couponList = new ArrayList<>();
                        JSONArray main = new JSONArray(response);
                        for (int i = 0; i < main.length(); i++) {
                            JSONObject object = main.getJSONObject(i);
                            Coupon coupon = new Coupon();
                            coupon.setCouponId(object.getString("coupon_id"));
                            coupon.setCouponName(object.getString("coupon_name"));
                            coupon.setCouponPrice(object.getString("coupon_price"));
                            coupon.setCouponCode(object.getString("coupon_code"));
                            coupon.setCouponExpire(object.getString("coupon_expiration"));
                            coupon.setCouponSdes(object.getString("coupon_sdes"));
                            coupon.setCouponImage(object.getString("coupon_fimg"));
                            couponList.add(coupon);
                        }
                        mAdapter = new BuyDealsAdapter(BuyDealsActivity.this, couponList);
                        rv.setAdapter(mAdapter);
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    Log.d(" deal Params ", "= " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(getPrice, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onClick(View v) {
      if(BuyDealsAdapter.allRowTotal > Float.parseFloat(textview.getText().toString())){
          Toast.makeText(this, "upto maximum value are allowed", Toast.LENGTH_SHORT).show();
      }

     else {
          String totalvalue = textview.getText().toString();
          pref.setTaxanid(totalvalue);
          Intent in = new Intent(getApplicationContext(),BuyPaymentActivity.class);
          in.putExtra("subjectId",subjectid);
          in.putExtra("courseid",courseid);
  //        in.putExtra("semesterid",semesterid);


          in.putExtra("semesterid",semesteridarr);

          startActivity(in);
          overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

      }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BuyDealsAdapter.allRowTotal=0;
        finish();
    }
    }



