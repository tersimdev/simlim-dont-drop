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

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Created by TanSiewLan2019

public class MainGameSceneState implements StateBase {

    private float timer = 0.0f;

    boolean isDown = false;
    Line line = new Line();

    @Override
    public String GetName() {
        return "MainGame";
    }

    @Override
    public void OnEnter(SurfaceView _view) {
        final int width = _view.getWidth();
        final int height = _view.getHeight();

        GameObject box = new GameObject();
        box.style = Paint.Style.STROKE;
        box.color = Color.RED;
        box.strokeWidth = 10.f;
        box.SetWidth(width * 0.9f);
        box.SetHeight(width * 0.9f);
        box.SetCenterX(width * 0.5f);
        box.SetCenterY(height * 0.5f);
        EntityManager.Instance.AddEntity(box);

        GameObject circle = new GameObject();
        circle.color = Color.WHITE;
        circle.SetWidth(width * 0.05f);
        circle.SetHeight(width * 0.05f);
        circle.SetCenterX(width * 0.5f);
        circle.SetCenterY(height * 0.5f);
        EntityManager.Instance.AddEntity(circle);

        line.style = Paint.Style.STROKE;
        line.color = Color.WHITE;
        line.strokeWidth = 10.f;
        line.start.x = width * 0.3f;
        line.start.y = height * 0.7f;
        line.end.x = width * 0.7f;
        line.end.y = height * 0.7f;
        EntityManager.Instance.AddEntity(line);
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

        if (TouchManager.Instance.HasTouch()) {
            final float x = TouchManager.Instance.GetPosX();
            final float y = TouchManager.Instance.GetPosY();

            if (!isDown) {
                line.start.x = x;
                line.start.y = y;
                isDown = true;
            }

            line.end.x = x;
            line.end.y = y;
        } else {
            isDown = false;
        }
    }


}



