package com.mygdx.Tools;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.Handlers.VisionMap;
import com.mygdx.Helpers.Constants;
import com.mygdx.RoleCast.ArmourGoblin;
import com.mygdx.RoleCast.Mage;
import com.mygdx.RoleCast.PlayableCharacter;

public class EnemyController {
    private PlayableCharacter enemy;
    private PlayableCharacter player;
    private VisionMap visionMap;

    public EnemyController(PlayableCharacter player, PlayableCharacter enemy, VisionMap visionMap) {
        this.player = player;
        this.enemy = enemy;
        this.visionMap = visionMap;
    }

    public void update() {
        if (!visionMap.sendSignal(enemy, player)) return;

        Vector2 enemyPos = enemy.getPosition();
        Vector2 playerPos = player.getPosition();
        if (enemyPos.x < playerPos.x) {
            if (enemy.getWallState() == 1) enemy.jump();
            else enemy.setMovementState(Constants.MSTATE.RIGHT);
        } else {
            if (enemy.getWallState() == -1) enemy.jump();
            else enemy.setMovementState(Constants.MSTATE.LEFT);
        }
    }




}
