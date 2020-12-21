package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.QuizChapterAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.ModTestChapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 9/27/2017.
 */

public class QuizChapterActivity extends AppCompatActivity {
    private RecyclerView rv;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private static final String TAG = "ChapterTest";
    public String semId, courseId, unitId;
    public QuizChapterAdapter mAdapter;
    public PrefManager pref;
    private List<ModTestChapter> chapterList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_chapter_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        pref = new PrefManager(this);
        progressDialog = new ProgressDialog(QuizChapterActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Chapter Test");
        collapsingToolbarLayout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setBackgroundColor(Color.parseColor("#" + pref.getCourseIcon()));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent in = getIntent();
        semId = in.getStringExtra("semId");
        courseId = in.getStringExtra("courseId");
        unitId = in.getStringExtra("unitId");
        initializeLayoutContents();
    }

    private void initializeLayoutContents() {
        chapterList = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv_chap_testList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new QuizChapterAdapter(this, chapterList);
        rv.setAdapter(mAdapter);
        getChapterList(semId, courseId, unitId);
        rv.addOnItemTouchListener(new Cons.RecyclerTouchListener(this, rv, new Cons.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModTestChapter testChapter = chapterList.get(position);
                Intent i = new Intent(getApplicationContext(), QuizActivityChapter.class);
                i.putExtra("chapterId", testChapter.getChapterId());
                i.putExtra("chapterName", testChapter.getChapterName());
                i.putExtra("semId", semId);
                i.putExtra("courseId", courseId);
                i.putExtra("unitId", unitId);
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void getChapterList(final String semId, final String courseId, final String unitId) {

        if (Cons.isNetworkAvailable(this)) {

            StringRequest chapterrqst = new StringRequest(Request.Method.POST, Cons.URL_TEST_CHAPTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                Log.d(TAG, "onResponse: " + response);
                                if (status.equals("0")) {
                                } else {
                                    JSONArray array = object.getJSONArray("chapterList");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objectdata = array.getJSONObject(i);
                                        ModTestChapter testmod = new ModTestChapter();
                                        testmod.setChapterName(objectdata.getString("chaptername"));
                                        testmod.setChapterId(objectdata.getString("chapter_id"));
                                        chapterList.add(testmod);
                                    }
                                    mAdapter.notifyDataSetChanged();
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
                    Log.d(TAG, "Error : " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("courseId", courseId);
                    params.put("semId", semId);
                    params.put("unitId", unitId);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(chapterrqst, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
   }