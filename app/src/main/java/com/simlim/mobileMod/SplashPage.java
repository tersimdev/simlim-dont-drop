package com.simlim.mobileMod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashPage extends Activity {

    protected boolean active = true;
    protected int splashTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_page);

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    int waited = 0;
                    while (active && waited < splashTime) {
                        sleep(200);
                        if (active) {
                            waited += 200;
                        }
                    }
                }
                catch (InterruptedException e) {

                }
                finally {
                    finish();
                    Intent i = new Intent(SplashPage.this, MainMenu.class);
                    startActivity(i);


                }
            }
        };

        splashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            active = false;

        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
