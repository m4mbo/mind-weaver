package com.mygdx.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Tools.ColorGenerator;


public class ShaderHandler {

    private float time;
    private final ColorGenerator colorGenerator;
    private final ShaderProgram waveShader;
    private final ShaderProgram blinkShader;
    private final ShaderProgram redMaskShader;
    private final ShaderProgram outlineShader;
    private final ShaderProgram randColShader;

    public ShaderHandler(ColorGenerator colorGenerator) {
        this.colorGenerator = colorGenerator;
        time = 0;
        waveShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Wave.glsl").readString());
        blinkShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Blink.glsl").readString());
        redMaskShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/RedMask.glsl").readString());
        outlineShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Outline.glsl").readString());
        randColShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/RandomColorMask.glsl").readString());
        ShaderProgram.pedantic = false;
        if (!waveShader.isCompiled()) {
            System.out.println(waveShader.getLog());
        }
        if (!blinkShader.isCompiled()) {
            System.out.println(blinkShader.getLog());
        }
        if (!redMaskShader.isCompiled()) {
            System.out.println(redMaskShader.getLog());
        }
        if (!outlineShader.isCompiled()) {
            System.out.println(outlineShader.getLog());
        }
        if (!randColShader.isCompiled()) {
            System.out.println(randColShader.getLog());
        }
    }

    public void update(float delta) {
        time += delta;
        waveShader.bind();
        waveShader.setUniformf("u_time", time);
        waveShader.setUniformf("u_resolution", new Vector2(100, 100));
        blinkShader.bind();
        blinkShader.setUniformf("u_time", time);
        randColShader.bind();
        randColShader.setUniformf("r", colorGenerator.getCurrentColor().x);
        randColShader.setUniformf("g", colorGenerator.getCurrentColor().y);
        randColShader.setUniformf("b", colorGenerator.getCurrentColor().z);
    }

    public ShaderProgram getShaderProgram(String key) {
        if (key.equals("wave")) return waveShader;
        if (key.equals("blink")) return blinkShader;
        if (key.equals("redMask")) return redMaskShader;
        if (key.equals("outline")) return outlineShader;
        if (key.equals("rand_col")) return randColShader;
        return null;
    }
}
