package com.simlim.mobileMod;

public class Pickup extends GameObject implements Collidable {


    private float radius = 50;
    public boolean gotten = false;

    @Override
    public float GetPosX() {
        return GetCenter().x;
    }

    @Override
    public float GetPosY() {
        return GetCenter().y;
    }

    @Override
    public float GetRadius() {
        return 0;
    }

    public void SetRadius(float r) {
        radius = r;
    }

    @Override
    public void OnHit(Collidable _other) {
        if (_other instanceof PhysicsObj && ((GameObject)_other).tag == "ball")
        active = false;
        gotten = true;
    }
}
