package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.onlineinkpot.models.Course;
import com.onlineinkpot.models.QuestionModel;
import com.onlineinkpot.models.TimerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.onlineinkpot.helper.AndroidOpenDbHelper.STUDENT_ID;
import static com.onlineinkpot.helper.AndroidOpenDbHelper.TABLE_NAME_QUESTIONNEW;

/**
 * Created by USER on 9/22/2017.
 */


public class QuestionActivitySubject extends AppCompatActivity {
    private static final String TAG = "QuestionActivitySubject";
    private ListView listview;
    public List<Course> courseList;
    public RadioGroup radiogroup;
    public RadioButton radiobutton1, radiobutton2, radiobutton3, radiobutton4;
    public TextView text1, textview2, button, timertextvalue, tv_submit_button;
    public String name, option1, option2, option3, option4, timeminutenewsthird, radiobuttonvalue, correctanswer, correctoptionid;
    public JSONObject jsonObject;
    public JSONArray jsonarray;
    public int a, count;
    static int i = 0;
    public String s = "0", timeminutenew = "";
    public RadioButton radioButton;
    private Handler handler = new Handler();
    private Runnable runnable;
    private long milliSeconds = 0;
    public ArrayList<String> studentquestionarrayList, correctanswerarrayList;
    public String timeminute, studentid, semesterid, testid, questionid, subjectid, unitid, chapterid, topicid, ne, me;
    public CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 30 * 1000;
    private final long interval = 1;
    protected PrefManager pref;
    private HashMap<String, String> userdetail;
    public String courseId, subjectId, totaltime, optionanswer, requiredFormat;
    public Long startTime_second, startTime_minute, startTime_hour;
    public TextView totaltimervalue;
    public MyCountDownTimerNew countDownTimerNew;
    private ArrayList questionList;
    public AndroidOpenDbHelper androidopendbhelper;
    public int m = 0, n = 0;
    public int lengthnew;
    static String final_value;
    public static SQLiteDatabase sqlitedatabase;
    public LinearLayout linearlayout;
    public ProgressDialog progressDialog;
    int positionforth;
    public String positionforthnew;
    String value,countnew,timenew;
    private ArrayList timerList;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        //progressBar = (ProgressBar) findViewById(R.id.progressbar);
       //progressBar.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        setTitle("Take Test");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        questionList = new ArrayList();
        Intent in = getIntent();
        courseId = in.getStringExtra("courseId");
        subjectId = in.getStringExtra("subjectId");
        totaltime = in.getStringExtra("totaltime");

        countnew = in.getStringExtra("countnew");
        studentquestionarrayList = new ArrayList<String>();
        correctanswerarrayList = new ArrayList<String>();


        timerList = new ArrayList<String>();

