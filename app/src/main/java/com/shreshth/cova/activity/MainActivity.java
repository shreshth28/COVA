package com.shreshth.cova.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.shreshth.cova.R;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent DashboardIntent=new Intent(MainActivity.this, DashboardActivity.class);

                startActivity(DashboardIntent);
                finish();
            }
        },SPLASH_SCREEN_TIME);
    }
}
