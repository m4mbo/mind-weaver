package com.mygdx.Handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.DirectedGraph;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.Tools.ShapeDrawer;

public class VisionHandler {
    private boolean collision;
    private DirectedGraph targetAssociation;
    private World world;
    private ShapeDrawer shapeDrawer;

    public VisionHandler(PlayableCharacter character, World world, ShapeDrawer shapeDrawer) {
        collision = true;
        targetAssociation = new DirectedGraph(character);
        this.world = world;
    }

    public PlayableCharacter getNextNeighbour(PlayableCharacter character) {
        return (PlayableCharacter) targetAssociation.getNextNeighbour(character);
    }

    public PlayableCharacter getNextNeighbour(PlayableCharacter character, PlayableCharacter mask) {
        return (PlayableCharacter) targetAssociation.getNextNeighbour(character, mask);
    }

    public void addNeighbour(PlayableCharacter source, PlayableCharacter target) {
        targetAssociation.addNode(source, target);
    }

    public void removeNeighbour(PlayableCharacter target) {
        targetAssociation.removeNode(target);
    }

    public void printROV() {
        targetAssociation.printGraph();
    }

    public void update(float delta) {

    }

    public void attemptConnection(PlayableCharacter source) {
        boolean success = sendSignal(source);
    }

    public boolean sendSignal(PlayableCharacter source) {
        final Vector2 targetPos = source.getTarget().getPosition();
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
            establishConnection(source);
        } else {
            source.removePlayerState(Constants.PSTATE.EOT);
            if (false) source.getTarget().looseControl();
            else {
                if (targetAssociation.getNextNeighbour(source, source.getTarget()) != null) {
                    source.setTarget((PlayableCharacter) targetAssociation.getNextNeighbour(source, source.getTarget()));
                }
            }
            collision = false;
        }
    }

    public void establishConnection(PlayableCharacter source) {
        source.addPlayerState(Constants.PSTATE.EOT);

        float targetX = source.getTarget().getPosition().x;
        float targetY = source.getTarget().getPosition().y;
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
