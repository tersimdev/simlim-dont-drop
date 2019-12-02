package com.simlim.mobileMod;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

public class Sprite extends PhysicsObj implements SpriteAnimation {

    private SurfaceView holder;
    private Bitmap bmp = null;
    private int resourceId;

    public SpriteAnimationData animation = new SpriteAnimationData();
    public Rect uv = new Rect();

    public void Init(SurfaceView _view) {
        super.Init(_view);

        holder = _view;
        bmp = BitmapFactory.decodeResource(holder.getResources(), resourceId);
    }

    @Override
    public void Render(Canvas _canvas) {
        _canvas.drawBitmap(bmp, uv, rect, null);
    }

    @Override
    public void UpdateAnimation(float dt) {
        if (animation.frames == null || animation.frames.size() <= animation.currentFrame) return;

        Keyframe currentFrame = animation.frames.get(animation.currentFrame);
        animation.et += dt;
        if (animation.et >= currentFrame.duration) {
            ++animation.currentFrame;
            if (animation.currentFrame == animation.frames.size()) {
                animation.currentFrame = 0;
            }
            animation.et = 0.f;

            final int width = bmp.getWidth();
            final int height = bmp.getWidth();

            currentFrame = animation.frames.get(animation.currentFrame);

            uv.top = (int)((float) height * currentFrame.uvPosition.y);
            uv.bottom = uv.top + (int)((float) height * currentFrame.uvSize.y);
            uv.left = (int)((float) width * currentFrame.uvPosition.x);
            uv.right = uv.left + (int)((float) width * currentFrame.uvSize.x);
        }
    }

    public void SetResourceId(int _resourceId) {
        resourceId = _resourceId;
        if (holder != null) {
            bmp = BitmapFactory.decodeResource(holder.getResources(), resourceId);
        }
    }

}
