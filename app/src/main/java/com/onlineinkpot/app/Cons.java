package com.onlineinkpot.app;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.onlineinkpot.fragments.UpdateProfileFragment;

import org.json.JSONObject;


/**
 * Created by john on 7/26/2017.
 */


public class Cons {
    public static final String baseurl = "http://inkpotonline.com/index.php/api/";
    public static final String KEY_LOGGED_IN = "isLoggedIn";
    public static final String KEY_LOGIN = "login";
    public static final String URL_BASE = "http://inkpotonline.com/index.php/api/androidapi/";
    public static final String URL_LOGIN = "http://inkpotonline.com/index.php/api/androidapi/NewLogin";
    public static final String URL_REGISTER = "http://inkpotonline.com/index.php/api/androidapi/RegisterNew";
    public static final String URL_GETCOURSE_LIST = URL_BASE + "getCourseList/format/json/";
    public static final String FORGOT_PASSWORD = "http://inkpotonline.com/index.php/api/mainapi/forgotpassword/format/json/";
    public static final String UPDATE_TOKEN = URL_BASE + "updateFCM/format/json/";
    public static final String URL_TESTSUBJECT_LIST = URL_BASE + "getSubjectListBySem/format/json/";
    public static final String SEND_MESSAGE = URL_BASE + "sendMessage/format/json/";
    public static final String GET_ALL_MESSAGE = URL_BASE + "getMessageList/format/json/";
    public static final String URL_SEMESTER_DATA = URL_BASE + "getSubjectList/format/json/";
    public static final String URL_UNIT_DATA = URL_BASE + "getUnitContentList/format/json/";
    public static final String URL_IMAGE = "http://www.inkpotonline.com/inkpot/profile_pic/";
    public static final String URL_ORDER_LIST = URL_BASE + "getOrderList";
    public static final String URL_VIEW_PROFILE = URL_BASE + "getStudentProfile";
    public static final String URL_UPDATE_PROFILE = URL_BASE + "updateprofile";
    public static final String URL_UPDATE_PROFILE_IMAGE = URL_BASE + "updateImg";
    public static final String URL_UPDATE_PASSWORD = URL_BASE + "UpdatePassword";
    public static final String URL_MY_COURSE = URL_BASE + "getMyCourse";
    public static final String URL_MY_ORDER_DETAILS = URL_BASE + "getOrderDetailList";
    public static final String URL_VIDEO = "http://www.inkpotonline.com/admin/courses/";
    public static final String URL_OFFER = URL_BASE + "offers/format/json/";
    public static final String URL_NOTIFICATION = URL_BASE + "notification";
    public static final String URL_NOTIFICATION_DETAIL = "http://inkpotonline.com/index.php/api/androidapi/notificationDes";
    public static final String get_taxs = baseurl + "paymentapi/tax/format/json/";
    public static final String URL_SUBJECT_DETAIL = URL_BASE + "getSubjectDetailList";
    public static final String URL_PRICE_LIST = URL_BASE + "getPrice";
    public static final String URL_STATE = URL_BASE + "state/format/json/";
    public static final String URL_UNIVERSITY = URL_BASE + "getUniversityList/format/json/";
    public static final String URL_COLLEGE = URL_BASE + "getCollegeList/format/json/";
    public static final String URL_COURSE = URL_BASE + "getCourseLisst";
    public static final String URL_FREE_VIDEO = URL_BASE + "getFreeVideosList";
    public static final String URL_TEST_CHAPTER = URL_BASE + "getChapterLists";
    public static final String URL_COUPON_SUBMIT = URL_BASE + "sendOfferMails";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static boolean appendNotificationMessages = true;
    // id to handle the notification in the notification try
    public static final String URL_SUBJECT_QUESTION_LIST = URL_BASE + "getTestList/format/json/";
    public static final String URL_SUBJECT_PROGRESS_LIST = URL_BASE + "getProgressReport/format/json/";
    public static final String URL_QUEST_LIST = URL_BASE + "setTest/format/json/";
    public static final String promocode_from_listing_url = baseurl + "Categoryapi/activepromocode/format/json/";
    public static final String OFFERS_LIST = "http://inkpotonline.com/index.php/api/androidapi/offers";
    public static final String check_promocode_from_server_url = baseurl + "Categoryapi/checkfrontuserapp/format/json/";
    public static final String generate_promocode_from_server_url = baseurl + "Categoryapi/promostudentinsert/format/json/";
    public static final String appy_boucher_or_coupon = baseurl + "Promoapi/checkusercodess/format/json/";
    public static final String get_payment = baseurl + "paymentapi/order/format/json/";
    public static final String get_success_token = baseurl + "paymentapi/ordermap/format/json/";
    public static final String get_success_after_token = baseurl + "paymentapi/orderdetails/format/json/";
    public static final String update_payment_status = baseurl + "paymentapi/orderstatus/format/json/";
    public static final String get_wallete_hostory = baseurl + "Topicapi/wallethistory/format/json/";
    public static final String get_wallete_amount = baseurl + "Promoapi/walletbalance/format/json/";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String KEY_USER_ID = "r_id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_REGTYPE = "r_regtype";
    public static final String KEY_USER_REGNAME = "r_username";
    public static final String KEY_USER_REGEMAIL = "r_email";
    public static final String KEY_USER_MOBILE = "r_mobile";
    public static final String KEY_USER_PASS = "r_pass";
    public static final String KEY_USER_IMAGE = "r_profile_pic";
    public static final String KEY_TOKEN_SERVER = "tokenToServer";
    public static final String KEY_USER_LAST_NAME = "l_name";
    public static final String KEY_COURSE_COLOR = "color";
    public static final String KEY_SUBJECT_ID = "subjectid";
    public static final String KEY_COURSE_NAME = "name";
    public static final String KEY_COURSE_ICON = "icon";
    public static final String KEY_CURRENT_SUB = "csub";
    public static final String KEY_SUB_COUNT = "subcount";
    public static final String KEY_COURSE_ID = "courseid";
    public static final String KEY_SEM = "semesterid";
    public static final String KEY_COMBO = "combo";
    public static final String KEY_TXN_ID = "taxnid";
    public static final String KEY_VIEW_USER_NAME = "username";
    public static final String KEY_VIEW_USER_FNAME = "firstname";
    public static final String KEY_VIEW_USER_MNAME = "middlename";
    public static final String KEY_VIEW_USER_LNAME = "lastname";
    public static final String KEY_VIEW_USER_EMAIL = "emailid";
    public static final String KEY_VIEW_USER_MOBILENO = "mobileno";
    public static final String KEY_VIEW_USER_STATEID = "stateid";
    public static final String KEY_VIEW_USER_UNIVERSITYID = "universityid";
    public static final String KEY_VIEW_USER_COLLEGEID = "collegeid";
    public static final String KEY_VIEW_USER_COURSEID = "courseid";
    public static final String KEY_VIEW_USER_PROFILEPIC = "profilepic";
    public static final String KEY_VIEW_USER_PNAME = "parentName";
    public static final String KEY_VIEW_USER_PEMAIL = "parentEmail";
    public static final String KEY_VIEW_USER_PMOBILE = "parentMobile";
    public static final String KEY_COUNT_VIDEOS = "videos";
    public static final String KEY_COUNT_PDF = "pdf";
    public static final String KEY_COUNT_MCQ = "mcq";
    public static final String KEY_VIEW_USER_IMAGE = "image";
    public static final String KEY_USER_PURCHASED = "purchased";
    public static final String VALIDATE_DATE = "validate_date";
    public static final String KEY_COURSE_SELECT_TYPE = "courseselecttype";
    public static final String KEY_CATEGORY_ID = "catid";
    public static final String NETWORK_ERROR = "Please connect to Internet!";
    public static final String login = baseurl + "mainapi/login/format/json/";
    public static final String KEY_INT = "int";
    public static final String URL_SUBJECTPROGRESS_LIST = "http://inkpotonline.com/index.php/api/androidapi/getProgressReport/format/json/";
    public static final String URL_SUBJECTQUESTION_LIST = "http://inkpotonline.com/index.php/api/androidapi/getTestList/format/json/";
    public static final String get_permissions = baseurl + "paymentapi/appstatus/format/json/";
    // Flag to identify whether to show single line or multi line text in push notification tray.
    public static boolean appendNotificationMessage = true;

    //Getting DevideID
    public static String deviceId(Context c) {
        return Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //n/w Connection check
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void displayToast(Context context, String s, UpdateProfileFragment activity) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void ShowToast(Context activity, String str) {
        if (str != null)
            Toast.makeText(activity, str, Toast.LENGTH_LONG).show();
    }

    // validate email address.
    public static boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    // RecyclerView OnItemTouchListener.
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView rv, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, rv.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

    }

    public static float getFloat(JSONObject jObject, String title) {
        try {
            return (float) jObject.getDouble(title);
        } catch (Exception ignored) {
        }
        return 0;
    }

    public static String getString(JSONObject jObject, String title) {
        try {
            return jObject.getString(title);
        } catch (Exception ignored) {
        }
        return null;
    }
}
