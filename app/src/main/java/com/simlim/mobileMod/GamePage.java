package com.simlim.mobileMod;

// Created by TanSiewLan2019
// Create a GamePage is an activity class used to hold the GameView which will have a surfaceview

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.api.Distribution;

import java.util.ArrayList;
import java.util.List;

public class GamePage extends Activity {

    public static GamePage Instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide titlebar
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);  // Hide topbar

        Instance = this;

        setContentView(R.layout.activity_game_scene);
        ConstraintLayout container = findViewById(R.id.container);

        List<View> childViews = new ArrayList<>();

        TextView tmp;
        tmp = findViewById(R.id.score);
        childViews.add(tmp);
        tmp = findViewById(R.id.highscore);
        childViews.add(tmp);

        for (View v : childViews) {
            v.setElevation(1);
        }

        GameView gameView = new GameView(this, childViews);
        container.addView(gameView);
        container.setElevation(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // WE are hijacking the touch event into our own system
        int x = (int) event.getX();
        int y = (int) event.getY();

        TouchManager.Instance.Update(x, y, event.getAction());

        return true;
    }

    public void backToMainMenu(View view) {
        StateManager.Instance.ChangeState("Mainmenu");
    }
}

