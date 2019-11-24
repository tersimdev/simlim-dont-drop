package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.SurfaceView;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Created by TanSiewLan2019

public class MainGameSceneState implements StateBase {

    public enum TileType {
        BLOCKED, PATH, TRASH, BIN;

        private static final List<TileType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static TileType random() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    };

    private float timer = 0.0f;

    private int mapWidth = 24;
    private int mapHeight = 24;
    private float mapOffsetX = 0.f;
    private float mapOffsetY = 0.f;
    private float tileSize = 0.f;
    private float padding = 0.f;

    private List<TileType> map = new ArrayList<>();
    private List<GameObject> mapObjects = new ArrayList<GameObject>();

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
        if (mapObjects.size() != 0) return;

        final float mapHalfWidth = (float)mapWidth * 0.5f;
        final float mapHalfHeight = (float)mapHeight * 0.5f;

        final float mapScreenWidth = (float)_view.getHeight() * 0.5f;
        tileSize = mapScreenWidth / (float)mapWidth;
        final float mapScreenHeight = tileSize * (float)mapHeight;

        mapOffsetX =  ((float)_view.getWidth() - mapScreenWidth) * 0.5f;
        mapOffsetY =  ((float)_view.getHeight() - mapScreenHeight) * 0.5f;

        for (int x = 0; x < mapWidth; ++x) {
            for (int y = 0; y < mapHeight; ++y) {
                final TileType type = TileType.random();
                map.add(type);

                final int posX = x * (int)tileSize + (int)mapOffsetX + (int)padding;
                final int posY = y * (int)tileSize + (int)mapOffsetY + (int)padding;
                final int size = (int)(tileSize - 2.f * padding);

                GameObject tile = new GameObject();

                switch (type) {
                    case BLOCKED:
                        tile.color = Color.BLACK;
                        break;
                    case PATH:
                        tile.color = Color.WHITE;
                        break;
                    case TRASH:
                        tile.color = Color.RED;
                        break;
                    case BIN:
                        tile.color = Color.GRAY;
                        break;
                    default:
                        break;
                }


                tile.rect = new Rect(posX, posY, posX + size, posY + size);
                EntityManager.Instance.AddEntity(tile);
                mapObjects.add(tile);
            }
        }
        
        player = new GameObject();
        player.color = Color.BLUE;
        player.rect = new Rect(0, 0, (int)(tileSize * 0.8f), (int)(tileSize * 0.8f));
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

            final int mapIndex = getScreenSpaceToMapIndex(x, y);
            if (mapIndex >= 0)
                mapObjects.get(mapIndex).color = Color.GREEN;

            PointF mapPosition = getScreenSpaceToMapPosition(x, y);

            player.SetPosX(mapPosition.x * tileSize + mapOffsetX + tileSize * 0.5f);
            player.SetPosY(mapPosition.y * tileSize + mapOffsetY + tileSize * 0.5f);
        }
    }

    private int getMapIndex(int _x, int _y) {
        if (_x < 0 || _x >= mapWidth || _y < 0 || _y >= mapHeight)
            return -1;

        return _x * mapHeight + _y;
    }

    private int getScreenSpaceToMapIndex(float _x, float _y) {
        final PointF position = getScreenSpaceToMapPosition(_x, _y);
        return getMapIndex((int)position.x, (int)position.y);
    }

    private PointF getScreenSpaceToMapPosition(float _x, float _y) {
        return new PointF(
                (float)Math.floor((_x - mapOffsetX) / tileSize),
                (float) Math.floor((_y - mapOffsetY) / tileSize)
        );
    }

}



