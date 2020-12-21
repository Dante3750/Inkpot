package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 9/26/2017.
 */

public class QuizActivitySubject extends AppCompatActivity implements View.OnClickListener {
    public Button button;
    public String courseId;
    public long Total_time = 0;
    public JSONObject jsonObject;
    public JSONArray jsonarray;
    public int seconds;
    public long l;
    public String subjectId, attemptquestion, percentage, totalcorrect;
    public TextView numbertext, secondtextview, third, noData;
    protected PrefManager pref;
    private HashMap<String, String> userdetail;
    public String timeminute;
    public long startTime_second, startTime_minute, startTime_hour = 0;
    public String timeminutenew = "00:00:00";
    public int secondnewsnine = 0;
    public int secondforth;
    public int secondnews;
    public String secondnewsforth = "00:00:00";
    public String f;
    public String newvalue, semesterId, courseIdNew, name, option1, option2, option3, option4, correctoptionid;
    public TextView notext;
    public LinearLayout linearlayout;
    private ProgressDialog progressDialog;
//    public ProgressBar progressBar;
    public static int i = 0;
    public int count;
    public  String countnew;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        setTitle("Start Quiz");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        noData = (TextView) findViewById(R.id.tv_free_test_text);
        numbertext = (TextView) findViewById(R.id.numbertext);
        secondtextview = (TextView) findViewById(R.id.second);
        third = (TextView) findViewById(R.id.third);
        linearlayout = (LinearLayout) findViewById(R.id.linear10);
        notext = (TextView) findViewById(R.id.notetext);
        pref = new PrefManager(this);
        userdetail = pref.getUserDetail();
        Intent in = getIntent();
        courseId = in.getStringExtra("courseId");
        subjectId = in.getStringExtra("subjectId");
        semesterId = in.getStringExtra("semesterId");
        courseIdNew = in.getStringExtra("courseIdNew");
        button = (Button) findViewById(R.id.startquizbtn);
//        progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        progressBar.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        hitApi();
        hitApiForCount();
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
        button.setOnClickListener(this);
    }


    private void hitApiForCount() {

        if (Cons.isNetworkAvailable(this)) {

            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_SUBJECTQUESTION_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response.toString());
//                        progressBar.setVisibility(View.GONE);
                            try {
                                jsonObject = new JSONObject(response);
                                jsonarray = jsonObject.getJSONArray("data");
                                count = jsonarray.length();
                                countnew = String.valueOf(count);
                                third.setText(countnew);
                                JSONObject object = jsonarray.getJSONObject(i);
                                name = object.getString("question_name");
                                option1 = object.getString("option1");
                                option2 = object.getString("option2");
                                option3 = object.getString("option3");
                                option4 = object.getString("option4");
                                correctoptionid = object.getString("subjecanswer");
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
                    params.put("studentId", userdetail.get(Cons.KEY_USER_ID));
                    params.put("type", "subject");
                    params.put("helpId", subjectId);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseListRequest, "vgh");
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }








    private void hitApiForTimer() {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest timer = new StringRequest(Request.Method.POST, Cons.URL_SUBJECTQUESTION_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response.toString());
                            try {
                                jsonObject = new JSONObject(response);
                                jsonarray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    timeminute = object.getString("time_minute");
                                    String timeSplit[] = timeminute.split(":");
                                    seconds = Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);
                                    String timesplitNewsfifth[] = secondnewsforth.split(":");
                                    secondnews = Integer.parseInt(timesplitNewsfifth[0]) * 60 * 60 + Integer.parseInt(timesplitNewsfifth[1]) * 60 + Integer.parseInt(timesplitNewsfifth[2]);
                                    secondnews = seconds + secondnews;
                                    secondnewsnine = secondnews + secondnewsnine;
                                }
                                final int MINUTES_IN_AN_HOUR = 60;
                                final int SECONDS_IN_A_MINUTE = 60;
                                int seconds = secondnewsnine % SECONDS_IN_A_MINUTE;
                                int totalMinutes = secondnewsnine / SECONDS_IN_A_MINUTE;
                                int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
                                int hours = totalMinutes / MINUTES_IN_AN_HOUR;
                                f = hours + ":" + minutes + ":" + seconds;
                                newvalue = String.format("%02d:%02d:%02d", secondnewsnine / 3600,
                                        (secondnewsnine % 3600) / 60, (secondnewsnine % 60));
                                String[] parts = newvalue.split(":", 3);
                                String string1 = parts[0];
                                String string2 = parts[1];
                                String string3 = parts[2];
                                startTime_second = Long.parseLong(string3);
                                startTime_minute = 60 * (Long.parseLong(string2));
                                startTime_hour = 3600 * (Long.parseLong(string1));
                                Total_time = (startTime_second + startTime_minute + startTime_hour) * 1000;
                                secondtextview.setText("" + newvalue);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentId", userdetail.get(Cons.KEY_USER_ID));
                    params.put("type", "subject");
                    params.put("helpId", subjectId);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(timer, "vgh");
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }






    private void hitApi() {
        if (Cons.isNetworkAvailable(this)) {

            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_SUBJECTPROGRESS_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response.toString());
                            try {
                                jsonObject = new JSONObject(response);
                                JSONObject jsonObjectnew = jsonObject.getJSONObject("data");
                                attemptquestion = jsonObjectnew.getString("total_attempt_que");
                                percentage = jsonObjectnew.getString("percentage");
                                if (attemptquestion.equals("0")) {
//                                progressBar.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    linearlayout.setVisibility(View.GONE);
                                    noData.setVisibility(View.VISIBLE);
                                } else {
//                                progressBar.setVisibility(View.GONE);
                                    linearlayout.setVisibility(View.VISIBLE);
                                    totalcorrect = jsonObjectnew.getString("total_correct_que");
                                    numbertext.setText("" + attemptquestion);
                                    hitApiForTimer();
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
                    params.put("studentId", userdetail.get(Cons.KEY_USER_ID));
                    params.put("type", "subject");
                    params.put("helpId", subjectId);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseListRequest, "vgh");
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), QuestionActivitySubject.class);
        i.putExtra("courseId", courseId);
        i.putExtra("subjectId", subjectId);
        i.putExtra("totaltime", String.valueOf(Total_time));

        i.putExtra("countnew",countnew);



        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}