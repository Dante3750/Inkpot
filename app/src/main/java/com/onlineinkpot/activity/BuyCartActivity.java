package com.onlineinkpot.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.BuyCartAdapter;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.AndroidOpenDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anytime on 9/13/2017.
 */

public class BuyCartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BuyCartActivity";

    public static AndroidOpenDbHelper androidopendbhelper;
    public static SQLiteDatabase sqlitedatabase;
    Cursor cursor;
    protected PrefManager pref;
    public String totalvalue;
    private HashMap<String, String> userDetail;
    public static String promoCode;
    public static float total;
    public float wallete_amount;
    public static int total_flat;
    public static float totalaa;
    public ImageView ivPromoCode;
    public int ser;
    public float total_per1;
    public static String promoid = "";
    public static String t_amounttype;
    public boolean flag = false;
    public static EditText etPromoCode;
    public static BuyCartAdapter ListAdapter;
    private AlertDialog alertDialog;
    String subjectid, courseid, semesterid;
    ArrayList<String> StudentID_ArrayList = new ArrayList<String>();
    ArrayList<String> SemesterID_ArrayList = new ArrayList<String>();
    ArrayList<String> SubjectId_ArrayList = new ArrayList<String>();
    ArrayList<String> SubjectName_ArrayList = new ArrayList<String>();
    ArrayList<String> CourseID_ArrayList = new ArrayList<String>();
    ArrayList<String> TotalPrice_ArrayList = new ArrayList<String>();
    ArrayList<String> Duration_ArrayList = new ArrayList<String>();
    public static ListView Listview;
   public static   Button button;
    public static TextView textview4;
    private TextView noData;
    public static ArrayList<String> semesteridarr;
   //ArrayList<String> semesterid;
    Button addmorebtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_cart_activity);
        setTitle("Cart");
        Listview = (ListView) findViewById(R.id.listview);
        pref = new PrefManager(this);
        userDetail = pref.getUserDetail();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        button = (Button) findViewById(R.id.button1);
        addmorebtn = (Button) findViewById(R.id.addmorebutton);



        textview4 = (TextView) findViewById(R.id.text4);
        noData = (TextView) findViewById(R.id.tv_empty_cart);
        ivPromoCode = (ImageView) findViewById(R.id.add_code);
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
      //subjectid = in.getStringExtra("subjectId");
      //courseid = in.getStringExtra("courseid");
       //semesterid = in.getStringExtra("semesterid");

       //semesterid=BuyCartAdapter.semesterid;

      // semesteridarr= BuyCartAdapter.semesterid;

   //   Log.d("semesteridarr",BuyCartAdapter.semesterid.toString());


     //   Bundle b = getIntent().getExtras();



        subjectid = in.getStringExtra("subjectId");
         courseid = in.getStringExtra("courseid");
          semesterid = in.getStringExtra("semesterId");


        // semesteridarr = b.getStringArrayList("semesterid");
        androidopendbhelper = new AndroidOpenDbHelper(this);
        showSQliteDbData();
        total = BuyCartAdapter.totalamount;

        semesteridarr= BuyCartAdapter.semesterid;
        if(semesteridarr.isEmpty()){

            button.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "You have no subjects added in cart", Toast.LENGTH_SHORT).show();
            addmorebtn.setVisibility(View.VISIBLE);
            addmorebtn.setOnClickListener(this);

        }


        float total = BuyCartAdapter.totalamount;
        textview4.setText("" + total);
        button.setOnClickListener(this);
        ivPromoCode.setOnClickListener(this);
        }



    private void promoCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_promo_code, null);
        builder.setView(dialogView);
        etPromoCode = (EditText) dialogView.findViewById(R.id.et_promo_code);
        dialogView.findViewById(R.id.bt_cancel).setOnClickListener(this);
        dialogView.findViewById(R.id.bt_send).setOnClickListener(this);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showSQliteDbData() {
        sqlitedatabase = androidopendbhelper.getWritableDatabase();
        cursor = sqlitedatabase.rawQuery("SELECT * FROM basic_details_table_five", null);
        StudentID_ArrayList.clear();
        SemesterID_ArrayList.clear();
        SubjectId_ArrayList.clear();
        SubjectName_ArrayList.clear();
        CourseID_ArrayList.clear();
        TotalPrice_ArrayList.clear();
        Duration_ArrayList.clear();
        if (cursor.moveToFirst()) {
            do {
                StudentID_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.STUDENT_ID)));
                SemesterID_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.SEMESTER_ID)));
                SubjectId_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.SUBJECT_ID)));
                SubjectName_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.SUBJECT_NAME)));
                CourseID_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.COURSE_ID)));
                SubjectId_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.SUBJECT_ID)));
                TotalPrice_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.TOTAL_PRICE)));
                Duration_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.DURATION)));
            }
            while (cursor.moveToNext());
        }

        ArrayList<String> tempList = new ArrayList<String>();
        String s1, s2;
        int i = 0, j = 0;
        while (i < SubjectId_ArrayList.size()) {
            s1 = SubjectId_ArrayList.get(i);
            s2 = SubjectId_ArrayList.get(i + 1);
            if (s1.equalsIgnoreCase(s2)) {
                tempList.add(s1);
                i = i + 2;
            } else {
                tempList.add(s1);
                i = i + 1;
            }
        }

        while (j < tempList.size()) {
            SubjectId_ArrayList.set(j, tempList.get(j));
            j++;
        }

        ListAdapter = new BuyCartAdapter(BuyCartActivity.this,
                StudentID_ArrayList,
                SemesterID_ArrayList,
                SubjectId_ArrayList,
                SubjectName_ArrayList,
                CourseID_ArrayList,
                TotalPrice_ArrayList,
                Duration_ArrayList
        );
        Listview.setAdapter(ListAdapter);
        cursor.close();
        }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                totalvalue = textview4.getText().toString();
                Intent in = new Intent(BuyCartActivity.this, BuyDealsActivity.class);
                in.putExtra("subjectId", subjectid);
                in.putExtra("courseid", courseid);

                //in.putExtra("semesterid", semesterid);
                semesteridarr= BuyCartAdapter.semesterid;




                in.putExtra("semesterid", semesteridarr);

                in.putExtra("totalvalue", totalvalue);



                startActivity(in);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.add_code:
                promoCode();
                break;
            case R.id.bt_cancel:
                alertDialog.dismiss();
                break;

            case R.id.bt_send:
              //  Toast.makeText(this, "promocode"+subjectid, Toast.LENGTH_SHORT).show();
                promoCode = etPromoCode.getText().toString();
               if (subjectid.length() > 3) {
                    ser = -1;
                } else {
                    ser = 0;
                }
                applyCouponApi(Cons.appy_boucher_or_coupon, promoCode);
                break;

            case R.id.addmorebutton:

              Intent in21 =new Intent(BuyCartActivity.this,MainActivity.class);
              startActivity(in21);



                break;





        }
        }

    protected void applyCouponApi(String url, final String coupon) {
        final RequestQueue rq = Volley.newRequestQueue(this);

     //   if (Cons.isNetworkAvailable(this)) {

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
                            Log.d(TAG, "onResponse: " + totalaa);
                            t_amounttype = jsonObject.getString("t_amounttype");
                            if (t_amounttype.equals("1")) {
                                if (total > totalaa) {
                                    float total12 = total - totalaa;
                                    total_flat = (int) total12;
                                    textview4.setText("" + total12);
                                    if (wallete_amount >= total_flat) {
                                        int wallte1 = (int) wallete_amount;
                                    }
                                    promoid = jsonObject.getString("t_promoid");
                                    flag = true;
                                    Toast.makeText(BuyCartActivity.this, "Promocode applied successfully", Toast.LENGTH_SHORT).show();
                                    ivPromoCode.setVisibility(View.GONE);
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(BuyCartActivity.this, "Promocode not applied successfully", Toast.LENGTH_SHORT).show();
                                }
                            } else if (t_amounttype.equals("2")) {
                                float total_per = totalaa * total / 100;
                                float total_per1 = total - total_per;
                                if (wallete_amount >= total_per1) {
                                } else {
                                    int card_amount = (int) (total_per1 - wallete_amount);
                                    int wallte1 = (int) wallete_amount;
                                }
                                promoid = jsonObject.getString("t_promoid");
                                flag = true;
                                Toast.makeText(BuyCartActivity.this, "Promocode  applied successfully", Toast.LENGTH_SHORT).show();
                                ivPromoCode.setVisibility(View.GONE);
                            }
                           }
                        else {
                            Toast.makeText(BuyCartActivity.this, "Promocode not match", Toast.LENGTH_SHORT).show();
                        }
                        }
                        }



                        catch (JSONException e) {
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
                String[] tag = {"p_couponcode", "p_userid", "cart_price", "catid", "classid", "semid", "subject"};
                String[] value = {coupon, userDetail.get(Cons.KEY_USER_ID), String.valueOf(total), "2", courseid, semesterid , subjectid};
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


     @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent in=new Intent(BuyCartActivity.this,MainActivity.class);
  //      in.putExtra(,);
  //       in.putExtra(,);
  //      in.putExtra(,);
        startActivity(in);

    }
}