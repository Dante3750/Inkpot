package com.onlineinkpot.fragments;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.activity.NotificationDetailActivity;
import com.onlineinkpot.adapters.FragmentNotificationAdapter;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.helper.AndroidOpenDbHelper;
import com.onlineinkpot.models.Notification;
import com.onlineinkpot.models.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private static final String TAG = "NotificationFragment";
    public PrefManager pref;
    private ProgressDialog progressDialog;
    public List<Notification> notificationList;
    private RecyclerView rv;
    private TextView noData;
    private FragmentNotificationAdapter mAdapter;
    public AndroidOpenDbHelper androidopendbhelper;
    String header, topic, chapter, unit, subject, semester, course, college, university, date;
    //ArrayList<String> notificationlistnew;
    ArrayList<String> HEADER_ArrayList = new ArrayList<String>();
    ArrayList<String> FLAG_ArrayList = new ArrayList<String>();
    String flag = "0";
    public static SQLiteDatabase sqliteDatabase;
    Cursor cursor;
    private ArrayList notificationListNew;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Notification");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_notification, null, false);
        initializeLayoutContents(view);
        pref = new PrefManager(getContext());
        return view;
    }

    private void initializeLayoutContents(View view) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        notificationList = new ArrayList<>();
        notificationListNew = new ArrayList();
        noData = (TextView) view.findViewById(R.id.tv_no_notification_text);
        rv = (RecyclerView) view.findViewById(R.id.rv_notification);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addOnItemTouchListener(new Cons.RecyclerTouchListener(getContext(), rv, new Cons.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                NotificationModel order = (NotificationModel) notificationListNew.get(position);
                Intent i = new Intent(getActivity(), NotificationDetailActivity.class);
                String headername = order.getHeader().toString();
                i.putExtra("headername", headername);
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.push_left_in, R.anim.push_left_out).toBundle();
                getActivity().startActivity(i, bundle1);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareNotificationData();
    }

    public void hitSaveNotification(String header, String flag) {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setHeader(header);
        notificationModel.setFlag(flag);
        notificationListNew.add(notificationModel);
        insertNotification(notificationModel);
    }

    public void insertNotification(NotificationModel basicPriceModelObj) {
        AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(getActivity());
        sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AndroidOpenDbHelper.HEADER, basicPriceModelObj.getHeader());
        contentValues.put(AndroidOpenDbHelper.FLAG, basicPriceModelObj.getFlag());
        long affectedColumnId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_NOTIFICATION, null, contentValues);
        sqliteDatabase.close();
        Log.d(TAG, "insertNotification: " + basicPriceModelObj.getFlag());
        androidopendbhelper = new AndroidOpenDbHelper(getActivity());
        showSQliteDbData();
    }

    public void showSQliteDbData() {
        sqliteDatabase = androidopendbhelper.getWritableDatabase();
        cursor = sqliteDatabase.rawQuery("SELECT * FROM  notificationtable", null);
        HEADER_ArrayList.clear();
        FLAG_ArrayList.clear();
        if (cursor.moveToFirst()) {
            do {
                HEADER_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.HEADER)));
                FLAG_ArrayList.add(cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.FLAG)));
            }
            while (cursor.moveToNext());
        }
        mAdapter = new FragmentNotificationAdapter(getActivity(),
                HEADER_ArrayList,
                FLAG_ArrayList
        );
        rv.setAdapter(mAdapter);
        cursor.close();
    }

    private void prepareNotificationData() {
        if (Cons.isNetworkAvailable(getContext())) {
            final StringRequest notification = new StringRequest(Request.Method.GET, Cons.URL_NOTIFICATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        if (status.equals("0")) {
                            noData.setVisibility(View.VISIBLE);

                        } else {
                            JSONArray array = object.getJSONArray("noteList");
                            for (int i = 0; i < array.length(); i++) {
                                Notification notification = new Notification();
                                JSONObject obj = array.getJSONObject(i);
                                header = obj.getString("message");
                                hitSaveNotification(header, flag);
                                notificationList.add(notification);
                            }
                        }
                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.d(TAG, "Error : " + error.toString());
                }
            });
            AppController.getInstance().addToRequestQueue(notification, TAG);

        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}