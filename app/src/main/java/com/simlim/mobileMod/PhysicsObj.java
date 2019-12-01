package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

public class PhysicsObj extends GameObject implements Collidable{

    private float radius = 25;

    private boolean hasGravity = true;
    private float gravScale = 10;
    private float mass = 1;
    private PointF gravity = new PointF(0, 9.8f);
    private PointF velocity = new PointF(0, 0);
    private PointF prevVel = velocity;
    private PointF force = new PointF(0, 0);

    private boolean isKinematic = true;

    @Override
    public void Update(float _dt) {
        super.Update(_dt);
        if (!isKinematic) return;
        PointF accel = PointFOps.mul(force ,  1.f / mass);
        PointF avgVel = PointFOps.mul(PointFOps.add(velocity, prevVel), 0.5f);
        prevVel = velocity;
        velocity = PointFOps.add(avgVel, PointFOps.mul(accel,_dt));

        if (hasGravity)
            velocity = PointFOps.add(velocity, PointFOps.mul(gravity, gravScale * _dt));

        PointF center = GetCenter();
        center = PointFOps.add(center, PointFOps.mul(velocity, _dt));
        SetCenterX(center.x);
        SetCenterY(center.y);
    }

    @Override
    public void Render(Canvas _canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);

        _canvas.drawCircle(center.x, center.y, radius, paint);
//        _canvas.drawRect(rect, paint);

    }

    public void Stop() {
        isKinematic = false;
        velocity.x = prevVel.x = 0;
        velocity.y = prevVel.y = 0;
    }

    public void Reflect(PointF normal) {
        normal = PointFOps.normalize(normal);

        float dot = PointFOps.dot(normal, velocity);

        if (normal != null && dot < 0) {
            PointF resNormal2 = PointFOps.mul(normal, dot * 2.2f);

            prevVel = velocity = PointFOps.minus(velocity, resNormal2);
            isKinematic = true;
        }
    }

    //getter setters

    public boolean isKinematic() {
        return isKinematic;
    }

    public void setKinematic(boolean kinematic) {
        isKinematic = kinematic;
    }

    public boolean hasGravity() {
        return hasGravity;
    }

    public void setHasGravity(boolean hasGravity) {
        this.hasGravity = hasGravity;
    }

    public float getGravScale() {
        return gravScale;
    }

    public void setGravScale(float gravScale) {
        this.gravScale = gravScale;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public PointF getGravity() {
        return gravity;
    }

    public void setGravity(PointF gravity) {
        this.gravity = gravity;
    }

    public PointF getVelocity() {
        return velocity;
    }

    public void setVelocity(PointF velocity) {
        this.velocity = velocity;
    }

    public PointF getPrevVel() {
        return prevVel;
    }

    public void setPrevVel(PointF prevVel) {
        this.prevVel = prevVel;
    }

    public PointF getForce() {
        return force;
    }

    public void setForce(PointF force) {
        this.force = force;
    }

    @Override
    public float GetPosX() {
        return center.x;
    }

    @Override
    public float GetPosY() {
        return center.y;
    }

    @Override
    public float GetRadius() {
        return radius;
    }

    @Override
    public void OnHit(Collidable _other) {
        if (_other instanceof Line) {
            Line line = (Line) _other;
            HitInfo hitInfo = Collision.LineIntersect(line, new Line(center, PointFOps.add(center, velocity)));
            if (hitInfo.hit) {
                //System.out.println("raycast hit");
                if (PointFOps.distSqr(hitInfo.point, center) < radius * radius) {
                    //System.out.println("hitting the line");

                    //calculate normal
                    PointF normal = PointFOps.minus(line.getStart(), line.getEnd());
                    normal.set(-normal.y, normal.x);
                    if (PointFOps.dot(normal, PointFOps.minus(center, line.center)) < 0)
                        normal.set(-normal.x, -normal.y);

                    Reflect(normal);
//                    velocity = PointFOps.mul(velocity, 1.1f);
                }
            }
        }

    }
    public void SetRadius(float _radius) {
        radius = _radius;
    }
}
