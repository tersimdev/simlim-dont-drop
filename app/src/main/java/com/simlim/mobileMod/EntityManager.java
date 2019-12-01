package com.simlim.mobileMod;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

// Created by TanSiewLan2019

public class EntityManager {

    public final static EntityManager Instance = new EntityManager();
    private LinkedList<EntityBase> entityList = new LinkedList<EntityBase>();
    private SurfaceView view = null;

    private EntityManager()
    {
    }

    public void Init(SurfaceView _view)
    {
        view = _view;
    }

    public void Update(float _dt)
    {
        LinkedList<EntityBase> removalList = new LinkedList<EntityBase>();

        // Update all
        for (EntityBase currEntity : entityList)
        {
            // Lets check if is init, initialize if not
            if (!currEntity.IsInit())
                currEntity.Init(view);

            if (currEntity.GetActive())
                currEntity.Update(_dt);

            // Check if need to clean up
            if (currEntity.IsDone()) {
                // Done! Time to add to the removal list
                removalList.add(currEntity);
            }
        }

        // Remove all entities that are done
        for (EntityBase currEntity : removalList) {
            entityList.remove(currEntity);
        }
        removalList.clear(); // Clean up of removal list

        // Collision Check
        for (int i = 0; i < entityList.size(); ++i)
        {
            EntityBase currEntity = entityList.get(i);

            if (currEntity.GetActive()) {
                if (currEntity instanceof Collidable) {
                    Collidable first = (Collidable) currEntity;

                    for (int j = i + 1; j < entityList.size(); ++j) {
                        EntityBase otherEntity = entityList.get(j);

                        if (otherEntity.GetActive() && otherEntity instanceof Collidable) {
                            Collidable second = (Collidable) otherEntity;

                            if (Collision.SphereToSphere(first.GetPosX(), first.GetPosY(), first.GetRadius(), second.GetPosX(), second.GetPosY(), second.GetRadius())) {
                                first.OnHit(second);
                                second.OnHit(first);
                            }
                        }
                    }
                }

                if (currEntity instanceof SpriteAnimation) {
                    SpriteAnimation sprite = (SpriteAnimation)currEntity;
                    sprite.UpdateAnimation(_dt);
                }
            }

            // Check if need to clean up
            if (currEntity.IsDone()) {
                removalList.add(currEntity);
            }
        }

        // Remove all entities that are done
        for (EntityBase currEntity : removalList) {
            entityList.remove(currEntity);
        }
        removalList.clear();
    }

    public void Render(Canvas _canvas)
    {
      
        // Use the new "rendering layer" to sort the render order
        Collections.sort(entityList, new Comparator<EntityBase>() {
            @Override
            public int compare(EntityBase o1, EntityBase o2) {
                return o1.GetRenderLayer() - o2.GetRenderLayer();
            }
        });

        for (EntityBase currEntity : entityList) {
            if (currEntity.GetActive())
                currEntity.Render(_canvas);
        }
    }

    public void AddEntity(EntityBase _newEntity)
    {
        entityList.add(_newEntity);
    }

    public void Clean()
    {
        entityList.clear();
    }
}


