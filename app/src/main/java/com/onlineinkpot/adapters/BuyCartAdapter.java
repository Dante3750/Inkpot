package com.onlineinkpot.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onlineinkpot.R;
import com.onlineinkpot.activity.BuyCartActivity;
import com.onlineinkpot.models.CartTotalModel;

import java.util.ArrayList;

import static com.onlineinkpot.helper.AndroidOpenDbHelper.SEMESTER_ID;
import static com.onlineinkpot.helper.AndroidOpenDbHelper.SUBJECT_ID;
import static com.onlineinkpot.helper.AndroidOpenDbHelper.TABLE_NAME_BASIC_FIVE;


public class BuyCartAdapter extends BaseAdapter {
    private static final String TAG = "BuyCartAdapter";
    public Context context;
    public ArrayList<String> studentid,  subjectid, subjectname, courseid, totalpriceid, durationid;
    String textidnewid,textidnewidnews;
    String textid;
    static public ArrayList<String> semesterid;

    private float cartTotal = 0;
    public static float totalamount;

    public BuyCartAdapter(
            Context context2,
            ArrayList<String> studentidnew,
            ArrayList<String> semesteridnew,
            ArrayList<String> subjectidnew,
            ArrayList<String> subjectnamenew,
            ArrayList<String> courseidnew,
            ArrayList<String> totalpricenew,
            ArrayList<String> durationnew
    ) {
        this.context = context2;
        this.studentid = studentidnew;
        this.semesterid = semesteridnew;
        this.subjectid = subjectidnew;
        this.subjectname = subjectnamenew;
        this.courseid = courseidnew;
        this.totalpriceid = totalpricenew;
        this.durationid = durationnew;
        CartTotalModel cartTotalT = new CartTotalModel();

        for (int i = 0; i < totalpricenew.size(); i++) {
            cartTotal = cartTotal + Integer.parseInt(totalpriceid.get(i));
            cartTotalT.setCartTotal(cartTotal);
        }
        totalamount = cartTotalT.getCartTotal();
       }


    public int getCount()
    {
        return studentid.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {
        final Holder holder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.adapter_buy_cart, null);
            holder = new Holder();
            holder.textviewid = (TextView) child.findViewById(R.id.text1);
            holder.textviewname = (TextView) child.findViewById(R.id.text2);
            holder.text29= (TextView) child.findViewById(R.id.text29);
            holder.text39= (TextView) child.findViewById(R.id.text39);
            holder.text59= (TextView) child.findViewById(R.id.text59);

            holder.imageview = (ImageView) child.findViewById(R.id.delete);


            child.setTag(holder);
        }

        else {
            holder = (Holder) child.getTag();
        }

        holder.textviewid.setText(subjectname.get(position));
        holder.textviewname.setText(totalpriceid.get(position));
        holder.text29.setText(subjectid.get(position));
        holder.text39.setText(semesterid.get(position));
        holder.text59.setText(courseid.get(position));



        holder.imageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //String textid = holder.textviewid.getText().toString();
                 textid = holder.text29.getText().toString();
                textidnewid = holder.text39.getText().toString();

                textidnewidnews = holder.text59.getText().toString();




                deleteRow(textid,textidnewid,textidnewidnews);

                //ad

            }
                          });

        Log.d(TAG, "kyahai: " + subjectid);
        Log.d(TAG, "kyahaisemesterid: " + semesterid);

       //itemdata(textid,semesterid);
      //Toast.makeText(context, "Semester deleted" + semesterid, Toast.LENGTH_SHORT).show();



        return child;
       }





    public void deleteRow(String rowId,String semesteridnew,String textidnewidnews) {
        BuyCartActivity.sqlitedatabase.delete(TABLE_NAME_BASIC_FIVE, SUBJECT_ID + "=? AND " + SEMESTER_ID + "=?", new String[]{rowId,semesteridnew});



        semesterid.remove(semesteridnew);


        Intent in = new Intent(context, BuyCartActivity.class);
        //in.putExtra("subjectId", rowId);

        in.putExtra("subjectId", rowId);

        in.putExtra("courseid", textidnewidnews);
        in.putExtra("semesterId", semesteridnew);


       //in.putExtra("semesterid", semesteridnew);

        context.startActivity(in);
        ((BuyCartActivity) context).finish();
        ((BuyCartActivity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);



         Toast.makeText(context, "Item deleted" + semesterid, Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
        //itemdata();

     /*   if(semesterid.isEmpty()){

          BuyCartActivity.button.setVisibility(View.INVISIBLE);

        //  Toast.makeText(context, "Array is blank", Toast.LENGTH_SHORT).show();


        }*/




    }


    public void itemdata(String textid,ArrayList<String> semesterid) {



        Intent in = new Intent(context, BuyCartActivity.class);
        in.putExtra("subjectId", textid);
        in.putExtra("courseid", courseid);
        in.putExtra("semesterid", semesterid);

        // Toast.makeText(context, "Semester deleted" + semesterid, Toast.LENGTH_SHORT).show();



        context.startActivity(in);
        ((BuyCartActivity) context).finish();
        ((BuyCartActivity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


     //   Toast.makeText(context, "Semester deleted" + semesterid, Toast.LENGTH_SHORT).show();








    }


    public class Holder {
        TextView textviewid,text29,text59,text39;
        TextView textviewname;
        ImageView imageview;
    }
}