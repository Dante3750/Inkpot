package com.onlineinkpot.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.onlineinkpot.R;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;
import com.onlineinkpot.fcm.FCMIntentService;
import com.onlineinkpot.fragments.DashBoardFragment;
import com.onlineinkpot.fragments.MCQFragment;
import com.onlineinkpot.fragments.MyCourseFragment;
import com.onlineinkpot.fragments.NotificationFragment;
import com.onlineinkpot.fragments.OfferFragment;
import com.onlineinkpot.fragments.OrderFragment;
import com.onlineinkpot.fragments.ProfileFragment;
import com.onlineinkpot.fragments.ProgressPageFragment;
import com.onlineinkpot.fragments.PromoCodeFragment;
import com.onlineinkpot.fragments.WalletFragment;
import com.onlineinkpot.helper.PicassoClient;
import com.onlineinkpot.models.ChatUser;
import com.onlineinkpot.models.JModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    protected PrefManager pref;
    public static List<ChatUser> chatList;
    public TextView tvFirstName, tvuserRegType, tvLastName;
    private NavigationView navigationView;
    private View navHeader;
    private DrawerLayout drawer;
    public ImageView ivUserProfile;
    private HashMap<String, String> user, changeimage;
    private HashMap<String, String> update;
    public static TextView walleteTv, tvWallet;
    String currentVersion = null;
    private String appUpdatMessage;
    public String username, firstname, email, mobile, parentName, parentMobileNo, parentEmail, lastname, middlename;
    public TextView notificationBadge;
    public static String length;
    public static boolean isClicked = false;
    private HashMap<String, String> userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new PrefManager(this);
        user = pref.getUserDetail();
        update = pref.getUpdatedData();
        String forFinalInt = pref.getInt();
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#28b5d3"));
        fetchUserList();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        walleteTv = (TextView) findViewById(R.id.main_wallete_amount_tv);
        tvuserRegType = (TextView) navHeader.findViewById(R.id.tv_email);
        tvFirstName = (TextView) navHeader.findViewById(R.id.tv_fname);
        tvLastName = (TextView) navHeader.findViewById(R.id.tv_lname);
        tvWallet = (TextView) navHeader.findViewById(R.id.tv_wallet);
        ivUserProfile = (ImageView) navHeader.findViewById(R.id.iv_userProfile);
        navigationView.setNavigationItemSelectedListener(this);
        notificationBadge = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notification));
        initializeCountDrawer();
        tvuserRegType.setText(user.get(Cons.KEY_USER_REGTYPE));
        tvFirstName.setText(user.get(Cons.KEY_USER_NAME));
        tvLastName.setText(user.get(Cons.KEY_USER_LAST_NAME));

        if (forFinalInt == "0") {
            PicassoClient.downloadImage(this, Cons.URL_IMAGE + user.get(Cons.KEY_USER_IMAGE), ivUserProfile);
        } else {
            pref = new PrefManager(this);
            changeimage = pref.getImageNew();
            String imageString = changeimage.get(Cons.KEY_VIEW_USER_IMAGE);
            if (imageString == null) {
                PicassoClient.downloadImage(this, Cons.URL_IMAGE + user.get(Cons.KEY_USER_IMAGE), ivUserProfile);
            } else {
                StringToBitMapMain(imageString);
            }
        }
        displaySelectedScreen(R.id.nav_home);
        courseListProcess();
        // registerToken.
        registerTokenToServer();
        getUpdatedUserDeatail();
        getWalleteAmount(Cons.get_wallete_amount, user.get(Cons.KEY_USER_ID));
        getUpdateInfo(Cons.get_permissions);
    }

    public void setTag() {
        tvFirstName.setText(userDetail.get(Cons.KEY_VIEW_USER_FNAME));
        tvLastName.setText(userDetail.get(Cons.KEY_VIEW_USER_LNAME));
    }

    private void initializeCountDrawer() {
        prepareNotificationData();
        notificationBadge.setGravity(Gravity.CENTER_VERTICAL);
        notificationBadge.setTypeface(null, Typeface.BOLD);
        notificationBadge.setTextColor(getResources().getColor(R.color.orange_pack));
    }

    private void prepareNotificationData() {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest notification = new StringRequest(Request.Method.GET, Cons.URL_NOTIFICATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        if (status.equals("0")) {
                        } else {
                            JSONArray array = object.getJSONArray("noteList");
                            for (int i = 0; i < array.length(); i++) {
                                length = String.valueOf(array.length());
                                notificationBadge.setText(length);
                                Log.d(TAG, "length: " + length);
                            }
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
            });
            AppController.getInstance().addToRequestQueue(notification, TAG);
          }
        else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void getWalleteAmount(String get_wallete_amount, final String s) {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest signRequest = new StringRequest(Request.Method.POST, get_wallete_amount,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "walletAmount: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("wamt")) {
                                    String wallete_amount = jsonObject.getString("wamt");
                                    float f = Float.parseFloat(wallete_amount);
                                    Log.d(TAG, "walletBAL: " + f);
                                    MainActivity.walleteTv.setText("\u20b9 " + f);
                                    MainActivity.tvWallet.setText("\u20b9 " + f);
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
                    params.put("userid", s);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void getUpdatedUserDeatail() {
        if (Cons.isNetworkAvailable(this)) {
            final StringRequest signRequest = new StringRequest(Request.Method.POST, Cons.URL_VIEW_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "updatedata: " + response);
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    username = JModel.getString(object, Cons.KEY_VIEW_USER_NAME);
                                    firstname = JModel.getString(object, Cons.KEY_VIEW_USER_FNAME);
                                    middlename = JModel.getString(object, Cons.KEY_VIEW_USER_MNAME);
                                    lastname = JModel.getString(object, Cons.KEY_VIEW_USER_LNAME);
                                    email = JModel.getString(object, Cons.KEY_VIEW_USER_EMAIL);
                                    mobile = JModel.getString(object, Cons.KEY_VIEW_USER_MOBILENO);
                                    parentName = JModel.getString(object, Cons.KEY_VIEW_USER_PNAME);
                                    parentEmail = JModel.getString(object, Cons.KEY_VIEW_USER_PEMAIL);
                                    parentMobileNo = JModel.getString(object, Cons.KEY_VIEW_USER_PMOBILE);
                                    pref.updateLoginData(username, firstname, middlename, lastname, email, mobile, parentName, parentEmail, parentMobileNo);
                                }
                                tvFirstName.setText(firstname);
                                tvLastName.setText(lastname);
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
                    params.put("studentid", user.get(Cons.KEY_USER_ID));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void registerTokenToServer() {
        startService(new Intent(this, FCMIntentService.class));
        if (!AppController.getInstance().getPref().getTokenToServer()) {
        }
    }

    private void fetchUserList() {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest signRequest = new StringRequest(Request.Method.POST, Cons.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray list = null;
                                if (pref.getType().equals("Student")) {
                                    list = object.getJSONArray("mentorList");
                                } else if (pref.getType().equals("Mentor")) {
                                    list = object.getJSONArray("studentList");
                                }
                                chatList = new ArrayList<>();
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject ob = list.getJSONObject(i);
                                    String id = ob.getString("r_id");
                                    String type = ob.getString("r_regtype");
                                    String na = ob.getString("r_name");
                                    String uName = ob.getString("r_username");
                                    String uEmail = ob.getString("r_email");
                                    String uMobile = ob.getString("r_mobile");
                                    ChatUser chat = new ChatUser(id, type, na, uName, uEmail, uMobile);
                                    chatList.add(chat);
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
                    params.put("username", pref.getEmail());
                    params.put("password", pref.getPass());
                    params.put("type", pref.getType());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(signRequest, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                pref.clearSession();
                Intent i = new Intent(MainActivity.this, WelcomeLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = new DashBoardFragment();
        switch (itemId) {
            case R.id.nav_home:
                fragment = new DashBoardFragment();
                break;
            case R.id.nav_offer:
                fragment = new OfferFragment();
                break;
            case R.id.nav_order:
                fragment = new OrderFragment();
                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.onlineinkpot");
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.send_intent_title)));
                break;
            case R.id.nav_notification:
                fragment = new NotificationFragment();
                isClicked = true;
                if (isClicked == true) {
                    notificationBadge.setText("");
                }
                break;
            case R.id.nav_mcq:
                fragment = new MCQFragment();
                break;
            case R.id.nav_progress:
                fragment = new ProgressPageFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_mycourse:
                fragment = new MyCourseFragment();
                break;
            case R.id.nav_wallet:
                fragment = new WalletFragment();
                break;
            case R.id.nav_promo:
                fragment = new PromoCodeFragment();
                break;
            case R.id.nav_logout:
                pref.clearSession();
                Intent i = new Intent(MainActivity.this, WelcomeLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void courseListProcess() {
        if (Cons.isNetworkAvailable(this)) {
            StringRequest courseReq = new StringRequest(Request.Method.POST, Cons.URL_MY_COURSE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                if (status.equals("0")) {
                                    Toast.makeText(MainActivity.this, "Please Purchase Content", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray array = object.getJSONArray("mycourse");
                                    String[] masterCats = new String[array.length()];
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject mycourse = array.getJSONObject(i);
                                        masterCats[i] = mycourse.getString(Cons.KEY_SUBJECT_ID);
                                    }
                                    pref.setSubjectIdArray(masterCats);
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
                    params.put("studentId", user.get(Cons.KEY_USER_ID));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(courseReq, TAG);
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String newVersion = null;
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    ShowDialog(MainActivity.this, appUpdatMessage);
                }
            }
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }
    }

    public void ShowDialog(Activity mMainActivity, String msg) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final Dialog imageDialog = new Dialog(mMainActivity);
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageDialog.setCancelable(false);
        imageDialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = (LayoutInflater) mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alert_update_app, null);
        TextView mTextView = (TextView) layout.findViewById(R.id.id_network_ok);
        TextView mTextmsg = (TextView) layout.findViewById(R.id.id_network_not);
        mTextmsg.setText(msg);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.onlineinkpot&hl=en"));
                startActivity(in);
                imageDialog.dismiss();
            }
        });
        layout.setLayoutParams(param);
        imageDialog.setContentView(layout, param);
        imageDialog.show();
    }

    public void getUpdateInfo(String url) {
        if (Cons.isNetworkAvailable(this)) {
            JsonArrayRequest jreq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response.length() > 0) {
                                Log.d("Taxes", response + "");
                                for (int i = 0; i < response.length(); i++) {
                                    if (i == 0) {
                                        try {
                                            JSONObject js = response.getJSONObject(i);
                                            String check = Cons.getString(js, "r_status");
                                            Log.d("Title", Cons.getString(js, "title"));
                                            appUpdatMessage = Cons.getString(js, "msg");
                                            GetVersionCode request = new GetVersionCode();
                                            Log.d(TAG, "onResponse: " + appUpdatMessage);
                                            request.execute();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                Cons.ShowToast(getApplicationContext(), "Sorry no more data available!!! ");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            AppController.getInstance().addToRequestQueue(jreq, "jreq");
        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap StringToBitMapMain(String temp) {
        Bitmap bitmap = null;
        try {
            pref = new PrefManager(this);
            changeimage = pref.getImageNew();
            byte[] encodeByte = Base64.decode(temp, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            ivUserProfile.setImageBitmap(bitmap);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            Toast.makeText(this, "Please Try Again", Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    public void runnable() {

        pref = new PrefManager(getApplicationContext());
        userDetail = pref.getUserUpdatedDetail();
        setTag();
    }

}

