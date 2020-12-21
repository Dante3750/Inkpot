package com.onlineinkpot.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.onlineinkpot.R;
import com.onlineinkpot.activity.DetailFreeVideoActivity;
import com.onlineinkpot.activity.DetailActivityUnit;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.ChildListener;
import com.onlineinkpot.helper.SectionStateChangeListener;
import com.onlineinkpot.models.ModSubject;
import com.onlineinkpot.models.ModUnit;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by USER on 9/7/2017.
 */

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.UnitViewHolder> {
    private static final String TAG = "ChapterListAdapter";
    private ArrayList<Object> mDataList;
    private Context mContext;
    private ChildListener mChildListener;
    PrefManager pref;
    private boolean a = false;
    private SectionStateChangeListener mSectionStateChangeListener;
    private static final int VIEW_TYPE_CHAPTER = R.layout.list_chapter_row;
    private static final int VIEW_TYPE_TOPIC = R.layout.list_item_topic;

    public ChapterListAdapter(Context c, ArrayList<Object> dataList, ChildListener childListener, UnitTabAdapter sectionStateChangeListener) {
        mContext = c;
        mChildListener = childListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataList = dataList;
        pref = new PrefManager(c.getApplicationContext());
    }

    private boolean isSection(int pos) {
        return mDataList.get(pos) instanceof ModSubject;
    }

    @Override
    public ChapterListAdapter.UnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChapterListAdapter.UnitViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }

    protected static class UnitViewHolder extends RecyclerView.ViewHolder {
        View view;
        int viewType;
        //for Chapter
        RelativeLayout rlSub;
        LinearLayout llSubject;
        ToggleButton tbArrow;
        TextView ChapterName, ivFreeVideo, ChapterNum;
        // for unit.
        ImageView topicThumb;
        TextView topicName;

        public UnitViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            this.view = itemView;
            if (viewType == VIEW_TYPE_TOPIC) {
                topicThumb = (ImageView) view.findViewById(R.id.iv_topic_thumb);
                topicName = (TextView) view.findViewById(R.id.tv_topic_name);
            } else {
                ivFreeVideo = (TextView) view.findViewById(R.id.tv_free_video);
                rlSub = (RelativeLayout) view.findViewById(R.id.rl_topic);
                llSubject = (LinearLayout) view.findViewById(R.id.ll_topic_item);
                ChapterName = (TextView) view.findViewById(R.id.tv_topic_name);
                ChapterNum = (TextView) view.findViewById(R.id.tv_topic_num);
                tbArrow = (ToggleButton) view.findViewById(R.id.tv_topic_arrow_down);
            }
        }
    }

    @Override
    public void onBindViewHolder(final ChapterListAdapter.UnitViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_CHAPTER:
                final ModSubject subject = (ModSubject) mDataList.get(position);
                holder.ChapterName.setText(subject.getSubName());
                holder.ChapterNum.setText("Chapter");
                holder.rlSub.setBackgroundColor(Color.parseColor("#" + pref.getCourseIcon()));
                holder.llSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, ""+subject.getSubId(), Toast.LENGTH_SHORT).show();

                        if (subject.isExpanded) {
                            mSectionStateChangeListener.onSectionStateChanged(subject, false);
                            holder.tbArrow.setChecked(false);
                        } else {
                            if (pref.getSubjectIdArray() != null) {
                                String subs[] = pref.getSubjectIdArray().split(Pattern.quote(","));
                                for (int i = 0; i < subs.length; i++) {

                                    if (pref.getCurrentSub().equals(subs[i])) {
                                        a = true;
                                    }
                                }
                            }
                            mSectionStateChangeListener.onSectionStateChanged(subject, true);
                            holder.tbArrow.setChecked(true);
                        }
                    }
                });
                holder.tbArrow.setChecked(subject.isExpanded);
                holder.tbArrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSectionStateChangeListener.onSectionStateChanged(subject, isChecked);
                        if (pref.getSubjectIdArray() != null) {
                            String subs[] = pref.getSubjectIdArray().split(Pattern.quote(","));
                            for (int i = 0; i < subs.length; i++) {

                                if (pref.getCurrentSub().equals(subs[i])) {
                                    a = true;
                                }
                            }
                        }

                    }
                });

                holder.ivFreeVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, DetailFreeVideoActivity.class);
                        i.putExtra("ChapterId", subject.getSubId());
                        i.putExtra("SubId", pref.getCurrentSub());
                        ((DetailActivityUnit) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        mContext.startActivity(i);

                    }
                });
                break;

            case VIEW_TYPE_TOPIC:
                final ModUnit unit = (ModUnit) mDataList.get(position);
                holder.topicName.setText(unit.getuName());
                holder.topicThumb.setColorFilter(Color.parseColor("#" + pref.getCourseIcon()));
                String imageicon = unit.getuSubId().toString();
                boolean test = imageicon.endsWith("mp4");

                if (!a) {
                    holder.topicThumb.setBackgroundResource(R.drawable.ic_lock);
                } else {

                    if (test) {

                        holder.topicThumb.setBackgroundResource(R.drawable.play);

                    } else {
                        holder.topicThumb.setBackgroundResource(R.drawable.pdf);

                    }
                }

                holder.topicThumb.setColorFilter(Color.parseColor("#" + pref.getCourseIcon()));
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!a) {

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    mContext).create();
                            alertDialog.setTitle("Not Purchased");
                            alertDialog.setMessage("Please purchase first to view the content.");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        } else {
                            mChildListener.itemClicked(unit);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position)) {
            return VIEW_TYPE_CHAPTER;
        } else {
            return VIEW_TYPE_TOPIC;
        }
    }
}
