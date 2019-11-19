package com.simlim.mobileMod;

// Created by TanSiewLan2019

public interface Collidable {
    String GetType();

    float GetPosX();
    float GetPosY();
    float GetRadius();

    void OnHit(Collidable _other);
}

