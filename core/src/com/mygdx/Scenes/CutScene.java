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
    private LinkedList<TextureRegion> characterRegions;
    private int currIndex;
    private final BitmapFont font;
    private float stringCompleteness;
    private TextureRegion bgRegion;
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

        currIndex = 0;

        font = FancyFontHelper.getInstance().getFont(Color.WHITE, 80);
    }

    public void update(float delta) {
        stringCompleteness += Constants.TEXT_SPEED * delta;
    }

    public boolean cycleMessage() {
        currIndex++;
        stringCompleteness = 0;
        if (currIndex >= messageChain.size()) {
            currIndex = 0;
            return true;
        }
        return false;
    }

    private void handleTag(String tag) {

        switch (tag) {
            case "intro":
                messageChain.add("zzz");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_chained")));
                messageChain.add("Ohhhhh!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_freed")));
                messageChain.add("You found me!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_happy")));
                messageChain.add(".... ..  ....");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("butterfly")));
                messageChain.add("How did I end up here?");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                messageChain.add("...           .");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("butterfly")));
                break;
            case "open_shop":
                messageChain.add("You found me!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                break;
            case "closed_shop":
                messageChain.add("You found me!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                break;
            default:
                break;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(bgRegion, (stage.getViewport().getWorldWidth() - bgWidth) / 2, 50, bgWidth, bgHeight);

        batch.draw(characterRegions.get(currIndex), (stage.getViewport().getWorldWidth() - bgWidth) / 2 + 110, 150, 600, 600);

        int charCountThisFrame = (int) stringCompleteness;

        // Drawing first charCountThisFrame characters of the message
        font.draw(batch, messageChain.get(currIndex).substring(0, Math.min(messageChain.get(currIndex).length(), charCountThisFrame)), getX(), getY());
    }
}
