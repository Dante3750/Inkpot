package com.onlineinkpot.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlineinkpot.R;
import com.onlineinkpot.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 9/29/2017.
 */

public class QuizSolutionAdapter extends RecyclerView.Adapter<QuizSolutionAdapter.MyViewHolder> {
    public ImageView imageView;
    private List<Course> courseList;
    public Context context;
    public static String studentresultanswer, correctanswernew, correctanswer;
    public String item,correctanswervaluenews;
    public ArrayList<String> studentid;
    public ArrayList<String> id;

    public ArrayList<String> questionid;
    public ArrayList<String> answerid;
    public ArrayList<String> option1;
    public ArrayList<String> option2;
    public ArrayList<String> option3;
    public ArrayList<String> option4;
    public ArrayList<String> studentanswerid;
    public ArrayList<String> correctanswervalue;


    public  static String studentcorrectanswer,showcorrectanswer;
    public int j = 0;
    public int k = 0;
    View itemView;
   //public static TextView  tvCourseName,tvCourseDetail,optiontextA,optiontextB,optiontextC, optiontextD;



    public
    QuizSolutionAdapter(
            Context context2,
            ArrayList<String> studentidnew,

            ArrayList<String> idnew,
            ArrayList<String> questionidnew,
            ArrayList<String> correctansweridnew,
            ArrayList<String> studentansweridnew,

            ArrayList<String> correctanswervaluenew,

            ArrayList<String> OPTION1_ArrayList, ArrayList<String> OPTION2_ArrayList, ArrayList<String> OPTION3_ArrayList, ArrayList<String> OPTION4_ArrayList) {
        this.context = context2;
        this.studentid = studentidnew;
        this.id=idnew;
        this.questionid = questionidnew;
        this.answerid = correctansweridnew;

        this.studentanswerid = studentansweridnew;

        this.correctanswervalue = correctanswervaluenew;

        this.option1 = OPTION1_ArrayList;
        this.option2 = OPTION2_ArrayList;
        this.option3 = OPTION3_ArrayList;
        this.option4 = OPTION4_ArrayList;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
      //public static View optiontextA;
    public   static   TextView   tvCourseName, questionnumbertext,tvCourseDetail, optiontextA, optiontextB, optiontextC, optiontextD;
         ImageView ivIcon;
    public  static  TextView tvshowcorrectanswer,tvstudentcorrectanswer, tvcorrectanswervalue,counttextview;

        public MyViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.imageiconnew);
            tvCourseName = (TextView) view.findViewById(R.id.questiontext2);
            questionnumbertext = (TextView) view.findViewById(R.id.questionnumbertext);



            optiontextA = (TextView) view.findViewById(R.id.optiontextA);
            optiontextB = (TextView) view.findViewById(R.id.optiontextB);
            optiontextC = (TextView) view.findViewById(R.id.optiontextC);
            optiontextD = (TextView) view.findViewById(R.id.optiontextD);
            tvshowcorrectanswer = (TextView) view.findViewById(R.id.showcorrectanswer);

            tvstudentcorrectanswer = (TextView) view.findViewById(R.id.studentcorrectanswer);

            tvcorrectanswervalue = (TextView) view.findViewById(R.id.tv_ans);


            counttextview = (TextView) view.findViewById(R.id.counttext);
        }
    }

    public QuizSolutionAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_quiz_solution, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

       //final AcademicStatus mItems=this.data.get(position);

        holder.tvCourseName.setText(questionid.get(position));
        correctanswernew = answerid.get(position).toString();
        correctanswervaluenews=correctanswervalue.get(position).toString();



        //correctanswernew=correctanswervalue.get(position).toString();

        holder.optiontextA.setText(option1.get(position));
        holder.optiontextB.setText(option2.get(position));
        holder.optiontextC.setText(option3.get(position));
        holder.optiontextD.setText(option4.get(position));

        holder.questionnumbertext.setText(id.get(position));

        //String optiontextAvalue = holder.optiontextA.getText().toString();
        // String optiontextBvalue = holder.optiontextB.getText().toString();
        // String optiontextCvalue = holder.optiontextC.getText().toString();
        // String optiontextDvalue = holder.optiontextD.getText().toString();

        holder.tvstudentcorrectanswer.setText(studentanswerid.get(position));


        holder.tvshowcorrectanswer.setText("" + correctanswernew);

        holder.tvcorrectanswervalue.setText("" + correctanswervaluenews);




        studentcorrectanswer = holder.tvstudentcorrectanswer.getText().toString();

        showcorrectanswer = holder.tvshowcorrectanswer.getText().toString();


     if (studentcorrectanswer.equals(showcorrectanswer)) {

            if (studentcorrectanswer.equals("1")) {
                holder.optiontextA.setBackgroundColor(Color.parseColor("#008000"));


            }


         else     if (studentcorrectanswer.equals("2")) {

                holder.optiontextB.setBackgroundColor(Color.parseColor("#008000"));


            }


      else      if (studentcorrectanswer.equals("3")) {

                holder.optiontextC.setBackgroundColor(Color.parseColor("#008000"));

            }


      else      if (studentcorrectanswer.equals("4")) {

                holder.optiontextD.setBackgroundColor(Color.parseColor("#008000"));

            }


     }




      else
          if (studentcorrectanswer.equals("1")) {
         holder.optiontextA.setBackgroundColor(Color.parseColor("#FF0000"));


     }


     else     if (studentcorrectanswer.equals("2")) {

         holder.optiontextB.setBackgroundColor(Color.parseColor("#FF0000"));


     }


     else      if (studentcorrectanswer.equals("3")) {

         holder.optiontextC.setBackgroundColor(Color.parseColor("#FF0000"));

     }


     else      if (studentcorrectanswer.equals("4")) {

         holder.optiontextD.setBackgroundColor(Color.parseColor("#FF0000"));

     }


    }





       /*for(String s: QuizSolutionActivity.arr) {
              for (String s1 : QuizSolutionActivity.arr2) {
                  if (s.equals(s1)) {

                      if (s1.equals("1")) {
                       holder. optiontextA.setBackgroundColor(Color.parseColor("#FF0000"));
                          //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                      }
                      else if (s1.equals("2")) {
                          //  Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                       holder.optiontextB.setBackgroundColor(Color.parseColor("#FF0000"));
                      }
                      else if (s1.equals("3")) {
                         holder.optiontextC.setBackgroundColor(Color.parseColor("#FF0000"));
                          //Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                      }
                      else if (s1.equals("4")) {
                          // Toast.makeText(getApplicationContext(), "found" + s, Toast.LENGTH_SHORT).show();
                          holder.optiontextD.setBackgroundColor(Color.parseColor("#FF0000"));
                      }
                      }
                  else {
                      Toast.makeText(context, " not found" + s, Toast.LENGTH_SHORT).show();
                  }
              }*/









   /* @Override
    public int getItemCount() {
        return  questionid.size();

    }*/

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return studentanswerid.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}

