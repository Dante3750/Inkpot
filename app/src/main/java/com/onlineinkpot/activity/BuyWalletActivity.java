package com.onlineinkpot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BuyWalletActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BuyWalletActivity";
    public TextView OrderSummaryCourseName, OrderSem, OrderPay, tvConfirm, orderSGST, orderCgst, orderIgst, ordergstTotal;
    public String subjectid, courseid, semesterid;
    private PrefManager pref;
    public boolean flag = false;
    public static float sgst;
    public static float cgst;
    public int total_flat;
    public String totalvalue;
    public String gsttotal;
    public static float gstTotal;
    public TextView OrderWalletAmount;
    public TextView OrderTotalPrice;
    private HashMap<String, String> user;
    private String order_json_no;
    private String payment_mode;
    public float wallete_amount;
    public String p_allsubid;
    public float total;
    private String order_id;
    public int ser;
    public String promoid = "";
    public String t_amounttype;
    public boolean re = false;
    private int zero = 0;
    private boolean paymentcheck = true;
    private String taxtnid = "";
    public float total_per1;
    private int promolist_size = 0;
    public static ArrayList<String> semesteridarr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Wallet");
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.buy_wallet_activity);
        Intent in = getIntent();

        subjectid = in.getStringExtra("subjectId");
        courseid = in.getStringExtra("courseid");
        //semesterid = in.getStringExtra("semesterid");

        Bundle b = getIntent().getExtras();

        semesteridarr = b.getStringArrayList("semesterid");

        totalvalue = in.getStringExtra("price");
        pref = new PrefManager(this);
        user = pref.getUserDetail();
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
        initializeLayoutContents();
    }

    private void initializeLayoutContents() {
        ordergstTotal = (TextView) findViewById(R.id.order_gst_total);
        orderCgst = (TextView) findViewById(R.id.order_cgst);
        orderIgst = (TextView) findViewById(R.id.order_igst);
        orderSGST = (TextView) findViewById(R.id.order_sgst);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        OrderSummaryCourseName = (TextView) findViewById(R.id.order_CourseName);
        OrderSem = (TextView) findViewById(R.id.order_Sem);
        OrderPay = (TextView) findViewById(R.id.order_to_pay);
        OrderWalletAmount = (TextView) findViewById(R.id.order_wallet_amount);
        OrderTotalPrice = (TextView) findViewById(R.id.order_total_Price);
        tvConfirm.setOnClickListener(this);
        if (subjectid.length() > 3) {
            p_allsubid = String.valueOf(-1);
        } else {
            p_allsubid = String.valueOf(1);
        }
        setView();
        getTaxes(Cons.get_taxs);
        applyCouponApi(Cons.appy_boucher_or_coupon, BuyCartActivity.promoCode, user.get(Cons.KEY_USER_ID), "2", "" + totalvalue, courseid, semesterid, subjectid, ser);
    }

    public void getTaxes(String url) {
        if (Cons.isNetworkAvailable(this)) {

            final StringRequest tax = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (response.length() > 0) {
                            String s1 = Cons.getString(object, "t_value1");
                            String s2 = Cons.getString(object, "t_value2");
                            String s3 = Cons.getString(object, "t_value3");
                            String amount = (String) totalvalue;
                            float a = Float.parseFloat(amount);
                            String SGST = String.valueOf(sgst = (a * 9) / 100);
                            String CGST = String.valueOf(cgst = (a * 9 / 100));
                            orderSGST.setText("\u20b9 " + SGST);
                            orderCgst.setText("\u20b9 " + CGST);
                            gstTotal = sgst + cgst + a;
                            gsttotal = String.valueOf(gstTotal);
                            ordergstTotal.setText("\u20b9 " + gsttotal);
                            total = Float.parseFloat(gsttotal);
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


    @Override
    protected void onResume() {
        super.onResume();
        getWalletAmount(Cons.get_wallete_amount, user.get(Cons.KEY_USER_ID));
    }

    protected void getPromocode(String url, final String user_id) {
        Log.d("dkp  promocode: url : ", url + ":user_id:" + user_id);
        final RequestQueue rq = Volley.newRequestQueue(this);
        final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("dkp promocode res : ", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    promolist_size = jsonArray.length();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.d("error ocurred", "TimeoutError");
                } else if (error instanceof AuthFailureError) {
                    Log.d("error ocurred", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.d("error ocurred", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.d("error ocurred", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.d("error ocurred", "ParseError");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String[] tag = {"act_mode"};
                String[] value = {"activepromocode"};
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
    }


    private void getWalletAmount(String get_wallete_amount, final String s) {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest signRequest = new StringRequest(Request.Method.POST, get_wallete_amount,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("dkp wallete res : ", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("wamt")) {
                                    Log.d("dkp wallete amt1 : ", jsonObject.getString("wamt"));
                                    wallete_amount = Cons.getFloat(jsonObject, "wamt");
                                    int walte_amt = (int) wallete_amount;
                                    MainActivity.walleteTv.setText("\u20b9 " + walte_amt);
                                    if (wallete_amount < total) {
                                        int wallte1 = (int) wallete_amount;
                                        OrderWalletAmount.setText("- \u20b9 " + wallte1);
                                        int stotal = (int) (total - wallete_amount);
                                        OrderPay.setText("\u20b9 " + stotal);
                                    } else {
                                        Log.d("dkp wallete amt 3: ", "" + total);
                                        int total1 = (int) total;
                                        OrderWalletAmount.setText("- \u20b9 " + total1);
                                        OrderPay.setText("\u20b9 " + zero);
                                    }
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
                    params.put("userid", s);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }


    private void setView() {
        //OrderSem.setText(semesterid);

        String semesteridnew = semesteridarr.toString().replaceAll("(^\\[|\\]$)", "");
        OrderSem.setText("" + semesteridnew);


        OrderTotalPrice.setText("\u20b9 " + totalvalue);
        OrderSummaryCourseName.setText(pref.getCourseName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                if (paymentcheck) {
                    if (flag) {
                        if (t_amounttype.equals("1")) {
                            if (wallete_amount >= total_flat) {
                                taxtnid = "TXN_";
                                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                                Log.d("  Uniqe id", timeStamp);
                                taxtnid = taxtnid + timeStamp;
                                Log.d("TaxId kkkkkkkk1", taxtnid);
                                getOrderIdFromServer(Cons.get_payment, "" + total_flat, "" + total);
                            } else {
                                int card_amount = (int) (total_flat - wallete_amount);
                                String a = String.valueOf(card_amount);
                                Intent in = new Intent(getApplicationContext(), BuyPaymentActivityDitto.class);
                                in.putExtra("subjectId", subjectid);
                                in.putExtra("courseid", courseid);
                                in.putExtra("semesterid", semesterid);
                                in.putExtra("wallet", "wall");
                                pref.setTaxanid(a);
                                startActivity(in);
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        } else if (t_amounttype.equals("2")) {
                            if (wallete_amount >= total_per1) {
                                taxtnid = "TXN_";
                                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                                Log.d("  Uniqe id", timeStamp);
                                taxtnid = taxtnid + timeStamp;
                                Log.d("TaxId kkkkkkkk1", taxtnid);
                                getOrderIdFromServer(Cons.get_payment, "" + total_per1, "" + total);
                                Toast.makeText(this, "" + total_per1, Toast.LENGTH_SHORT).show();
                            } else {
                                int card_amount = (int) (total_per1 - wallete_amount);
                                int wallte1 = (int) wallete_amount;
                                OrderWalletAmount.setText("- \u20b9 " + wallte1);
                                OrderTotalPrice.setText("₹ " + card_amount);
                                String a = String.valueOf(card_amount);
                                Intent in = new Intent(getApplicationContext(), BuyPaymentActivityDitto.class);
                                in.putExtra("subjectId", subjectid);
                                in.putExtra("courseid", courseid);
                                in.putExtra("semesterid", semesterid);
                                in.putExtra("wallet", "wall");
                                pref.setTaxanid(a);
                                startActivity(in);
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        }
                    } else {
                        if (wallete_amount >= total) {
                            taxtnid = "TXN_";
                            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
                            Log.d("  Uniqe id", timeStamp);
                            taxtnid = taxtnid + timeStamp;
                            getOrderIdFromServer(Cons.get_payment, "" + total, "" + total);
                        } else {
                            int card_amount = (int) (total - wallete_amount);
                            String a = String.valueOf(card_amount);
                            Intent in = new Intent(getApplicationContext(), BuyPaymentActivityDitto.class);
                            in.putExtra("subjectId", subjectid);
                            in.putExtra("courseid", courseid);
                            in.putExtra("semesterid", semesterid);
                            in.putExtra("wallet", "wall");
                            pref.setTaxanid(a);
                            startActivity(in);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    }
                } else {
                    Toast.makeText(this, "Payment Not Allowed", Toast.LENGTH_SHORT).show();
                }
        }

    }

    protected void applyCouponApi(String url, final String coupon, final String user_id, final String cat_id, final String total11, final String classid, final String semid, final String subjectId, final int course_bundle) {
        Log.d("dkp : url : ", url + ":coupon:" + coupon + ":user_id:" + user_id + ":cat_id:" + cat_id + ":total:" + total + ":classid:" + classid + ":semid:" + semid + ":subjectId:" + subjectId + ":course_bundle:" + course_bundle);
        final RequestQueue rq = Volley.newRequestQueue(this);
        if (Cons.isNetworkAvailable(this)) {

            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("dkp : response : ", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("co")) {
                            String co = jsonObject.getString("co");
                            if (co.equals("1")) {
                                float totalaa = (float) jsonObject.getDouble("t_amount");
                                t_amounttype = jsonObject.getString("t_amounttype");
                                if (t_amounttype.equals("1")) {
                                    if (total > totalaa) {
                                        float total12 = total - totalaa;
                                        total_flat = (int) total12;
                                        if (wallete_amount >= total_flat) {
                                            OrderWalletAmount.setText("- \u20b9 " + total_flat);
                                            OrderTotalPrice.setText("₹ 0");
                                        } else {
                                            int card_amount = (int) (total_flat - wallete_amount);
                                            int wallte1 = (int) wallete_amount;
                                            OrderWalletAmount.setText("- \u20b9 " + wallte1);
                                            OrderTotalPrice.setText("₹ " + card_amount);
                                        }
                                        promoid = jsonObject.getString("t_promoid");
                                        flag = true;
                                    } else {
                                    }
                                } else if (t_amounttype.equals("2")) {
                                    float total_per = totalaa * total / 100;
                                    total_per1 = total - total_per;
                                    Log.d("dkp", ":" + total_per1);
                                    if (wallete_amount >= total_per1) {
                                        OrderWalletAmount.setText("- \u20b9 " + total_per1);
                                        OrderTotalPrice.setText("₹ 0");
                                    } else {
                                        int card_amount = (int) (total_per1 - wallete_amount);
                                        int wallte1 = (int) wallete_amount;
                                        OrderWalletAmount.setText("- \u20b9 " + wallte1);
                                        OrderTotalPrice.setText("₹ " + card_amount);
                                    }
                                    promoid = jsonObject.getString("t_promoid");
                                    flag = true;
                                }
                            } else {
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.d("error ocurred", "TimeoutError");
                    } else if (error instanceof AuthFailureError) {
                        Log.d("error ocurred", "AuthFailureError");
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String[] tag = {"p_couponcode", "p_userid", "cart_price", "catid", "classid", "semid", "coursebundle", "subject"};
                    String[] value = {coupon, user_id, total11, "", "", cat_id, classid, semid, subjectId, "" + course_bundle};
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


    protected void getOrderIdFromServer(String url, final String total, final String subtotal) {


        Log.d("dkp server url", ":" + url);
        final RequestQueue rq = Volley.newRequestQueue(this);
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("resonse success ", "= " + response);
                    if (response != null) {
                        Log.d("success hey ", "= " + response);
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
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String[] tag = {"promoid", "p_discountid", "p_tax", "orderstatus", "paymentmode", "p_PaymentStatus", "p_studentid", "p_maincatid", "p_clacouid", "p_enumtypeid", "p_durid", "p_semid", "p_allsubid", "p_validdate", "p_totalcost", "p_txnid", "servicetype", "subtotal", "walletbal"};
                    String[] value = {BuyCartActivity.promoid, String.valueOf(BuyCartActivity.totalaa), "9", "Confirmed", "IPWallet", "failure", user.get(Cons.KEY_USER_ID), "2", courseid, "1", "6", semesterid, p_allsubid, "15-12-2017", gsttotal, taxtnid, "Android", subtotal, String.valueOf(BuyCartActivity.total)};
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


    protected void getSuccessToken() {

        String url = Cons.get_success_token;
        final RequestQueue rq = Volley.newRequestQueue(this);
        if (Cons.isNetworkAvailable(this)) {

            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Token res success ", "= " + response);
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
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String[] tag = {"orderid", "p_maincatid", "p_clacouid", "subject_id", "subprice"};
                    String[] value = {order_id, "2", courseid, subjectid, String.valueOf(total)};
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


    protected void getSuccessAfterToken() {
        if (Cons.isNetworkAvailable(this)) {
            String url = Cons.get_success_after_token;
            final RequestQueue rq = Volley.newRequestQueue(this);
            final StringRequest postReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("after token success ", "= " + response);
                    if (response != null) {
                        Log.d("success hey ", "= " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            order_json_no = jsonObject.getString("order_number");
                            payment_mode = jsonObject.getString("payment_mode");
                            order_id = jsonObject.getString("id");
                            pref.setOrderNo(order_json_no);
                            pref.setOrderPaymentMode(payment_mode);
                            pref.setOrderID(order_id);
                            Log.d("dkp order number", ":" + order_json_no);
                            Log.d("dkp payment mode", ":" + payment_mode);
                            Log.d("dkp payment mode", ":" + order_id);
                            Intent intent = new Intent(BuyWalletActivity.this, BuyOrderSuccessActivity.class);
                            intent.putExtra("taxtnid", taxtnid);
                            intent.putExtra("check", "1");
                            startActivity(intent);
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
                    } else if (error instanceof ServerError) {
                        Log.d("error ocurred", "ServerError");
                    } else if (error instanceof NetworkError) {
                        Log.d("error ocurred", "NetworkError");
                    } else if (error instanceof ParseError) {
                        Log.d("error ocurred", "ParseError");
                    }
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
