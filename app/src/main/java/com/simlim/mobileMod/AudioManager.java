package com.simlim.mobileMod;

import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.HashMap;

public class AudioManager {
    public final static AudioManager Instance = new AudioManager();
    private HashMap<Integer, MediaPlayer> audioMap = new HashMap<>();
    private SurfaceView view = null;

    private boolean isEnabled;

    private AudioManager() { }

    public void Init(SurfaceView _view) {
        view = _view;
        isEnabled = true;
        Reset();
    }

    public void Reset() {
        for (HashMap.Entry<Integer, MediaPlayer> entry : audioMap.entrySet()) {
            MediaPlayer m = entry.getValue();
            m.stop();
            m.reset();
            m.release();
        }
        audioMap.clear();
    }

    public void PlayAudio(final int _id, float _vol) {
        if (!isEnabled) return;

        if (audioMap.containsKey(_id)) {
            MediaPlayer m = audioMap.get(_id);
            m.seekTo(0);
            m.setVolume(_vol, _vol);
            m.start();
        }
        else {
            MediaPlayer m = MediaPlayer.create(view.getContext(), _id);
            audioMap.put(_id, m);
            m.start();
        }
    }

    public void StopAudio(int _id) {
        if (audioMap.containsKey(_id))
            audioMap.get(_id).pause();
    }

    public void SetEnabled(boolean _isEnabled) {
        isEnabled = _isEnabled;
    }
}
