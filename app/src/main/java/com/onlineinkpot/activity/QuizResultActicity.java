package com.onlineinkpot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.onlineinkpot.R;

import java.util.ArrayList;

/**
 * Created by anytime on 9/30/2017.
 */

public class QuizResultActicity extends AppCompatActivity implements View.OnClickListener {
    private TextView questiontextview, correctanswertext, wronganswertext, totalmarkstext;
    public static ArrayList<String> arr;
    public static ArrayList<String> arr2;
    private String znew, count, subjectId, correctanswer, incorrectanswer;
    private Button button;
    int k=0;
    int l=0;
    String countnew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_result_acticity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        correctanswertext = (TextView) findViewById(R.id.correctanswertext);
        wronganswertext = (TextView) findViewById(R.id.wronganswertext);
        totalmarkstext = (TextView) findViewById(R.id.totalmarkstext);
        questiontextview = (TextView) findViewById(R.id.questiontext);
        button = (Button) findViewById(R.id.btttn);

        Bundle b = getIntent().getExtras();
        subjectId = b.getString("subjectId");
        correctanswer = b.getString("correctanswer");
        incorrectanswer = b.getString("incorrectanswer");
        countnew = b.getString("countnew");

        count = b.getString("count");
        arr = b.getStringArrayList("studentquestionarraylist");
        arr2 = b.getStringArrayList("correctanswerarraylist");


      for (int i = 0; i <arr.size() ; i++) {

       if (arr.get(i).equals(arr2.get(i))) {

           k = k + 1;

          //Toast.makeText(getApplicationContext(), "correct" + k, Toast.LENGTH_SHORT).show();
           Log.d("correct",String.valueOf(k));

           correctanswertext.setText("" + String.valueOf(k));
           String correcans = correctanswertext.getText().toString();
           totalmarkstext.setText("" + String.valueOf(k));
           questiontextview.setText("" + count);
           String totalques = questiontextview.getText().toString();
           int wrongans = Integer.parseInt(totalques) - Integer.parseInt(correcans);
           String wrongansvalue = String.valueOf(wrongans);
           wronganswertext.setText("" + wrongansvalue);
         }


      else {

           if (arr.size() < arr2.size()) {
               //correctanswertext.setText("0");
             //  l = l + 1;
               Log.d("incorrect", String.valueOf(l));
               //  wronganswertext.setText("" + String.valueOf(l));

               // String wrongans = wronganswertext.getText().toString();
               //totalmarkstext.setText("" + String.valueOf(k));
               //     questiontextview.setText("" + count);
               //     String totalques = questiontextview.getText().toString();
               //   int correctanswer = Integer.parseInt(totalques) - Integer.parseInt(wrongans);
               questiontextview.setText("" + count);
               wronganswertext.setText("" +countnew);


               totalmarkstext.setText("" + countnew);


               //String wrongans = wronganswertext.getText().toString();


               String totalmarks = totalmarkstext.getText().toString();

               String wrongans = wronganswertext.getText().toString();


               int correctanswervalue = Integer.parseInt(totalmarks) - Integer.parseInt(wrongans);

               //String correctanswervalue = String.valueOf(correctanswer);

               correctanswertext.setText("" + String.valueOf(correctanswervalue));


           }

           else{

               l = l + 1;

              wronganswertext.setText("" + String.valueOf(l));

             String wrongans = wronganswertext.getText().toString();
               totalmarkstext.setText("" + String.valueOf(k));
                    questiontextview.setText("" + count);
                    String totalques = questiontextview.getText().toString();
                  int correctanswer = Integer.parseInt(totalques) - Integer.parseInt(wrongans);
               correctanswertext.setText("" + String.valueOf(correctanswer));

           }
          }

         }
       Log.d("arr",arr.toString());
        Log.d("arr2",arr2.toString());


        //if (b != null)
        //{
         // correctanswertext.setText("" + correctanswer);

            button.setOnClickListener(this);
         //}
         }




    @Override
    public void onClick(View view) {

        if (correctanswertext.getText().toString().equals("0")) {

            //Toast.makeText(this, "correctanswer 0", Toast.LENGTH_SHORT).show();

            //Intent in = new Intent(QuizResultActicity.this, QuizSolutionActivity.class);

            Intent in = new Intent(QuizResultActicity.this, QuizSolutionSecondActivity.class);

            in.putExtra("studentquestionarraylist", arr);
            in.putExtra("subjectId", subjectId);
            in.putExtra("correctanswerarraylist", arr2);

            startActivity(in);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);






        }
        else {

            Intent in = new Intent(QuizResultActicity.this, QuizSolutionActivity.class);
            in.putExtra("studentquestionarraylist", arr);
            in.putExtra("subjectId", subjectId);
            in.putExtra("correctanswerarraylist", arr2);

            startActivity(in);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(QuizResultActicity.this, MainActivity.class);
        startActivity(in);
    }
    }