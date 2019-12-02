package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.SurfaceView;

import java.util.Random;

import androidx.core.content.res.ResourcesCompat;

import static android.content.Context.VIBRATOR_SERVICE;

// Created by TanSiewLan2019

public class MainGameSceneState implements StateBase {
    //init at OnEnter
    private GamePage gamePage;
    private GameView gameView;
    private PointF center = new PointF();
    private int width, height;
    private boolean gameOver = true;

    private float timer = 0.0f;
    private float[] bounceTime = {0.5f,3.f};
    private boolean isDown = false;
    private int score = 0;
    private int highscore = -1;

    private Line line = new Line();
    private Sprite circle = new Sprite();
    private Pickup[] pickups = new Pickup[10];

    public MainGameSceneState() { }

    @Override
    public String GetName() {
        return "MainGame";
    }

    @Override
    public void OnEnter(SurfaceView _view) {

        //get handles to other views
        gameView = (GameView) _view;
        gamePage = (GamePage) gameView.context;

        //hide and show views
        gamePage.ShowUI(GamePage.UI.BTN_LEADERBOARD, false);
        gamePage.ShowUI(GamePage.UI.BTN_SHARE, false);
        gamePage.ShowUI(GamePage.UI.TXT_DRAWLINE, true);

        if (highscore < 0)
            gamePage.ShowUI(GamePage.UI.TXT_HSCORE, false);

        //initialise scores
        gamePage.UpdateUIText(GamePage.UI.TXT_SCORE, Integer.toString(score));
        final String prefix = gameView.getResources().getString(R.string.highscore);
        gamePage.UpdateUIText(GamePage.UI.TXT_HSCORE, prefix + " " + Integer.toString(highscore));

        width = _view.getWidth();
        height = _view.getHeight();

        center.set(width * 0.5f, height * 0.5f);
        final float size = width * 0.9f;
        final float halfSize = size * 0.5f;

        GameObject back = new GameObject();
        back.color = ResourcesCompat.getColor(gameView.getResources(), R.color.BLACK, null);//
        back.rect.top = (int)(center.y - halfSize);
        back.rect.bottom = (int)(center.y + halfSize);
        back.rect.left = (int)(center.x - halfSize);
        back.rect.right = (int)(center.x + halfSize);
        EntityManager.Instance.AddEntity(back);


        int borderColor = ResourcesCompat.getColor(gameView.getResources(), R.color.MAIN, null);
        float borderWidth = 20.f;

        Line top = new Line();
        top.tag = "boundary";
        top.style = Paint.Style.STROKE;
        top.color = borderColor;
        top.strokeWidth = borderWidth;
        top.setStart(new PointF(0.f, 0.f));
        top.setEnd(new PointF(size + borderWidth, 0.f));
        top.setCenter(new PointF(center.x, center.y - halfSize));
        EntityManager.Instance.AddEntity(top);

        Line bottom = new Line();
        bottom.tag = "boundary";
        bottom.style = Paint.Style.STROKE;
        bottom.color = borderColor;
        bottom.strokeWidth = borderWidth;
        bottom.setStart(new PointF(0.f, 0.f));
        bottom.setEnd(new PointF(size + borderWidth, 0.f));
        bottom.setCenter(new PointF(center.x, center.y + halfSize));
        EntityManager.Instance.AddEntity(bottom);

        Line left = new Line();
        left.tag = "boundary";
        left.style = Paint.Style.STROKE;
        left.color = borderColor;
        left.strokeWidth = borderWidth;
        left.setStart(new PointF(0.f, 0.f));
        left.setEnd(new PointF(0.f, size + borderWidth));
        left.setCenter(new PointF(center.x - halfSize, center.y));
        EntityManager.Instance.AddEntity(left);

        Line right = new Line();
        right.tag = "boundary";
        right.style = Paint.Style.STROKE;
        right.color = borderColor;
        right.strokeWidth = borderWidth;
        right.setStart(new PointF(0.f, 0.f));
        right.setEnd(new PointF(0.f, size + borderWidth));
        right.setCenter(new PointF(center.x + halfSize, center.y));
        EntityManager.Instance.AddEntity(right);

        circle.color = Color.WHITE;
        circle.SetRadius(50.f);
        circle.setKinematic(false);
        circle.SetCenterX(center.x);
        circle.SetCenterY(center.y);
        circle.SetResourceId(R.drawable.sprites);
        for (int y = 0; y < 4; ++y) {
            for (int x = 0; x < 4; ++x) {
                circle.animation.frames.add(new Keyframe(1.f, x, y, 1, 1, 4, 4));
            }
        }

        circle.tag = "ball";
        circle.onHitCallBack = new Callback() {
            @Override
            public void doThing(GameObject target) {
                String tag = target.tag;

                Vibrate();
                if (tag.equals("pLine")) {
                    ++score;
                    gamePage.UpdateUIText(GamePage.UI.TXT_SCORE, Integer.toString(score));
                    target.active = false;
                } else if (tag.equals("pickup")) {
                    score += 2;
                    gamePage.UpdateUIText(GamePage.UI.TXT_SCORE, Integer.toString(score));
                } else if (tag.equals("boundary")) {
                    OnGameEnd();
                }
            }
        };
        EntityManager.Instance.AddEntity(circle);

        line.style = Paint.Style.STROKE;
        line.color = ResourcesCompat.getColor(gameView.getResources(), R.color.WHITE, null);
        line.strokeWidth = 10.f;
        line.setStart(new PointF(width * 0.3f, height * 0.7f));
        line.setEnd(new PointF(width * 0.7f, height * 0.7f));
        line.tag = "pLine";
        line.active = false;
        line.setValid(false);
        line.invalidCol = Color.DKGRAY;
        EntityManager.Instance.AddEntity(line);

        for (int i = 0; i < pickups.length; ++i) {
            Pickup go = new Pickup();
            go.color = ResourcesCompat.getColor(gameView.getResources(), R.color.POWERUP, null);
            go.SetRadius(20);
            EntityManager.Instance.AddEntity(go);
            go.tag = "pickup";
            go.active = false;
            pickups[i] = go;
        }
    }

