package com.onlineinkpot.fragments;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.activity.DetailActivitySemester;
import com.onlineinkpot.adapters.FragmentMyCourseAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.MyCourse;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCourseFragment extends Fragment {
    private static final String TAG = "MyCourseFragment";
    public PrefManager pref;
    private RecyclerView rv_course;
    public List<MyCourse> courseList;
    //    List<String> data;
    private FragmentMyCourseAdapter mAdapter;
    private TextView noData;
    private HashMap<String, String> userdetail;
    private ProgressDialog progressDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Course");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mycourse, null, false);
        pref = new PrefManager(getContext());
        userdetail = pref.getUserDetail();
        initializeLayoutContents(view);
        return view;
    }

    private void initializeLayoutContents(View view) {
        noData = (TextView) view.findViewById(R.id.tv_nomycourse);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        courseList = new ArrayList<>();
//        List<String>data=new ArrayList<>();
        rv_course = (RecyclerView) view.findViewById(R.id.rv_course);
        mAdapter = new FragmentMyCourseAdapter(getContext(), courseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_course.setLayoutManager(mLayoutManager);
        rv_course.setAdapter(mAdapter);
        courseListProcess();
        rv_course.addOnItemTouchListener(new Cons.RecyclerTouchListener(getContext(), rv_course, new Cons.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MyCourse myCourse = courseList.get(position);
                Intent i = new Intent(getActivity(), DetailActivitySemester.class);
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                pref.setCourseIcon(myCourse.getMyCourseColor());
                i.putExtra("courseId", myCourse.getMyCourse_id());
                i.putExtra("courseTitle", myCourse.getMyCourseName());
                i.putExtra("courseIcon", myCourse.getMyCourseIcon());
                i.putExtra("courseColor", myCourse.getMyCourseColor());
                getActivity().startActivity(i, bundle1);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void courseListProcess() {
        if (Cons.isNetworkAvailable(getContext())) {
            StringRequest courseReq = new StringRequest(Request.Method.POST, Cons.URL_MY_COURSE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "orderResponse: " + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString("status");
                                if (status.equals("0")) {
                                    noData.setVisibility(View.VISIBLE);
                                } else {
                                    JSONArray array = obj.getJSONArray("mycourse");
                                    for (int i = 0; i < array.length(); i++) {
                                        MyCourse mycourse = new MyCourse();
                                        JSONObject object = array.getJSONObject(i);
                                        mycourse.setMySubject_id(object.getString("subjectid"));
                                        mycourse.setMySubject_name(object.getString("subjectname"));
                                        mycourse.setMySemester_name(object.getString("semestername"));
                                        mycourse.setMySemester_id(object.getString("semesterid"));
                                        mycourse.setMyCourse_id(object.getString("courseid"));
                                        mycourse.setMyCourseIcon(object.getString("courseIcon"));
                                        mycourse.setMyCourseColor(object.getString("courseColor"));
                                        mycourse.setMyCourseName(object.getString("courseName"));
                                        courseList.add(mycourse);
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
                    Log.d(TAG, "Error : " + error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentId", userdetail.get(Cons.KEY_USER_ID));
                    Log.d(TAG, "getParams: " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseReq, TAG);

            courseReq.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
