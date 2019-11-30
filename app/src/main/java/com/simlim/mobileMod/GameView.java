package com.simlim.mobileMod;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// Created by TanSiewLan2019
// GameView is the SurfaceView

public class GameView extends SurfaceView {
    // Surfaceview has a holder to be used to hold the content.
    private SurfaceHolder holder = null;

    //Thread to be known for its existence
    private UpdateThread updateThread = new UpdateThread(this);

    //views from xml that i shld know of
    public List<View> childViews;

    public GameView(Context _context, List<View> _childViews)
    {
        super(_context);
        holder = getHolder();

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

        childViews = _childViews;

    }
}

