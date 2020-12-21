package com.onlineinkpot.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import static com.onlineinkpot.app.Cons.KEY_COMBO;
import static com.onlineinkpot.app.Cons.KEY_COURSE_COLOR;
import static com.onlineinkpot.app.Cons.KEY_COURSE_ICON;
import static com.onlineinkpot.app.Cons.KEY_COURSE_ID;
import static com.onlineinkpot.app.Cons.KEY_COURSE_NAME;
import static com.onlineinkpot.app.Cons.KEY_CURRENT_SUB;
import static com.onlineinkpot.app.Cons.KEY_INT;
import static com.onlineinkpot.app.Cons.KEY_LOGGED_IN;
import static com.onlineinkpot.app.Cons.KEY_SEM;
import static com.onlineinkpot.app.Cons.KEY_SUBJECT_ID;
import static com.onlineinkpot.app.Cons.KEY_SUB_COUNT;
import static com.onlineinkpot.app.Cons.KEY_TOKEN_SERVER;
import static com.onlineinkpot.app.Cons.KEY_TXN_ID;
import static com.onlineinkpot.app.Cons.KEY_USER_ID;
import static com.onlineinkpot.app.Cons.KEY_USER_IMAGE;
import static com.onlineinkpot.app.Cons.KEY_USER_LAST_NAME;
import static com.onlineinkpot.app.Cons.KEY_USER_MOBILE;
import static com.onlineinkpot.app.Cons.KEY_USER_NAME;
import static com.onlineinkpot.app.Cons.KEY_USER_PASS;
import static com.onlineinkpot.app.Cons.KEY_USER_PURCHASED;
import static com.onlineinkpot.app.Cons.KEY_USER_REGEMAIL;
import static com.onlineinkpot.app.Cons.KEY_USER_REGNAME;
import static com.onlineinkpot.app.Cons.KEY_USER_REGTYPE;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_EMAIL;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_FNAME;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_IMAGE;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_LNAME;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_MNAME;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_MOBILENO;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_NAME;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_PEMAIL;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_PMOBILE;
import static com.onlineinkpot.app.Cons.KEY_VIEW_USER_PNAME;
import static com.onlineinkpot.app.Cons.VALIDATE_DATE;


