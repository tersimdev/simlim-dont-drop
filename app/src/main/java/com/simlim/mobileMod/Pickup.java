package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pickup extends GameObject implements Collidable {


    public float hsv[] = new float[] { 0,1,1 };

    private float radius = 50;
    public Callback onHitCallBack = null;

    @Override
    public void Render(Canvas _canvas) {
        Paint paint = new Paint();
        hsv[0] += 10;
        if (hsv[0] >= 360)
            hsv[0] = 0;
        paint.setColor(Color.HSVToColor(hsv));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);

        _canvas.drawCircle(center.x, center.y, radius, paint);
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

    public void SetRadius(float r) {
        radius = r;
    }

    @Override
    public void OnHit(Collidable _other) {
        String tag = ((GameObject)_other).tag;
        if (_other instanceof PhysicsObj && tag == "ball")
        {
            active = false;
        if (onHitCallBack != null)
            onHitCallBack.doThing((GameObject) _other);
        }
    }
}
