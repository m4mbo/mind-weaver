package com.mygdx.Handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.Entity;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Tools.ShapeDrawer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class VisionHandler {
    private boolean collision;
    private final HashMap<PlayableCharacter, LinkedList<PlayableCharacter>> targetMap;
    private final EntityHandler entityHandler;
    private final World world;
    private final ShapeDrawer shapeDrawer;

    public VisionHandler(World world, ShapeDrawer shapeDrawer, EntityHandler entityHandler) {
        collision = true;
        this.world = world;
        this.targetMap = new HashMap<>();
        this.entityHandler = entityHandler;
        this.shapeDrawer = shapeDrawer;
        for (Entity entity : entityHandler.getEntities()) {
            if (entity instanceof PlayableCharacter) {
                targetMap.put((PlayableCharacter) entity, new LinkedList<PlayableCharacter>());
            }
        }
    }

    public void addTarget(PlayableCharacter source, PlayableCharacter target) {
        targetMap.get(source).add(target);
    }

    public void removeTarget(PlayableCharacter source, PlayableCharacter target) {
        targetMap.get(source).remove(target);
    }

    public void update(float delta) {
        for (Map.Entry<PlayableCharacter, LinkedList<PlayableCharacter>> entry : targetMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                attemptConnection(entry.getKey());
            }
        }
    }

    public void attemptConnection(PlayableCharacter source) {
        LinkedList<PlayableCharacter> targets = targetMap.get(source);
        for (PlayableCharacter target : targets) {
            if (sendSignal(source, target)) {
                if (source instanceof Mage || entityHandler.eyesOnMe(source) || entityHandler.eyesOnMe(target)) {
                    source.setBullseye(target);
                    establishConnection(source, target);
                    return;
                }
            } else {
                if (source.getBullseye() != null && source.getBullseye().equals(target)) source.setBullseye(null);
            }
        }
    }

    public boolean sendSignal(PlayableCharacter source, PlayableCharacter target) {
        final Vector2 targetPos = target.getPosition();
        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 vector2, Vector2 vector21, float v) {
                if (fixture.getUserData().equals("ground") || fixture.getUserData().equals("hazard")) {
                    collision = true;
                    return 0;
                }
                return 1;
            }
        };
        world.rayCast(callback, source.getPosition() , targetPos);
        if (!collision) {
            return true;
        } else {
            collision = false;
            return false;
        }
    }

    public void establishConnection(PlayableCharacter source, PlayableCharacter target) {

        float targetX = target.getPosition().x;
        float targetY = target.getPosition().y;
        float playerX = source.getPosition().x;
        float playerY = source.getPosition().y;

        targetY += 2 / Constants.PPM;
        playerY += 2 / Constants.PPM;

        if (playerX < targetX) {
            targetX -= 8 / Constants.PPM;
            playerX += 8 / Constants.PPM;
        } else {
            targetX += 8 / Constants.PPM;
            playerX -= 8 / Constants.PPM;
        }
        shapeDrawer.drawWave(new Vector2(targetX, targetY) , new Vector2(playerX, playerY), 3 / Constants.PPM);
    }

}
