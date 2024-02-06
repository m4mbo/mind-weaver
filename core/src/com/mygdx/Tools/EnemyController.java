package com.mygdx.Tools;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.Handlers.VisionMap;
import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.Subscriber;
import com.mygdx.RoleCast.PlayableCharacter;

public class EnemyController implements Subscriber {
    private PlayableCharacter enemy;
    private PlayableCharacter player;
    private VisionMap visionMap;
    private MyTimer timer;
    private boolean waitForNextJump;

    public EnemyController(PlayableCharacter player, PlayableCharacter enemy, VisionMap visionMap, MyTimer timer) {
        this.player = player;
        this.enemy = enemy;
        this.visionMap = visionMap;
        this.timer = timer;
        waitForNextJump = false;
    }

    public void update() {
        if (!visionMap.sendSignal(enemy, player)) return;

        Vector2 enemyPos = enemy.getPosition();
        Vector2 playerPos = player.getPosition();

        if (enemyPos.x < playerPos.x) {
            enemy.setMovementState(Constants.MSTATE.RIGHT);
        } else {
            enemy.setMovementState(Constants.MSTATE.LEFT);
        }

        Vector2 vel = enemy.getB2body().getLinearVelocity();

        if (vel.x == 0 && !waitForNextJump) {
            enemy.jump();
            waitForNextJump = true;
            timer.start(1f, "jump_cooldown", this);
        }
    }

    @Override
    public void notify(String flag) {
        switch (flag) {
            case "jump_cooldown":
                waitForNextJump = false;
                break;
        }
    }
}
