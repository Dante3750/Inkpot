package com.onlineinkpot.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.JModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UpdatePasswordFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "UpdatePasswordFragment";
    protected PrefManager pref;
    public HashMap<String, String> userdetail;
    public EditText etOldpass, etNewpass, etConfirmpass;
    public Button btnUpdatePass;
    private String curnt_password, new_pawword, confirm_pasword;
    private String stud_id;

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    public void refreshApi() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, null, false);


        pref = new PrefManager(getContext());
        userdetail = pref.getUserDetail();
        initializeLayoutContents(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initializeLayoutContents(View view) {
        stud_id = userdetail.get(Cons.KEY_USER_ID);
        etOldpass = (EditText) view.findViewById(R.id.et_profile_old_password);
        etNewpass = (EditText) view.findViewById(R.id.et_profile_new_password);
        etConfirmpass = (EditText) view.findViewById(R.id.et_profile_cnfrm_password);
        btnUpdatePass = (Button) view.findViewById(R.id.bt_profile_pass_submit);
        btnUpdatePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (Cons.isNetworkAvailable(getContext())) {
            switch (v.getId()) {
                case R.id.bt_profile_pass_submit:
                    curnt_password = etOldpass.getText().toString();
                    new_pawword = etNewpass.getText().toString();
                    confirm_pasword = etConfirmpass.getText().toString();
                    if (curnt_password != null && !curnt_password.isEmpty()) {
                        if (new_pawword != null && !new_pawword.isEmpty()) {
                            if (confirm_pasword != null && !confirm_pasword.isEmpty()) {
                                if (new_pawword.equals(confirm_pasword)) {
                                    updatePassword(Cons.URL_UPDATE_PASSWORD, stud_id, curnt_password, new_pawword);
                                } else {
                                    Toast.makeText(getContext(), " Please re enter new Password", Toast.LENGTH_LONG).show();
                                    etNewpass.setText("");
                                    etConfirmpass.setText("");
                                }
                            } else {
                                Toast.makeText(getContext(), " Confirm the new password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), " Enter new password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), " Enter current password", Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }

    private void updatePassword(String urlUpdatePassword, final String stud_id, final String oldpass, final String confirmpass) {
        if (Cons.isNetworkAvailable(getContext())) {
            final StringRequest signRequest = new StringRequest(Request.Method.POST, urlUpdatePassword,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String msg = object.getString("message");
                                if (JModel.getString(object, "status").equals("0")) {
                                    etOldpass.setText("");
                                    etNewpass.setText("");
                                    etConfirmpass.setText("");
                                    Log.d(TAG, "testing: " + msg);
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
                    params.put("studentid", stud_id);
                    params.put("oldpass", oldpass);
                    params.put("newpass", confirmpass);
                    Log.d(TAG, "testparams: " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest, TAG);
        } else {
            Toast.makeText(getContext(), "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
