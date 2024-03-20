package com.mygdx.Tools;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.World.VisionMap;
import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.Subscriber;
import com.mygdx.RoleCast.ArmourGoblin;
import com.mygdx.RoleCast.PlayableCharacter;

import java.util.Random;

public class EnemyController implements Subscriber {    //control enemy
    private final PlayableCharacter enemy;    //enemy to control
    private final PlayableCharacter player;   //player
    private VisionMap visionMap;
    private MyTimer timer;
    private boolean jumpCoolDown;
    private boolean eyesOnPlayer;       //checks if enemy can see player
    private final Random random;        //compute random behaviour

    public EnemyController(PlayableCharacter player, PlayableCharacter enemy, VisionMap visionMap, MyTimer timer) {
        this.player = player;
        this.enemy = enemy;
        this.visionMap = visionMap;
        this.timer = timer;
        jumpCoolDown = false;
        random = new Random();
    }

    public void update() {

        //if enemy or player is in a dying state, adjust movement accordingly
        if (enemy.isStateActive(Constants.PSTATE.DYING)) return;

        if (player.isStateActive(Constants.PSTATE.DYING)) {
            enemy.setMovementState(Constants.MSTATE.HSTILL);
            return;
        }

        // Handling behaviour when eyes are not on player
        if (!visionMap.sendSignal(enemy, player)) {
            eyesOnPlayer = false;
            int rand = random.nextInt(300);
            if (rand == 1) {
                if (enemy.getWallState() != 1) enemy.setMovementState(Constants.MSTATE.RIGHT);
                else enemy.setMovementState(Constants.MSTATE.LEFT);
                timer.start(0.3f, "stop", this);
            } else if (rand == 2) {
                if (enemy.getWallState() != -1) enemy.setMovementState(Constants.MSTATE.LEFT);
                else enemy.setMovementState(Constants.MSTATE.RIGHT);
                timer.start(0.3f, "stop", this);
            }
            return;
        }

        // Handling behaviour when eyes are on player
        eyesOnPlayer = true;
        Vector2 enemyPos = enemy.getPosition();
        Vector2 playerPos = player.getPosition();

        if (MathWizard.inRange(enemyPos.x, playerPos.x, 16 / Constants.PPM)) {
            enemy.setMovementState(Constants.MSTATE.HSTILL);
            if (MathWizard.inRange(enemyPos.y, playerPos.y, 8 / Constants.PPM)) {
                ((ArmourGoblin) enemy).attack();
            } else if (enemyPos.y < playerPos.y - 8 / Constants.PPM && !jumpCoolDown) {
                enemy.jump();
                jumpCoolDown = true;
                timer.start(1f, "jump_cooldown", this);
            } else if (!MathWizard.inRange(enemyPos.x, playerPos.x, 2 / Constants.PPM)) {
                follow(enemyPos, playerPos);
            }
            return;
        }

        follow(enemyPos, playerPos);

        Vector2 vel = enemy.getB2body().getLinearVelocity();

        if (vel.x == 0 && !jumpCoolDown) {
            enemy.jump();
            jumpCoolDown = true;
            timer.start(1f, "jump_cooldown", this);
        }
    }

    //change movement of enemy to follow player
    public void follow(Vector2 enemyPos, Vector2 playerPos) {
        if (enemyPos.x < playerPos.x) {
            enemy.setMovementState(Constants.MSTATE.RIGHT);
        } else {
            enemy.setMovementState(Constants.MSTATE.LEFT);
        }
    }

    @Override
    public void notify(String flag) {
        if (flag.equals("jump_cooldown")) {
            jumpCoolDown = false;
        } else if (flag.equals("stop") && !eyesOnPlayer) {
            enemy.setMovementState(Constants.MSTATE.HSTILL);
        }
    }
}
