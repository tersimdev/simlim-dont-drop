package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Line extends GameObject {

    public PointF start = new PointF();
    public PointF end = new PointF();

    @Override
    public void Render(Canvas _canvas) {
        Paint paint = new Paint();
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);

        _canvas.drawLine(start.x, start.y, end.x, end.y, paint);
    }


}