    @Override
    public void OnExit() {
        EntityManager.Instance.Clean();
        GamePage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas) {
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void Update(float _dt) {
        EntityManager.Instance.Update(_dt);

        timer += _dt;

        if (bounceTime[0] < timer) {
            bounceTime[0] = timer + 1.0f;
        } else if (bounceTime[1] < timer) {
            bounceTime[1] = timer + 3.0f;
            Random rand = new Random();
            // * (0.7 - 0.3) + 0.3
            if (!gameOver) {
                SpawnPickup(width * (rand.nextFloat() * 0.4f + 0.3f), height * (rand.nextFloat() * 0.4f + 0.3f));
            }
        }

        if (gameOver) {
            if ((int)(timer) % 2 == 0) {
                gamePage.UpdateUITextColor(GamePage.UI.TXT_DRAWLINE, Color.WHITE);
            } else {
                gamePage.UpdateUITextColor(GamePage.UI.TXT_DRAWLINE, Color.GRAY);
            }
        }

        if (TouchManager.Instance.HasTouch()) {
            final float x = TouchManager.Instance.GetPosX();
            final float y = TouchManager.Instance.GetPosY();

            if (!isDown) {
                line.active = true;
                line.setStart(new PointF(x, y));
                isDown = true;

                if (gameOver)
                    OnGameStart();
            }

            line.setEnd(new PointF(x, y));
        } else {
            isDown = false;
        }

        line.setValid(!isDown); //comment this line out if want line to collide w ball when drawing
    }

    private void SpawnPickup(float _x, float _y) {
        int numActive = 0;
        Pickup ref = null;
        for (Pickup go : pickups) {
            if (go.active)
                ++numActive;
            else
                ref = go;
        }

        if (numActive < 5 && ref != null) {
            ref.active = true;
            ref.SetCenterX(_x);
            ref.SetCenterY(_y);
        }
    }

    public void Vibrate() {
        final short dura;
        final float speedSqr = PointFOps.lenSqr(circle.getVelocity());
        if (speedSqr >= circle.maxSpeedHard * circle.maxSpeedHard * 0.81f)
            dura = 50;
        else
            dura = 10;

        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) gameView.context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(dura, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) gameView.context.getSystemService(VIBRATOR_SERVICE)).vibrate(dura);
        }

        System.out.println("Vibrated");
    }

    //call me when game ends
    public void OnGameEnd() {
        gameOver = true;

        circle.ResetKinematic();
        circle.SetCenterX(center.x);
        circle.SetCenterY(center.y);
        circle.setKinematic(false);

        line.active = false;
        for (Pickup p : pickups) {
            p.active = false;
        }

        if (score > highscore) {
            highscore = score;
            final String prefix = gameView.getResources().getString(R.string.highscore);
            gamePage.UpdateUIText(GamePage.UI.TXT_HSCORE, prefix + " " + Integer.toString(highscore));
        }

        gamePage.ShowUI(GamePage.UI.BTN_LEADERBOARD, true);
        gamePage.ShowUI(GamePage.UI.BTN_SHARE, true);
        gamePage.ShowUI(GamePage.UI.TXT_DRAWLINE, true);
        gamePage.ShowUI(GamePage.UI.TXT_HSCORE, true);
    }

    private void OnGameStart() {
        gameOver = false;
        score = 0;

        gamePage.ShowUI(GamePage.UI.BTN_LEADERBOARD, false);
        gamePage.ShowUI(GamePage.UI.BTN_SHARE, false);
        gamePage.ShowUI(GamePage.UI.TXT_DRAWLINE, false);
        gamePage.ShowUI(GamePage.UI.TXT_HSCORE, false);

        circle.setKinematic(true);
        line.active = true;
    }
}



