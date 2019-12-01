package com.simlim.mobileMod;

import android.graphics.PointF;

public class PointFOps {

    public static PointF clone(PointF _point) {
        PointF result = new PointF();
        result.x = _point.x;
        result.y = _point.y;
        return result;
    }

    public static PointF mul(PointF lhs, float rhs){
        PointF ret = new PointF(lhs.x * rhs, lhs.y * rhs);
        return ret;
    }

    public static PointF mul(PointF lhs, PointF rhs){
        PointF ret = new PointF(lhs.x * rhs.x, lhs.y * rhs.y);
        return ret;
    }

    public static  PointF add(PointF lhs, PointF rhs){
        PointF ret = new PointF(lhs.x + rhs.x, lhs.y + rhs.y);
        return ret;
    }

    public static  PointF minus(PointF lhs, PointF rhs){
        PointF ret = new PointF(lhs.x - rhs.x, lhs.y - rhs.y);
        return ret;
    }

    public static float distSqr (PointF pt1, PointF pt2) {
        return ((pt1.x - pt2.x) * (pt1.x - pt2.x) + (pt1.y - pt2.y)* (pt1.y - pt2.y));
    }

    public static float distSqr (PointF pt1) {
        return (pt1.x * pt1.x + pt1.y * pt1.y);
    }

    public static float dist (PointF pt1, PointF pt2) {
        return (float)Math.sqrt(distSqr(pt1,pt2));
    }

    public static float dist (PointF pt1) {
        return (float)Math.sqrt(distSqr(pt1));
    }

    public static float dot(PointF pt1, PointF pt2){
        return pt1.x * pt2.x + pt1.y * pt2.y;
    }

    public static PointF normalize(PointF pt){
        float d = dist(pt);
        if (d > 0)
            return mul(pt, 1.f/d);
        else
            System.out.println("Divied by zero when normalizing");
        return null;
    }

    public static boolean inRange(PointF p1, PointF p2, PointF point) {
        PointF min = new PointF();
        PointF max = new PointF();

        if (p1.x < p2.x) {
            min.x = p1.x;
            max.x = p2.x;
        } else {
            min.x = p2.x;
            max.x = p1.x;
        }

        if (p1.y < p2.y) {
            min.y = p1.y;
            max.y = p2.y;
        } else {
            min.y = p2.y;
            max.y = p1.y;
        }

        return point.x >= min.x && point.x <= max.x && point.y >= min.y && point.y <= max.y;
    }

}
