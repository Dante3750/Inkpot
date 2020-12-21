package com.onlineinkpot.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.ModTestSubject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizSubjectActivity extends AppCompatActivity {
    private static final String TAG = "QuizSubjectActivity";
    public String semesterId, courseId;
    private RecyclerView rv;
    public List<ModTestSubject> testSubjectList;
    public com.onlineinkpot.adapters.QuizSubjectActivity mAdapter;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    public PrefManager pref;
    private ProgressDialog progressDialog;
    public static boolean click = true;
    public TextView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_subject_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        pref = new PrefManager(this);
        progressDialog = new ProgressDialog(QuizSubjectActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        noData = (TextView) findViewById(R.id.tv_test_no_data);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Subject Test");
        collapsingToolbarLayout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setBackgroundColor(Color.parseColor("#" + pref.getCourseIcon()));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent in = getIntent();
        semesterId = in.getStringExtra("semesterId");
        courseId = in.getStringExtra("courseId");
        Log.d(TAG, "onCreate: " + semesterId + courseId);
        initializeLayoutContents();
    }

    private void initializeLayoutContents() {
        testSubjectList = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv_sub_testList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new com.onlineinkpot.adapters.QuizSubjectActivity(this, testSubjectList);
        rv.setAdapter(mAdapter);
        getSubjectList(courseId, semesterId);
        rv.addOnItemTouchListener(new Cons.RecyclerTouchListener(this, rv, new Cons.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModTestSubject subject = testSubjectList.get(position);
                if (pref.getSubjectIdArray() != null) {
                    if (pref.getSubjectIdArray().contains(subject.getSubId())) {
                        Intent in = new Intent(getApplicationContext(), QuizActivitySubject.class);
                        in.putExtra("courseId", subject.getSubName());
                        in.putExtra("subjectId", subject.getSubId());
                        in.putExtra("semesterId", semesterId);
                        in.putExtra("courseIdNew", courseId);
                        startActivity(in);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                QuizSubjectActivity.this).create();
                        alertDialog.setTitle("Not Purchased");
                        alertDialog.setMessage("Subject has to be purchased for the test to be available");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            QuizSubjectActivity.this).create();
                    alertDialog.setTitle("Not Purchased");
                    alertDialog.setMessage("Subject has to be purchased for the test to be available");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void getSubjectList(final String courseId, final String semesterId) {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest testData = new StringRequest(Request.Method.POST, Cons.URL_TESTSUBJECT_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                if (status.equals("0")) {
                                } else {
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objectdata = array.getJSONObject(i);
                                        ModTestSubject testmod = new ModTestSubject();
                                        testmod.setSubName(objectdata.getString("sub_displayname"));
                                        testmod.setSubId(objectdata.getString("n_subjectID"));
                                        if (pref.getSubjectIdArray() != null) {
                                            if (pref.getSubjectIdArray().contains(testmod.getSubId())) {
                                                testSubjectList.add(testmod);
                                            }
                                        }
                                    }
                                    if (testSubjectList.size() == 0) {
                                        progressDialog.onStart();
                                        noData.setVisibility(View.VISIBLE);
                                    }
                                    mAdapter.notifyDataSetChanged();
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
                    params.put("courseId", courseId);
                    params.put("semesterId", semesterId);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(testData, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }



}