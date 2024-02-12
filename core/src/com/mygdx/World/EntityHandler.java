package com.mygdx.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.Graphics.ShaderHandler;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.Entity;
import com.mygdx.RoleCast.Mage;
import com.mygdx.RoleCast.Pet;
import com.mygdx.RoleCast.PlayableCharacter;
import java.util.HashMap;
import java.util.LinkedList;

// Class to handle risky operations outside world step and map entities to their id
public class EntityHandler {
    private final HashMap<Integer, Entity> entities;
    private final LinkedList<EntityOp> entityOps;
    private final CharacterCycle characterCycle;
    private final ShaderHandler shaderHandler;
    private final VisionMap visionMap;
    private Pet pet;

    public EntityHandler (CharacterCycle characterCycle, ShaderHandler shaderHandler, VisionMap visionMap) {
        entityOps = new LinkedList<>();
        entities = new HashMap<>();
        this.characterCycle = characterCycle;
        this.shaderHandler = shaderHandler;
        this.visionMap = visionMap;
    }

    public void addPet(Pet pet) {
        this.pet = pet;
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
                if (!(entityOp.entity instanceof Mage) && (entityOp.entity instanceof PlayableCharacter)) {
                    visionMap.removeCharacter((PlayableCharacter) entityOp.entity);
                    entities.remove(entityOp.entity.getID());
                } else if (entityOp.entity instanceof Mage) {
                    ((Mage) entityOp.entity).respawn();
                }
            }
        }
        entityOps.clear();
    }

    public void update(float delta) {
        pet.update(delta);
        for (Entity entity : entities.values()) {
            //if (entity instanceof Mage) System.out.println(((PlayableCharacter) entity).getPosition());
            entity.update(delta);
        }
        handleEntities();
    }

    public void render(SpriteBatch batch) {
        pet.render(batch);
        for (Entity entity : entities.values()) {
            if (entity instanceof PlayableCharacter) {
                if (((PlayableCharacter) entity).isStateActive(Constants.PSTATE.HIT)) {
                    batch.setShader(shaderHandler.getShaderProgram("redMask"));
                }
            }
            if (characterCycle.getCurrentCharacter().equals(entity) && !(entity instanceof Mage)) {
                batch.setShader(shaderHandler.getShaderProgram("outline"));
            }
            entity.render(batch);
            batch.setShader(null);
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
