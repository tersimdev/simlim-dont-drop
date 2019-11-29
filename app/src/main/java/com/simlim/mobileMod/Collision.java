package com.simlim.mobileMod;

// Created by TanSiewLan2019

import android.graphics.PointF;

class HitInfo
{
    HitInfo(boolean _hit, PointF _pt) {
        hit = _hit;
        point = _pt;
    }
    public boolean hit = false;
    public PointF point = null;
};

public class Collision {

    final static float EPSILON = 0.0001f;

    public static boolean SphereToSphere(float x1, float y1, float radius1, float x2, float y2, float radius2)
    {
        float xVec = x2 - x1;
        float yVec = y2 - y1;

        float distSquared = xVec * xVec + yVec * yVec;

        float rSquared = radius1 + radius2;
        rSquared *= rSquared;

        if (distSquared > rSquared)
            return false;

        return true;
    }

    public static HitInfo LineIntersect(Line line1, Line line2) {

        float denominator = (line1.start.x - line1.end.x) * (line2.start.y - line2.end.y) - (line1.start.y - line1.end.y) * (line2.start.x - line2.end.x);

        if (denominator < EPSILON && denominator > -EPSILON) //lines are parallel
            return new HitInfo(false, null);

        float t = ((line1.start.x - line2.start.x) * (line2.start.y - line2.end.y) - (line1.start.y - line2.start.y) * (line2.start.x - line2.end.x)) / denominator;
        float u = ((line1.start.x - line1.end.x) * (line1.start.y - line2.start.y) - (line1.start.y - line1.end.y) * (line1.start.x - line2.start.x)) / denominator;

        PointF intersect = null;
        if (t >= 0 && t <= 1)
            intersect = new PointF(line1.start.x + t * (line1.end.x - line1.start.x), line1.start.y + t * (line1.end.y - line1.start.y));
        else if (u >= 0 && u <= 1)
            intersect = new PointF(line2.start.x + u * (line2.end.x - line2.start.x), line2.start.y + u * (line2.end.y - line2.start.y));
        else
            return new HitInfo(false, null);

        return new HitInfo(true, intersect);
    }
}
