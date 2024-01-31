package com.mygdx.Handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.RoleCast.Entity;
import com.mygdx.RoleCast.PlayableCharacter;
import java.util.HashMap;
import java.util.LinkedList;

// Class to handle risky operations outside world step and map entities to their id
public class EntityHandler {
    private HashMap<Integer, Entity> entities;
    private LinkedList<EntityOp> entityOps;

    public EntityHandler () {}

    public void initialize(PlayableCharacter currCharacter) {
        entityOps = new LinkedList<>();
        entities = new HashMap<>();
        addEntity(currCharacter);
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getID(), entity);
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }
    
    public Entity getEntity(Body b2body) {
        for (Entity entity : entities.values()) {
            if (entity.getB2body().equals(b2body)) {
                return entity;
            }
        }        
        return null;
    }

    public void addEntityOperation(Entity entity, String operation) {
        entityOps.add(new EntityOp(entity, operation));
    }

    public void handleEntities() {
        for (EntityOp entityOp : entityOps) {
            if (entityOp.operation.equals("die")) {
                entityOp.entity.die();
            } else if (entityOp.operation.equals("teleport")) {

            }
        }
        entityOps.clear();
    }

    public void update(float delta) {
        for (Entity entity : entities.values()) {
            entity.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (Entity entity : entities.values()) {
            entity.render(batch);
        }
    }

    public LinkedList<Entity> getEntities() {
        return new LinkedList<>(entities.values());
    }

    private static class EntityOp {
        public Entity entity;
        public String operation;
        public EntityOp(Entity entity, String op) {
            this.entity = entity;
            this.operation = op;
        }
    }
}
