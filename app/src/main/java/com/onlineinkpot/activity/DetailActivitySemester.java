package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.DetailSemesterAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.PicassoClient;
import com.onlineinkpot.models.ModSemest;
import com.onlineinkpot.models.ModSubject;
import com.onlineinkpot.models.ModUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivitySemester extends AppCompatActivity {

    private static final String TAG = "DetailActivitySemester";
    public static List<ModSemest> semList;
    private ImageView semIcon;
    private TextView semTitle;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private TabLayout tabs;
    PrefManager pref;
    public static String colordata;
    private HashMap<String, String> userdetail;
    private DetailSemesterAdapter mAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_semester);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar_sem);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(DetailActivitySemester.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        pref = new PrefManager(this);
        userdetail = pref.getUserDetail();
        initiateLayoutVariables(getIntent());
        getSemesterData(getIntent());
    }

    private void initiateLayoutVariables(Intent in) {
        semList = new ArrayList<>();
        tabs = (TabLayout) findViewById(R.id.tab_sem);
        semIcon = (ImageView) findViewById(R.id.iv_sem_icon);
        semTitle = (TextView) findViewById(R.id.tv_sem_title);
        toolbar.setBackgroundColor(Color.parseColor("#" + getIntent().getStringExtra("courseColor")));
        tabs.setBackgroundColor(Color.parseColor("#" + getIntent().getStringExtra("courseColor")));
        colordata = getIntent().getStringExtra("courseColor");
        pref.setCourseColor(colordata);
        semTitle.setText(in.getStringExtra("courseTitle"));
        PicassoClient.downloadImage(getApplicationContext(), Cons.URL_IMAGE + in.getStringExtra("courseIcon"), semIcon);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mAdapter = new DetailSemesterAdapter(getSupportFragmentManager(), semList);
        mViewPager.setAdapter(mAdapter);
        tabs.setupWithViewPager(mViewPager);
    }

    private void getSemesterData(final Intent intent) {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest semesRequest = new StringRequest(Request.Method.POST, Cons.URL_SEMESTER_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                int status = object.getInt("status");
                                if (status == 1) {
                                    JSONArray semArray = object.getJSONArray("0");
                                    semList.clear();
                                    if (semArray.length() > 0) {
                                        for (int i = 0; i < semArray.length(); i++) {
                                            JSONObject semOb = semArray.getJSONObject(i);
                                            String semName = semOb.getString("semName");
                                            String semesterId = semOb.getString("semId");
                                            JSONArray subArray = semOb.getJSONArray("data");
                                            List<ModSubject> subList = new ArrayList<>();
                                            if (subArray.length() > 0) {
                                                for (int j = 0; j < subArray.length(); j++) {
                                                    JSONObject subOb = subArray.getJSONObject(j);
                                                    String courseId = subOb.getString("n_classid");
                                                    String semId = subOb.getString("n_CourseDuration");
                                                    String subId = subOb.getString("n_subjectID");
                                                    String subName = subOb.getString("sub_displayname");
                                                    Log.d(TAG, "Sub" + subId);
                                                    JSONArray unitArray = subOb.getJSONArray("unit");
                                                    List<ModUnit> unitList = new ArrayList<>();
                                                    if (unitArray.length() > 0) {
                                                        for (int k = 0; k < unitArray.length(); k++) {
                                                            JSONObject untOb = unitArray.getJSONObject(k);
                                                            String uId = untOb.getString("n_unitID");
                                                            String uName = untOb.getString("t_unitName");
                                                            String uSubId = untOb.getString("n_subjectID");
                                                            unitList.add(k, new ModUnit((k + 1), uId, uName, uSubId));
                                                        }
                                                    }
                                                    subList.add(j, new ModSubject(subId, subName, null, semId, courseId, unitList));
                                                }
                                            }
                                            semList.add(i, new ModSemest(semName, semesterId, null, subList));
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("courseId", intent.getStringExtra("courseId"));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(semesRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}