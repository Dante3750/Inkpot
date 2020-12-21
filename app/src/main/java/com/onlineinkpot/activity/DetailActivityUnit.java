package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.DetailUnitAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
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

/**
 * Created by USER on 9/4/2017.
 */

public class DetailActivityUnit extends AppCompatActivity {
    public Toolbar toolbar;
    public ProgressBar progressBar;
    public ViewPager mViewPager;
    public static List<ModSemest> semList;
    private TextView semTitle;
    private TabLayout tabs;
    private static final String TAG = "DetailActivityUnit";
    private DetailUnitAdapter mAdapter;
    public static List<ModUnit> unitLit;
    public PrefManager pref;
    public List<ModUnit> unitList;
    public String chapterName;
    public List<String> unitListnew;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_unit);
        pref = new PrefManager(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_sem);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(DetailActivityUnit.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#28b5d3"));
        initiateLayoutVariables(getIntent());
    }

    private void initiateLayoutVariables(Intent in) {
        semList = new ArrayList<>();
        tabs = (TabLayout) findViewById(R.id.tab_sem);
        semTitle = (TextView) findViewById(R.id.tv_sem_title);
        toolbar.setBackgroundColor(Color.parseColor("#" + pref.getCourseColor()));
        tabs.setBackgroundColor(Color.parseColor("#" + pref.getCourseColor()));
        mViewPager = (ViewPager) findViewById(R.id.container);
        mAdapter = new DetailUnitAdapter(getSupportFragmentManager(), semList);
        mViewPager.setAdapter(mAdapter);
        tabs.setupWithViewPager(mViewPager);
        getUnitData(getIntent());
    }
    private void getUnitData(final Intent in) {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest semesRequest = new StringRequest(Request.Method.POST, Cons.URL_UNIT_DATA,
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
                                            String unitName = semOb.getString("unitName");
                                            String unitId = semOb.getString("unitId");
                                            String resemId = semOb.getString("semId");
                                            JSONArray subArray = semOb.getJSONArray("data");
                                            List<ModSubject> subList = new ArrayList<>();
                                            for (int j = 0; j < subArray.length(); j++) {
                                                unitListnew = new ArrayList<>();
                                                JSONObject subOb = subArray.getJSONObject(j);
                                                String chapterId = subOb.getString("chapterId");
                                                chapterName = subOb.getString("chapterName");
                                                unitListnew.add(chapterName);
                                                JSONArray unitArray = subOb.getJSONArray("topic");
                                                unitList = new ArrayList<>();
                                                if (unitArray.length() > 0) {
                                                    for (int k = 0; k < unitArray.length(); k++) {
                                                        JSONObject untOb = unitArray.getJSONObject(k);
                                                        String topicName = untOb.getString("t_topicName");
                                                        Log.d(TAG, "onTopic: " + topicName);
                                                        JSONArray contentArray = untOb.getJSONArray("content");
                                                        for (int l = 0; l < contentArray.length(); l++) {
                                                            JSONObject contentObj = contentArray.getJSONObject(l);
                                                            String contentId = contentObj.getString("n_ccid");
                                                            String filenamenew = contentObj.getString("t_freefileName");
                                                            unitList.add(l, new ModUnit((l + 1), contentId, topicName, filenamenew));
                                                        }
                                                    }
                                                    subList.add(j, new ModSubject(chapterId, chapterName, null, chapterName, chapterId, unitList));
                                                }
                                            }
                                            semList.add(i, new ModSemest(unitName, unitId, resemId, subList));
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
                    params.put("subjectId", in.getStringExtra("uSubId"));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(semesRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}