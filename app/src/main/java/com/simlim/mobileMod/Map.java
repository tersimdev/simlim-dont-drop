package com.simlim.mobileMod;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map {

    public enum TileType {
        BLOCKED, PATH, TRASH, BIN;

        private static final List<TileType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static TileType random() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    };

    private SurfaceView parent;

    private int width;
    private int height;

    private PointF offset = new PointF();
    private float tileSize;
    private float padding;

    private List<TileType> tiles = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<GameObject>();

    public Map(int _width, int _height, SurfaceView _view) {
        parent = _view;
        width = _width;
        height = _height;

        final float mapScreenWidth = (float) _view.getHeight() * 0.5f;
        tileSize = mapScreenWidth / (float) width;
        final float mapScreenHeight = tileSize * (float) height;

        offset.x = ((float) _view.getWidth() - mapScreenWidth) * 0.5f;
        offset.y = ((float) _view.getHeight() - mapScreenHeight) * 0.5f;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                tiles.add(TileType.BLOCKED);
            }
        }

        PointF direction = new PointF();
        direction.x = 0;
        direction.y = 1;

        tiles.set(0, TileType.PATH);

        generatePath(tiles, new PointF(), direction, 0, 10);

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                final int posX = x * (int)tileSize + (int)offset.x + (int)padding;
                final int posY = y * (int)tileSize + (int)offset.y + (int)padding;
                final int size = (int)(tileSize - 2.f * padding);

                GameObject tile = new GameObject();
                tile.color = getColor(getIndex(x, y));
                tile.rect = new Rect(posX, posY, posX + size, posY + size);
                tile.SetResourceId(R.drawable.back);
                EntityManager.Instance.AddEntity(tile);
                objects.add(tile);
            }
        }
    }

    private void generatePath(List<TileType> mapRef, PointF position, PointF direction, int depth, int min) {
        Random random = new Random();

        ++depth;

        if (random.nextBoolean() || depth < min) {
            final PointF newPosition = setPath(mapRef, position, direction);

            if (newPosition.x != -1.f) {
                generatePath(mapRef, newPosition, direction, depth, min);
            }
        }

        if (random.nextBoolean()) {
            PointF newDirection = new PointF();
            newDirection.x = direction.y;
            newDirection.y = direction.x;

            final PointF newPosition = setPath(mapRef, position, newDirection);

            if (newPosition.x != -1.f) {
                generatePath(mapRef, newPosition, newDirection, depth, min);
            }
        }

        if (random.nextBoolean()) {
            PointF newDirection = new PointF();
            newDirection.x = -direction.y;
            newDirection.y = -direction.x;

            final PointF newPosition = setPath(mapRef, position, newDirection);

            if (newPosition.x != -1.f) {
                generatePath(mapRef, newPosition, newDirection, depth, min);
            }
        }
    }

    private PointF setPath(List<TileType> mapRef, PointF position, PointF direction) {
        PointF newPosition = new PointF();
        newPosition.x = position.x;
        newPosition.y = position.y;

        for (int i = 0; i < 5; ++i) {
            newPosition = add(newPosition, direction);
            int index = getIndex(newPosition);
            if (index >= 0 && mapRef.get(index) == TileType.BLOCKED) {
                if (new Random().nextInt(10) == 0) {
                    mapRef.set(index, TileType.TRASH);
                } else {
                    mapRef.set(index, TileType.PATH);
                }
            } else {
                newPosition.x = -1.f;
                newPosition.y = -1.f;
                break;
            }
        }

        return newPosition;
    }

    public void Reset(List<Integer> path) {
        for (int i: path) {
            GameObject go = getGameObject(i);
            go.color = getColor(i);
        }
    }

    public GameObject getGameObject(int _index) {
        return objects.get(_index);
    }

    public TileType getTile(int _index) {
        return tiles.get(_index);
    }

    public float getTileSize() {
        return tileSize;
    }

    public int getIndex(int _x, int _y) {
        if (_x < 0 || _x >= width || _y < 0 || _y >= height)
            return -1;

        return _x * height + _y;
    }

    public int getIndex(PointF _p) {
        final int x = (int)_p.x;
        final int y = (int)_p.y;

        if (x < 0 || x >= width || y < 0 || y >= height)
            return -1;

        return x * height + y;
    }

    public int getScreenSpaceToMapIndex(float _x, float _y) {
        final PointF position = getScreenSpaceToMapPosition(_x, _y);
        return getIndex((int)position.x, (int)position.y);
    }

    public PointF getScreenSpaceToMapPosition(float _x, float _y) {
        return new PointF(
                (float)Math.floor((_x - offset.x) / tileSize),
                (float) Math.floor((_y - offset.y) / tileSize)
        );
    }

    public PointF getTilePosition(float _x, float _y) {
        PointF position = new PointF(
                (float)Math.floor((_x - offset.x) / tileSize) * tileSize,
                (float) Math.floor((_y - offset.y) / tileSize) * tileSize
        );
        return add(position, offset);

    }

    private int getColor(int idx) {
        switch (getTile(idx)) {
            case BLOCKED:
                return Color.BLACK;
            case PATH:
                return Color.WHITE;
            case  TRASH:
                return Color.RED;
            case BIN:
                return Color.GRAY;
            default:
                return Color.YELLOW;
        }
    }

    public PointF add(PointF a, PointF b) {
        PointF p = new PointF();
        p.x = a.x + b.x;
        p.y = a.y + b.y;
        return p;
    }

}
