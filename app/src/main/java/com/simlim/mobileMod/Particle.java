package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Particle extends GameObject {

    public ParticleEmitter emitter;

    private float radius = 50;

    public float age = 0;
    public float lifetime = 0;

    public PointF velocity = new PointF(0.f, 0.f);

    public float startRadius = 50;
    public float endRadius = 0;

    @Override
    public void Render(Canvas _canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);

        _canvas.drawCircle(center.x, center.y, radius, paint);
    }

    @Override
    public void Update(float dt) {
        age += dt;

        velocity = PointFOps.add(velocity, PointFOps.mul(emitter.gravity, dt));

        PointF dir = PointFOps.normalize(PointFOps.minus(center, emitter.position));
        float a = emitter.accelRad + emitter.accelRadRange * ParticleEmitter.r();
        velocity = PointFOps.add(velocity, PointFOps.mul(dir, a * dt));

        center = PointFOps.add(center, PointFOps.mul(velocity, dt));

        radius = (endRadius - startRadius) * (age / lifetime) + startRadius;

        if (age > lifetime) {
            active = false;
            Reset();
        }
    }

    public void SetRadius(float r) {
        radius = r;
    }

    public void Reset() {
        age = 0;
        radius = startRadius;
    }

}
