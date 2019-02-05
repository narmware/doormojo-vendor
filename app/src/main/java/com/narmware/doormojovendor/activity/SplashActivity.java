package com.narmware.doormojovendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.narmware.doormojovendor.R;
import com.narmware.doormojovendor.helper.SharedPreferencesHelper;


import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {
    private static int TIMEOUT = 2000;
    ImageView mImgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        /*YoYo.with(Techniques.Bounce)
                .duration(400)
                .playOn(mImgLogo);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(SharedPreferencesHelper.getIsLogin(SplashActivity.this)==false)
                {
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, TIMEOUT);
    }
}
