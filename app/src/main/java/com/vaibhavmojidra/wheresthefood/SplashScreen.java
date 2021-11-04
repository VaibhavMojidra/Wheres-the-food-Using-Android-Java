package com.vaibhavmojidra.wheresthefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    String lst="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIND", Context.MODE_PRIVATE);
                String lst=pref.getString("LOGIN_STATUS", null);
                if(lst==null){
                    startActivity(new Intent(SplashScreen.this,LROptionsPage.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                else{
                    if(lst.equalsIgnoreCase("LOGGEDIN")){
                        startActivity(new Intent(SplashScreen.this,MainScreen.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }else{
                        startActivity(new Intent(SplashScreen.this,LROptionsPage.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                }

            }
        },2000);
    }
}
