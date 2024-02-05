package com.mygdx.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.util.LinkedList;

public class ParticleHandler {

    // Using particle pools to avoid garbage collection
    private ParticleEffectPool dustRightEffectPool;
    private ParticleEffectPool dustLeftEffectPool;
    private ParticleEffectPool dustCenterEffectPool;
    private LinkedList<PooledEffect> effects;
    private TextureAtlas particleAtlas;

    public ParticleHandler() {

        effects = new LinkedList<>();
        particleAtlas = new TextureAtlas();

        ParticleEffect dustRight = new ParticleEffect();
        dustRight.load(Gdx.files.internal(""), particleAtlas);
        ParticleEffect dustLeft = new ParticleEffect();
        dustLeft.load(Gdx.files.internal(""), particleAtlas);
        ParticleEffect dustCenter = new ParticleEffect();
        dustCenter.load(Gdx.files.internal(""), particleAtlas);

        dustRightEffectPool = new ParticleEffectPool(dustRight, 1, 2);
        dustLeftEffectPool = new ParticleEffectPool(dustLeft, 1, 2);
        dustCenterEffectPool = new ParticleEffectPool(dustCenter, 1, 2);
    }

    public void addParticleEffect(String tag, float x, float y) {
        PooledEffect effect = null;
        switch (tag) {
            case "dust_left":
                effect = dustLeftEffectPool.obtain();
                break;
            case "dust_right":
                effect = dustRightEffectPool.obtain();
                break;
            case "dust_center":
                effect = dustCenterEffectPool.obtain();
                break;
        }

        assert effect != null;
        effect.setPosition(x, y);
        effects.add(effect);
    }

    public void render(SpriteBatch batch, float delta) {
        LinkedList<ParticleEffect> toRemove = new LinkedList<>();

        for (int i = effects.size() - 1; i >= 0; i--) {
            PooledEffect effect = effects.get(i);
            effect.draw(batch, delta);
            if (effect.isComplete()) {
                effect.free();
                toRemove.add(effect);
            }
        }
        effects.removeAll(toRemove);
    }


}
