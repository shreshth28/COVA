package com.shreshth.cova.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shreshth.cova.R;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_news);
    }

}
