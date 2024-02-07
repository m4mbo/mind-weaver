package com.mygdx.World;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.AdjacencyList;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.Entity;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Tools.ShapeDrawer;

import java.util.*;

public class VisionMap {
    private boolean collision;
    private final AdjacencyList<PlayableCharacter> targetMap;
    private EntityHandler entityHandler;
    private final World world;
    private final ShapeDrawer shapeDrawer;
    private PlayableCharacter mage;

    public VisionMap(World world, ShapeDrawer shapeDrawer) {
        collision = true;
        this.world = world;
        this.shapeDrawer = shapeDrawer;
        this.targetMap = new AdjacencyList<>();
    }

    public void initialize(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        this.mage = (PlayableCharacter) entityHandler.getEntity(0);
        for (Entity entity : entityHandler.getEntities()) {
            if (entity instanceof PlayableCharacter) {
                targetMap.addVertex((PlayableCharacter) entity);
            }
        }
    }

    public void addTarget(PlayableCharacter source, PlayableCharacter target) { targetMap.addEdge(source, target); }

    public void removeTarget(PlayableCharacter source, PlayableCharacter target) {
        targetMap.removeEdge(source, target);
    }

    public void update(float delta) {
        for (PlayableCharacter character : targetMap.getVerticesWithNeighbours()) {
            attemptConnection(character);
        }
    }

    public void attemptConnection(PlayableCharacter source) {
        LinkedList<PlayableCharacter> targets = targetMap.getNeighbours(source);
        for (PlayableCharacter target : targets) {
            if (sendSignal(source, target)) {
                if (source instanceof Mage || traceable(mage, source) || traceable(mage, target)) {
                    if (target.getBullseye() == null || !target.getBullseye().equals(source)){
                        if (source.getBullseye() == null || !source.getBullseye().equals(target)) source.setBullseye(target);
                        establishConnection(source, target);
                        return;
                    }
                }
            } else {
                if (source.getBullseye() != null && source.getBullseye().equals(target)) {
                    source.setBullseye(null);
                    if (!traceable(target)) target.looseControl();
                    if (!traceable(source)) source.looseControl();
                }
            }
        }
    }

    public boolean sendSignal(PlayableCharacter source, PlayableCharacter target) {
        final Vector2 targetPos = target.getPosition();
        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 vector2, Vector2 vector21, float v) {
                if (fixture.getUserData().equals("ground")) {
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

    public LinkedList<PlayableCharacter> getObservers(PlayableCharacter character) {
        if (entityHandler == null) return null;
        LinkedList<PlayableCharacter> observers = new LinkedList<>();
        for (Entity entity :  entityHandler.getEntities()) {
            if (entity instanceof PlayableCharacter) {
                if (((PlayableCharacter) entity).getBullseye() != null && ((PlayableCharacter) entity).getBullseye().equals(character)){
                    observers.add((PlayableCharacter) entity);
                }
            }
        }
        return observers;
    }

    public boolean traceable(PlayableCharacter current, PlayableCharacter destination) {
        while(current != null) {
            if (current.equals(destination)) return true;
            current = current.getBullseye();
        }
        return false;
    }

    public boolean traceable(PlayableCharacter destination) {
        PlayableCharacter current = mage;
        while(current != null) {
            if (current.equals(destination)) return true;
            current = current.getBullseye();
        }
        return false;
    }

    public List<PlayableCharacter> getBullseyeStream() {
        PlayableCharacter current = mage;
        ArrayList<PlayableCharacter> stream = new ArrayList<>();
        while(current != null) {
            stream.add(current);
            PlayableCharacter temp = current.getBullseye();
            if (temp == null) {
                for (PlayableCharacter observer : getObservers(current)) {
                    if (!stream.contains(observer)) {
                        temp = observer;
                    }
                }
            } else {
                if (stream.contains(temp)) temp = null;
            }
            current = temp;
        }
        return stream;
    }

    public void removeCharacter(PlayableCharacter character) {
        targetMap.removeVertex(character);
    }
}
