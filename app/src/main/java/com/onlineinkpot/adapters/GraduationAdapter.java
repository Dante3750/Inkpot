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
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.helper.PicassoClient;
import com.onlineinkpot.models.Course;

import java.util.List;

public class GraduationAdapter extends RecyclerView.Adapter<GraduationAdapter.MyViewHolder> {

    private List<Course> courseList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCourseName, tvCourseDetail;
        private ImageView ivIcon;

        public MyViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvCourseName = (TextView) view.findViewById(R.id.tv_course_name);
            tvCourseDetail = (TextView) view.findViewById(R.id.tv_course_details);
        }
    }

    public GraduationAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_graduation, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvCourseDetail.setText(course.getCourseSub() + " Subjects, " + course.getCourseUnit() + " Units");
        PicassoClient.downloadImage(context, Cons.URL_IMAGE + course.getCourseIcon(), holder.ivIcon);
        holder.ivIcon.setColorFilter(Color.parseColor("#" + course.getIconColor()));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}

