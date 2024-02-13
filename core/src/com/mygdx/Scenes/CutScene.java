package com.mygdx.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.Helpers.Constants;
import com.mygdx.Tools.FancyFontHelper;
import com.mygdx.Tools.MyResourceManager;

import java.util.LinkedList;

// Cutscene actor represented as a stack of other actors
public class CutScene extends Actor {
    private final Stage stage;
    private final MyResourceManager resourceManager;
    private LinkedList<String> messageChain;
    private int message;
    private final BitmapFont font;
    private float stringCompleteness;
    private TextureRegion bgRegion;
    private LinkedList<TextureRegion> characterRegions;
    private int currCharacter;
    private int bgWidth = 3000;
    private int bgHeight = 800;

    public CutScene(Stage stage, String tag, MyResourceManager resourceManager) {
        this.stage = stage;
        this.resourceManager = resourceManager;

        messageChain = new LinkedList<>();
        characterRegions = new LinkedList<>();

        bgRegion = new TextureRegion(resourceManager.getTexture("cutscene_bg"));

        setBounds(stage.getViewport().getWorldWidth() / 2 - 600, 550, bgWidth, bgHeight);

        handleTag(tag);

        message = 0;
        currCharacter = 0;

        font = FancyFontHelper.getInstance().getFont(Color.WHITE, 80);
    }

    public void update(float delta) {
        stringCompleteness += Constants.TEXT_SPEED * delta;
    }

    public boolean cycleMessage() {
        message++;
        currCharacter++;
        stringCompleteness = 0;
        if (message >= messageChain.size()) {
            message = 0;
            currCharacter = 0;
            return true;
        }
        return false;
    }

    private void handleTag(String tag) {

        switch (tag) {
            case "intro":
                messageChain.add("Where am I? yessi ghthg\nfgfgfg fggfgf");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                break;
            case "merchant":
                messageChain.add("You found me!");
                break;
            default:
                break;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(bgRegion, (stage.getViewport().getWorldWidth() - bgWidth) / 2, 50, bgWidth, bgHeight);

        batch.draw(characterRegions.get(currCharacter), (stage.getViewport().getWorldWidth() - bgWidth) / 2 + 110, 150, 600, 600);

        int charCountThisFrame = (int) stringCompleteness;

        // Drawing first charCountThisFrame characters of the message
        font.draw(batch, messageChain.get(message).substring(0, Math.min(messageChain.get(message).length(), charCountThisFrame)), getX(), getY());
    }
}
