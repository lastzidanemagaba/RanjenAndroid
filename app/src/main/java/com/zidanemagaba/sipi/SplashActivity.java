package com.zidanemagaba.sipi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ahmadrofiul.sipi.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //ngecek apakah sudah pernah login
                //mengambil nilai di shared preferences
                SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
                boolean sudah_login = share.getBoolean("login", false);
                if(sudah_login){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 1250);

    }
}
