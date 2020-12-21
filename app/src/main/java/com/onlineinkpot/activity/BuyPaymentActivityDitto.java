package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 9/14/2017.
 */

public class BuyPaymentActivityDitto extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BuyPaymentActivity";
    private ProgressDialog mProgressDialog;
    private String taxtnid = "";
    public String totalvalue;
    private PrefManager prefManager;
    public String subjectid, courseid, semesterid;
    private String order_id;
    private String order_json_no;
    private HashMap<String, String> userdetail;
    private String payment_mode;
    public static float sgst;
    public static float cgst;
    public static float gstTotal;
    public String gsttotal;
    private String wallet = "0";
    private String enum_typeId = "1";
    private String p_durid = "6";
    private String basic_p_allsubid = "1";
    private String combo_p_allsubid = "-1";
    private String p_maincatid = "2";
    private CardView cvPaytm, cvPayU, cvWallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Payment");
        setContentView(R.layout.buy_payment_activity_ditto);
        prefManager = new PrefManager(getApplicationContext());
        userdetail = prefManager.getUserDetail();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        subjectid = in.getStringExtra("subjectId");
        Log.d(TAG, "onCreate: " + subjectid);
        courseid = in.getStringExtra("courseid");
        semesterid = in.getStringExtra("semesterid");
        totalvalue = prefManager.getTxanid();
        cvPaytm = (CardView) findViewById(R.id.cv_paytm);
        cvPayU = (CardView) findViewById(R.id.cv_payu);
        cvWallet = (CardView) findViewById(R.id.cv_wallet);
        mProgressDialog = new ProgressDialog(getApplicationContext(), android.app.AlertDialog.THEME_HOLO_LIGHT);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Loading...");
        cvPaytm.setOnClickListener(this);
        cvPayU.setOnClickListener(this);
        cvWallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_paytm:
                taxtnid = "TXN_";
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                Log.d("  Uniqe id", timeStamp);
                taxtnid = taxtnid + timeStamp;
                Log.d("tax id", taxtnid);
                getTaxes(Cons.get_taxs, totalvalue);
                getOrderIdFromServer(Cons.get_payment);
                onStartTransaction(taxtnid, totalvalue);
                break;
            case R.id.cv_payu:
                taxtnid = "TXN_";
                String timeStampp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                Log.d("  Uniqe id", timeStampp);
                taxtnid = taxtnid + timeStampp;
                Log.d("tax id", taxtnid);
                getOrderIDFromServerPayU(Cons.get_payment);
                Intent payu = new Intent(BuyPaymentActivityDitto.this, BuyPayUmoneyActivity.class);
                payu.putExtra("taxtnid", taxtnid);
                payu.putExtra("price", totalvalue);
                startActivity(payu);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.cv_wallet:
                Intent wallet = new Intent(BuyPaymentActivityDitto.this, BuyWalletActivity.class);
                wallet.putExtra("subjectId", subjectid);
                wallet.putExtra("courseid", courseid);
                wallet.putExtra("semesterid", semesterid);
                wallet.putExtra("price", totalvalue);
                startActivity(wallet);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    public void getTaxes(String url, final String totalvalue) {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest tax = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (response.length() > 0) {
                            String amount = (String) totalvalue;
                            float a = Float.parseFloat(amount);
                            sgst = (a * 9) / 100;
                            cgst = (a * 9) / 100;
                            gstTotal = sgst + cgst + a;
                            gsttotal = String.valueOf(gstTotal);
                            Log.d(TAG, "onResponse: " + amount);
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
            };
            AppController.getInstance().addToRequestQueue(tax, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrderIDFromServerPayU(String get_payment) {
        if (Cons.isNetworkAvailable(this)) {
            final RequestQueue rq = Volley.newRequestQueue(BuyPaymentActivityDitto.this);
            final StringRequest postReq = new StringRequest(Request.Method.POST, get_payment, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("resonse success ", "= " + response);
                    mProgressDialog.dismiss();
                    if (response != null) {
                        Log.d("successpayu", "= " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            order_id = jsonObject.get("lastinertid").toString();
                            getSuccessToken();
                            getSuccessAfterToken();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                        Toast.makeText(getApplicationContext(), "AuthFailureError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                        Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                        Toast.makeText(getApplicationContext(), "ParseError", Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String[] tag = {"promoid", "p_discountid", "p_tax", "orderstatus", "paymentmode", "p_PaymentStatus", "p_studentid", "p_maincatid", "p_clacouid", "p_enumtypeid", "p_durid", "p_semid", "p_allsubid", "p_validdate", "p_totalcost", "p_txnid", "p_applycode", "servicetype", "subtotal", "walletbal"};
                    if (subjectid.length() > 3) {
                        String[] value = {BuyCartActivity.promoCode, "500", "null", "Confirmed", "PayU", "failure", userdetail.get(Cons.KEY_USER_ID), p_maincatid, courseid, enum_typeId, p_durid, semesterid, combo_p_allsubid, "null", totalvalue, taxtnid, "null", "Android", totalvalue, wallet};
                        for (int i = 0; i < tag.length; i++)
                            params.put(tag[i], value[i]);
                        Log.d(TAG, "combo: " + combo_p_allsubid);
                    } else {
                        String[] value = {BuyCartActivity.promoCode, "500", "null", "Confirmed", "PayU", "failure", userdetail.get(Cons.KEY_USER_ID), p_maincatid, courseid, enum_typeId, p_durid, semesterid, basic_p_allsubid, "null", totalvalue, taxtnid, "null", "Android", totalvalue, wallet};
                        for (int i = 0; i < tag.length; i++)
                            params.put(tag[i], value[i]);
                        Log.d(TAG, "basic: " + basic_p_allsubid);
                    }
                    return params;
                }
            };
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    800000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rq.add(postReq);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public void onStartTransaction(String transactionId, String price) {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", transactionId);
        paramMap.put("MID", "InkPot24214310894920");
        paramMap.put("CUST_ID", userdetail.get(Cons.KEY_USER_ID));
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail109");
        paramMap.put("THEME", "merchant");
        paramMap.put("WEBSITE", "InkPWAP");
        paramMap.put("TXN_AMOUNT", totalvalue);
        Log.d("price", price);
        PaytmOrder Order = new PaytmOrder(paramMap);
        PaytmMerchant Merchant = new PaytmMerchant(
                "http://www.inkpotonline.com/Paytm_App_Checksum/generateChecksum.php",
                "http://www.inkpotonline.com/Paytm_App_Checksum/verifyChecksum.php");
        Service.initialize(Order, Merchant, null);
        Service.startPaymentTransaction(BuyPaymentActivityDitto.this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), BuyOrderSuccessActivity.class);
                        intent.putExtra("taxtnid", taxtnid);
                        intent.putExtra("check", "1");
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        Intent intent = new Intent(getApplicationContext(), BuyOrderFailureActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        Toast.makeText(getApplicationContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                        Intent in = new Intent(BuyPaymentActivityDitto.this, BuyCartActivity.class);
                        in.putExtra("subjectId", subjectid);
                        in.putExtra("courseid", courseid);
                        in.putExtra("semesterid", semesterid);
                        startActivity(in);
                        finish();
                    }
                });
    }

    protected void getOrderIdFromServer(String url) {
        if (Cons.isNetworkAvailable(this)) {
            final RequestQueue rq = Volley.newRequestQueue(BuyPaymentActivityDitto.this);
            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("resonse success ", "= " + response);
                    mProgressDialog.dismiss();
                    if (response != null) {
                        Log.d("success", "= " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            order_id = jsonObject.get("lastinertid").toString();
                            getSuccessToken();
                            getSuccessAfterToken();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                        Toast.makeText(getApplicationContext(), "AuthFailureError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                        Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                        Toast.makeText(getApplicationContext(), "ParseError", Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String[] tag = {"promoid", "p_discountid", "p_tax", "orderstatus", "paymentmode", "p_PaymentStatus", "p_studentid", "p_maincatid", "p_clacouid", "p_enumtypeid", "p_durid", "p_semid", "p_allsubid", "p_validdate", "p_totalcost", "p_txnid", "p_applycode", "servicetype", "subtotal", "walletbal"};
                    if (subjectid.length() > 3) {
                        String[] value = {BuyCartActivity.promoCode, "500", "null", "Confirmed", "PayTm", "failure", userdetail.get(Cons.KEY_USER_ID), p_maincatid, courseid, enum_typeId, p_durid, semesterid, combo_p_allsubid, "null", totalvalue, taxtnid, "null", "Android", totalvalue, wallet};
                        for (int i = 0; i < tag.length; i++)
                            params.put(tag[i], value[i]);
                        Log.d(TAG, "combo: " + combo_p_allsubid);
                    } else {
                        String[] value = {BuyCartActivity.promoCode, "500", "null", "Confirmed", "PayTm", "failure", userdetail.get(Cons.KEY_USER_ID), p_maincatid, courseid, enum_typeId, p_durid, semesterid, basic_p_allsubid, "null", totalvalue, taxtnid, "null", "Android", totalvalue, wallet};
                        for (int i = 0; i < tag.length; i++)
                            params.put(tag[i], value[i]);
                        Log.d(TAG, "basic: " + basic_p_allsubid);
                    }
                    return params;
                }
            };
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    800000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rq.add(postReq);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }

    protected void getSuccessToken() {
        if (Cons.isNetworkAvailable(this)){
        String url = Cons.get_success_token;
        final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Token res success ", "= " + response);
                mProgressDialog.dismiss();
                if (response != null) {
                    Log.d("success hey ", "= " + response);
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
                    Toast.makeText(getApplicationContext(), "AuthFailureError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Log.d("error ocurred", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.d("error ocurred", "NetworkError");
                    Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Log.d("error ocurred", "ParseError");
                    Toast.makeText(getApplicationContext(), "ParseError", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String[] tag = {"orderid", "p_maincatid", "p_clacouid", "subject_id", "subprice"};
                String[] value = {order_id, p_maincatid, courseid, subjectid, totalvalue};
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
    }else {
            Toast.makeText(this, ""+Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }}


    protected void getSuccessAfterToken() {
        if (Cons.isNetworkAvailable(this)) {
            String url = Cons.get_success_after_token;
            final RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("after token success ", "= " + response);
                    mProgressDialog.dismiss();
                    if (response != null) {
                        Log.d("success hey ", "= " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            order_json_no = jsonObject.getString("order_number");
                            payment_mode = jsonObject.getString("payment_mode");
                            order_id = jsonObject.getString("id");
                            prefManager.setOrderNo(order_json_no);
                            prefManager.setOrderPaymentMode(payment_mode);
                            prefManager.setOrderID(order_id);
                            Log.d("dkp order number", ":" + order_json_no);
                            Log.d("dkp payment mode", ":" + payment_mode);
                            Log.d("dkp payment mode", ":" + order_id);
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                        Toast.makeText(getApplicationContext(), "AuthFailureError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                        Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                        Toast.makeText(getApplicationContext(), "ParseError", Toast.LENGTH_LONG).show();
                    }
                    mProgressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String[] tag = {"orderid"};
                    String[] value = {order_id};
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
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}