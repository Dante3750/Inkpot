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
 * Created by USER on 9/6/2017.
 */


public class UnitTabAdapter implements SectionStateChangeListener {
    // Data list.
    private LinkedHashMap<ModSubject, ArrayList<ModUnit>> subjectDataMap = new LinkedHashMap<>();
    private ArrayList<Object> dataArrayList = new ArrayList<>();
    // Subject map.
    private HashMap<String, ModSubject> subjectMap = new HashMap<>();
    // Unit Map.
    private ChapterListAdapter mUnitAdapter;
    private RecyclerView rvUnit;

    public UnitTabAdapter(Context context, RecyclerView recyclerView, ChildListener childListener) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mUnitAdapter = new ChapterListAdapter(context, dataArrayList, childListener, this);
        recyclerView.setAdapter(mUnitAdapter);
        this.rvUnit = recyclerView;
    }

    public void addSubject(String id, String subject, ArrayList<ModUnit> modUnits) {
        ModSubject newSubject;
        subjectMap.put(subject, (newSubject = new ModSubject(id, subject, null, null, null, null)));
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