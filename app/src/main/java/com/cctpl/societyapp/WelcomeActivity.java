package com.cctpl.societyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.cctpl.societyapp.utils.Constant;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.USER_DATA,0);
                String LOGGED_IN = sharedPreferences.getString(Constant.LOGGED_IN,null);
                if (!TextUtils.isEmpty(LOGGED_IN)){
                    if (LOGGED_IN.equalsIgnoreCase("true")){
                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(WelcomeActivity.this,RegisterActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(WelcomeActivity.this,RegisterActivity.class));
                    finish();
                }

            }
        },2000);

    }
}