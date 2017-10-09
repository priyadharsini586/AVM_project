package com.hexaenna.avm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    LinearLayout imgSplash;
    Animation imgAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);
        imgSplash = (LinearLayout) findViewById(R.id.ldtSplash);
        imgAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_splash);
        imgSplash.startAnimation(imgAnimation);
        imgAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                 new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //* Create an Intent that will start the Menu-Activity. *//*
                Intent mainIntent = new Intent(SplashActivity.this,RegistrationActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

       /* new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*
                Intent mainIntent = new Intent(SplashActivity.this,RegistrationActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/
    }
}