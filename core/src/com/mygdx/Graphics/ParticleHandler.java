package com.mygdx.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.LinkedList;

public class ParticleHandler {

    // Using particle pools to avoid garbage collection
    private ParticleEffectPool dustGroundEffectPool;
    private ParticleEffectPool dustWallEffectPool;
    private ParticleEffectPool airRightEffectPool;
    private ParticleEffectPool airLeftEffectPool;
    private ParticleEffectPool auraEffectPool;
    private LinkedList<PooledEffect> effects;

    public ParticleHandler() {

        effects = new LinkedList<>();

        float scaleFactor = 1 / 2000f;

        // Initializing particle effects

        ParticleEffect dustGround = new ParticleEffect();
        dustGround.load(Gdx.files.internal("Particles/dust_ground.p"), Gdx.files.internal("Particles"));
        dustGround.scaleEffect(scaleFactor);

        ParticleEffect dustWall = new ParticleEffect();
        dustWall.load(Gdx.files.internal("Particles/dust_wall.p"), Gdx.files.internal("Particles"));
        dustWall.scaleEffect(scaleFactor);

        ParticleEffect airRight = new ParticleEffect();
        airRight.load(Gdx.files.internal("Particles/air_right.p"), Gdx.files.internal("Particles"));
        airRight.scaleEffect(scaleFactor);

        ParticleEffect airLeft = new ParticleEffect();
        airLeft.load(Gdx.files.internal("Particles/air_left.p"), Gdx.files.internal("Particles"));
        airLeft.scaleEffect(scaleFactor);

        ParticleEffect aura = new ParticleEffect();
        aura.load(Gdx.files.internal("Particles/aura.p"), Gdx.files.internal("Particles"));
        aura.scaleEffect(scaleFactor);

        airRightEffectPool = new ParticleEffectPool(airRight, 1, 2);
        dustGroundEffectPool = new ParticleEffectPool(dustGround, 1, 2);
        dustWallEffectPool = new ParticleEffectPool(dustWall, 1, 2);
        airLeftEffectPool = new ParticleEffectPool(airLeft, 1, 2);
        auraEffectPool = new ParticleEffectPool(aura, 1, 2);
    }

    public void addParticleEffect(String tag, float x, float y) {
        PooledEffect effect = null;
        switch (tag) {
            case "dust_ground":
                effect = dustGroundEffectPool.obtain();
                break;
            case "dust_wall":
                effect = dustWallEffectPool.obtain();
                break;
            case "air_right":
                effect = airRightEffectPool.obtain();
                break;
            case "air_left":
                effect = airLeftEffectPool.obtain();
                break;
            case "aura":
                effect = auraEffectPool.obtain();
                break;
            default:
                break;
        }

        assert effect != null;
        effect.setPosition(x, y);
        effects.add(effect);
    }

    public void render(SpriteBatch batch, float delta) {
        // Linked list to keep track of particles to remove
        LinkedList<ParticleEffect> toRemove = new LinkedList<>();

        //Rendering all particles
        batch.begin();
        for (int i = effects.size() - 1; i >= 0; i--) {
            PooledEffect effect = effects.get(i);
            effect.draw(batch, delta);
            if (effect.isComplete()) {
                effect.free();
                toRemove.add(effect);
            }
        }
        batch.end();
        effects.removeAll(toRemove);
    }
}
