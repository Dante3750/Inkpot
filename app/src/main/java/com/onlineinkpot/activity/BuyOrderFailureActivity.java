package com.onlineinkpot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.onlineinkpot.R;

public class BuyOrderFailureActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BuyOrderFailureActivity";
    private TextView tvRollBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_order_failure_activity);
        Log.d(TAG, "onCreate: ");
        tvRollBack = (TextView) findViewById(R.id.tv_conti);
        tvRollBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_conti:
                Intent i = new Intent(this, BuyCartActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