        long l = Long.parseLong(totaltime);
        pref = new PrefManager(this);
        userdetail = pref.getUserDetail();
        androidopendbhelper = new AndroidOpenDbHelper(this);
        studentid = userdetail.get(Cons.KEY_USER_ID);
        delete(studentid);
        button = (TextView) findViewById(R.id.button);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radiobutton1 = (RadioButton) findViewById(R.id.radiobtn);
        radiobutton2 = (RadioButton) findViewById(R.id.radiobtn1);
        radiobutton3 = (RadioButton) findViewById(R.id.radiobtn2);
        radiobutton4 = (RadioButton) findViewById(R.id.radiobtn3);
        timertextvalue = (TextView) findViewById(R.id.timertextvalue);
        tv_submit_button = (TextView) findViewById(R.id.tv_submit_button);
        totaltimervalue = (TextView) findViewById(R.id.totaltimertextvalue);
        int hours = (int) l / 3600;
        int temp = (int) l - hours * 3600;
        int mins = temp / 60;
        temp = temp - mins * 60;
        int secs = temp;
        requiredFormat = hours + ": " + mins + ": " + secs;
        countDownTimerNew = new MyCountDownTimerNew(l, interval);
        countDownTimerNew.start();
        textview2 = (TextView) findViewById(R.id.text2);
        text1 = (TextView) findViewById(R.id.text1);
        i = 0;
        if (i == 0) {
            hitApi();
        }
        else
            {
            hitApiSecond();
        }


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int k = 0;
                if (i < jsonarray.length()) {
                    if (radiogroup.getCheckedRadioButtonId() != -1) {
                        int selectedId = radiogroup.getCheckedRadioButtonId();
                       radioButton = (RadioButton) findViewById(selectedId);

                        int positionnews = radiogroup.indexOfChild(radioButton);
                        int positionnewsecond=1;
                        positionforth = positionnews + positionnewsecond;

                        positionforthnew=String.valueOf(positionforth);

                        radiobuttonvalue = radioButton.getText().toString();
                        radiogroup.clearCheck();
//                        progressBar.setVisibility(View.VISIBLE);
                        linearlayout.setVisibility(View.GONE);
                        hitApiSecond();
                        hitApiForServer();
                        studentquestionarrayList.add("" + positionforth);

                        Log.d("i",String.valueOf(i));


                //        Toast.makeText(getApplicationContext(), "studentquestionarraylist"+studentquestionarrayList.toString(), Toast.LENGTH_SHORT).show();

                //        Toast.makeText(getApplicationContext(), "correctanswerlist"+correctanswerarrayList.toString(), Toast.LENGTH_SHORT).show();

                        //hitSaveQuestion(name, optionanswer, option1, option2, option3, option4);

                   //   Toast.makeText(getApplicationContext(), "optionanswer" + optionanswer, Toast.LENGTH_SHORT).show();

                         value = text1.getText().toString();

                     //   Toast.makeText(QuestionActivitySubject.this, "value"+value, Toast.LENGTH_SHORT).show();

                        hitSaveQuestion(value,name, correctoptionid,positionforthnew,optionanswer, option1, option2, option3, option4);

                        if (radiobuttonvalue.equals(optionanswer))
                        {
                            n = n + 1;
                            ne = String.valueOf(n);
                        }
                        else {
                            m = m + 1;
                            me = String.valueOf(m);
                        }
                        }
                       else
                        {
                        Toast.makeText(getApplicationContext(), "Please select atleast one option", Toast.LENGTH_SHORT).show();
                        }
                        }


                    else
                        {
                    tv_submit_button.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);

                    if (radiogroup.getCheckedRadioButtonId() != -1) {
                        int selectedId = radiogroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selectedId);


                        int positionnews = radiogroup.indexOfChild(radioButton);
                        int positionnewsecond=1;
                        positionforth = positionnews + positionnewsecond;
                        positionforthnew=String.valueOf(positionforth);
                        radiobuttonvalue = radioButton.getText().toString();
                        radiogroup.clearCheck();
                        hitApiSecond();
                        hitApiForServer();
                        studentquestionarrayList.add("" + positionforth);
                         String valuesecond = text1.getText().toString();

                     //   Toast.makeText(QuestionActivitySubject.this, "value"+valuesecond, Toast.LENGTH_SHORT).show();

                        hitSaveQuestion(valuesecond,name, correctoptionid, positionforthnew,optionanswer,option1, option2, option3, option4);

