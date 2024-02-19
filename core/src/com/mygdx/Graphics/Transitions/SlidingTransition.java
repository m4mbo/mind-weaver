package com.mygdx.Graphics.Transitions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.Helpers.Constants;

public class SlidingTransition extends Transition {

    private final Constants.SLIDE_DIR direction;
    private final float speed;
    private final TextureRegion screenRegion;
    private float currY, currX;

    public SlidingTransition(TextureRegion screenRegion, float duration, float speed, Constants.SLIDE_DIR direction, Matrix4 screenProjection) {
        super(duration, screenProjection);

        this.screenRegion = screenRegion;
        this.speed = speed;
        this.direction = direction;

        currX = 0;
        currY = 0;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {

        batch.setProjectionMatrix(screenProjection);

        timePassed += delta;

        if (direction == Constants.SLIDE_DIR.SLIDE_DOWN) currY -= speed;
        else if (direction == Constants.SLIDE_DIR.SLIDE_UP) currY += speed;
        else if (direction == Constants.SLIDE_DIR.SLIDE_LEFT) currX -= speed;
        else if (direction == Constants.SLIDE_DIR.SLIDE_RIGHT) currX += speed;

        batch.begin();
        //Flipped texture
        batch.draw(screenRegion, currX, currY + screenRegion.getRegionHeight(), screenRegion.getRegionWidth(), -screenRegion.getRegionHeight());
        batch.end();
    }
}
