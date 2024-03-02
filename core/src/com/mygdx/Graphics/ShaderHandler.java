package com.mygdx.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Tools.ColorGenerator;

// Centralised point to manage shader programs
public class ShaderHandler {

    private float time;         // Current program time
    private final ColorGenerator colorGenerator;        // Random color generator
    private final ShaderProgram waveShader;             // Wave signal
    private final ShaderProgram blinkShader;            // Blinking objects (original colours - white)
    private final ShaderProgram alphaShader;            // Alpha blinking objects (original colours - alpha 0)
    private final ShaderProgram redMaskShader;          // Tinting objects red
    private final ShaderProgram outlineShader;          // Making the outlines purple
    private final ShaderProgram randColShader;          // Random color tinting
    private final ShaderProgram waterShader;            // Water like movement

    public ShaderHandler(ColorGenerator colorGenerator) {
        this.colorGenerator = colorGenerator;
        time = 0;

        // Initializing shader programs

        waveShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Wave.glsl").readString());
        blinkShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Blink.glsl").readString());
        redMaskShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/RedMask.glsl").readString());
        outlineShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Outline.glsl").readString());
        randColShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/RandomColorMask.glsl").readString());
        alphaShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Alpha.glsl").readString());
        waterShader = new ShaderProgram(Gdx.files.internal("Shaders/Vertex.glsl").readString(), Gdx.files.internal("Shaders/Water.glsl").readString());

        ShaderProgram.pedantic = false;  // Makes updating uniform variables more flexible

        // Printing error messages, if any

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
        if (!alphaShader.isCompiled()) {
            System.out.println(randColShader.getLog());
        }
        if (!waterShader.isCompiled()) {
            System.out.println(randColShader.getLog());
        }
    }

    public void update(float delta) {
        time += delta;

        // Updating shader uniforms

        waveShader.bind();
        waveShader.setUniformf("u_time", time);
        waveShader.setUniformf("u_resolution", new Vector2(100, 100));

        blinkShader.bind();
        blinkShader.setUniformf("u_time", time);

        alphaShader.bind();
        alphaShader.setUniformf("u_time", time);

        randColShader.bind();
        randColShader.setUniformf("r", colorGenerator.getCurrentColor().x);
        randColShader.setUniformf("g", colorGenerator.getCurrentColor().y);
        randColShader.setUniformf("b", colorGenerator.getCurrentColor().z);

        waterShader.bind();
        waterShader.setUniformf("u_amount", 2.5f);
        waterShader.setUniformf("u_speed", 1f);
        waterShader.setUniformf("u_time", time);
    }

    // Returning shader program depending on key
    public ShaderProgram getShaderProgram(String key) {
        if (key.equals("wave")) return waveShader;
        if (key.equals("blink")) return blinkShader;
        if (key.equals("redMask")) return redMaskShader;
        if (key.equals("outline")) return outlineShader;
        if (key.equals("rand_col")) return randColShader;
        if (key.equals("alpha")) return alphaShader;
        if (key.equals("water")) return waterShader;
        return null;
    }
}
