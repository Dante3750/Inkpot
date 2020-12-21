package com.onlineinkpot.fragments;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onlineinkpot.R;
import com.onlineinkpot.activity.DetailActivityUnit;
import com.onlineinkpot.activity.DetailPlayerActivity;
import com.onlineinkpot.activity.MainActivity;
import com.onlineinkpot.activity.QuizChapterActivity;
import com.onlineinkpot.adapters.UnitTabAdapter;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.ChildListener;
import com.onlineinkpot.helper.VerticalSpaceItemDecoration;
import com.onlineinkpot.models.ModSubject;
import com.onlineinkpot.models.ModUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 9/5/2017.
 */

public class UnitTabFragment extends Fragment implements ChildListener, View.OnClickListener {

    private static final String TAG = "SemesterTabFragment";
    private RecyclerView rvSemester;
    private UnitTabFragment activity;
    private UnitTabAdapter mAdapter;
    private int pos;
    RelativeLayout button;
    PrefManager pref;

    public UnitTabFragment() {
    }

    public static UnitTabFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("semesPos", pos);
        Log.d(TAG, "newInstance: " + pos);
        UnitTabFragment fragment = new UnitTabFragment();
        fragment.setArguments(bundle);
        return fragment;
        //return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = UnitTabFragment.this;
        if (getArguments() == null)
            getActivity().finish();

        pos = getArguments().getInt("semesPos");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_tab, container, false);
        pref = new PrefManager(getContext());
        button = (RelativeLayout) view.findViewById(R.id.taketestbtn);
        button.setOnClickListener(this);
        rvSemester = (RecyclerView) view.findViewById(R.id.rv_semester);
        rvSemester.setBackgroundColor(Color.parseColor("#20" + pref.getCourseIcon()));
        rvSemester.addItemDecoration(new VerticalSpaceItemDecoration(20));
        mAdapter = new UnitTabAdapter(getContext(), rvSemester, activity);

        // fetch data from model for fragments.
        List<ModSubject> subList = DetailActivityUnit.semList.get(pos).getSubjectList();
        if (subList.size() > 0) {
            for (int i = 0; i < subList.size(); i++) {
                ModSubject subject = subList.get(i);
                ArrayList<ModUnit> arrayList = new ArrayList<>();
                List<ModUnit> unitList = subList.get(i).getUnitList();
                if (unitList.size() > 0) {
                    for (int j = 0; j < unitList.size(); j++) {
                        arrayList.add(unitList.get(j));
                    }
                }
                mAdapter.addSubject(subject.getSubId(), subject.getSubName(), arrayList);
            }
        }
        mAdapter.notifyData();

        return view;
    }


    @Override
    public void itemClicked(ModUnit unit) {
        String uid = unit.getuSubId().toString();
        String topicName = unit.getuName().toString();

      //Toast.makeText(getActivity(), "u clicked", Toast.LENGTH_SHORT).show();

        boolean test = uid.endsWith("mp4");
        if (test)
        {
            Intent i = new Intent(getContext(), DetailPlayerActivity.class);
            String imageicon = unit.getuSubId().toString();
            String url = Cons.URL_VIDEO + imageicon;
            i.putExtra("topic", topicName);
            i.putExtra("url",url);

            i.putExtra("uid",uid);

            startActivity(i);
         }

        else {

         /*String imageicon = unit.getuSubId().toString();
            String url = Cons.URL_VIDEO + imageicon;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "application/pdf");
            PackageManager pm = getActivity().getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
            if (activities.size() > 0) {
                startActivity(intent);
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                       getActivity()).create();
                // alertDialog.setTitle("Not Purchased");
                alertDialog.setMessage("No pdf Available");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in = new Intent(getActivity(), MainActivity.class);
                        //in.putExtra("unitid", uid);
                        startActivity(in);
                    }
                });
                alertDialog.show();
               // Toast.makeText(getActivity(), "cannot open", Toast.LENGTH_SHORT).show();
            }
           }*/
           //File pdfFile = new File(Environment
          //                   .getExternalStorageDirectory(), "Case Study.pdf");
          //   try {

                  /* if (pdfFile.exists()) {
                    Uri path = Uri.fromFile(pdfFile);
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);
                    objIntent.setDataAndType(path, "application/pdf");
                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(objIntent);*/

           try
           {
            String imageicon = unit.getuSubId().toString();
            String url = Cons.URL_VIDEO + imageicon;


               if (url.endsWith(".pdf")) {

                   Intent intent = new Intent(Intent.ACTION_VIEW);
                   intent.setDataAndType(Uri.parse(url), "application/pdf");

                   startActivity(intent);

                 }

                else {

                   //Toast.makeText(getActivity(), "cannot open", Toast.LENGTH_SHORT).show();

                   AlertDialog alertDialog = new AlertDialog.Builder(
                           getActivity()).create();
                   // alertDialog.setTitle("Not Purchased");
                   alertDialog.setMessage("No PDF Uploaded Yet");
                   alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           Intent in = new Intent(getActivity(), MainActivity.class);
                           //in.putExtra("unitid", uid);
                           startActivity(in);
                       }
                   });
                   alertDialog.show();
                   }
                 }
            catch (ActivityNotFoundException e) {

                Toast.makeText(getActivity(),

                        "No Viewer Application Found", Toast.LENGTH_SHORT)

                        .show();

            }






        }

       }


    @Override
    public void onClick(View v) {
        if (pref.getSubjectIdArray() != null) {
            if (pref.getSubjectIdArray().contains(pref.getCurrentSub())) {
                Intent in = new Intent(getActivity(), QuizChapterActivity.class);
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                in.putExtra("courseId", pref.getCourseId());
                in.putExtra("semId", DetailActivityUnit.semList.get(pos).getResemid());
                in.putExtra("unitId", DetailActivityUnit.semList.get(pos).getSemesterId());
                getActivity().startActivity(in, bundle1);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        getContext()).create();
                alertDialog.setTitle("Not Purchased");
                alertDialog.setMessage("Please Purchased First For Test");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    getContext()).create();
            alertDialog.setTitle("Not Purchased");
            alertDialog.setMessage("Please Purchased First For Test");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
           }
    }
}
