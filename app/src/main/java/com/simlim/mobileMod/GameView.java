package com.simlim.mobileMod;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// Created by TanSiewLan2019
// GameView is the SurfaceView

public class GameView extends SurfaceView {
    // Surfaceview has a holder to be used to hold the content.
    private SurfaceHolder holder = null;

    //Thread to be known for its existence
    private UpdateThread updateThread = new UpdateThread(this);

    public Context context;

    public GameView(Context _context) {
        super(_context);
        init(_context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context _ctx) {
        holder = getHolder();
        context = _ctx;

//        android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(1000, 1000);
//        this.setLayoutParams(params);

        if (holder != null)
        {
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    //Setup some stuff to indicate whether thread is running and initialized
                    if (!updateThread.IsRunning())
                        updateThread.Initialize();

                    if (!updateThread.isAlive())
                        updateThread.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    // Nothing to type here cos it will be handle by the thread
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    // Done then thread should not run too.
                    updateThread.Terminate();
                }
            });
        }
    }
}

