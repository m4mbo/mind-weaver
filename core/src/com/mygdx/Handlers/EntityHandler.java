package com.mygdx.Handlers;

import com.mygdx.Objects.Entity;
import com.mygdx.Objects.Player;
import sun.tools.jconsole.inspector.XOperations;

import java.util.LinkedList;

// Class to handle risky operation outside of world step
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
            } else if (entityOp.operation.equals("wallclimb")) {
                ((Player) entityOp.entity).wallClimb();
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
