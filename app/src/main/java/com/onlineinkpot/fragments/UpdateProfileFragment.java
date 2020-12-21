package com.onlineinkpot.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.activity.MainActivity;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.JModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "UpdateProfileFragment";
    public EditText etUserName, etName, etLastName, etEmail, etMobileNo, etMiddleName, etGuardianName, etGuardianEmail, etGuardianMobileNo;
    public Spinner SpState, SpUniverSity, SpCollege, SpCourse;
    public PrefManager pref;
    public ArrayList<String> stateList, universityList, collegeList, courseList;
    private ArrayAdapter<String> adapState, adapUniversity, adapCollege, adapCourse;
    public Button btnSubmit, btnEdit;
    public String username, firstname, email, mobile, parentName, parentMobileNo, parentEmail, lastname, middlename;
    private HashMap<String, String> userdetail;
    private boolean up = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }

    public void refreshApi() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        refreshApi();
        View view = inflater.inflate(R.layout.fragment_update_profile, null, false);
        pref = new PrefManager(getContext());
        userdetail = pref.getUserDetail();
        initializeLayoutContents(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initializeLayoutContents(View view) {
        btnEdit = (Button) view.findViewById(R.id.btn_edit_profile);
        etUserName = (EditText) view.findViewById(R.id.et_profile_user_name);
        etName = (EditText) view.findViewById(R.id.et_profile_name);
        etLastName = (EditText) view.findViewById(R.id.et_profile_lname);
        etEmail = (EditText) view.findViewById(R.id.et_profile_email);
        etMobileNo = (EditText) view.findViewById(R.id.et_profile_mob_no);
        etMiddleName = (EditText) view.findViewById(R.id.et_profile_middle_name);
        etGuardianName = (EditText) view.findViewById(R.id.et_profile_parentname);
        etGuardianEmail = (EditText) view.findViewById(R.id.et_profile_parent_email);
        etGuardianMobileNo = (EditText) view.findViewById(R.id.et_profile_parent_mobile_no);
        SpState = (Spinner) view.findViewById(R.id.sp_profile_state);
        SpUniverSity = (Spinner) view.findViewById(R.id.sp_profile_university);
        SpCollege = (Spinner) view.findViewById(R.id.sp_profile_college);
        SpCourse = (Spinner) view.findViewById(R.id.sp_profile_course);
        btnSubmit = (Button) view.findViewById(R.id.btn_profile_submit);
        btnSubmit.setOnClickListener(this);
        etUserName.setText(userdetail.get(Cons.KEY_USER_REGNAME));
        etUserName.setEnabled(false);
        etName.setEnabled(false);
        etMiddleName.setEnabled(false);
        etLastName.setEnabled(false);
        etEmail.setEnabled(false);
        etMobileNo.setEnabled(false);
        etGuardianName.setEnabled(false);
        etGuardianEmail.setEnabled(false);
        etGuardianMobileNo.setEnabled(false);
        SpState.setEnabled(false);
        SpUniverSity.setEnabled(false);
        SpCollege.setEnabled(false);
        SpCourse.setEnabled(false);
        getUserDeatail();
        stateList = new ArrayList<>();
        stateList.add("Select State:");
        adapState = new ArrayAdapter<>(getContext(), R.layout.spinner_three, stateList);
        adapState.setDropDownViewResource(android.R.layout.simple_list_item_1);
        SpState.setAdapter(adapState);
        getStateList();
        universityList = new ArrayList<>();
        universityList.add("Select UniverSity:");
        adapUniversity = new ArrayAdapter<>(getContext(), R.layout.spinner_three, universityList);
        adapUniversity.setDropDownViewResource(android.R.layout.simple_list_item_1);
        SpUniverSity.setAdapter(adapUniversity);
        collegeList = new ArrayList<>();
        collegeList.add("Select College");
        adapCollege = new ArrayAdapter<>(getContext(), R.layout.spinner_three, collegeList);
        adapCollege.setDropDownViewResource(android.R.layout.simple_list_item_1);
        SpCollege.setAdapter(adapCollege);
        courseList = new ArrayList<>();
        courseList.add("Select Course");
        adapCourse = new ArrayAdapter<>(getContext(), R.layout.spinner_three, courseList);
        adapCourse.setDropDownViewResource(android.R.layout.simple_list_item_1);
        SpCourse.setAdapter(adapCourse);
        btnEdit.setOnClickListener(this);
        SpState.setOnItemSelectedListener(this);
        SpUniverSity.setOnItemSelectedListener(this);
        SpCollege.setOnItemSelectedListener(this);
    }

    private void setUserData() {
        etName.setText(firstname);
        etMiddleName.setText(middlename);
        etLastName.setText(lastname);
        etEmail.setText(email);
        etMobileNo.setText(mobile);
        etGuardianName.setText(parentName);
        etGuardianEmail.setText(parentEmail);
        etGuardianMobileNo.setText(parentMobileNo);
    }

    public void onClick(View v) {
        if (Cons.isNetworkAvailable(getContext())) {
            switch (v.getId()) {
                case R.id.btn_profile_submit:
                    validationForm();
                    pref = new PrefManager(getContext());
                    pref.editUser(firstname, lastname);
                    ((MainActivity) getActivity()).runnable();
                    for (Fragment fragment : getFragmentManager().getFragments()) {
                        for (Fragment fragment1 : fragment.getFragmentManager().getFragments()) {
                            if (fragment1 instanceof ProfileFragment) {
                                ProfileFragment agmentA = ((ProfileFragment) fragment1);
                                agmentA.profileChange();
                            }
                        }
                    }
                    break;
                case R.id.btn_edit_profile:
                    makeEditable();
                    btnEdit.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        } else {
            Cons.displayToast(getContext(), Cons.NETWORK_ERROR, UpdateProfileFragment.this);
        }
    }

    private void makeEditable() {
        etName.setEnabled(true);
        etName.setTextColor(getResources().getColor(R.color.colorPrimary));
        etMiddleName.setEnabled(true);
        etMiddleName.setTextColor(getResources().getColor(R.color.colorPrimary));
        etLastName.setEnabled(true);
        etLastName.setTextColor(getResources().getColor(R.color.colorPrimary));
        etMobileNo.setEnabled(true);
        etMobileNo.setTextColor(getResources().getColor(R.color.colorPrimary));
        SpState.setEnabled(true);
        TextView spinnerText = (TextView) SpState.getChildAt(0);
        spinnerText.setTextColor(getResources().getColor(R.color.colorPrimary));
        SpUniverSity.setEnabled(true);
        spinnerText = (TextView) SpUniverSity.getChildAt(0);
        spinnerText.setTextColor(getResources().getColor(R.color.colorPrimary));
        SpCollege.setEnabled(true);
        spinnerText = (TextView) SpCollege.getChildAt(0);
        spinnerText.setTextColor(getResources().getColor(R.color.colorPrimary));
        SpCourse.setEnabled(true);
        spinnerText = (TextView) SpCourse.getChildAt(0);
        spinnerText.setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    private void validationForm() {
        username = etUserName.getText().toString().trim();
        firstname = etName.getText().toString().trim();
        middlename = etMiddleName.getText().toString().trim();
        lastname = etLastName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        mobile = etMobileNo.getText().toString().trim();
        parentName = etGuardianName.getText().toString().trim();
        parentEmail = etGuardianEmail.getText().toString().trim();
        parentMobileNo = etGuardianMobileNo.getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Enter Username", Toast.LENGTH_LONG).show();
            etUserName.requestFocus();
            return;
        }
        if (firstname.isEmpty()) {
            Toast.makeText(getContext(), "Enter First Name", Toast.LENGTH_LONG).show();
            etName.requestFocus();
            return;
        }
        if (lastname.isEmpty()) {
            Toast.makeText(getContext(), "Enter Last Name", Toast.LENGTH_LONG).show();
            etLastName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (!Cons.validEmail(email)) {
            Toast.makeText(getContext(), "Enter valid email address", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }
        if (mobile.isEmpty()) {
            Toast.makeText(getContext(), "Enter Mobile Number", Toast.LENGTH_LONG).show();
            etMobileNo.requestFocus();
            return;
        }
        if (!Cons.isValidPhoneNumber(mobile)) {
            Toast.makeText(getContext(), "Enter valid Mobile number", Toast.LENGTH_SHORT).show();
            etMobileNo.requestFocus();
            return;
        }
        updateUserdetail(username, firstname, middlename, lastname, mobile);
    }

    private void updateUserdetail(final String username, final String firstname, final String middlename, final String lastname, final String mobile) {
        if (Cons.isNetworkAvailable(getContext())) {
            final StringRequest signRequest = new StringRequest(Request.Method.POST, Cons.URL_UPDATE_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "DaTA: " + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String msg = object.getString("message");
                                if (JModel.getString(object, "status").equals("1")) {
                                    Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error : " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentId", userdetail.get(Cons.KEY_USER_ID));
                    params.put("firstname", firstname);
                    params.put("username", username);
                    params.put("middlename", middlename);
                    params.put("mobile", mobile);
                    params.put("lastname", lastname);
                    params.put("state", SpState.getSelectedItem().toString());
                    params.put("university", SpUniverSity.getSelectedItem().toString());
                    params.put("college", SpCollege.getSelectedItem().toString());
                    params.put("course", SpCourse.getSelectedItem().toString());
                    Log.d(TAG, "getParams: " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_profile_state:
                getSpinnerData(Cons.URL_UNIVERSITY, stateList.get(position), 1);
                Log.d(TAG, "onItemSelected: " + stateList.get(position));
                break;
            case R.id.sp_profile_university:
                getSpinnerData(Cons.URL_COLLEGE, universityList.get(position), 2);
                getSpinnerData(Cons.URL_COURSE, universityList.get(position), 3);
                break;
            case R.id.sp_profile_college:
                Log.d(TAG, "university: " + universityList.get(position));
                break;
        }
    }

    private void getSpinnerData(String url, final String s, final int i) {
        if (Cons.isNetworkAvailable(getContext())){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject mainobject = new JSONObject(response);
                            String status = mainobject.getString("status");
                            if (status.equals("0")) {
                                //no data
                            } else {
                                if (i == 1) {
                                    JSONArray array = mainobject.getJSONArray("universityList");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        universityList.add(object.getString("universityname"));
                                    }
                                    adapUniversity.notifyDataSetChanged();

                                } else if (i == 2) {
                                    JSONArray array = mainobject.getJSONArray("collegeList");
                                    collegeList.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        collegeList.add(obj.getString("collegename"));
                                    }
                                    adapCollege.notifyDataSetChanged();
                                } else if (i == 3) {
                                    JSONArray array = mainobject.getJSONArray("courseList");
                                    courseList.clear();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        courseList.add(obj.getString("coursename"));
                                    }
                                    adapCourse.notifyDataSetChanged();
                                    Log.d(TAG, "courseList: " + courseList);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (i == 1) {
                    params.put("state", s);
                } else if (i == 2) {
                    params.put("university", s);
                } else if (i == 3) {
                    params.put("university", s);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }
    else {
            Toast.makeText(getContext(), ""+Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getUserDeatail() {
        if (Cons.isNetworkAvailable(getContext())) {
            final StringRequest signRequest = new StringRequest(Request.Method.POST, Cons.URL_VIEW_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    username = userdetail.get(Cons.KEY_USER_REGNAME);
                                    firstname = JModel.getString(object, Cons.KEY_VIEW_USER_FNAME);
                                    middlename = JModel.getString(object, Cons.KEY_VIEW_USER_MNAME);
                                    lastname = JModel.getString(object, Cons.KEY_VIEW_USER_LNAME);
                                    email = userdetail.get(Cons.KEY_USER_REGEMAIL);
                                    mobile = userdetail.get(Cons.KEY_USER_MOBILE);
                                    parentName = JModel.getString(object, Cons.KEY_VIEW_USER_PNAME);
                                    parentEmail = JModel.getString(object, Cons.KEY_VIEW_USER_PEMAIL);
                                    parentMobileNo = JModel.getString(object, Cons.KEY_VIEW_USER_PMOBILE);
                                    setUserData();
                                    pref.updateLoginData(username, firstname, middlename, lastname, email, mobile, parentName, parentEmail, parentMobileNo);
                                    String state = object.getString(Cons.KEY_VIEW_USER_STATEID);
                                    String university = object.getString(Cons.KEY_VIEW_USER_UNIVERSITYID);
                                    String college = object.getString(Cons.KEY_VIEW_USER_COLLEGEID);
                                    String course = object.getString(Cons.KEY_VIEW_USER_COURSEID);
                                    Log.d(TAG, "university: " + university);

                                    if (!state.isEmpty()) {
                                        stateList.set(0, state);
                                        adapState.notifyDataSetChanged();
                                    }
                                    if (!university.isEmpty()) {
                                        universityList.set(0, university);
                                        adapUniversity.notifyDataSetChanged();
                                    }
                                    if (!college.isEmpty()) {
                                        collegeList.set(0, college);
                                        adapCollege.notifyDataSetChanged();
                                    }
                                    if (!course.isEmpty()) {
                                        courseList.set(0, course);
                                        adapCourse.notifyDataSetChanged();
                                    }
                                    Log.d(TAG, "State And University: " + state + university + college + course);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error : " + error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("studentid", userdetail.get(Cons.KEY_USER_ID));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest, TAG);

        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public void getStateList() {
        if (Cons.isNetworkAvailable(getContext())) {

            StringRequest sr = new StringRequest(Request.Method.GET, Cons.URL_STATE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                stateList.clear();
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    stateList.add(object.getString("state_name"));
                                }
                                adapState = new ArrayAdapter<>(getContext(), R.layout.spinner_three, stateList);
                                adapState.setDropDownViewResource(android.R.layout.simple_list_item_1);
                                SpState.setAdapter(adapState);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            AppController.getInstance().addToRequestQueue(sr);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}

