package com.mygdx.Handlers;

import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.RoleCast.Mage;

import java.util.*;

public class ControlHandler {
    private final List<PlayableCharacter> controlChain;
    private int currentIndex;
    private PlayableCharacter currCharacter;

    public ControlHandler() {
        controlChain = new ArrayList<>();
        currentIndex = 0;
    }

    public void initialize(PlayableCharacter currCharacter) {
        this.currCharacter = currCharacter;
        controlChain.add(currCharacter);
    }

    public boolean cycleCharacter() {
        if (currentIndex >= controlChain.size()) currentIndex--;
        currCharacter = controlChain.get(currentIndex);
        currentIndex = (currentIndex + 1) % controlChain.size();
        return !(currCharacter instanceof Mage);
    }

    public void addCharacterControl(PlayableCharacter character) {
        if (controlChain.contains(character)) return;
        controlChain.add(character);
    }

    public void removeCharacterControl(PlayableCharacter character) {
        controlChain.remove(character);
    }

    public PlayableCharacter getCurrCharacter() {
        return currCharacter;
    }
}
