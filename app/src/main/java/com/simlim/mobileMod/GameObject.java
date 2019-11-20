package com.simlim .mobileMod;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceView;

import java.lang.reflect.GenericArrayType;

public class GameObject implements EntityBase, Collidable {

    public Rect rect;
    public int color;

    private boolean isDone = false;
    private boolean isInit = false;
    private int renderLayer = 0;

    String type;
    float x, y, radius;

    @Override
    public boolean IsDone() {
        return this.isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone) {
        this.isDone = _isDone;
    }

    @Override
    public void Init(SurfaceView _view) {
        this.isInit = true;
    }

    @Override
    public void Update(float _dt) {

    }

    @Override
    public void Render(Canvas _canvas) {
        Bitmap bmp = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(color);

        Canvas c = new Canvas(bmp);
        c.drawRect(0, 0, rect.width(), rect.height(), paint);

        _canvas.drawBitmap(bmp, rect.left, rect.top, paint);
//        System.out.println("Rendering");
    }

    @Override
    public boolean IsInit() {
        return this.isInit;
    }

    @Override
    public int GetRenderLayer() {
        return this.renderLayer;
    }

    @Override
    public void SetRenderLayer(int _newLayer) {
        this.renderLayer = _newLayer;
    }

    @Override
    public String GetType() {
        return this.type;
    }

    @Override
    public float GetPosX() {
        return this.x;
    }

    @Override
    public float GetPosY() {
        return this.y;
    }

    @Override
    public float GetRadius() {
        return this.radius;
    }

    @Override
    public void OnHit(Collidable _other) {

    }
}