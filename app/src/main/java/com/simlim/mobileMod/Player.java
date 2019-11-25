package com.simlim.mobileMod;

import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject implements Collidable {

    public List<Integer> path = new ArrayList<>();

    public Player(int size) {
        color = Color.BLUE;
        rect = new Rect(0, 0, size, size);
        EntityManager.Instance.AddEntity(this);
    }

    @Override
    public float GetPosX() { return this.rect.centerX(); }

    @Override
    public float GetPosY() {
        return this.rect.centerY();
    }

    @Override
    public float GetRadius() {
        return this.radius;
    }

    @Override
    public void OnHit(Collidable _other) {

    }
}
