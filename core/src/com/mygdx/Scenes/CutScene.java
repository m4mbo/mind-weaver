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

// Cutscene actor
public class CutScene extends Actor {
    private final Stage stage;
    private final MyResourceManager resourceManager;
    private LinkedList<String> messageChain;
    private LinkedList<TextureRegion> regionChain;
    private int currIndex;              //Letter printing position
    private final BitmapFont font;      //Cutscene font
    private float stringCompleteness;   //Text animation
    private TextureRegion bgRegion;     //Background region
    private int bgWidth = 3000;         //Background width
    private int bgHeight = 800;         //Background height

    public CutScene(Stage stage, String tag, MyResourceManager resourceManager) {
        this.stage = stage;
        this.resourceManager = resourceManager;

        messageChain = new LinkedList<>();
        regionChain = new LinkedList<>();

        bgRegion = new TextureRegion(resourceManager.getTexture("cutscene_bg"));

        setPosition(stage.getViewport().getWorldWidth() / 2 - 600, 580);

        handleTag(tag);

        currIndex = 0;

        font = FancyFontHelper.getInstance().getFont(Color.WHITE, 80);
    }

    //Animate text printing
    public void update(float delta) {
        stringCompleteness += Constants.TEXT_SPEED * delta;
    }

    //Cycle through the message in the message chain and reset
    public boolean cycleMessage() {
        currIndex++;
        stringCompleteness = 0;
        if (currIndex >= messageChain.size()) {
            currIndex = 0;
            return true;
        }
        return false;
    }

    // Adding images and text to chains depending on the tag received
    private void handleTag(String tag) {

        switch (tag) {
            case "intro":
                messageChain.add("zzz\n\n\n(Press X to skip)");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_chained")));
                messageChain.add("Ohhhhh!");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_freed")));
                messageChain.add("You found me!");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_happy")));
                messageChain.add(".... ..  ....");
                regionChain.add(new TextureRegion(resourceManager.getTexture("butterfly")));
                messageChain.add("How did I end up here?");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                messageChain.add("...    .");
                regionChain.add(new TextureRegion(resourceManager.getTexture("butterfly")));
                break;
            case "closed_shop":
                messageChain.add("Hi there fellow\ntraveler!");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("Hello... sorry, who\nare you?");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                messageChain.add("WHO AM I? I am one of \nthe merchants of \nall time, of course.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_bling")));
                messageChain.add("Yeah...");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_pokerface")));
                messageChain.add("DO YOU SMELL THAT?");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("is that...");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("PAPAYA!");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_papaya")));
                messageChain.add("huh");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_surprised")));
                messageChain.add("You see, these nasty \ngoblins and I share \none thing in common.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_bling")));
                messageChain.add("We both have a BIG \nobsession with that\ntasty, sweet, juicy\nfruit.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("I have spent many years \nof my life roaming\nthrough this dungeon \nin search of those\ntasty treats.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("I see... well good luck.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_neutral")));
                messageChain.add("WAIT!");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_neutral")));
                messageChain.add("Please, if you find \nsome, let me know.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("I can reveal some very \nIMPORTANT secrets about \nthis place.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                break;
            case "closed_shop2":
                messageChain.add("Find me 3 slices of\nPAPAYA to reveal their\nsecrets.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_papaya3")));
                break;
            case "open_shop":
                messageChain.add("Aha! I can smell those \nslices from a mile \naway.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_smell")));
                messageChain.add("As promised, I will \nreveal some very \nIMPORTANT information \nabout this place.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_bling")));
                messageChain.add("I captured this little\nfella in the deepest \ncorner of the peruvian\nAmazon forest.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("It is said that he \ncarries very special mind\ncontrolling properties.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("The kind that will\nallow you to penetrate\neven the hardest\nmaterials.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("Please, have it.\nYou will put it\nto a better use.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("merchant_bug")));
                messageChain.add("....");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_eating")));
                messageChain.add("**5@!/=_@)**");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_ate")));
                messageChain.add("Time to test my new\npowers!");
                regionChain.add(new TextureRegion(resourceManager.getTexture("mage_happy")));
                break;
            case "lesson":
                messageChain.add("Welcome to Mind Weaver\n101.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To move around, feel free\nto choose between\nA and D, or the\nLEFT and RIGHT arrow keys.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To jump (and wall-jump):\nSPACE");
                regionChain.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To interact: X.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To control minds: shift.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                messageChain.add("To attack (?!): J.");
                regionChain.add(new TextureRegion(resourceManager.getTexture("bug_teacher")));
                break;
            default:
                break;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(bgRegion, (stage.getViewport().getWorldWidth() - bgWidth) / 2, 50, bgWidth, bgHeight);

        batch.draw(regionChain.get(currIndex), (stage.getViewport().getWorldWidth() - bgWidth) / 2 + 110, 150, 600, 600);

        int charCountThisFrame = (int) stringCompleteness;

        // Drawing first charCountThisFrame characters of the message
        font.draw(batch, messageChain.get(currIndex).substring(0, Math.min(messageChain.get(currIndex).length(), charCountThisFrame)), getX(), getY());
    }
}
