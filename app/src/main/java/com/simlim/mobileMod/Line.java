package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Line extends GameObject implements Collidable {

    public PointF start = new PointF();
    public PointF end = new PointF();

    private float radius = 500;

    private boolean valid = false;

    Line () {}

    Line(PointF _start, PointF _end) {
        start = _start;
        end = _end;
    }

    @Override
    public void Render(Canvas _canvas) {
        Paint paint = new Paint();
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);

        _canvas.drawLine(start.x, start.y, end.x, end.y, paint);


        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        _canvas.drawCircle(start.x, start.y, strokeWidth, paint);


        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        _canvas.drawCircle(end.x, end.y, strokeWidth, paint);
    }


    @Override
    public float GetPosX() {
        return GetCenter().x;
    }

    @Override
    public float GetPosY() {
        return GetCenter().y;
    }

    @Override
    public float GetRadius() {
        return radius;
    }

    @Override
    public void OnHit(Collidable _other) {

    }

    public void setCenter(PointF _center) {
        PointF dir = PointFOps.normalize(PointFOps.minus(start, end));
        if (dir == null) return;
        dir = PointFOps.mul(dir, radius);
        start = PointFOps.add(_center, dir);
        end = PointFOps.minus(_center, dir);
        center = _center;
    }

    public PointF getStart() {
        return start;
    }

    public void setStart(PointF start) {
        valid = false;
        this.start = start;
    }

    public PointF getEnd() {
        return end;
    }

    public void setEnd(PointF end) {
        valid = true;
        SetCenterX((start.x + end.x) * 0.5f);
        SetCenterY((start.y + end.y) * 0.5f);
        radius = (float)Math.sqrt((double) PointFOps.distSqr(start, end)) * 0.5f;
        this.end = end;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
