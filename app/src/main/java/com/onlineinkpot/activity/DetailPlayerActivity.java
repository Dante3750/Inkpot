package com.onlineinkpot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.onlineinkpot.R;
import com.onlineinkpot.app.PrefManager;


/**
 * Created by USER on 9/5/2017.
 */

public class DetailPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DetailPlayerActivity";
    public VideoView videoView;
    private ProgressDialog progressDialog;
    private TextView text;
    PrefManager pref;
    private ImageView semIcon;
    RelativeLayout relativelayout;
    public String uid, topic_name;
   TextView novideostext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_player_activity);



        Intent in = getIntent();
        uid = in.getStringExtra("uid");
        topic_name = in.getStringExtra("topic");
        Log.d(TAG, "topic: " + topic_name);
        videoView = (VideoView) findViewById(R.id.videoView1);
        relativelayout = (RelativeLayout) findViewById(R.id.rl_test);

        text = (TextView) findViewById(R.id.micro);

        novideostext = (TextView) findViewById(R.id.novideostext);
        text.setText(topic_name);
        pref = new PrefManager(this);
        String a = getIntent().getStringExtra("all_subject");
        pref.setAllSub(a);
        ImageView iv = (ImageView) findViewById(R.id.iv_fullscreen);
        iv.setKeepScreenOn(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(DetailPlayerActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.setCancelable(false);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayoutt = (LinearLayout) findViewById(R.id.ll_top);
                LinearLayout linearLayoutb = (LinearLayout) findViewById(R.id.ll_buttom);
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_test);
                ImageView imageView = (ImageView) findViewById(R.id.iv_fullscreen1);
                ImageView imageView1 = (ImageView) findViewById(R.id.iv_fullscreen);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                linearLayoutt.setVisibility(View.GONE);
                linearLayoutb.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                imageView1.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }


        });
        ImageView ivv = (ImageView) findViewById(R.id.iv_fullscreen1);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        relativelayout.setOnClickListener(this);
        ivv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayoutt = (LinearLayout) findViewById(R.id.ll_top);
                LinearLayout linearLayoutb = (LinearLayout) findViewById(R.id.ll_buttom);
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_test);
                ImageView imageView = (ImageView) findViewById(R.id.iv_fullscreen1);
                ImageView imageView1 = (ImageView) findViewById(R.id.iv_fullscreen);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                linearLayoutt.setVisibility(View.VISIBLE);
                linearLayoutb.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
        });
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setKeepScreenOn(true);
        String url = in.getStringExtra("url");
        Uri uri = Uri.parse(url);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {


            public void onPrepared(MediaPlayer arg0) {
                videoView.start();
                progressDialog.dismiss();

            }
        });



        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener(){
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // inform the user something went wrong
               progressDialog.dismiss();
                novideostext.setVisibility(View.VISIBLE);


                AlertDialog alertDialog = new AlertDialog.Builder(
                        DetailPlayerActivity.this).create();
               // alertDialog.setTitle("Not Purchased");
                alertDialog.setMessage("No Video Uploaded");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        //in.putExtra("unitid", uid);
                        startActivity(in);
                    }
                });
                alertDialog.show();
             //   Toast.makeText(DetailPlayerActivity.this, "no video available", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        videoView.setMediaController(new MediaController(this) {
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    ((Activity) getContext()).finish();
                return super.dispatchKeyEvent(event);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (uid== null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Not Purchased");
            alertDialog.setMessage("Subject has to be purchased for the test to be available.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }

        else {
            Intent in = new Intent(getApplicationContext(), QuizActivityTopic.class);
            in.putExtra("unitid", uid);
            startActivity(in);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent in = new Intent(getApplicationContext(), MainActivity.class);
//        //in.putExtra("unitid", uid);
//        startActivity(in);
//
//
//
//    }
}


