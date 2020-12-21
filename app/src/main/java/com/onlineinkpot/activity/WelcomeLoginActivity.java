package com.onlineinkpot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.models.JModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WelcomeLoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = WelcomeLoginActivity.class.getSimpleName();
    private Spinner spType;
    private EditText etLogin_user, etLogin_pass, etForget_email;
    private Button btLogin, btForget;
    private TextView tvRegLink, tvLogin_forgetlink;
    protected PrefManager pref;
    private ViewPager pager;
    public ViewPagerAdapter adapter;
    String forFinalInt = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_login_activity);
        spType = (Spinner) findViewById(R.id.sp_user_type);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_two, getResources().getStringArray(R.array.login_type));
        spAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spType.setAdapter(spAdapter);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initializeLayoutContents();
        btLogin.setOnClickListener(this);
        tvRegLink.setOnClickListener(this);
        tvLogin_forgetlink.setOnClickListener(this);
        btForget.setOnClickListener(this);
        pref = new PrefManager(this);
        pref.setInt(forFinalInt);
    }
    //    Initialzation of layout content
    private void initializeLayoutContents() {
        etLogin_user = (EditText) findViewById(R.id.et_login_email);
        etLogin_pass = (EditText) findViewById(R.id.et_login_pass);
        tvRegLink = (TextView) findViewById(R.id.tv_login_reglink);
        etForget_email = (EditText) findViewById(R.id.et_forget_email);
        tvLogin_forgetlink = (TextView) findViewById(R.id.tv_login_forgotlink);
        btForget = (Button) findViewById(R.id.bt_forget);
        final Drawable originalDrawable = etLogin_user.getBackground();
        final Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(getResources().getColor(R.color.white)));
        etLogin_user.setBackground(wrappedDrawable);
        etLogin_pass.setBackground(wrappedDrawable);
        etForget_email.setBackground(wrappedDrawable);
        etLogin_user.setTextColor(getResources().getColor(R.color.white));
        etLogin_pass.setTextColor(getResources().getColor(R.color.white));
        etForget_email.setTextColor(getResources().getColor(R.color.white));
        pager = (ViewPager) findViewById(R.id.login_pager);
        adapter = new ViewPagerAdapter();
        pager.setAdapter(adapter);
        btLogin = (Button) findViewById(R.id.bt_login);
    }

    @Override
    public void onClick(View v) {
        if (Cons.isNetworkAvailable(getApplicationContext())) {
            switch (v.getId()) {
                case R.id.bt_login:
                    String email = etLogin_user.getText().toString().trim();
                    String pass = etLogin_pass.getText().toString().trim();
                    if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(this, "Enter valid email address!", Toast.LENGTH_SHORT).show();
                        etLogin_user.requestFocus();
                        return;
                    }

                    if (pass.length() < 6) {
                        Toast.makeText(this, "Enter valid password!", Toast.LENGTH_SHORT).show();
                        etLogin_pass.requestFocus();
                        return;
                    }

                    if (spType.getSelectedItemPosition() == 0) {
                        Toast.makeText(this, "Enter Type!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    signInprocess(email, pass, spType.getSelectedItem().toString());
                    break;

                case R.id.tv_login_reglink:
                    startActivity(new Intent(WelcomeLoginActivity.this, WelcomeRegisterActivity.class));
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    break;

                case R.id.tv_login_forgotlink:
                    pager.setCurrentItem(1);
                    break;
                case R.id.bt_forget:
                    String forget_email = etForget_email.getText().toString().trim();
                    if (TextUtils.isEmpty(forget_email)) {
                        Toast.makeText(this, "Enter valid email address!", Toast.LENGTH_SHORT).show();
                        etForget_email.requestFocus();
                        return;

                    }
                    forgetProcess(forget_email);
                    break;
            }
        } else {
            Cons.ShowToast(getApplicationContext(), Cons.NETWORK_ERROR);
        }
    }

    //Forget Password Service
    private void forgetProcess(final String forget_email) {
        if (Cons.isNetworkAvailable(this)) {

            final StringRequest forgetRequest = new StringRequest(Request.Method.POST, Cons.FORGOT_PASSWORD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(" forgot password ", "= " + response);

                    try {
                        JSONObject object = new JSONObject(response);
                        Log.d(TAG, "Forget Post : " + object.toString());
                        if (JModel.getString(object, "email").equals("0")) {
                            Cons.ShowToast(getApplicationContext(), "Please check the entered email!!");
                        } else if (JModel.getString(object, "email").equals("1")) {
                            Cons.ShowToast(getApplicationContext(), "Success, Please check the mail for the password information");
                            etForget_email.setText("");
                        } else {
                            Cons.ShowToast(getApplicationContext(), "Please try again...!!!");
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
                    params.put("email", forget_email);
                    Log.d(" Forgot Params ", "= " + params);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(forgetRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    //    SignIn Service
    private void signInprocess(final String email, final String pass, final String type) {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest signRequest = new StringRequest(Request.Method.POST, Cons.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                String message = object.getString("message");
                                if (status.equals("0")) {
                                    Toast.makeText(WelcomeLoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject object2 = object.getJSONObject("login");
                                    String fname = JModel.getString(object2, "first_name");
                                    String lname = JModel.getString(object2, "last_name");
                                    String regid = JModel.getString(object2, "user_id");
                                    String regtype = JModel.getString(object2, "user_type");
                                    String regusername = JModel.getString(object2, "user_name");
                                    String reguseremail = JModel.getString(object2, "user_email");
                                    String regusermobile = JModel.getString(object2, "user_mobile");
                                    String profileimage = JModel.getString(object2, "user_image");
                                    pref.createLogin_cust(fname, lname, regid, regtype, regusername, reguseremail, regusermobile, profileimage);
                                    Intent i = new Intent(WelcomeLoginActivity.this, MainActivity.class);
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
                    params.put("email", email);
                    params.put("password", pass);
                    params.put("regtype", type);
                    params.put("deviceid", Cons.deviceId(getApplicationContext()));
                    Log.d(TAG, "Login Params : " + params.toString());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest, TAG);
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
                    resId = R.id.layout_login;
                    break;
                case 1:
                    resId = R.id.layout_forgetpass;
                    break;
            }
            return findViewById(resId);
        }
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 1) {
            pager.setCurrentItem(pager.getCurrentItem() - 1, false);
        } else
            moveTaskToBack(true);
    }
}
