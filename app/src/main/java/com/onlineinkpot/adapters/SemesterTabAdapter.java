package com.onlineinkpot.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.onlineinkpot.helper.ChildListener;
import com.onlineinkpot.helper.SectionStateChangeListener;
import com.onlineinkpot.models.ModSubject;
import com.onlineinkpot.models.ModUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by USER on 8/28/2017.
 */

public class SemesterTabAdapter implements SectionStateChangeListener {
    private LinkedHashMap<ModSubject, ArrayList<ModUnit>> subjectDataMap = new LinkedHashMap<>();
    private ArrayList<Object> dataArrayList = new ArrayList<>();
    // Subject map.
    private HashMap<String, ModSubject> subjectMap = new HashMap<>();
    // Unit Map.
    private SemesterListAdapter mUnitAdapter;
    private RecyclerView rvUnit;

    public SemesterTabAdapter(Context context, RecyclerView recyclerView, ChildListener childListener) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mUnitAdapter = new SemesterListAdapter(context, dataArrayList, childListener, this);
        recyclerView.setAdapter(mUnitAdapter);
        this.rvUnit = recyclerView;
    }

    public void addSubject(String id, String subject, String courseid, String semesterid, ArrayList<ModUnit> modUnits) {
        ModSubject newSubject;
        subjectMap.put(subject, (newSubject = new ModSubject(id, subject, null, semesterid, courseid, null)));
        subjectDataMap.put(newSubject, modUnits);
    }

    public void notifyData() {
        generateDataList();
        mUnitAdapter.notifyDataSetChanged();
    }

    private void generateDataList() {
        dataArrayList.clear();
        for (Map.Entry<ModSubject, ArrayList<ModUnit>> entry : subjectDataMap.entrySet()) {
            ModSubject key;
            dataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                dataArrayList.addAll(entry.getValue());
        }
    }

    @Override
    public void onSectionStateChanged(ModSubject modSubject, boolean isOpen) {
        if (!rvUnit.isComputingLayout()) {
            modSubject.isExpanded = isOpen;
            notifyData();
        }
    }
}
