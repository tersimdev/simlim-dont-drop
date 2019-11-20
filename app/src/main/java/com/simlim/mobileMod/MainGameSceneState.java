package com.simlim.mobileMod;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.method.Touch;
import android.view.SurfaceView;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

// Created by TanSiewLan2019

public class MainGameSceneState implements StateBase {
    private float timer = 0.0f;

    private int mapWidth = 8;
    private int mapHeight = 12;
    private List<GameObject> map = new ArrayList<GameObject>();


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
        final float mapHalfWidth = (float)mapWidth * 0.5f;
        final float mapHalfHeight = (float)mapHeight * 0.5f;

        final float mapScreenWidth = (float)_view.getHeight() * 0.5f;
        final float tileSize = mapScreenWidth / (float)mapWidth;
        final float mapScreenHeight = tileSize * (float)mapHeight;

        final float mapX =  ((float)_view.getWidth() - mapScreenWidth) * 0.5f;
        final float mapY =  ((float)_view.getHeight() - mapScreenHeight) * 0.5f;

        for (int x = 0; x < mapWidth; ++x) {
            for (int y = 0; y < mapHeight; ++y) {
                final int posX = x * (int)tileSize + (int)mapX;
                final int posY = y * (int)tileSize + (int)mapY;

                GameObject tile = new GameObject();
                tile.color = Color.RED;
                tile.rect = new Rect(posX, posY, posX + (int)(tileSize * 0.8f), posY + (int)(tileSize * 0.8f));
                EntityManager.Instance.AddEntity(tile);
                map.add(tile);
            }
        }
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

        if (TouchManager.Instance.IsDown()) {
			
            //Example of touch on screen in the main game to trigger back to Main menu
            float x = TouchManager.Instance.GetPosX() - map.get(0).GetPosX();
            float y = TouchManager.Instance.GetPosY() - map.get(0).GetPosY();
            if (Math.abs(x) < 100 && Math.abs(y) < 100)
                System.out.println(Float.toString(x) + ", " + Float.toString(y));
        }
    }
}



