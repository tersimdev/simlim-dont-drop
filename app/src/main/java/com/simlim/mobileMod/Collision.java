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

        PointF start1 = line1.getStart();
        PointF end1 = line1.getEnd();
        PointF start2 = line2.getStart();
        PointF end2 = line2.getEnd();

        float denominator = (start1.x - end1.x) * (start2.y - end2.y) - (start1.y - end1.y) * (start2.x - end2.x);

        if (denominator < EPSILON && denominator > -EPSILON) //lines are parallel
            return new HitInfo(false, null);

        float t = ((start1.x - start2.x) * (start2.y - end2.y) - (start1.y - start2.y) * (start2.x - end2.x)) / denominator;
        float u = ((start1.x - end1.x) * (start1.y - start2.y) - (start1.y - end1.y) * (start1.x - start2.x)) / denominator;

        PointF intersect = null;
        if (t >= 0 && t <= 1)
            intersect = new PointF(start1.x + t * (end1.x - start1.x), start1.y + t * (end1.y - start1.y));
        else if (u >= 0 && u <= 1)
            intersect = new PointF(start2.x + u * (end2.x - start2.x), start2.y + u * (end2.y - start2.y));
        else
            return new HitInfo(false, null);

        return new HitInfo(true, intersect);
    }
}
