package com.simlim.mobileMod;

// Created by TanSiewLan2019
// Create a GamePage is an activity class used to hold the GameView which will have a surfaceview

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.List;

public class GamePage extends Activity {

    public static GamePage Instance = null;

    private GameView gameView;
    private ConstraintLayout container;

    public enum UI {
        TXT_SCORE,
        TXT_HSCORE,
        BTN_LEADERBOARD,
        BTN_SHARE,
        BTN_PAUSE,
        TXT_DRAWLINE
    };

    private TextView scoreText, highscoreText;
    private Button btnLeaderboard, btnShare, btnPause;
    private TextView drawALine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide titlebar
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);  // Hide topbar

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Instance = this;

        setContentView(R.layout.activity_game_scene);
        container = findViewById(R.id.container);

        scoreText = findViewById(R.id.score);
        highscoreText = findViewById(R.id.highscore);
        btnLeaderboard = findViewById(R.id.btn_leaderboard);
        btnShare = findViewById(R.id.btn_share);
        btnPause = findViewById(R.id.btn_pause);
        drawALine = findViewById(R.id.drawaline);

        final int childSize = container.getChildCount();
        for (int i = 0; i < childSize; ++i) {
            container.getChildAt(i).setElevation(1);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        container.removeView(gameView);
    }

    @Override
        protected void onResume() {
        super.onResume();
        gameView = new GameView(this);
        gameView.setId(View.generateViewId());
        gameView.setElevation(0);
        container.addView(gameView, 0);
        final int id = gameView.getId();
        ConstraintSet c = new ConstraintSet();
        c.clone(container);
        c.connect(id, ConstraintSet.START, container.getId(), ConstraintSet.START);
        c.applyTo(container);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
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

    public void HandleOnClick(View _v) {
        if (_v.getId() == R.id.btn_leaderboard) {
            Intent intent = new Intent();
            intent.setClass(this, Leaderboard.class);
            StateManager.Instance.ChangeState("Leaderboard");
            startActivity(intent);
            return;
        }
        else if (_v.getId() == R.id.btn_pause) {
            GameSystem.Instance.TogglePause();
            return;
        }
    }

    public void UpdateUIText(final UI type, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case TXT_SCORE:
                        scoreText.setText(text);
                        break;
                    case TXT_HSCORE:
                        highscoreText.setText(text);
                        break;
                    case TXT_DRAWLINE:
                        drawALine.setText(text);
                        break;
                }
            }
        });
    }


    public void UpdateUITextColor(final UI type, final int color) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case TXT_SCORE:
                        scoreText.setTextColor(color);
                        break;
                    case TXT_HSCORE:
                        highscoreText.setTextColor(color);
                        break;
                    case TXT_DRAWLINE:
                        drawALine.setTextColor(color);
                        break;
                }
            }
        });
    }

    public void ShowUI(final UI type, final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case TXT_SCORE:
                        scoreText.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case TXT_HSCORE:
                        highscoreText.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case BTN_LEADERBOARD:
                        btnLeaderboard.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case BTN_SHARE:
                        btnShare.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case BTN_PAUSE:
                        btnPause.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case TXT_DRAWLINE:
                        drawALine.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                }
            }
        });
    }
}

