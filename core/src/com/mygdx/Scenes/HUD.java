package com.mygdx.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Graphics.ShaderHandler;
import com.mygdx.Tools.FancyFontHelper;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Tools.ColorGenerator;
import com.mygdx.Tools.MyResourceManager;
import com.mygdx.Tools.ShapeDrawer;

public class HUD {

    private final boolean[] papayas;
    public Stage stage;
    private final Viewport viewport;
    private final MyResourceManager resourceManager;
    private final InventoryActor inventoryActor;
    private final ShapeDrawer shapeDrawer;
    private boolean standBy;
    private boolean powersUnlocked;
    private CutScene currCutscene;

    public HUD(SpriteBatch batch, MyResourceManager resourceManager) {
        this.resourceManager = resourceManager;

        this.shapeDrawer = new ShapeDrawer(new ShaderHandler(new ColorGenerator()), resourceManager);

        papayas = new boolean[]{false, false, false, false, false};
        viewport = new FitViewport(3840, 2160, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        standBy = false;
        powersUnlocked = false;

        stage.addActor(new ButtonActor(resourceManager.getTexture("pause"), resourceManager.getTexture("inventory")));

        inventoryActor = new InventoryActor(resourceManager.getTexture("papaya"));

        stage.addActor(inventoryActor);

        currCutscene = null;

        inventoryActor.setVisible(false);
    }

    public void addPapaya(int level) {
        papayas[level] = true;
    }

    public void removePapaya(int level) {
        papayas[level-1] = false;
    }

    public void setPlayer(Mage player) {
        stage.addActor(new LifeActor(resourceManager.getTexture("life"), player));
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
    }

    public void pushInventory() {
        standBy = true;
        inventoryActor.setVisible(true);
    }

    public void pushCutscene(String tag) {
        if (tag.equals("open_shop")) powersUnlocked = true;
        currCutscene = new CutScene(stage, tag, resourceManager);
        stage.addActor(currCutscene);
        currCutscene.setVisible(true);
    }

    public void cycleCutscene() {
        if (!currCutscene.cycleMessage()) return;
        currCutscene.setVisible(false);
        currCutscene.remove();
        currCutscene = null;
    }

    public void removeCutscene() {
        if (currCutscene == null) return;
        currCutscene.setVisible(false);
        currCutscene.remove();
        currCutscene = null;
    }

    public void update(float delta) {
        if (currCutscene != null) currCutscene.update(delta);
    }

    public void removeInventory() {
        standBy = false;
        inventoryActor.setVisible(false);
    }

    public boolean standBy() {
        return standBy;
    }

    public CutScene getCurrCutscene() {
        return currCutscene;
    }

    public boolean enoughPapaya() {
        int papayaCount = 0;
        for (boolean item : papayas) {
            if (item) papayaCount++;
        }
        return papayaCount >= 3;
    }

    public boolean arePowersUnlocked() {
        return powersUnlocked;
    }

    private class LifeActor extends Actor {
        private final TextureRegion region;
        private final Mage player;

        public LifeActor(Texture texture, Mage player) {
            region = new TextureRegion(texture);
            this.player = player;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            float x = 20;
            float y = viewport.getWorldHeight() - 240;

            for (int i = 0; i < player.getLives(); i++) {
                batch.draw(region, x, y, 230, 180);
                x += 250;
            }
        }
    }

    private class ButtonActor extends Actor {
        private final TextureRegion region1;
        private final TextureRegion region2;

        public ButtonActor(Texture texture1, Texture texture2) {
            region1 = new TextureRegion(texture1);
            region2 = new TextureRegion(texture2);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            batch.draw(region1, viewport.getWorldWidth() - 310, viewport.getWorldHeight() - 210, 300, 180);

            batch.draw(region2, viewport.getWorldWidth() - 460, viewport.getWorldHeight() - 210, 150, 180);
        }
    }

    private class InventoryActor extends Table {
        private final Label papayaLabel;

        public InventoryActor(Texture papaya) {

            TextureRegion papayaRegion = new TextureRegion(papaya);

            papayaLabel = new Label("x" + 0, new Label.LabelStyle(FancyFontHelper.getInstance().getFont(Color.BLACK, 90), Color.BLACK));
            add(new Image(papayaRegion)).width(150).height(150).pad(10);
            add(papayaLabel).pad(10);

            setPosition((viewport.getWorldWidth() - this.getWidth()) / 2, (viewport.getWorldHeight() - this.getHeight()) / 2);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {

            int papayaCount = 0;

            for (boolean item : papayas) {
                if (item) papayaCount++;
            }

            papayaLabel.setText("x" + papayaCount);

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            shapeDrawer.drawRectangle(viewport.getWorldHeight(), viewport.getWorldWidth(), 0, 0, "translucent");
            shapeDrawer.drawRectangle(600, 600, (viewport.getWorldWidth() - 600) / 2, (viewport.getWorldHeight() - 600) / 2, "gray");

            batch.end();
            shapeDrawer.render((SpriteBatch) batch);
            batch.begin();

            super.draw(batch, parentAlpha);
        }
    }
}