public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "InkpotOnlineSharedPref";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String ORDER_NO = "order_no";
    private static final String ORDER_ID = "order_id";
    private static final String ALL_SUBJECT = "all_subject";
    private static final String ORDER_PAYMENT_MODE = "payment_mode";

    public PrefManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLogin_cust(String fname, String lname, String regId, String regType, String userName, String regEmail, String regMobile, String profileImage) {
        editor.putString(KEY_USER_NAME, fname);
        editor.putString(KEY_USER_LAST_NAME, lname);
        editor.putString(KEY_USER_ID, regId);
        editor.putString(KEY_USER_REGTYPE, regType);
        editor.putString(KEY_USER_REGNAME, userName);
        editor.putString(KEY_USER_REGEMAIL, regEmail);
        editor.putString(KEY_USER_MOBILE, regMobile);
        editor.putString(KEY_USER_IMAGE, profileImage);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.commit();
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USER_REGNAME, pref.getString(KEY_USER_REGNAME, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, null));
        user.put(KEY_USER_REGTYPE, pref.getString(KEY_USER_REGTYPE, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_REGEMAIL, pref.getString(KEY_USER_REGEMAIL, null));
        user.put(KEY_USER_LAST_NAME, pref.getString(KEY_USER_LAST_NAME, null));
        user.put(KEY_USER_MOBILE, pref.getString(KEY_USER_MOBILE, null));
        return user;
    }

    public void updateLoginData(String username, String firstname, String middlename, String lastname, String email, String mobile, String parentname, String parentmobile, String parentemail) {
        editor.putString(KEY_VIEW_USER_NAME, username);
        editor.putString(KEY_VIEW_USER_FNAME, firstname);
        editor.putString(KEY_VIEW_USER_MNAME, middlename);
        editor.putString(KEY_VIEW_USER_LNAME, lastname);
        editor.putString(KEY_VIEW_USER_EMAIL, email);
        editor.putString(KEY_VIEW_USER_MOBILENO, mobile);
        editor.putString(KEY_VIEW_USER_PNAME, parentname);
        editor.putString(KEY_VIEW_USER_PEMAIL, parentmobile);
        editor.putString(KEY_VIEW_USER_PMOBILE, parentemail);
        editor.commit();
    }

    public HashMap<String, String> getUpdatedData() {
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail.put(KEY_VIEW_USER_MOBILENO, pref.getString(KEY_VIEW_USER_MOBILENO, null));
        userDetail.put(KEY_VIEW_USER_EMAIL, pref.getString(KEY_VIEW_USER_EMAIL, null));
        userDetail.put(KEY_VIEW_USER_FNAME, pref.getString(KEY_VIEW_USER_FNAME, null));
        userDetail.put(KEY_VIEW_USER_LNAME, pref.getString(KEY_VIEW_USER_LNAME, null));
        userDetail.put(KEY_VIEW_USER_EMAIL, pref.getString(KEY_VIEW_USER_EMAIL, null));
        userDetail.put(KEY_VIEW_USER_PNAME, pref.getString(KEY_VIEW_USER_PNAME, null));
        userDetail.put(KEY_VIEW_USER_PEMAIL, pref.getString(KEY_VIEW_USER_PEMAIL, null));
        userDetail.put(KEY_VIEW_USER_PMOBILE, pref.getString(KEY_VIEW_USER_PMOBILE, null));
        return userDetail;
    }

    public void editUser(String fname, String lname) {
        editor.putString(KEY_VIEW_USER_FNAME, fname);
        editor.putString(KEY_VIEW_USER_LNAME, lname);
        editor.commit();
    }

    public HashMap<String, String> getUserUpdatedDetail() {
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail.put(KEY_VIEW_USER_FNAME, pref.getString(KEY_VIEW_USER_FNAME, null));
        userDetail.put(KEY_VIEW_USER_LNAME, pref.getString(KEY_VIEW_USER_LNAME, null));
        return userDetail;
    }

    public String getDataItem() {
        return pref.getString(KEY_USER_PURCHASED, null);
    }

    public void editImage(String image) {
        editor.putString(KEY_USER_IMAGE, image);
        editor.commit();
    }

    public HashMap<String, String> getUpdateImage() {
        HashMap<String, String> userImage = new HashMap<>();
        userImage.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, null));
        return userImage;
    }

    public void setDataItem(String r_purchsed) {
        editor.putString(KEY_USER_PURCHASED, r_purchsed);
        editor.commit();
    }

    public String getCustId() {
        return pref.getString(KEY_USER_ID, null);
    }

    //    set/get for user email
    public String getEmail() {
        return pref.getString(KEY_USER_REGEMAIL, null);
    }

    public void setEmail(String r_email) {
        editor.putString(KEY_USER_REGEMAIL, r_email);
        editor.commit();
    }

    public void setCourseIcon(String icon) {
        editor.putString(KEY_COURSE_ICON, icon);
        editor.commit();
    }

    public String getCourseIcon() {
        return pref.getString(KEY_COURSE_ICON, null);
    }

    public void setCourseName(String icon) {
        editor.putString(KEY_COURSE_NAME, icon);
        editor.commit();
    }

    public String getCourseName() {
        return pref.getString(KEY_COURSE_NAME, null);
    }

    public void setSemCount(int i) {
        editor.putInt(KEY_SUB_COUNT, i);
        editor.commit();
    }

    public HashMap<String, String> getImageNew() {
        HashMap<String, String> userImage = new HashMap<>();
        userImage.put(KEY_VIEW_USER_IMAGE, pref.getString(KEY_VIEW_USER_IMAGE, null));
        return userImage;
    }

    public int getSemCount() {
        return pref.getInt(KEY_SUB_COUNT, 0);
    }

    public String getPamentMode() {
        return pref.getString(ORDER_PAYMENT_MODE, null);
    }

    public void setOrderPaymentMode(String payment_mode) {
        editor.putString(ORDER_PAYMENT_MODE, payment_mode);
        editor.commit();
    }

    public void setCourseColor(String color) {
        editor.putString(KEY_COURSE_COLOR, color);
        editor.commit();
    }

    public String getOrderNO() {
        return pref.getString(ORDER_NO, null);
    }

    public void setOrderNo(String order_no) {
        editor.putString(ORDER_NO, order_no);
        editor.commit();
    }

    public String getOrderID() {
        return pref.getString(ORDER_ID, null);
    }

    public void setOrderID(String order_id) {
        editor.putString(ORDER_ID, order_id);
        editor.commit();
    }

    public String getCourseColor() {
        return pref.getString(KEY_COURSE_COLOR, null);
    }

    public void setSubjectIdArray(String[] s) {

        String stringIdArray = null;
        for (int i = 0; i < s.length; i++) {
            if (stringIdArray == null) {
                stringIdArray = s[i];
            } else {
                stringIdArray = stringIdArray + "," + s[i];
            }
        }

        editor.putString(KEY_SUBJECT_ID, stringIdArray);
        editor.commit();

    }

    public String getSubjectIdArray() {
        return pref.getString(KEY_SUBJECT_ID, null);
    }

    public void setCombo(String[] s) {
        String stringIdArray = null;
        for (int i = 0; i < s.length; i++) {
            if (stringIdArray == null) {
                stringIdArray = s[i];
            } else {
                stringIdArray = stringIdArray + "," + s[i];
            }
        }
        editor.putString(KEY_COMBO, stringIdArray);
        editor.commit();
    }

    public String getCombo() {
        return pref.getString(KEY_COMBO, null);
    }

    public String getPass() {
        return pref.getString(KEY_USER_PASS, null);
    }

    public String getType() {
        return pref.getString(KEY_USER_REGTYPE, null);
    }

    //set/get for user mobile number
    public String getmobile() {
        return pref.getString(KEY_USER_MOBILE, null);
    }

    public void setuser_mobile(String r_mobile) {
        editor.putString(KEY_USER_MOBILE, r_mobile);
        editor.commit();
    }

    public void setTokenToServer(boolean b) {
        editor.putBoolean(KEY_TOKEN_SERVER, b);
        editor.commit();
    }

    public void removetxn() {
        editor.remove(Cons.KEY_TXN_ID);
        editor.commit();
    }

    public void setImage(String image) {
        editor.putString(KEY_VIEW_USER_IMAGE, image);
        editor.commit();
        editor.apply();
    }

    public String getInt() {
        return pref.getString(KEY_INT, null);
    }

    public void setInt(String finalInt) {
        editor.putString(KEY_INT, finalInt);
        editor.commit();
        editor.apply();
    }

    public boolean getTokenToServer() {
        return pref.getBoolean(KEY_TOKEN_SERVER, false);
    }

    //for user login session
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void removeCount() {
        editor.remove(Cons.KEY_SUB_COUNT);
        editor.commit();
    }

    public String getCourseId() {
        return pref.getString(KEY_COURSE_ID, null);
    }

    public void setCourseId(String courseid) {
        editor.putString(KEY_COURSE_ID, courseid);
        editor.commit();
    }

    public String getSemId() {
        return pref.getString(KEY_SEM, null);
    }

    public String setSemId(String semid) {
        editor.putString(KEY_SEM, semid);
        editor.commit();
        return semid;
    }

    public String getTxanid() {
        return pref.getString(KEY_TXN_ID, null);
    }

    public void setTaxanid(String txnid) {
        editor.putString(KEY_TXN_ID, txnid);
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
        Log.d("TAG", "Deleted all user info from shared preference");
    }

    public String getCurrentSub() {
        return pref.getString(KEY_CURRENT_SUB, null);
    }

    public void setCurrentSub(String csub) {
        editor.putString(KEY_CURRENT_SUB, csub);
        editor.commit();
    }

    public String getAllSub() {
        return pref.getString(ALL_SUBJECT, null);
    }

    public void setAllSub(String all_subject) {
        editor.putString(ALL_SUBJECT, all_subject);
        editor.commit();
    }

    public String getValidateDate() {
        return pref.getString(VALIDATE_DATE, null);
    }

    public void setValidateDate(String validate_date) {
        editor.putString(VALIDATE_DATE, validate_date);
        editor.commit();
    }
}