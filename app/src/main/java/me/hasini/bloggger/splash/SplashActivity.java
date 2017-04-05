package me.hasini.bloggger.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.hasini.bloggger.R;
import me.hasini.bloggger.home.HomeActivity;
import me.hasini.bloggger.lib.models.AppSession;
import me.hasini.bloggger.lib.preference.PreferenceManager;
import me.hasini.bloggger.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceManager = PreferenceManager.getInstance(this.getApplicationContext());

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                AppSession session = preferenceManager.getSession();

                Intent intent;
                if(session != null && session.getExpiration() > System.currentTimeMillis()) {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    }

