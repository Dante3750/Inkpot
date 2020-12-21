package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.AndroidOpenDbHelper;
import com.onlineinkpot.models.BasicPrice;
import com.onlineinkpot.models.BasicPriceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyContentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BuyContentActivity";
    public TextView tvBasicDuration, tvBasicSubjectName, tvBasicTotalPrice;
    public TextView tvComboDuration, tvComboSubjectName, tvComboTotalPrice;
    public PrefManager pref;
    public String subjectid, courseid, semesterid;
    private HashMap<String, String> userdetail;
    public Button buttonbasicbuy, buttoncombobuy;
    public String subjectnamenew;
    private ArrayList priceList;

    public ArrayList<String>  semesteridnew=new ArrayList<String>();


    public AndroidOpenDbHelper androidopendbhelper;
    public String displaynew = null;
    private ProgressDialog progressDialog;
    private LinearLayout ll_combo;
    public static ArrayList<String> semesteridarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_content_activity);
        pref = new PrefManager(getApplicationContext());
        userdetail = pref.getUserDetail();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(BuyContentActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);


      //  Bundle b = getIntent().getExtras();

        ll_combo= (LinearLayout) findViewById(R.id.ll_combo);
        tvBasicDuration = (TextView) findViewById(R.id.tv_Basic_Duration);
        tvBasicSubjectName = (TextView) findViewById(R.id.tv_Basic_Subject);
        tvBasicTotalPrice = (TextView) findViewById(R.id.tv_Basic_Price);
        tvComboDuration = (TextView) findViewById(R.id.tv_Combo_Duration);
        tvComboSubjectName = (TextView) findViewById(R.id.tv_Combo_Subject);
        tvComboTotalPrice = (TextView) findViewById(R.id.tv_Combo_Price);
        buttonbasicbuy = (Button) findViewById(R.id.btn_Basic_Buy);
        buttoncombobuy = (Button) findViewById(R.id.btn_Combo_buy);
        priceList = new ArrayList();
        Intent in = getIntent();
        subjectid = in.getStringExtra("subjectId");
        Log.d(TAG, "onCreate: "+subjectid);
        courseid = in.getStringExtra("courseid");


        semesterid = in.getStringExtra("semesterid");


      //  semesteridarr = b.getStringArrayList("semesterid");
    //   semesteridnew.add(""+semesterid);
     //  Toast.makeText(this, "semesterid"+semesteridnew, Toast.LENGTH_SHORT).show();

        getDataPrice();
        setData();
        buttonbasicbuy.setOnClickListener(this);
        buttoncombobuy.setOnClickListener(this);
    }

    private void setData() {
        BasicPrice basicPrice = new BasicPrice();
        tvBasicDuration.setText(basicPrice.getDuration());
        tvBasicSubjectName.setText(basicPrice.getSubjectName());
        tvBasicTotalPrice.setText(basicPrice.getPrice());
    }

    private void getDataPrice() {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest getPrice = new StringRequest(Request.Method.POST, Cons.URL_PRICE_LIST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(" Price Rqst", "= " + response);
                    try {

                        JSONObject main = new JSONObject(response);
                        JSONObject object = main.getJSONObject("BesicPrice");
                        JSONObject object2 = main.getJSONObject("ComboPrice");
                        tvBasicSubjectName.setText(object.getString("subjectName"));
                        subjectnamenew = tvBasicSubjectName.getText().toString();
                        String durationtext = object.getString("duration");
                        String combodurationtext = object2.getString("duration");
                        String total = object2.getString("subTotal");
                        tvBasicDuration.setText("Total For " + durationtext);
                        tvComboDuration.setText("Total For " + combodurationtext);
                        tvComboTotalPrice.setText(total);
                        tvBasicTotalPrice.setText(object.getString("p_price"));
                        JSONArray subjectname = object2.getJSONArray("subjectName");
                        if (subjectname.length() == 1) {
                            ll_combo.setVisibility(View.GONE);
                        }
                        String[] combo = new String[subjectname.length()];
                        for (int i = 0; i < subjectname.length(); i++) {
                            JSONObject jsonObject = subjectname.getJSONObject(i);
                            String displayname = jsonObject.getString("sub_displayname");
                            if (displaynew == null) {
                                displaynew = displayname;
                                tvComboSubjectName.setText(" " + displaynew);
                            } else {
                                // Pass above values to the setter methods in POJO class
                                displaynew = displaynew + " + " + displayname;

                                tvComboSubjectName.setText(" " + displaynew);

                            }
                            combo[i] = jsonObject.getString("n_subjectID");
                            Log.d(TAG, "onResponse: " + combo[i]);
                        }
                        pref.setCombo(combo);
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
                    params.put("courseId", courseid);
                    params.put("semesterId", semesterid);
                    params.put("subjectId", subjectid);
                    Log.d(" data Params ", "= " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(getPrice, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public void insertUndergraduate(BasicPriceModel basicPriceModelObj) {
        AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);
        SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AndroidOpenDbHelper.STUDENT_ID, basicPriceModelObj.getStudentId());
        contentValues.put(AndroidOpenDbHelper.SEMESTER_ID, basicPriceModelObj.getSemesterId());
        contentValues.put(AndroidOpenDbHelper.SUBJECT_ID, basicPriceModelObj.getSubjectId());
        contentValues.put(AndroidOpenDbHelper.SUBJECT_NAME, basicPriceModelObj.getSubjectName());
        contentValues.put(AndroidOpenDbHelper.COURSE_ID, basicPriceModelObj.getCourseId());
        contentValues.put(AndroidOpenDbHelper.TOTAL_PRICE, basicPriceModelObj.getTotalPrice());
        contentValues.put(AndroidOpenDbHelper.DURATION, basicPriceModelObj.getDuration());
        long affectedColumnId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_BASIC_FIVE, null, contentValues);
        sqliteDatabase.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent();
        in.putExtra("subjectid", subjectid);
        in.putExtra("semesterid", semesterid);
        in.putExtra("courseid", courseid);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonbasicbuy.getId()) {
            forBasic();
        } else if (v.getId() == buttoncombobuy.getId()) {
            forCombo();
        }
    }

    private void forBasic() {
        BasicPriceModel basicPriceDataModel = new BasicPriceModel();
        basicPriceDataModel.setStudentId(userdetail.get(Cons.KEY_USER_ID));
        basicPriceDataModel.setSemesterId(semesterid);
        basicPriceDataModel.setSubjectId(subjectid);
        basicPriceDataModel.setSubjectName(subjectnamenew);
        basicPriceDataModel.setCourseId(courseid);
        basicPriceDataModel.setTotalPrice(tvBasicTotalPrice.getText().toString());
        basicPriceDataModel.setDuration(tvBasicDuration.getText().toString());

        priceList.add(basicPriceDataModel);
        insertUndergraduate(basicPriceDataModel);

       //semesteridnew.add(basicPriceDataModel.getSemesterId());

        androidopendbhelper = new AndroidOpenDbHelper(this);
        Intent in = new Intent(BuyContentActivity.this, BuyCartActivity.class);
        in.putExtra("subjectId", subjectid);
        in.putExtra("courseid", courseid);
        in.putExtra("semesterId",semesterid);



         //in.putExtra("semesterid", semesteridnew);


        //in.putExtra("semesterid", semesteridarr);
        //Log.d("semesteridbasic",semesterid);



        startActivity(in);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }






    private void forCombo() {
        BasicPriceModel basicPriceDataModel = new BasicPriceModel();
        basicPriceDataModel.setStudentId(userdetail.get(Cons.KEY_USER_ID));
        basicPriceDataModel.setSemesterId(semesterid);
        basicPriceDataModel.setSubjectId(pref.getCombo());
        Log.d(TAG, "forCombo: " + pref.getCombo());
        basicPriceDataModel.setSubjectName(tvComboSubjectName.getText().toString());
        basicPriceDataModel.setCourseId(courseid);
        basicPriceDataModel.setTotalPrice(tvComboTotalPrice.getText().toString());
        basicPriceDataModel.setDuration(tvComboDuration.getText().toString());
        priceList.add(basicPriceDataModel);
        insertUndergraduate(basicPriceDataModel);
        androidopendbhelper = new AndroidOpenDbHelper(this);
        Intent in = new Intent(BuyContentActivity.this, BuyCartActivity.class);
        in.putExtra("subjectId", basicPriceDataModel.getSubjectId());
        in.putExtra("courseid", courseid);
        in.putExtra("semesterid", semesterid);
        startActivity(in);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


    }
}
