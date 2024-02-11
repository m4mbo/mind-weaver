package com.mygdx.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.mygdx.Helpers.FancyFontHelper;
import com.mygdx.Objects.Item;
import com.mygdx.RoleCast.Mage;
import com.mygdx.Tools.MyResourceManager;
import java.util.LinkedList;

public class HUD {

    private LinkedList<Item> inventory;
    public Stage stage;
    private Viewport viewport;
    private MyResourceManager resourceManager;
    private InventoryActor inventoryActor;
    private boolean standBy;

    public HUD(SpriteBatch batch, MyResourceManager resourceManager) {
        this.resourceManager = resourceManager;

        inventory = new LinkedList<>();
        viewport = new FitViewport(2000, 1000, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        standBy = false;

        stage.addActor(new ButtonActor(resourceManager.getTexture("pause"), resourceManager.getTexture("inventory")));

        inventoryActor = new InventoryActor(resourceManager.getTexture("papaya"), resourceManager.getTexture("bug"));

        stage.addActor(inventoryActor);

        inventoryActor.setVisibility(false);
    }

    public void addItem(Item item) {
        inventory.add(item);
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
        inventoryActor.setVisibility(true);
    }

    public void removeInventory() {
        standBy = false;
        inventoryActor.setVisibility(false);
    }

    public boolean standBy() {
        return standBy;
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
            float x = 10;
            float y = viewport.getWorldHeight() - 110;

            for (int i = 0; i < player.getLives(); i++) {
                batch.draw(region, x, y, 100, 100);
                x += 110;
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

            batch.draw(region1, viewport.getWorldWidth() - 110, viewport.getWorldHeight() - 110, 100, 100);

            batch.draw(region2, viewport.getWorldWidth() - 220, viewport.getWorldHeight() - 110, 50, 100);

        }
    }

    private class InventoryActor extends Table {
        private final TextureRegion papayaRegion;
        private final TextureRegion bugRegion;
        private Label papayaLabel;
        private Label bugLabel;

        public InventoryActor(Texture papaya, Texture bug) {

            papayaRegion = new TextureRegion(papaya);
            bugRegion = new TextureRegion(bug);

            papayaLabel = new Label("x" + 0, new Label.LabelStyle(FancyFontHelper.getInstance().getFont(Color.BLACK, 90), Color.BLACK));
            add(new Image(papayaRegion)).width(150).height(150).pad(10);
            add(papayaLabel).pad(10);

            setPosition((viewport.getWorldWidth() - this.getWidth()) / 2, (viewport.getWorldHeight() - this.getHeight()) / 2);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {

            int papayaCount = 0;
            int bugCount = 0;

            for (Item item : inventory) {
                if (item.getName().equals("papaya")) papayaCount++;
                if (item.getName().equals("bug")) bugCount++;
            }

            papayaLabel.setText("x" + papayaCount);

            if (bugCount != 0) {
                row();
                add(new Image(bugRegion)).width(50).height(50).pad(10);
            }

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            super.draw(batch, parentAlpha);
        }

        public void setVisibility(boolean state) {
            setVisible(state);
        }
    }
}
