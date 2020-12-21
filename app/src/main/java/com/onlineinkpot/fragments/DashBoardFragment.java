package com.onlineinkpot.fragments;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.onlineinkpot.R;
import com.onlineinkpot.activity.ChatUserListActivity;
import com.onlineinkpot.activity.CommingSoon;
import com.onlineinkpot.activity.GraduationActivity;
import com.onlineinkpot.app.PrefManager;


public class DashBoardFragment extends Fragment implements View.OnClickListener {
    CardView cvGraduation,cvMun,cvCorporate;
    FloatingActionButton floatbutton;
    public PrefManager pref;
    public DashBoardFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, null, false);
        initializeLayoutContents(view);
        pref = new PrefManager(getContext());
        return view;
    }

    private void initializeLayoutContents(View view) {
        cvGraduation = (CardView) view.findViewById(R.id.cv_graduation);
        cvCorporate = (CardView) view.findViewById(R.id.cv_corporate);
        cvMun= (CardView) view.findViewById(R.id.cv_mun);
        floatbutton = (FloatingActionButton)view.findViewById(R.id.fab);
        cvCorporate.setOnClickListener(this);
        cvMun.setOnClickListener(this);
        cvGraduation.setOnClickListener(this);
        floatbutton.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_graduation:
                Intent i = new Intent(getActivity(), GraduationActivity.class);
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                getActivity().startActivity(i, bundle1);
                break;
            case R.id.cv_mun:
                Intent j = new Intent(getActivity(), CommingSoon.class);
                Bundle bundle2 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                getActivity().startActivity(j, bundle2);
                break;
            case R.id.cv_corporate:
                Intent k = new Intent(getActivity(), CommingSoon.class);
                Bundle bundle3 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                getActivity().startActivity(k, bundle3);
                break;
            case R.id.fab:
                Intent fb = new Intent(getActivity(),ChatUserListActivity.class);
                getActivity().startActivity(fb);
                break;
        }

    }
}

