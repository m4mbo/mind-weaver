package com.mygdx.Handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.Helpers.DirectedGraph;
import com.mygdx.RoleCast.Entity;
import com.mygdx.RoleCast.PlayableCharacter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

// Class to handle risky operations outside world step and map entities to their id
public class EntityHandler {
    private HashMap<Integer, Entity> entities;
    private LinkedList<EntityOp> entityOps;
    private Stack<CharacterPair> controlChain;    // Chain of current and prev characters
    private ROVHandler univEyesight;

    public EntityHandler () {}

    public void initializeHandler(PlayableCharacter currCharacter) {
        entityOps = new LinkedList<>();
        entities = new HashMap<>();
        controlChain = new Stack<>();
        univEyesight = new ROVHandler(currCharacter);
        controlChain.add(new CharacterPair(currCharacter, null));     // Mage will have the previous character as null
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

    public boolean characterRollback() {
        if (controlChain.peek().prevCharacter == null) return false;
        controlChain.pop();
        return true;
    }

    public void setCurrCharacter(PlayableCharacter currCharacter) {
        controlChain.add(new CharacterPair(currCharacter, controlChain.peek().currCharacter));
    }

    public ROVHandler getUnivEyesight() {
        return univEyesight;
    }

    public PlayableCharacter getCurrCharacter() {
        return controlChain.peek().currCharacter;
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

    private static class EntityOp {
        public Entity entity;
        public String operation;
        public EntityOp(Entity entity, String op) {
            this.entity = entity;
            this.operation = op;
        }
    }

    private class CharacterPair {
        PlayableCharacter currCharacter;
        PlayableCharacter prevCharacter;

        public CharacterPair(PlayableCharacter currCharacter, PlayableCharacter prevCharacter) {
            this.currCharacter = currCharacter;
            this.prevCharacter = prevCharacter;
        }
    }
}
