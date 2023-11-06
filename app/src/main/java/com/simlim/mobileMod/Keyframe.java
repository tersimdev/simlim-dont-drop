package com.simlim.mobileMod;

import android.graphics.PointF;

public class Keyframe {

    public float duration;

    private PointF tilemapUnit = new PointF();
    private PointF cellSize = new PointF();
    private PointF cellPosition = new PointF();

    public PointF uvPosition = new PointF();
    public PointF uvSize = new PointF();

    Keyframe(float _duration, int x, int y, int width, int height, int mapWidth, int mapHeight) {
        duration = _duration;

        tilemapUnit.x = 1.f / (float) mapWidth;
        tilemapUnit.y = 1.f / (float) mapHeight;

        cellPosition.set(x, y);
        cellSize.set(width, height);

        uvPosition = PointFOps.mul(cellPosition, tilemapUnit);
        uvSize = PointFOps.mul(cellSize, tilemapUnit);
    }

    public void SetTilemapSize(int width, int height) {
        tilemapUnit.x = 1.f / (float) width;
        tilemapUnit.y = 1.f / (float) height;

        uvPosition = PointFOps.mul(cellPosition, tilemapUnit);
        uvSize = PointFOps.mul(cellSize, tilemapUnit);
    }

    public void SetCellRect(int x, int y, int width, int height) {
        cellPosition.set(x, y);
        cellSize.set(width, height);

        uvPosition = PointFOps.mul(cellPosition, tilemapUnit);
        uvSize = PointFOps.mul(cellSize, tilemapUnit);
    }

}
