package com.mygdx.World;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.Helpers.AdjacencyList;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.ArmourGoblin;
import com.mygdx.RoleCast.Entity;
import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Tools.ShapeDrawer;
import com.mygdx.Scenes.HUD;
import java.util.*;

public class VisionMap {
    private boolean collision;
    private final AdjacencyList<PlayableCharacter> targetMap;       // Map keeping characters in target, helps with performance
    private final AdjacencyList<PlayableCharacter> bullseyeMap;

    // Avoiding spanning tree creation at every frame, performance tweak
    private boolean bullseyeChange;
    private Map<PlayableCharacter, List<PlayableCharacter>> bullseyeStream;     // Spanning tree of bullseyeMap
    private final World world;
    private final ShapeDrawer shapeDrawer;
    private CharacterCycle characterCycle;
    private HUD hud;
    private PlayableCharacter mage;

    public VisionMap(World world, ShapeDrawer shapeDrawer, HUD hud) {
        collision = true;
        bullseyeChange = true;
        this.world = world;
        this.shapeDrawer = shapeDrawer;
        this.targetMap = new AdjacencyList<>();
        this.bullseyeMap = new AdjacencyList<>();
        this.hud = hud;
    }

    public void initialize(EntityHandler entityHandler, CharacterCycle characterCycle) {
        this.mage = (PlayableCharacter) entityHandler.getEntity(0);
        for (Entity entity : entityHandler.getEntities()) {
            if (entity instanceof PlayableCharacter) {
                targetMap.addVertex((PlayableCharacter) entity);
                bullseyeMap.addVertex((PlayableCharacter) entity);
            }
        }
        this.characterCycle = characterCycle;
    }

    public void addTarget(PlayableCharacter source, PlayableCharacter target) { targetMap.addEdge(source, target); }

    public void removeTarget(PlayableCharacter source, PlayableCharacter target) {
        if (bullseyeMap.removeEdge(source, target)) {
            characterCycle.updateCycle();
            bullseyeChange = true;
        }
        targetMap.removeEdge(source, target);
    }

    public void update(float delta) {

        for (PlayableCharacter character : targetMap.getVerticesWithNeighbours()) {
            // Modifying graph edges
            attemptConnection(character);
        }
        // Solidifying connection according to updated edges
        solidifyConnections();
    }

    public void solidifyConnections() {
        if (bullseyeChange) {
            // Computing spanning tree with source node as the mage
            bullseyeStream = bullseyeMap.getSpanningTree(mage);     // We can assume that the bullseyeMap has already been successfully modified
            bullseyeChange = false;
        }

        // Handling tree connections
        for (PlayableCharacter character : bullseyeStream.keySet()) {
            for (PlayableCharacter neighbour : bullseyeStream.get(character)) {
                if (!character.isStateActive(Constants.PSTATE.DYING) && !neighbour.isStateActive(Constants.PSTATE.DYING)) {
                    establishConnection(character, neighbour);
                }
            }
        }
    }

    public void attemptConnection(PlayableCharacter source) {

        boolean armourUnlocked = hud.arePowersUnlocked();   // Not counting armour goblins if powers not unlocked

        if (source instanceof ArmourGoblin && !armourUnlocked) return;

        LinkedList<PlayableCharacter> targets = targetMap.getNeighbours(source);

        // Iterating through characters in target map
        for (PlayableCharacter target : targets) {

            if (target instanceof ArmourGoblin && !armourUnlocked) continue;

            // Sending ray from source to target, if reached
            if (sendSignal(source, target)) {
                if (source instanceof Mage || traceable(mage, source)) {
                    if (bullseyeMap.addEdge(source, target)) {
                        characterCycle.updateCycle();   // Updating cycle with new connection
                        bullseyeChange = true;
                    }
                }
            } else {
                // If ray not reached
                if (bullseyeMap.removeEdge(source, target) || bullseyeMap.removeEdge(target, source)) {
                    characterCycle.updateCycle();   // If successfully removed edge (meaning it was part of graph before)
                    bullseyeChange = true;
                }
                if (!traceable(target)) target.looseControl();  // If the target cannot be traced from mage
                if (!traceable(source)) source.looseControl();  // If the source ``
            }
        }
    }

    // Sending ray cast from source to target
    public boolean sendSignal(PlayableCharacter source, PlayableCharacter target) {
        final Vector2 targetPos = target.getPosition();
        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 vector2, Vector2 vector21, float v) {
                if (fixture.getUserData().equals("ground") || fixture.getUserData().equals("platform")) {
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

    // Drawing connection wave between characters
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

    public boolean traceable(PlayableCharacter source, PlayableCharacter destination) {
        return bullseyeMap.traceable(source, destination);
    }

    public boolean traceable(PlayableCharacter destination) {
        return bullseyeMap.traceable(mage, destination);
    }

    public List<PlayableCharacter> getBullseyeStream() {
        return bullseyeMap.getReachableVertices(mage);
    }

    public void removeCharacter(PlayableCharacter character) {
        targetMap.removeVertex(character);
    }
}
