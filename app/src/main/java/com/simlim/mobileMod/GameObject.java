package com.simlim .mobileMod;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceView;

import java.lang.reflect.GenericArrayType;

public class GameObject implements EntityBase { //, Collidable {

    public Rect rect;
    public int color;

    private Bitmap bmp = null;

    private SurfaceView holder;

    private boolean isDone = false;
    private boolean isInit = false;
    private int renderLayer = 0;

    String type;
    float radius;

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
        this.holder = _view;
    }

    @Override
    public void Update(float _dt) { }

    @Override
    public void Render(Canvas _canvas) {
        Rect src = new Rect();
        src.left = 0;
        src.top = 0;
        src.right = bmp.getWidth();
        src.bottom = bmp.getHeight();

        System.out.println("Src : " + src);
        System.out.println("Dst : " + rect);

        _canvas.drawBitmap(bmp, src, rect, null);

        System.out.println("Rendering");
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

    public void SetResourceId(int _resourceId) {
        bmp = BitmapFactory.decodeResource(holder.getResources(), _resourceId);
    }

    public void SetPosX(float _x) {
        final float halfWidth = ((float)rect.right - (float)rect.left) * 0.5f;
        rect.left = (int)(_x - halfWidth);
        rect.right = (int)(_x + halfWidth);
    }

    public void SetPosY(float _y) {
        final float halfHeight = ((float)rect.bottom - (float)rect.top) * 0.5f;
        rect.top = (int)(_y - halfHeight);
        rect.bottom = (int)(_y + halfHeight);
    }

//    @Override
//    public String GetType() {
//        return this.type;
//    }
//
//    @Override
//    public float GetPosX() { return this.rect.centerX(); }
//
//    @Override
//    public float GetPosY() {
//        return this.rect.centerY();
//    }
//
//    @Override
//    public float GetRadius() {
//        return this.radius;
//    }
//
//    @Override
//    public void OnHit(Collidable _other) {
//
//    }
}