package com.mygdx.World;

import com.mygdx.RoleCast.PlayableCharacter;
import com.mygdx.Tools.ColourGenerator;
import java.util.*;

// Class holding playable character cycle
public class CharacterCycle {
    private List<PlayableCharacter> characters;     // Characters ready to cotrol
    private int currentIndex;
    private final VisionMap visionMap;
    private final ColourGenerator colourGenerator;

    public CharacterCycle(VisionMap visionMap, ColourGenerator colourGenerator) {
        characters = new ArrayList<>();
        this.visionMap = visionMap;
        this.colourGenerator = colourGenerator;
        currentIndex = 0;
    }

    public void initialize(PlayableCharacter currCharacter) { characters.add(currCharacter); }

    /*
     * Updating cycle based on vision map bullseye stream
     * Reachable nodes from mage
     */
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

    public boolean cycleNext() {
        PlayableCharacter temp = getCurrentCharacter();
        if (!characters.isEmpty()) {
            currentIndex = (currentIndex + 1) % characters.size();  // Adjusting index to avoid going over list size
        }
        colourGenerator.getNextColor();
        return !temp.equals(getCurrentCharacter());
    }

    public void resetCurrIndex() {
        currentIndex = 0;
    }

}
