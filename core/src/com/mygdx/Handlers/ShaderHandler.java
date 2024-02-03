package com.mygdx.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;


public class ShaderHandler {

    private float time;
    private ShaderProgram waveShader;
    private ShaderProgram blinkShader;

    public ShaderHandler() {
        time = 0;
        waveShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Wave.glsl").readString());
        blinkShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Blink.glsl").readString());
        ShaderProgram.pedantic = false;
        if (!waveShader.isCompiled()) {
            System.out.println(waveShader.getLog());
        }
    }

    public void update(float delta) {
        time += delta;
        waveShader.bind();
        waveShader.setUniformf("u_time", time);
        waveShader.setUniformf("u_resolution", new Vector2(100, 100));
        blinkShader.bind();
        blinkShader.setUniformf("u_time", time);
    }

    public ShaderProgram getShaderProgram(String key) {
        if (key.equals("wave")) return waveShader;
        if (key.equals("blink")) return blinkShader;
        return null;
    }
}
