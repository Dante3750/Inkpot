package com.onlineinkpot.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.adapters.QuizSolutionAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.AndroidOpenDbHelper;
import com.onlineinkpot.models.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.onlineinkpot.helper.AndroidOpenDbHelper.STUDENT_ID;
import static com.onlineinkpot.helper.AndroidOpenDbHelper.TABLE_NAME_QUESTIONNEW;

/**
 * Created by USER on 9/25/2017.
 */

public class QuizSolutionActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public PrefManager pref;
    public ArrayList<Course> courseList;
    public RecyclerView rv;
    private static final String TAG = "ResultActivity";
    public JSONObject jsonObject;
    public JSONArray jsonarray;
    public String studentquestionlist, correctanswerList, studentid, znew;
    public static ArrayList<String> arr, arr2;
    public int k = 0;
    private HashMap<String, String> userdetail;
    public String subjectId;
    public static AndroidOpenDbHelper androidopendbhelper;
    public static SQLiteDatabase sqlitedatabase;
    public Cursor cursor;
    public QuizSolutionAdapter resultnewadapter;
    public ArrayList<String> StudentID_ArrayList = new ArrayList<String>();

    public ArrayList<String> ID_ArrayList = new ArrayList<String>();


    public ArrayList<String> QUESTIONID_ArrayList = new ArrayList<String>();
    public ArrayList<String> ANSWERID_ArrayList = new ArrayList<String>();
    public ArrayList<String> CORRECTANSWERVALUE_ArrayList = new ArrayList<String>();
    public ArrayList<String> OPTION1_ArrayList = new ArrayList<String>();
    public ArrayList<String> OPTION2_ArrayList = new ArrayList<String>();
    public ArrayList<String> OPTION3_ArrayList = new ArrayList<String>();
    public ArrayList<String> OPTION4_ArrayList = new ArrayList<String>();
    public ArrayList<String> STUDENTANSWER_ArrayList = new ArrayList<String>();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_solution_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Solutions");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        pref = new PrefManager(this);
        userdetail = pref.getUserDetail();
        Bundle b = getIntent().getExtras();
        subjectId = b.getString("subjectId");

        arr2 = b.getStringArrayList("correctanswerarraylist");

        arr = b.getStringArrayList("studentquestionarraylist");


         /*for(String s:arr)
        {
            for(String s1:arr2 )
            {
                if(s.equals(s1) )
                {

                    if (s1.equals("1"))
                    {
             QuizSolutionAdapter.optiontextA.setBackgroundColor(Color.parseColor("#FF0000"));
                        //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                    }
                    else if (s1.equals("2"))
                    {
                        //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                        QuizSolutionAdapter.optiontextB.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                    else if (s1.equals("3"))
                    {
                        QuizSolutionAdapter.optiontextC.setBackgroundColor(Color.parseColor("#FF0000"));
                        //Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                    }
                    else if (s1.equals("4"))
                    {
                        // Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                        QuizSolutionAdapter.optiontextD.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), " not found" + s, Toast.LENGTH_SHORT).show();
                }
                }

              }*/


        if (b != null) {
            int i = 0;
            Log.d("studentanswerlist", arr.toString());
            }

        initializeLayoutContents();
        androidopendbhelper = new AndroidOpenDbHelper(this);
        showSQliteDbData();
        studentid = userdetail.get(Cons.KEY_USER_ID);
        //showResult();
    }


    private void showResult() {
    /*    for (int i = 0; i <= arr.size() ; i++) {
            for (int j = 0; i <= arr2.size() ; j++) {
                if (arr.get(i).equals(arr2.get(j))) {
                   // Toast.makeText(getApplicationContext(), "correct", Toast.LENGTH_SHORT).show();
                //}
                    if (arr2.equals("1"))
                    {
                        QuizSolutionAdapter.optiontextA.setBackgroundColor(Color.parseColor("#FF0000"));
                        //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                    }
                    else if (s1.equals("2"))
                    {
                        //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                        QuizSolutionAdapter.optiontextB.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                    else if (s1.equals("3"))
                    {
                        QuizSolutionAdapter.optiontextC.setBackgroundColor(Color.parseColor("#FF0000"));
                        //Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                    }
                    else if (s1.equals("4"))
                    {
                        // Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                        QuizSolutionAdapter.optiontextD.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), " not found" + s, Toast.LENGTH_SHORT).show();
                }
            }
*/



   /*  for(String s:arr)
        {
            for(String s1:arr2 )
            {
                if(s.equals(s1) )
                {

                    if (s1.equals("1"))
                    {
             QuizSolutionAdapter.MyViewHolder.optiontextA.setBackgroundColor(Color.parseColor("#FF0000"));
                        //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                    }
                    else if (s1.equals("2"))
                    {
                        //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                        QuizSolutionAdapter.MyViewHolder.optiontextB.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                    else if (s1.equals("3"))
                    {
                        QuizSolutionAdapter.MyViewHolder.optiontextC.setBackgroundColor(Color.parseColor("#FF0000"));
                        //Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                    }
                    else if (s1.equals("4"))
                    {
                        // Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                        QuizSolutionAdapter.MyViewHolder.optiontextD.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), " not found" + s, Toast.LENGTH_SHORT).show();
                }
            }

        }*/
         }



    public void delete(String rowId) {
        sqlitedatabase = androidopendbhelper.getWritableDatabase();
        sqlitedatabase.delete(TABLE_NAME_QUESTIONNEW, STUDENT_ID + "=?", new String[]{rowId});
    }

    public void showSQliteDbData() {
        sqlitedatabase = androidopendbhelper.getWritableDatabase();
        cursor = sqlitedatabase.rawQuery("SELECT * FROM  question_tablenew", null);
        StudentID_ArrayList.clear();

        ID_ArrayList.clear();


        QUESTIONID_ArrayList.clear();
        ANSWERID_ArrayList.clear();
        STUDENTANSWER_ArrayList.clear();


       CORRECTANSWERVALUE_ArrayList.clear();



        OPTION1_ArrayList.clear();
        OPTION2_ArrayList.clear();
        OPTION3_ArrayList.clear();
        OPTION4_ArrayList.clear();

        if (cursor.moveToFirst()) {
            do {
                StudentID_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.STUDENT_ID)));

                ID_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.ID)));

                QUESTIONID_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.QUESTION)));
                ANSWERID_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.CORRECT_ANSWER)));

                STUDENTANSWER_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.STUDENT_ANSWER)));

                 CORRECTANSWERVALUE_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.CORRECT_ANSWERVALUE)));






                OPTION1_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.OPTION1)));
                OPTION2_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.OPTION2)));
                OPTION3_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.OPTION3)));
                OPTION4_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.OPTION4)));




            }
            while (cursor.moveToNext());
        }
        resultnewadapter = new QuizSolutionAdapter(QuizSolutionActivity.this,
                StudentID_ArrayList,
                ID_ArrayList,
                QUESTIONID_ArrayList,
                ANSWERID_ArrayList,

                STUDENTANSWER_ArrayList,

               CORRECTANSWERVALUE_ArrayList,


                OPTION1_ArrayList,
                OPTION2_ArrayList,
                OPTION3_ArrayList,
                OPTION4_ArrayList
        );
        rv.setAdapter(resultnewadapter);
      //  resultnewadapter.setHasStableIds(true);


        cursor.close();


    }

    private void initializeLayoutContents() {
        courseList = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv_courseList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void getCourseData() {
        StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_SUBJECTQUESTION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response.toString());
                        try {
                            jsonObject = new JSONObject(response);
                            jsonarray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                Course course = new Course();
                                course.setQuestionName(object.getString("question_name"));
                                course.setOption1(object.getString("option1"));
                                course.setOption2(object.getString("option2"));
                                course.setOption3(object.getString("option3"));
                                course.setOption4(object.getString("option4"));
                                course.setSubjectAnswer(object.getString("subjecanswer"));
                                course.setQuestionId(object.getString("q_map_id"));
                                courseList.add(course);
                            }

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
        AppController.getInstance().addToRequestQueue(courseListRequest, TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(QuizSolutionActivity.this, MainActivity.class);
        startActivity(in);
        delete(studentid);
    }
}