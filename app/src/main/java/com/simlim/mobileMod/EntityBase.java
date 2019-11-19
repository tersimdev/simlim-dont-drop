package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.view.SurfaceView;

// Created by TanSiewLan2019

public interface EntityBase
{
    boolean IsDone();
    void SetIsDone(boolean _isDone);

    void Init(SurfaceView _view);
    void Update(float _dt);
    void Render(Canvas _canvas);

    boolean IsInit();

    int GetRenderLayer();
    void SetRenderLayer(int _newLayer);


}
