package com.mygdx.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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

        setPosition(stage.getViewport().getWorldWidth() / 2 - 600, 580);

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
                messageChain.add("...    .");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("butterfly")));
                break;
            case "closed_shop":
                messageChain.add("Hi there fellow\ntraveler!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("Hello... sorry, who\nare you?");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                messageChain.add("WHO AM I? I am one of \nthe merchants of \nall time, of course.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_bling")));
                messageChain.add("Yeah...");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_pokerface")));
                messageChain.add("DO YOU SMELL THAT?");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("is that...");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("PAPAYA!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_papaya")));
                messageChain.add("huh");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_surprised")));
                messageChain.add("You see, these nasty \ngoblins and I share \none thing in common.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_bling")));
                messageChain.add("We both have a BIG \nobsession with that\ntasty, sweet, juicy\nfruit.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("I have spent many years \nof my life roaming\nthrough this dungeon \nin search of those\ntasty treats.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("I see... well good luck.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                messageChain.add("WAIT!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("Please, if you find \nsome, let me know.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("I can reveal some very \nIMPORTANT secrets about \nthis place.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                break;
            case "closed_shop2":
                messageChain.add("Find me 3 slices of\nPAPAYA to reveal their\nsecrets.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_papaya3")));
                break;
            case "open_shop":
                messageChain.add("Aha! I can smell those \nslices from a mile \naway.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("As promised, I will \nreveal some very \nIMPORTANT information \nabout this place.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_bling")));
                messageChain.add("I captured this little\nfella in the deepest \ncorner of the peruvian\nAmazon forest.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("It is said that he \ncarries very special mind\ncontrolling properties.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("The kind that will\nallow you to penetrate\neven the hardest\nmaterials.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("Please, have it.\nYou will put it\nto a better use.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("....");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_eating")));
                messageChain.add("**5@!/=_@)**");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_ate")));
                messageChain.add("Time to test my new\npowers!");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("mage_happy")));
                break;
            case "lesson":
                messageChain.add("Welcome to Mind Weaver\n101.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To move around, feel free\nto choose between\nA and D, or the\nLEFT and RIGHT arrow keys.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To jump: SPACE or \nUP arrow keys.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To interact: X.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To control minds: shift.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To attack (?!): J.");
                characterRegions.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
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
