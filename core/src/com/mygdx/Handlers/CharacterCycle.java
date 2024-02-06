package com.mygdx.Handlers;

import com.mygdx.RoleCast.PlayableCharacter;
import java.util.*;

public class CharacterCycle {
    private List<PlayableCharacter> characters;
    private int currentIndex;
    private final VisionMap visionMap;

    public CharacterCycle(VisionMap visionMap) {
        characters = new ArrayList<>();
        this.visionMap = visionMap;
        currentIndex = 0;
    }

    public void initialize(PlayableCharacter currCharacter) { characters.add(currCharacter); }

    public void updateCycle() {
        characters = visionMap.getBullseyeStream();
        if (characters.size() >= currentIndex) {
            currentIndex = currentIndex % characters.size(); // Adjust the current index after updating
        }
    }

    public PlayableCharacter getCurrentCharacter() {
        if (currentIndex >= characters.size()) currentIndex = 0;
        return characters.get(currentIndex);
    }

    public void cycleNext() {
        if (!characters.isEmpty()) {
            currentIndex = (currentIndex + 1) % characters.size();
        }
    }

    public void resetCurrIndex() {
        currentIndex = 0;
    }

}
