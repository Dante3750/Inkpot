package com.onlineinkpot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.onlineinkpot.R;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;


public class WelcomeSplashActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeSplashActivity";
    public static final int SPLASH_TIMEOUT = 3000;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomea_splash_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#000000"));
        Log.d(TAG, "onCreate: " + Cons.deviceId(getApplicationContext()));
        pref = new PrefManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.isLoggedIn()) {
                    Intent i = new Intent(WelcomeSplashActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    startActivity(new Intent(WelcomeSplashActivity.this, WelcomeActivity.class));
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        }, SPLASH_TIMEOUT);
    }

    @Override
    public void onBackPressed() {
        System.exit(1);
    }
}
