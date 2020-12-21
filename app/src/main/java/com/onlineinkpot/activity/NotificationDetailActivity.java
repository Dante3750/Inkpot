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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.FragmentNotificationDetailsAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.models.NotificationDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 10/5/2017.
 */

public class NotificationDetailActivity extends AppCompatActivity {

    String headername;
    private static final String TAG = "NotificationDetailActivity";
    FragmentNotificationDetailsAdapter mAdapter;
    private RecyclerView rv;
    private ProgressDialog progressDialog;
    public List<NotificationDetailModel> notificationHistories;
    TextView noData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noData = (TextView) findViewById(R.id.tv_notification_text);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent in = getIntent();
        headername = in.getStringExtra("headername");
        initiateLayoutVariables(headername);
    }

    private void initiateLayoutVariables(String string) {
        getNotificationData(getIntent());
        rv = (RecyclerView) findViewById(R.id.rvList_order);
        notificationHistories = new ArrayList<>();
        mAdapter = new FragmentNotificationDetailsAdapter(this, notificationHistories);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(mAdapter);

    }

    private void getNotificationData(final Intent intent) {

        if (Cons.isNetworkAvailable(this)) {

            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_NOTIFICATION_DETAIL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonobject = new JSONObject(response);
                                String status = jsonobject.getString("status");
                                if (status.equals("0")) {
                                    noData.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();
                                } else {
                                    JSONArray jsonArray = jsonobject.getJSONArray("noteList");
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject object = jsonArray.getJSONObject(i);
                                        NotificationDetailModel notificationdetailmodel = new NotificationDetailModel();
                                        notificationdetailmodel.setTopic(object.getString("id"));
                                        notificationdetailmodel.setChapter(object.getString("name"));
                                        notificationdetailmodel.setSubject(object.getString("subject_name"));
//                                notificationdetailmodel.setUnit(object.getString("unit_name"));
                                        notificationdetailmodel.setCourse(object.getString("course_name"));
                                        notificationdetailmodel.setUniversity(object.getString("university_name"));
                                        notificationdetailmodel.setDate(object.getString("date"));
                                        notificationHistories.add(notificationdetailmodel);
                                    }

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
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("message", intent.getStringExtra("headername"));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseListRequest, TAG);
        }
        else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }


    }
}