package com.simlim.mobileMod;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

    private Map map;

    private GameObject player;

    @Override
    public String GetName() {
        return "MainGame";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {

//        RenderBackground.Create();
        // Example to include another Renderview
        // for Pause Button

        // Generating map
        map = new Map(30, 30, _view);

        player = new GameObject();
        player.color = Color.BLUE;
        player.rect = new Rect(0, 0, (int)(map.getTileSize() * 0.8f), (int)(map.getTileSize() * 0.8f));
        EntityManager.Instance.AddEntity(player);
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
            //Example of touch on screen in the main game to trigger back to Main menu
            final float x = TouchManager.Instance.GetPosX();
            final float y = TouchManager.Instance.GetPosY();

            final int mapIndex = map.getScreenSpaceToMapIndex(x, y);
            if (mapIndex >= 0)
                map.getGameObject(mapIndex).color = Color.GREEN;

            PointF tilePosition = map.getTilePosition(x, y);

            player.SetPosX(tilePosition.x);
            player.SetPosY(tilePosition.y);
        }
    }


}



