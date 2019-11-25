package com.simlim.mobileMod;

import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

    public List<Integer> path = new ArrayList<>();

    public Player(int size) {
        color = Color.BLUE;
        rect = new Rect(0, 0, size, size);
        EntityManager.Instance.AddEntity(this);
    }
}
