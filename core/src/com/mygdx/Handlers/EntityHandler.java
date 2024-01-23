package com.mygdx.Handlers;

import com.mygdx.Objects.Entity;

import java.util.LinkedList;

// Class to handle risky operations outside of world step
public class EntityHandler {

    LinkedList<EntityOp> entityOps;

    public EntityHandler () {
        entityOps = new LinkedList<>();
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

    private static class EntityOp {
        public Entity entity;
        public String operation;
        public EntityOp(Entity entity, String op) {
            this.entity = entity;
            this.operation = op;
        }
    }

}
