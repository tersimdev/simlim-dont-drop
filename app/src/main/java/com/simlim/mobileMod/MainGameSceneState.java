package com.simlim.mobileMod;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.service.quicksettings.Tile;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewDebug;
import android.widget.TextView;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Created by TanSiewLan2019

public class MainGameSceneState implements StateBase {

    private float timer = 0.0f;
    private float bounceTime = timer;

    int width, height; //init at OnEnter

    private boolean isDown = false;
    private Line line = new Line();
    private TextView scoreText, highscoreText;
    private float score = 0.f;
    private float highscore = -1.f;

    private Pickup[] pickups = new Pickup[10];

    public MainGameSceneState() {
    }

    @Override
    public String GetName() {
        return "MainGame";
    }

    @Override
    public void OnEnter(SurfaceView _view) {

        //get handles to other views
        GameView gameView = (GameView) _view;
        for (View v : gameView.childViews) {
            if (v.getId() == R.id.score)
                scoreText = (TextView) v;
            else if (v.getId() == R.id.highscore)
                highscoreText = (TextView) v;
        }

        //initialise scores
        scoreText.setText(Integer.toString((int)score));
        highscoreText.setText("0");

        width = _view.getWidth();
        height = _view.getHeight();

        GameObject box = new GameObject();
        box.style = Paint.Style.STROKE;
        box.color = Color.RED;
        box.strokeWidth = 10.f;
        box.SetWidth(width * 0.9f);
        box.SetHeight(width * 0.9f);
        box.SetCenterX(width * 0.5f);
        box.SetCenterY(height * 0.5f);
        box.tag = "boundary";
        EntityManager.Instance.AddEntity(box);

        PhysicsObj circle = new PhysicsObj();
        circle.color = Color.WHITE;
        circle.SetWidth(width * 0.05f);
        circle.SetHeight(width * 0.05f);
        circle.SetCenterX(width * 0.5f);
        circle.SetCenterY(height * 0.5f);
        circle.tag = "ball";
        EntityManager.Instance.AddEntity(circle);

        line.style = Paint.Style.STROKE;
        line.color = Color.WHITE;
        line.strokeWidth = 10.f;
        line.setStart(new PointF(width * 0.3f, height * 0.7f));
        line.setEnd(new PointF(width * 0.7f, height * 0.7f));
        line.tag = "pLine";
        EntityManager.Instance.AddEntity(line);

        for (int i = 0; i < pickups.length; ++i) {

            Pickup go = new Pickup();
            go.color = Color.BLUE;
            go.SetWidth(width * 0.05f);
            go.SetHeight(width * 0.05f);
            go.SetCenterX(width * 0.5f);
            go.SetCenterY(height * 0.5f - 200);
            EntityManager.Instance.AddEntity(go);
            go.tag = "pickup" + Integer.toString(i);
            go.active = false;
            pickups[i] = go;
        }
        pickups[0].active = true;
    }

    @Override
    public void OnExit() {
        EntityManager.Instance.Clean();
        GamePage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void Update(float _dt) {
        EntityManager.Instance.Update(_dt);

        score += _dt;
        timer += _dt;

        for (int i = 0; i < pickups.length; ++i) {
            if (pickups[i].gotten) {
                pickups[i].gotten = false;
                score += 10;
            }
        }

        if (bounceTime <= timer) {
            bounceTime = timer + 1.0f;
//            for (Pickup go : pickups) {
//                if (!go.active) {
//                    go.active = true;
//                }
//            }
            scoreText.setText(Integer.toString((int)score));
        }

        if (TouchManager.Instance.HasTouch()) {
            final float x = TouchManager.Instance.GetPosX();
            final float y = TouchManager.Instance.GetPosY();

            if (!isDown) {
                line.setStart(new PointF(x, y));
                isDown = true;
            }

            line.setEnd(new PointF(x, y));
        } else {
            isDown = false;
        }
    }

    //call me when game ends
    private void OnGameEnd() {
        if (score > highscore) {
            highscore = score;
            highscoreText.setText(Integer.toString((int)highscore));
        }
    }

}



