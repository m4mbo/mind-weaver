package com.mygdx.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.Graphics.ShaderHandler;
import com.mygdx.Helpers.Constants;
import com.mygdx.Sprites.B2Sprite;
import com.mygdx.Tools.MyResourceManager;

public class Item extends B2Sprite {

    private final String name;
    private final ShaderHandler shaderHandler;

    public Item(float x, float y, World world, String name, ShaderHandler shaderHandler, MyResourceManager resourceManager) {
        this.name = name;
        this.shaderHandler = shaderHandler;

        if (name.equals("papaya")) setAnimation(TextureRegion.split(resourceManager.getTexture("papaya"), 13, 10)[0], 1/4f, true, 0.7f);
        else if (name.equals("bug")) setAnimation(TextureRegion.split(resourceManager.getTexture("bug"), 11, 13)[0], 1/4f, false, 0.7f);


        BodyDef bdef = new BodyDef();
        bdef.position.set(x / Constants.PPM, y / Constants.PPM);
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        if (name.equals("bug")) return;

        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(4 / Constants.PPM, 4 / Constants.PPM);
        fdef.shape = polygonShape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Constants.BIT_GROUND;
        fdef.filter.maskBits = Constants.BIT_MAGE;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void render(SpriteBatch batch) {
        if (name.equals("papaya")) batch.setShader(shaderHandler.getShaderProgram("blink"));
        super.render(batch);
        batch.setShader(null);
    }

    public String getName() {
        return name;
    }

}
