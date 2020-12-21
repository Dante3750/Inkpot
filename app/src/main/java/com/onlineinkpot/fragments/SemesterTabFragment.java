package com.onlineinkpot.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.onlineinkpot.R;
import com.onlineinkpot.activity.DetailActivitySemester;
import com.onlineinkpot.activity.DetailActivityUnit;
import com.onlineinkpot.activity.QuizSubjectActivity;
import com.onlineinkpot.adapters.SemesterTabAdapter;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.ChildListener;
import com.onlineinkpot.helper.VerticalSpaceItemDecoration;
import com.onlineinkpot.models.ModSubject;
import com.onlineinkpot.models.ModUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 8/26/2017.
 */
public class SemesterTabFragment extends Fragment implements ChildListener, View.OnClickListener {
    private static final String TAG = "SemesterTabFragment";
    private RecyclerView rvSemester;
    private SemesterTabFragment activity;
    private SemesterTabAdapter mAdapter;
    private int pos;
    public PrefManager pref;
    public String courseid;
    public RelativeLayout testButton;
    public static String semId;

    public SemesterTabFragment() {
    }

    public static SemesterTabFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("semesPos", pos);
        Log.d(TAG, "newInstance: " + pos);
        SemesterTabFragment fragment = new SemesterTabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = SemesterTabFragment.this;
        if (getArguments() == null)
            getActivity().finish();
        pos = getArguments().getInt("semesPos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semester_tab, container, false);
        pref = new PrefManager(getContext());
        rvSemester = (RecyclerView) view.findViewById(R.id.rv_semester);
        testButton = (RelativeLayout) view.findViewById(R.id.rl_test_button);
        rvSemester.setBackgroundColor(Color.parseColor("#20" + pref.getCourseIcon()));
        rvSemester.addItemDecoration(new VerticalSpaceItemDecoration(20));
        mAdapter = new SemesterTabAdapter(getContext(), rvSemester, activity);
        testButton.setOnClickListener(this);

        //fetch data from model for fragments.

        List<ModSubject> subList = DetailActivitySemester.semList.get(pos).getSubjectList();
        pref.setSemId(DetailActivitySemester.semList.get(pos).getSemesterId());
        Log.d(TAG, "onClick: " + pref.getSemId());
        if (subList.size() > 0) {
            for (int i = 0; i < subList.size(); i++) {
                ModSubject subject = subList.get(i);
                courseid = subject.getCourseId();
                ArrayList<ModUnit> arrayList = new ArrayList<>();
                List<ModUnit> unitList = subList.get(i).getUnitList();
                if (unitList.size() > 0) {
                    for (int j = 0; j < unitList.size(); j++) {
                        arrayList.add(unitList.get(j));
                    }
                }
                mAdapter.addSubject(subject.getSubId(), subject.getSubName(), subject.getCourseId(), subject.getSemId(), arrayList);
            }
        }
        mAdapter.notifyData();
        return view;
    }

    @Override
    public void itemClicked(ModUnit unit) {
        Intent i = new Intent(getContext(), DetailActivityUnit.class);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
        int id = unit.getsId();
        String idnew = String.valueOf(id);
        i.putExtra("uSubId", unit.getuSubId());
        ActivityCompat.startActivity(getActivity(), i, bundle);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_test_button:
                Intent in = new Intent(getActivity(), QuizSubjectActivity.class);
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                in.putExtra("semesterId", DetailActivitySemester.semList.get(pos).getSemesterId());
                in.putExtra("courseId", courseid);
                ActivityCompat.startActivity(getActivity(), in, bundle1);
                break;

        }
    }
}


