package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.BuyDealsAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 9/19/2017.
 */

public class BuyOrderSuccessActivity extends AppCompatActivity {
    private static final String TAG = "BuyOrderSuccessActivity";
    private ProgressDialog mProgressDialog;
    private String order_id;
    private TextView orderStatusTv, orderNoTv;
    private String taxtnid;
    public static boolean purchaseCourse = false;
    private FrameLayout contiueShoppingRl;
    private PrefManager prefManager;
    private String orderNo;
    private String paymentMode;
    private String check;
    private HashMap<String, String> userdetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_order_success_activity);
        check = getIntent().getStringExtra("check");
        if (check.equalsIgnoreCase("1")) {
            taxtnid = getIntent().getStringExtra("taxtnid");
            Log.d("Taxtn ID", ":" + taxtnid);
        }
        initView();
        listenerView();
    }

    private void initView() {
        orderStatusTv = (TextView) findViewById(R.id.order_status_tv);
        contiueShoppingRl = (FrameLayout) findViewById(R.id.fame_sucess);
        orderNoTv = (TextView) findViewById(R.id.order_id_tv);
        mProgressDialog = new ProgressDialog(BuyOrderSuccessActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Loading...");
        prefManager = new PrefManager(BuyOrderSuccessActivity.this);
        userdetail = prefManager.getUserDetail();
        orderNo = prefManager.getOrderNO();
        paymentMode = prefManager.getPamentMode();
        order_id = prefManager.getOrderID();
    }

    private void listenerView() {
        contiueShoppingRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchaseCourse = true;
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("key", "1");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSuccessToken(Cons.update_payment_status);
        courseListProcess();
        processOfferDetails();
    }

    private void courseListProcess() {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest courseReq = new StringRequest(Request.Method.POST, Cons.URL_MY_COURSE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject object = new JSONObject(response);

                                String status = object.getString("status");
                                if (status.equals("0")) {
                                    Toast.makeText(BuyOrderSuccessActivity.this, "Please Purchase Content", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray array = object.getJSONArray("mycourse");
                                    String[] masterCats = new String[array.length()];
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject mycourse = array.getJSONObject(i);
                                        masterCats[i] = mycourse.getString(Cons.KEY_SUBJECT_ID);
                                    }
                                    prefManager.setSubjectIdArray(masterCats);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error : " + error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentId", userdetail.get(Cons.KEY_USER_ID));
                    Log.d(TAG, "getParams: " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseReq, TAG);
        } else {
            Toast.makeText(this, ""+Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    protected void getSuccessToken(String url) {
        if (Cons.isNetworkAvailable(this)) {
            final RequestQueue rq = Volley.newRequestQueue(BuyOrderSuccessActivity.this);
            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Token res success ", "= " + response);
                    if (response != null) {
                        Log.d("success hey ", "= " + response);
                        orderNoTv.setText(orderNo);
                        orderStatusTv.setText(paymentMode);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("INKPOT", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.d("error ocurred", "TimeoutError");
                    } else if (error instanceof AuthFailureError) {
                        Log.d("error ocurred", "AuthFailureError");
                        Toast.makeText(getApplication(), "AuthFailureError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                        Toast.makeText(getApplication(), "NetworkError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                        Toast.makeText(getApplication(), "ParseError", Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    Log.d("order_id  ", "= " + order_id);
                    String[] tag = {"orderid", "orderstatus", "p_PaymentStatus"};
                    String[] value = {order_id, "Confirmed", "success",};
                    for (int i = 0; i < tag.length; i++)
                        params.put(tag[i], value[i]);
                    return params;
                }
            };
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    800000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rq.add(postReq);
        } else {
            Toast.makeText(this, ""+Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void processOfferDetails() {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest getPrice = new StringRequest(Request.Method.POST, Cons.URL_COUPON_SUBMIT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(" offer Rqst", "= " + response);
                    try {
                        JSONObject main = new JSONObject(response);
                        String message = main.getString("message");
                        String status = main.getString("status");
                        if (status.equals("0")) {
                            Toast.makeText(BuyOrderSuccessActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error : " + error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentId", userdetail.get(Cons.KEY_USER_ID));
                    params.put("offerId", BuyDealsAdapter.wherenew);
                    Log.d(" Forgot Params ", "= " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(getPrice, TAG);
        } else {
            Toast.makeText(this, ""+Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        BuyDealsAdapter.where.clear();
    }
}
