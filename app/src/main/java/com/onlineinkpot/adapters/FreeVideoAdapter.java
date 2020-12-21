package com.onlineinkpot.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlineinkpot.R;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.FreeSubMod;

import java.util.List;

/**
 * Created by john on 9/18/2017.
 */

public class FreeVideoAdapter extends RecyclerView.Adapter<FreeVideoAdapter.MyViewHolder> {
    private List<FreeSubMod> videoList;
    private PrefManager pref;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vname, vid, Vtname;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            Vtname = (TextView) view.findViewById(R.id.tv_topic_name);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.rLfreeVideo);
        }
    }

    public FreeVideoAdapter(Context context, List<FreeSubMod> videoList) {
        this.videoList = videoList;
        this.context = context;
        pref = new PrefManager(context.getApplicationContext());
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_free_video, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FreeSubMod freeSubMod = videoList.get(position);
        holder.Vtname.setText(freeSubMod.getVtopicName());
        holder.relativeLayout.setBackgroundColor(Color.parseColor("#" + pref.getCourseIcon()));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
