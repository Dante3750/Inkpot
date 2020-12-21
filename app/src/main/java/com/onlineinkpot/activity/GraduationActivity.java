package com.onlineinkpot.activity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.GraduationAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraduationActivity extends AppCompatActivity {
    public PrefManager pref;
    private static final String TAG = "GraduationActivity";
    public List<Course> courseList;
    private RecyclerView rv;
    private GraduationAdapter mAdapter;
    public static String allColor;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graduation_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new PrefManager(this);
        toolbar.setTitleTextColor(Color.BLACK);
        progressDialog = new ProgressDialog(GraduationActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Graduation");
        collapsingToolbarLayout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initializeLayoutContents();
    }

    private void initializeLayoutContents() {
        courseList = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv_courseList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new GraduationAdapter(this, courseList);
        rv.setAdapter(mAdapter);
        rv.addOnItemTouchListener(new Cons.RecyclerTouchListener(this, rv, new Cons.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Course c = courseList.get(position);
                Intent i = new Intent(getApplicationContext(), DetailActivitySemester.class);
                allColor = c.getIconColor();
                pref.setCourseIcon(allColor);
                pref.setCourseId(c.getCourseId());
                pref.setCourseName(c.getCourseName());
                i.putExtra("courseId", c.getCourseId());
                i.putExtra("courseTitle", c.getCourseName());
                i.putExtra("courseIcon", c.getCourseIcon());
                i.putExtra("courseColor", c.getIconColor());
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        getCourseData();
    }

    private void getCourseData() {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_GETCOURSE_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    Course course = new Course();
                                    course.setCourseUnit(object.getString("unitCount"));
                                    course.setCourseId(object.getString("courseId"));
                                    course.setCourseName(object.getString("courseName"));
                                    course.setCourseSub(object.getString("subCount"));
                                    course.setCourseIcon(object.getString("icon"));
                                    course.setIconColor(object.getString("iconColor"));
                                    courseList.add(course);
                                    String a = course.getCourseId();
                                    Log.d(TAG, "CourseID: " + a);
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
                    params.put("catId", String.valueOf(2));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseListRequest, TAG);
        } else {
            Toast.makeText(this, ""+Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}