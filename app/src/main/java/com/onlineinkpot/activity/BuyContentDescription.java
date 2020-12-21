package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuyContentDescription extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ContentDetailDescription";
    public TextView tvVideo, tvPdf, tvMcq;
    public LinearLayout videoButton;
    public String subjectid, courseid, semesterid;
    private ProgressDialog progressDialog;
    public ArrayList<String> semesterarrayList = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_content_description);
       //semesterarrayList =
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(BuyContentDescription.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        tvVideo = (TextView) findViewById(R.id.tv_count_video);
        tvPdf = (TextView) findViewById(R.id.tv_count_pdf);
        tvMcq = (TextView) findViewById(R.id.tv_count_MCQ);
        videoButton = (LinearLayout) findViewById(R.id.video_btn);
        videoButton.setOnClickListener(this);
        getContentDetail(getIntent());

        Intent in = getIntent();
        subjectid = in.getStringExtra("subjectId");
        courseid = in.getStringExtra("courseid");
        semesterid = in.getStringExtra("semesterid");

        semesterarrayList.add(semesterid);

      //  SemesterListModel list = new SemesterListModel();

         //list.ad

      //  list.setSemesterList(semesterarrayList);

      //Toast.makeText(this, "semesterlist"+list.getSemesterList(), Toast.LENGTH_SHORT).show();



    }

    private void getContentDetail(final Intent intent) {
        StringRequest courseListRequest = new StringRequest(Request.Method.POST, Cons.URL_SUBJECT_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONObject objecttwo = object.getJSONObject("details");
                            tvVideo.setText(objecttwo.getString(Cons.KEY_COUNT_VIDEOS));
                            tvPdf.setText(objecttwo.getString(Cons.KEY_COUNT_PDF));
                            tvMcq.setText(objecttwo.getString(Cons.KEY_COUNT_MCQ));

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
               // Log.d(TAG, "Error : " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subjectid", intent.getStringExtra("subjectId"));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(courseListRequest, TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_btn:
                Intent i = new Intent(BuyContentDescription.this, BuyContentActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("subjectId", subjectid);
                i.putExtra("courseid", courseid);
                 i.putExtra("semesterid", semesterid);


              //  i.putExtra("semesterid", semesterarrayList);




                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }
}