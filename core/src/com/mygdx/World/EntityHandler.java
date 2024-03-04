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
    private final HashMap<Integer, Entity> entities;        // Map of entities with id
    private final LinkedList<EntityOp> entityOps;           // Entity operations to handle
    private final CharacterCycle characterCycle;
    private final ShaderHandler shaderHandler;
    private final VisionMap visionMap;
    private Pet pet;

    public EntityHandler(CharacterCycle characterCycle, ShaderHandler shaderHandler, VisionMap visionMap) {
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

    // Returning entity based on id
    public Entity getEntity(int id) {
        return entities.get(id);
    }

    // Returning entity based on box2d body
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

    // Handling all entity operations
    public void handleEntities() {
        for (EntityOp entityOp : entityOps) {
            entityOp.resolve();
        }
        entityOps.clear();
    }

    public void update(float delta) {
        pet.update(delta);
        // Updating all entities
        for (Entity entity : entities.values()) {
            entity.update(delta);
        }
        handleEntities();
    }

    public void render(SpriteBatch batch) {
        pet.render(batch);
        // Rendering entities
        for (Entity entity : entities.values()) {
            if (entity instanceof PlayableCharacter) {
                // Applying red mask if entity is hit
                if (((PlayableCharacter) entity).isStateActive(Constants.PSTATE.HIT)) {
                    batch.setShader(shaderHandler.getShaderProgram("redMask"));
                }
            }
            // Changing outline color if goblin is being controlled
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

    // Helper entity operation class mapping an entity with an operation
    private class EntityOp {
        public Entity entity;
        public String operation;

        public EntityOp(Entity entity, String op) {
            this.entity = entity;
            this.operation = op;
        }

        public void resolve() {
            if (operation.equals("die")) {
                if (!(entity instanceof Mage) && (entity instanceof PlayableCharacter)) {
                    visionMap.removeCharacter((PlayableCharacter) entity);
                    entities.remove(entity.getID());
                } else if (entity instanceof Mage) {
                    ((Mage) entity).respawn();
                }
            }
        }
    }
}

