package com.mygdx.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Objects.Interactable;
import com.mygdx.Objects.Reactable;
import java.util.LinkedList;

public class ObjectHandler {

    public LinkedList<Interactable> interactables;
    public LinkedList<Reactable> reactables;

    public ObjectHandler() {
        interactables = new LinkedList<>();
        reactables = new LinkedList<>();
    }

    public void addObject(Interactable interactable) {
        interactables.add(interactable);
    }

    public void addObject(Reactable reactable) {
        reactables.add(reactable);
    }

    public void update(float delta) {
        for (Interactable interactable : interactables) {
            interactable.update(delta);
        }
        for (Reactable reactable : reactables) {
            reactable.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (Interactable interactable : interactables) {
            interactable.render(batch);
        }
        for (Reactable reactable : reactables) {
            reactable.render(batch);
        }
    }
}