                        if (radiobuttonvalue.equals(optionanswer)) {
                            n = n + 1;
                            ne = String.valueOf(n);
                            Intent intent = new Intent(QuestionActivitySubject.this, QuizResultActicity.class);
                            intent.putExtra("studentquestionarraylist", studentquestionarrayList);

                            intent.putExtra("correctanswerarraylist", correctanswerarrayList);
                            intent.putExtra("subjectId", subjectId);
                            intent.putExtra("correctanswer", ne);
                            intent.putExtra("incorrectanswer", me);
                            intent.putExtra("count", String.valueOf(count));
                            intent.putExtra("countnew", countnew);
                            startActivity(intent);
                        } else {
                            String incorrectans = "0";
                            m = m + 1;
                            me = String.valueOf(m);
                            Intent intent = new Intent(QuestionActivitySubject.this, QuizResultActicity.class);
                            intent.putExtra("studentquestionarraylist", studentquestionarrayList);

                            intent.putExtra("correctanswerarraylist", correctanswerarrayList);
                            intent.putExtra("subjectId", subjectId);
                            intent.putExtra("correctanswer", incorrectans);
                            intent.putExtra("incorrectanswer", me);
                            intent.putExtra("count", String.valueOf(count));
                            intent.putExtra("countnew", countnew);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select atleast one option", Toast.LENGTH_SHORT).show();
                    }
                }
                          }


        });
                 }



    public void delete(String rowId) {
        sqlitedatabase = androidopendbhelper.getWritableDatabase();
        sqlitedatabase.delete(TABLE_NAME_QUESTIONNEW, STUDENT_ID + "=?", new String[]{rowId});
    }


    public void hitSaveQuestion(String questionid,String questionname, String optionanswer, String positionforth, String correctoptionanswer,String option1, String option2, String option3, String option4) {
        QuestionModel questionModel = new QuestionModel();

        questionModel.setStudentId(userdetail.get(Cons.KEY_USER_ID));

        questionModel.setQuestionId(questionid);
        questionModel.setQuestion(questionname);
        questionModel.setCorrectAnswer(optionanswer);
        questionModel.setStudentAnswer(positionforth);

        questionModel.setCorrectAnswerValue(correctoptionanswer);


        questionModel.setOption1(option1);
        questionModel.setOption2(option2);
        questionModel.setOption3(option3);
        questionModel.setOption4(option4);



        questionList.add(questionModel);
        insertQuestion(questionModel);
        androidopendbhelper = new AndroidOpenDbHelper(this);
        }

    public void insertQuestion(QuestionModel basicPriceModelObj) {

        AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);
        SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AndroidOpenDbHelper.STUDENT_ID, basicPriceModelObj.getStudentId());

        contentValues.put(AndroidOpenDbHelper.ID, basicPriceModelObj.getQuestionId());


        contentValues.put(AndroidOpenDbHelper.QUESTION, basicPriceModelObj.getQuestion());
        contentValues.put(AndroidOpenDbHelper.CORRECT_ANSWER, basicPriceModelObj.getCorrectAnswer());

        contentValues.put(AndroidOpenDbHelper.STUDENT_ANSWER, basicPriceModelObj.getStudentAnswer());


        contentValues.put(AndroidOpenDbHelper.CORRECT_ANSWERVALUE, basicPriceModelObj.getCorrectAnswerValue());




        contentValues.put(AndroidOpenDbHelper.OPTION1, basicPriceModelObj.getOption1());
        contentValues.put(AndroidOpenDbHelper.OPTION2, basicPriceModelObj.getOption2());
        contentValues.put(AndroidOpenDbHelper.OPTION3, basicPriceModelObj.getOption3());
        contentValues.put(AndroidOpenDbHelper.OPTION4, basicPriceModelObj.getOption4());

        long affectedColumnId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_QUESTIONNEW, null, contentValues);
        sqliteDatabase.close();
        }

    private void hitApiForServer() {
        StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_QUEST_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {

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
                params.put("studentId", studentid);
                params.put("semesterId", semesterid);
                params.put("subjectId", subjectid);
                params.put("unitId", unitid);
                params.put("chapterId", chapterid);
                params.put("topicId", topicid);
                params.put("testId", testid);
                params.put("quesId", questionid);
                params.put("quesAns", correctanswer);
                params.put("quesAnsId", correctoptionid);
                params.put("studentAns", radiobuttonvalue);
                params.put("studentAnsId", "165");
                params.put("question_time", timeminute);
                params.put("student_total_time", timertextvalue.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(courseListRequest, "vgh");
       }



    private void hitApiForServerSecond() {
        if (Cons.isNetworkAvailable(this)) {

            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_QUEST_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response.toString());
                            try {
                                jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("1")) {
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
                    params.put("studentId", studentid);
                    params.put("semesterId", semesterid);
                    params.put("subjectId", subjectid);
                    params.put("unitId", unitid);
                    params.put("chapterId", chapterid);
                    params.put("topicId", topicid);
                    params.put("testId", testid);
                    params.put("quesId", questionid);
                    params.put("quesAns", correctanswer);
                    params.put("quesAnsId", correctoptionid);
                    params.put("studentAns", "not answered");
                    params.put("studentAnsId", "165");
                    params.put("question_time", timeminute);
                    params.put("student_total_time", timertextvalue.getText().toString());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseListRequest, "vgh");
        }
        else
            {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
        }






    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(Long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            hitApiSecond();
            hitApiForServerSecond();
            ne = "0";
            me = "0";

            String positionsixthnew="0";

            String valuethird = text1.getText().toString();

            Toast.makeText(getApplicationContext(), "value"+valuethird, Toast.LENGTH_SHORT).show();
            hitSaveQuestion(valuethird,name,correctoptionid, positionsixthnew,optionanswer,option1, option2, option3, option4);

            studentquestionarrayList.add("0");
            //Toast.makeText(getApplicationContext(), "studentquestionarraylisttimer"+studentquestionarrayList.toString(), Toast.LENGTH_SHORT).show();

           //Toast.makeText(getApplicationContext(), "correctanswerlisttimer"+correctanswerarrayList.toString(), Toast.LENGTH_SHORT).show();
             }




        @Override
        public void onTick(long millisUntilFinished)
        {
            final_value = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            timertextvalue.setText(final_value);
        }
        }



    public class MyCountDownTimerNew extends CountDownTimer
    {
        public MyCountDownTimerNew(Long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            hitApiSecond();

            hitApiForServerSecond();
            studentquestionarrayList.add("0");

        }

        @Override
        public void onTick(long millisUntilFinished) {
            String final_value = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            totaltimervalue.setText(final_value);
            String totalnewvalue = totaltimervalue.getText().toString();
            if (totalnewvalue.equals("00:00:00")) {
                String z = "0";
                Intent in = new Intent(QuestionActivitySubject.this, QuizResultActicity.class);
                in.putExtra("studentquestionarraylist", studentquestionarrayList);
                in.putExtra("correctanswerarraylist", correctanswerarrayList);
                in.putExtra("subjectId", subjectId);
                in.putExtra("correctanswer", ne);
                in.putExtra("incorrectanswer", me);
                in.putExtra("count", String.valueOf(count));
                in.putExtra("countnew", countnew);
                startActivity(in);
            }
        }
    }

    @Override
    protected void onStop() {
        countDownTimer.cancel();
        countDownTimerNew.cancel();
        super.onStop();
    }

    private void hitApi() {
        if (Cons.isNetworkAvailable(this)) {

            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_SUBJECTQUESTION_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response.toString());
//                        progressBar.setVisibility(View.GONE);
                            linearlayout.setVisibility(View.VISIBLE);
                            try {
                                jsonObject = new JSONObject(response);
                                jsonarray = jsonObject.getJSONArray("data");
                                count = jsonarray.length();
                                for(int i=0;i<jsonarray.length();i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    name = object.getString("question_name");
                                    option1 = object.getString("option1");
                                    option2 = object.getString("option2");
                                    option3 = object.getString("option3");
                                    option4 = object.getString("option4");

                                    correctoptionid = object.getString("subjecanswer");

                                    correctanswer = "option" + correctoptionid;


                                    if (correctoptionid.equals("1")) {
                                        optionanswer = object.getString("option1");
                                    } else if (correctoptionid.equals("2")) {
                                        optionanswer = object.getString("option2");
                                    } else if (correctoptionid.equals("3")) {
                                        optionanswer = object.getString("option3");
                                    } else if (correctoptionid.equals("4")) {
                                        optionanswer = object.getString("option4");
                                    }

                                    studentid = object.getString("student_id");
                                    subjectid = object.getString("subject_id");
                                    testid = object.getString("q_map_id");
                                    questionid = object.getString("q_ans_id");
                                    semesterid = object.getString("semst_id");
                                    unitid = object.getString("n_unitID");
                                    chapterid = object.getString("n_chapterID");
                                    topicid = object.getString("n_topicID");
                                    timeminute = object.getString("time_minute");


                                    TimerModel timerModel = new TimerModel();
                                    timerModel.setTime(timeminute);

                                    timerList.add(timeminute);

                                    Log.d("timerminute", timerList.toString());
                                    timenew = timerList.get(0).toString();

                                }




                                if (timerHasStarted) {
                                    countDownTimer.cancel();
                                    timerHasStarted = false;
                                }
                            //    timertextvalue.setText("" + timeminute);
                                timertextvalue.setText("" + timenew);



                              //  String[] parts = timeminute.split(":", 3);

                                String[] parts = timenew.split(":", 3);



                                String string1 = parts[0];
                                String string2 = parts[1];
                                String string3 = parts[2];
                                startTime_second = Long.parseLong(string3);
                                startTime_minute = 60 * (Long.parseLong(string2));
                                startTime_hour = 3600 * (Long.parseLong(string1));
                                long Total_time = (startTime_second + startTime_minute + startTime_hour) * 1000;
                                countDownTimer = new MyCountDownTimer(Total_time, interval);
                                if (!timerHasStarted) {
                                    countDownTimer.start();
                                    timerHasStarted = true;
                                }
                                textview2.setText("" + name);
                                radiobutton1.setText("" + option1);
                                radiobutton2.setText("" + option2);
                                radiobutton3.setText("" + option3);
                                radiobutton4.setText("" + option4);
                                i++;
                                text1.setText("" + i + ".");
                                //correctanswerarrayList.add("" + optionanswer);

                                correctanswerarrayList.add("" + correctoptionid);


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






    private void hitApiSecond() {
        courseList = new ArrayList<>();
        if (Cons.isNetworkAvailable(this)) {

            StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_SUBJECTQUESTION_LIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response.toString());
                         //progressBar.setVisibility(View.GONE);
                            linearlayout.setVisibility(View.VISIBLE);
                            try {
                                jsonObject = new JSONObject(response);
                                jsonarray = jsonObject.getJSONArray("data");
                                JSONObject object = jsonarray.getJSONObject(i);
                                name = object.getString("question_name");
                                option1 = object.getString("option1");
                                option2 = object.getString("option2");
                                option3 = object.getString("option3");
                                option4 = object.getString("option4");
                                correctoptionid = object.getString("subjecanswer");
                                correctanswer = "option" + correctoptionid;
                                if (correctoptionid.equals("1")) {
                                    optionanswer = object.getString("option1");
                                } else if (correctoptionid.equals("2")) {
                                    optionanswer = object.getString("option2");
                                } else if (correctoptionid.equals("3")) {
                                    optionanswer = object.getString("option3");
                                } else if (correctoptionid.equals("4")) {
                                    optionanswer = object.getString("option4");
                                }
                                studentid = object.getString("student_id");
                                subjectid = object.getString("subject_id");
                                testid = object.getString("q_map_id");
                                questionid = object.getString("q_ans_id");
                                semesterid = object.getString("semst_id");
                                unitid = object.getString("n_unitID");
                                chapterid = object.getString("n_chapterID");
                                topicid = object.getString("n_topicID");
                                //timeminute = object.getString("time_minute");


                                timenew=timerList.get(i).toString();


                                Log.d("timerapihitsecond",timenew);


                                if (timerHasStarted) {
                                    countDownTimer.cancel();
                                    timerHasStarted = false;
                                }

                                //timertextvalue.setText("" + timeminute);

                                timertextvalue.setText("" + timenew);


                                String[] parts = timenew.split(":", 3);


                                String string1 = parts[0];
                                String string2 = parts[1];
                                String string3 = parts[2];
                                startTime_second = Long.parseLong(string3);
                                startTime_minute = 60 * (Long.parseLong(string2));
                                startTime_hour = 3600 * (Long.parseLong(string1));
                                long Total_time = (startTime_second + startTime_minute + startTime_hour) * 1000;
                                countDownTimer = new MyCountDownTimer(Total_time, interval);


                                if (!timerHasStarted) {
                                    countDownTimer.start();
                                    timerHasStarted = true;
                                }

                                textview2.setText("" + name);
                                radiobutton1.setText("" + option1);
                                radiobutton2.setText("" + option2);
                                radiobutton3.setText("" + option3);
                                radiobutton4.setText("" + option4);
                                i++;
                                text1.setText("" + i + ".");
                                //   correctanswerarrayList.add("" + optionanswer);

                                correctanswerarrayList.add("" + correctoptionid);
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
    public void onBackPressed() {
        super.onBackPressed();
        i = 0;
    }
}