package com.onlineinkpot.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onlineinkpot.app.AppController;
import com.onlineinkpot.app.Cons;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 7/26/2017.
 */

public class FCMIntentService extends IntentService {
    private static final String TAG = FCMIntentService.class.getSimpleName();

    public FCMIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // register with FCM
        registerFCM();
    }

    // Registering FCM and obtaining fcm registration token.
    private void registerFCM() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "FCM registration token : " + token);

        // sending registration id to our server.
        sendRegistrationToServer(token);
        AppController.getInstance().getPref().setTokenToServer(true);

        // Notify the UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        StringRequest strReq = new StringRequest(Request.Method.POST, Cons.UPDATE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", AppController.getInstance().getPref().getCustId());
                params.put("fcm_token", token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }
}