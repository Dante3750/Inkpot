package com.onlineinkpot.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.onlineinkpot.R;
import com.onlineinkpot.activity.DetailActivityUnit;
import com.onlineinkpot.adapters.TopicAdapter;
import com.onlineinkpot.helper.VerticalSpaceItemDecoration;
import com.onlineinkpot.models.ModTopic;

import java.util.ArrayList;
import java.util.List;

public class ChapterTabFragment extends Fragment {
    private static final String TAG = "ChapterTabFragment";
    private int pos, unitPos;
    private List<ModTopic> topList = new ArrayList<>();
    private RecyclerView rv;
    private TopicAdapter mAdapter;

    public ChapterTabFragment() {
    }

    public static ChapterTabFragment newInstance(int pos, int unitPos) {
        ChapterTabFragment fragment = new ChapterTabFragment();
        Bundle args = new Bundle();
        args.putInt("chapPos", pos);
        args.putInt("unitPos", unitPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("chapPos");
        unitPos = getArguments().getInt("unitPos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chapter_detail, container, false);

        topList = DetailActivityUnit.unitLit.get(unitPos).getChapList().get(pos).getTopList();
        rv = (RecyclerView) view.findViewById(R.id.rv_top);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new VerticalSpaceItemDecoration(20));
        mAdapter = new TopicAdapter(getContext(), topList);
        rv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return view;
    }
}
