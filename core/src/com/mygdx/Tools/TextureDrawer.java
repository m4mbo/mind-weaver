package com.mygdx.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.Graphics.ShaderHandler;
import com.mygdx.Helpers.Constants;

import java.util.LinkedList;

//Used to draw textures
public class TextureDrawer {

    private LinkedList<CoordTexture> textures;

    private ShaderHandler shaderHandler;

    public TextureDrawer(ShaderHandler shaderHandler) {
        textures = new LinkedList<>();
        this.shaderHandler = shaderHandler;
    }

    public void addTexture(Texture texture, float x, float y, float resize) {
        textures.add(new CoordTexture(new TextureRegion(texture), x, y, resize));
    }

    public void render(SpriteBatch batch) {
        for (CoordTexture texture : textures) {
            texture.render(batch);
        }
    }

    private class CoordTexture {    //Used to draw texture with x and y coordinates using alpha shader

        TextureRegion region;
        float x, y, width, height, resize;

        public CoordTexture(TextureRegion region, float x, float y, float resize) {
            this.region = region;
            this.x = x;
            this.y = y;
            width = region.getRegionWidth() * resize;
            height = region.getRegionHeight() * resize;
            this.resize = resize;
        }

        public void render(SpriteBatch batch) {
            batch.begin();
            batch.setShader(shaderHandler.getShaderProgram("alpha"));
            batch.draw(region, x - ((width / resize) / Constants.PPM) / 2 , y - ((height / resize) / Constants.PPM) / 2, width / Constants.PPM, height / Constants.PPM);
            batch.setShader(null);
            batch.end();
        }
    }

}
