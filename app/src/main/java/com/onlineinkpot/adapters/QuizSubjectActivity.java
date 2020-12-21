package com.onlineinkpot.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlineinkpot.R;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.ModTestSubject;

import java.util.List;


public class QuizSubjectActivity extends RecyclerView.Adapter<QuizSubjectActivity.MyViewHolder> {
    public static List<ModTestSubject> testSubjects;
    public Context context;
    public PrefManager pref;

    public QuizSubjectActivity(Context context, List<ModTestSubject> testSubjects) {
        this.context = context;
        this.testSubjects = testSubjects;
        pref = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_quiz_subject, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModTestSubject testSubject = testSubjects.get(position);
        holder.SubjectName.setText(testSubject.getSubName());
        holder.rlTestSub.setBackgroundColor(Color.parseColor("#" + pref.getCourseColor()));
        holder.rlTestSub.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return testSubjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView SubjectName;
        public ImageView subCart;
        public RelativeLayout rlTestSub;
        public TextView noData;

        public MyViewHolder(View itemView) {
            super(itemView);
            SubjectName = (TextView) itemView.findViewById(R.id.tv_sub_name);
            rlTestSub = (RelativeLayout) itemView.findViewById(R.id.rl_sub);
            subCart = (ImageView) itemView.findViewById(R.id.iv_cart_sub);
        }
    }
}
