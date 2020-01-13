package com.simlim.mobileMod;

// Created by TanSiewLan2019
// Need a delicated thread to run Surfaceview's update method

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class UpdateThread extends Thread {

    static final long targetFPS = 60;

    private GameView view = null;       //Gameview = Surfaceview
    private SurfaceHolder holder = null;

    private boolean isRunning = false;

    public static String deviceId;

    public UpdateThread(GameView _view)
    {
        view = _view;
        holder = _view.getHolder();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceId = instanceIdResult.getId();
            }
        });

        StateManager.Instance.Init(_view);
        EntityManager.Instance.Init(_view);
        GameSystem.Instance.Init(_view);
        AudioManager.Instance.Init(_view);
    }

    public boolean IsRunning()
    {
        return isRunning;
    }

    public void Initialize()
    {
        isRunning = true;
    }

    public void Terminate()
    {
        isRunning = false;
    }

    @Override   //Every game thread will always have a run() method
    public void run()
    {
        // This is for frame rate control
        long framePerSecond = 1000 / targetFPS;     // 1000 is milliseconds --> 1 second
        long startTime = 0;

        // This is to calculate delta time (more precise)
        long prevTime = System.nanoTime();

        StateManager.Instance.Start("MainGame");

        // This is the game loop
        while (isRunning && StateManager.Instance.GetCurrentState() != "INVALID")
        {
            startTime = System.currentTimeMillis();
            long currTime = System.nanoTime();
            float deltaTime = (float) ((currTime - prevTime) / 1000000000.0f);
            prevTime = currTime;

            // Update
            StateManager.Instance.Update(deltaTime);

            // Render
            Canvas canvas = holder.lockCanvas(null);
            if (canvas != null)
            {
                // Able to render
                synchronized (holder)
                {
                    // Fill the background color to reset
                    canvas.drawColor(ResourcesCompat.getColor(view.getResources(), R.color.MAIN, null));

                    StateManager.Instance.Render(canvas);
                }
                holder.unlockCanvasAndPost(canvas);
            }

            // Frame rate controller
            try
            {
                long sleepTime = framePerSecond - (System.currentTimeMillis() - startTime);

                if (sleepTime > 0)
                    sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
                isRunning = false;
                Terminate();
            }

            // End of Loop
        }
    }
}


