package com.mygdx.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.mygdx.Helpers.Constants;
import com.mygdx.Helpers.FancyFontHelper;

// Cutscene actor represented as a stack of other actors
public class CutScene extends Stack {

    private String message;
    private Label messageLabel;
    private float stringCompleteness;

    public CutScene(String tag) {
        handleTag(tag);

        messageLabel = new Label("", new Label.LabelStyle(FancyFontHelper.getInstance().getFont(Color.BLACK, 90), Color.BLACK));
    }

    public void update(float delta) {
        stringCompleteness = Constants.TEXT_SPEED * delta;
    }

    private void handleTag(String tag) {

        switch (tag) {
            case "mage_intro_1":
                message = "Where am I?";
                break;
            case "mage_intro_2":
                message = "You found me!";
                break;
            default:
                break;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        int charCountThisFrame = (int) stringCompleteness;

        // Drawing first charCountThisFrame characters of the message
        messageLabel.setText(message.substring(0, Math.min(message.length(), charCountThisFrame)));

        super.draw(batch, parentAlpha);
    }
}
