package com.mygdx.Tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Audio.MusicManager;
import com.mygdx.Graphics.LightManager;
import com.mygdx.Graphics.ParticleHandler;
import com.mygdx.Graphics.ShaderHandler;
import com.mygdx.World.CharacterCycle;
import com.mygdx.World.EntityHandler;
import com.mygdx.World.ObjectHandler;
import com.mygdx.World.VisionMap;

//Centralized point of access for handler interaction
public class UtilityStation {

    private final EntityHandler entityHandler;
    private final ObjectHandler objectHandler;
    private final CharacterCycle characterCycle;
    private final VisionMap visionMap;
    private final ParticleHandler particleHandler;
    private final ShaderHandler shaderHandler;
    private final LightManager lightManager;
    private final MusicManager musicManager;

    public UtilityStation(EntityHandler entityHandler, ObjectHandler objectHandler, CharacterCycle characterCycle, VisionMap visionMap, ParticleHandler particleHandler, ShaderHandler shaderHandler, LightManager lightManager, MusicManager musicManager) {
        this.entityHandler = entityHandler;
        this.objectHandler = objectHandler;
        this.characterCycle = characterCycle;
        this.visionMap = visionMap;
        this.particleHandler = particleHandler;
        this.shaderHandler = shaderHandler;
        this.lightManager = lightManager;
        this.musicManager = musicManager;
    }

    public void update(float delta, OrthographicCamera gameCam) {
        shaderHandler.update(delta);
        entityHandler.update(delta);
        visionMap.update(delta);
        objectHandler.update(delta);
        lightManager.update(gameCam);
    }

    public void render(SpriteBatch batch, float delta) {
        particleHandler.render(batch, delta);
        objectHandler.render(batch);
        lightManager.render();
        entityHandler.render(batch);
    }

    public CharacterCycle getCharacterCycle() {
        return characterCycle;
    }

    public ObjectHandler getObjectHandler() {
        return objectHandler;
    }

    public EntityHandler getEntityHandler() {
        return entityHandler;
    }

    public VisionMap getVisionMap() {
        return visionMap;
    }

    public ParticleHandler getParticleHandler() {
        return particleHandler;
    }

    public ShaderHandler getShaderHandler() {
        return shaderHandler;
    }

    public LightManager getLightManager() {
        return lightManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }
}
