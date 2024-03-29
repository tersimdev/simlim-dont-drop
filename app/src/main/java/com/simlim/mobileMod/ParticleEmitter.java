package com.simlim.mobileMod;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Random;

public class ParticleEmitter {

    static private Random rand = new Random();
    private ArrayList<Particle> particles = new ArrayList<Particle>();

    public float lifetime = 0.f;
    public float lifetimeRange = 0.f;

    public float angle = 0.f;
    public float angleRange = 0.f;

    public float speed = 0.f;
    public float speedRange = 0.f;

    public PointF offset = new PointF();
    public PointF offsetRange = new PointF();
    public PointF position = new PointF();
    public PointF positionRange = new PointF();

    public PointF gravity = new PointF();

    public float accelRad = 0.f;
    public float accelRadRange = 0.f;

    public float startRadius;
    public float startRadiusRange;

    public float endRadius;
    public float endRadiusRange;

    public int color;
    public boolean rainbow = false;

    public void Spawn(int count, PointF _position) {
        position = _position;

        for (int i = 0; i < count; ++i) {
            Particle go = Fetch();

            go.age = 0.f;
            go.lifetime = lifetime + lifetimeRange * r();

            float s = speed + speedRange * r();
            double theta = Math.toRadians(angle + angleRange * r());
            go.velocity.set((float)Math.sin(theta) * s, (float)Math.cos(theta) * s);

            PointF d = PointFOps.add(offset, PointFOps.mul(offsetRange, r()));
            PointF pos = PointFOps.add(d, position);
            go.center = PointFOps.add(pos, PointFOps.mul(positionRange, r()));

            go.startRadius = startRadius + startRadiusRange * r();
            go.endRadius = endRadius + endRadiusRange * r();

            go.color = color;
            go.rainbow = rainbow;

            go.emitter = this;
        }
    }

    private Particle Fetch() {
        if (!particles.isEmpty()) {
            Particle front = particles.get(0);
            if (!front.active) {
                particles.remove(0);
                particles.add(front);
                front.active = true;
                return front;
            }
        }

        Particle particle = new Particle();
        particle.active = true;
        particle.tag = "particle";
        particles.add(particle);
        EntityManager.Instance.AddEntity(particle);
        return particle;
    }

    static public float r() {
        return rand.nextFloat() * 2.f - 1.f;
    }

}
