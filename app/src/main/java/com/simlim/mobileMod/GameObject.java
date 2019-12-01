package com.simlim .mobileMod;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.SurfaceView;

public class GameObject implements EntityBase {

    public String tag = "gameobject";

    private PointF halfSize = new PointF();
    protected PointF center = new PointF();
    protected Rect rect = new Rect();

    public Paint.Style style = Paint.Style.FILL;
    public int color;
    public float strokeWidth = 1.f;

    private boolean isDone = false;
    private boolean isInit = false;
    private int renderLayer = 0;

    public boolean active = true;

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
    public void Update(float _dt) { }

    @Override
    public void Render(Canvas _canvas) {
        Paint paint = new Paint();
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);

        _canvas.drawRect(rect, paint);
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
    public boolean GetActive() {
        return active;
    }

    public void SetCenterX(float _x) {
        center.x = _x;
        rect.left = (int)(center.x - halfSize.x);
        rect.right = (int)(center.x + halfSize.x);
    }

    public void SetCenterY(float _y) {
        center.y = _y;
        rect.top = (int)(center.y - halfSize.y);
        rect.bottom = (int)(center.y + halfSize.y);
    }

    public PointF GetCenter() {
        return center;
    }

    public void SetWidth(float _width) {
        halfSize.x = _width * 0.5f;
        rect.left = (int)(center.x - halfSize.x);
        rect.right = (int)(center.x + halfSize.x);
    }

    public void SetHeight(float _height) {
        halfSize.y = _height * 0.5f;
        rect.top = (int)(center.y - halfSize.y);
        rect.bottom = (int)(center.y + halfSize.y);
    }
}