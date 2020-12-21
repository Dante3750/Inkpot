package com.onlineinkpot.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WelcomeRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = WelcomeRegisterActivity.class.getSimpleName();
    public EditText etReg_Fname, etReg_email, etReg_mobile, etReg_pass, etReg_Mname, etReg_Lname;
    public TextView tvAlready_aclink;
    public Button btnReg;
    private Spinner spType;
    private String[] type = {"Select Type:", "Student", "Teacher", "Mentor"};
    private ArrayList<Integer> Category_id;
    private ViewPager pager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_register_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spType = (Spinner) findViewById(R.id.sp_reguser_type);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_two, type);
        spAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spType.setAdapter(spAdapter);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initializeLayoutContents();
    }

    private void initializeLayoutContents() {
        etReg_Fname = (EditText) findViewById(R.id.et_register_firstname);
        etReg_Lname = (EditText) findViewById(R.id.et_register_lastname);
        etReg_Mname = (EditText) findViewById(R.id.et_register_middlename);
        etReg_email = (EditText) findViewById(R.id.et_register_email);
        etReg_pass = (EditText) findViewById(R.id.et_register_password);
        etReg_mobile = (EditText) findViewById(R.id.et_register_mobile);
        tvAlready_aclink = (TextView) findViewById(R.id.tv_already_ac_link);
        final Drawable originalDrawable = etReg_Fname.getBackground();
        final Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        etReg_Fname.setBackground(wrappedDrawable);
        etReg_Fname.setTextColor(getResources().getColor(R.color.white));
        etReg_Lname.setTextColor(getResources().getColor(R.color.white));
        etReg_Mname.setTextColor(getResources().getColor(R.color.white));
        etReg_email.setTextColor(getResources().getColor(R.color.white));
        etReg_pass.setTextColor(getResources().getColor(R.color.white));
        etReg_mobile.setTextColor(getResources().getColor(R.color.white));
        etReg_Lname.setBackground(wrappedDrawable);
        etReg_Mname.setBackground(wrappedDrawable);
        etReg_email.setBackground(wrappedDrawable);
        etReg_pass.setBackground(wrappedDrawable);
        etReg_mobile.setBackground(wrappedDrawable);
        pager = (ViewPager) findViewById(R.id.register_pager);
        adapter = new ViewPagerAdapter();
        pager.setAdapter(adapter);
        btnReg = (Button) findViewById(R.id.bt_register);
        btnReg.setOnClickListener(this);
        tvAlready_aclink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (Cons.isNetworkAvailable(getApplicationContext())) {
            switch (v.getId()) {
                case R.id.bt_register:
                    validationForm();
                    break;
                case R.id.tv_already_ac_link:
                    Intent i = new Intent(WelcomeRegisterActivity.this, WelcomeLoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        } else {
            Cons.ShowToast(getApplicationContext(), Cons.NETWORK_ERROR);
        }
    }

    private void validationForm() {
        String fname = etReg_Fname.getText().toString().trim();
        String lname = etReg_Lname.getText().toString().trim();
        String mname = etReg_Mname.getText().toString().trim();
        String email = etReg_email.getText().toString().trim();
        String mobile = etReg_mobile.getText().toString().trim();
        String pass = etReg_pass.getText().toString().trim();
        if (fname.isEmpty()) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_LONG).show();
            etReg_Fname.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_LONG).show();
            etReg_Lname.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_LONG).show();
            etReg_pass.requestFocus();
            return;
        }
        if (mobile.isEmpty()) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_LONG).show();
            etReg_mobile.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_LONG).show();
            etReg_email.requestFocus();
            return;
        }
        if (!Cons.validEmail(email)) {
            Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show();
            etReg_email.requestFocus();
            return;
        }
        if (!Cons.isValidPhoneNumber(mobile)) {
            Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
            etReg_mobile.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "Password is short", Toast.LENGTH_SHORT).show();
            etReg_pass.requestFocus();
            return;
        }
        registerProcess(fname, lname, mname, email, mobile, spType.getSelectedItem().toString(), pass);
    }

    private void registerProcess(final String fname, final String lname, final String mname, final String email, final String mobile, final String type, final String pass) {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest regRequest = new StringRequest(Request.Method.POST, Cons.URL_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response registration ", "= " + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                String message = object.getString("message");
                                if (status.equals("0")) {
                                    Toast.makeText(WelcomeRegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(
                                            WelcomeRegisterActivity.this).create();
                                    alertDialog.setTitle("Register Successfully");
                                    alertDialog.setMessage("Please Check Your Email for Verification");
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    alertDialog.show();
                                    Intent i = new Intent(WelcomeRegisterActivity.this, WelcomeLoginActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
                    params.put("name", fname);
                    params.put("mname", mname);
                    params.put("lname", lname);
                    params.put("email", email);
                    params.put("password", pass);
                    params.put("mobile", mobile);
                    params.put("servicetype", "Android");
                    params.put("deviceid", Cons.deviceId(getApplicationContext()));
                    params.put("regtype", type);
                    Log.d(TAG, "Register: " + params.toString());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(regRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_register;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;
            }
            return findViewById(resId);
        }
    }
}