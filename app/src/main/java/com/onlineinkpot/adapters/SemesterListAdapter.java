package com.onlineinkpot.adapters;

import android.content.Context;
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
import android.widget.ToggleButton;


import com.onlineinkpot.R;
import com.onlineinkpot.activity.BuyContentDescription;
import com.onlineinkpot.activity.DetailActivitySemester;
import com.onlineinkpot.activity.QuizActivityUnit;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.ChildListener;
import com.onlineinkpot.helper.SectionStateChangeListener;
import com.onlineinkpot.models.ModSubject;
import com.onlineinkpot.models.ModUnit;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Created by USER on 8/28/2017.
 */

public class SemesterListAdapter extends RecyclerView.Adapter<SemesterListAdapter.UnitViewHolder> {
    private static final String TAG = "SemesterListAdapter";

    // Data Array.
    private ArrayList<Object> mDataList;
    private Context mContext;
    PrefManager pref;

    // Listener.
    private ChildListener mChildListener;
    private SectionStateChangeListener mSectionStateChangeListener;

    // View type.
    private static final int VIEW_TYPE_SUBJECT = R.layout.list_item_subject;
    private static final int VIEW_TYPE_UNIT = R.layout.list_item_unit;

    public SemesterListAdapter(Context c, ArrayList<Object> dataList, ChildListener childListener, SectionStateChangeListener sectionStateChangeListener) {
        mContext = c;
        mChildListener = childListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataList = dataList;
        pref = new PrefManager(c);
    }

    private boolean isSection(int pos) {
        return mDataList.get(pos) instanceof ModSubject;
    }

    @Override
    public UnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UnitViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }

    protected static class UnitViewHolder extends RecyclerView.ViewHolder {
        LinearLayout unitButton;
        // common.
        View view;
        int viewType;

        // for subject.
        RelativeLayout rlSub;
        LinearLayout llSubject;
        ToggleButton tbArrow;
        ImageView subCart;
        TextView subName, subNum;

        // for unit.
        ImageView unitThumb;
        TextView unitName, unitNumber;

        public UnitViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            this.view = itemView;

            if (viewType == VIEW_TYPE_UNIT) {
                unitNumber = (TextView) view.findViewById(R.id.iv_unit_number);
                unitName = (TextView) view.findViewById(R.id.tv_unit_name);
                unitButton = (LinearLayout) view.findViewById(R.id.tv_unittest_tbtn);
            } else {
                rlSub = (RelativeLayout) view.findViewById(R.id.rl_sub);
                llSubject = (LinearLayout) view.findViewById(R.id.ll_sub_item);
                subCart = (ImageView) view.findViewById(R.id.iv_cart_sub);
                subName = (TextView) view.findViewById(R.id.tv_sub_name);
                subNum = (TextView) view.findViewById(R.id.tv_sub_num);
                tbArrow = (ToggleButton) view.findViewById(R.id.tv_sub_arrow_down);
            }
        }
    }

    @Override
    public void onBindViewHolder(final UnitViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_SUBJECT:
                final ModSubject subject = (ModSubject) mDataList.get(position);

                holder.subName.setText(subject.getSubName());
                holder.subCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(mContext, BuyContentDescription.class);
                        in.putExtra("subjectId", subject.getSubId());
                        in.putExtra("courseid", subject.getCourseId());
                        in.putExtra("semesterid", subject.getSemId());
                        mContext.startActivity(in);
                        ((DetailActivitySemester) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                holder.rlSub.setBackgroundColor(Color.parseColor("#" + pref.getCourseIcon()));
                if (pref.getSubjectIdArray() != null) {
                    String subs[] = pref.getSubjectIdArray().split(Pattern.quote(","));
                    for (int i = 0; i < subs.length; i++) {
                        if (subject.getSubId().equals(subs[i])) {
                            holder.subCart.setVisibility(View.GONE);
                        } else {

                        }
                    }
                }

                holder.llSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (subject.isExpanded) {
                            mSectionStateChangeListener.onSectionStateChanged(subject, false);
                            holder.tbArrow.setChecked(false);
                        } else {
                            mSectionStateChangeListener.onSectionStateChanged(subject, true);
                            holder.tbArrow.setChecked(true);
                        }
                    }
                });

                holder.tbArrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSectionStateChangeListener.onSectionStateChanged(subject, isChecked);
                        holder.tbArrow.setChecked(subject.isExpanded);

                    }
                });

                holder.tbArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                break;
            case VIEW_TYPE_UNIT:
                final ModUnit unit = (ModUnit) mDataList.get(position);
                holder.unitName.setText(unit.getsId() + ". " + unit.getuName());
                holder.unitButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        String unitid = unit.getuId().toString();
                        Intent in = new Intent(mContext, QuizActivityUnit.class);

                        in.putExtra("unitid", unitid);
                        mContext.startActivity(in);
                        ((DetailActivitySemester) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pref.setCurrentSub(unit.getuSubId());
                        mChildListener.itemClicked(unit);
                        ((DetailActivitySemester) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
            return VIEW_TYPE_SUBJECT;
        } else {
            return VIEW_TYPE_UNIT;
        }
    }
}
