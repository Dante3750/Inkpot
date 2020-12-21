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
import com.onlineinkpot.models.ModTestChapter;

import java.util.List;

public class QuizChapterAdapter extends RecyclerView.Adapter<QuizChapterAdapter.MyViewHolder> {
    private List<ModTestChapter> chapterList;
    public Context context;
    public PrefManager pref;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ChapterName;
        public ImageView subCart;
        public RelativeLayout rlTestChapter;

        public MyViewHolder(View view) {
            super(view);
            ChapterName = (TextView) itemView.findViewById(R.id.tv_test_chapter_name);
            subCart = (ImageView) itemView.findViewById(R.id.iv_cart_sub);
            rlTestChapter = (RelativeLayout) itemView.findViewById(R.id.rl_test_chapter);
        }
    }

    public QuizChapterAdapter(Context context, List<ModTestChapter> courseList) {
        this.context = context;
        this.chapterList = courseList;
        pref = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_quiz_chapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModTestChapter testChapter = chapterList.get(position);
        holder.ChapterName.setText(testChapter.getChapterName());
        holder.rlTestChapter.setBackgroundColor(Color.parseColor("#" + pref.getCourseColor()));
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

}

