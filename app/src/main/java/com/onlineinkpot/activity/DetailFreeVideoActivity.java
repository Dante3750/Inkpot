package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.onlineinkpot.adapters.FreeVideoAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.FreeSubMod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailFreeVideoActivity extends AppCompatActivity {
    private static final String TAG = "DetailFreeVideoActivity";
    private List<FreeSubMod> videoList;
    private RecyclerView recyclerView;
    private FreeVideoAdapter mAdapter;
    private PrefManager pref;
    private TextView noData;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_free_video_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        videoList = new ArrayList<>();
        progressDialog = new ProgressDialog(DetailFreeVideoActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Free Video");
        collapsingToolbarLayout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setBackgroundColor(Color.parseColor("#" + pref.getCourseIcon()));
        Intent in = getIntent();
        noData = (TextView) findViewById(R.id.tv_free_video_text);
        recyclerView = (RecyclerView) findViewById(R.id.rv_freevideo);
        mAdapter = new FreeVideoAdapter(this, videoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareFreeData(in.getStringExtra("ChapterId"), in.getStringExtra("SubId"));
        recyclerView.addOnItemTouchListener(new Cons.RecyclerTouchListener(this, recyclerView, new Cons.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FreeSubMod freevideo = videoList.get(position);
                Intent i = new Intent(DetailFreeVideoActivity.this, DetailPlayerActivity.class);
                i.putExtra("url", Cons.URL_VIDEO + freevideo.getVname());
                i.putExtra("topic", freevideo.getVtopicName());
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareFreeData(final String chapterId, final String subId) {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest freevideoRqst = new StringRequest(Request.Method.POST, Cons.URL_FREE_VIDEO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);

                                String status = object.getString("status");
                                if (status.equals("0")) {
                                    progressDialog.dismiss();
                                    noData.setVisibility(View.VISIBLE);
                                } else {
                                    JSONArray array = object.getJSONArray("videoList");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objectdata = array.getJSONObject(i);
                                        FreeSubMod submod = new FreeSubMod();
                                        submod.setVname(objectdata.getString("videoname"));
                                        submod.setVtopicId(objectdata.getString("topicid"));
                                        submod.setVtopicName(objectdata.getString("topicname"));
                                        videoList.add(submod);
                                        progressDialog.dismiss();
                                    }
                                }

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
                    params.put("chapterId", chapterId);
                    params.put("subjectId", subId);
                    Log.d(TAG,"tyyt"+params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(freevideoRqst, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

}
