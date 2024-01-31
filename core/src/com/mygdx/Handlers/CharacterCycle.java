package com.mygdx.Handlers;

import com.mygdx.RoleCast.PlayableCharacter;
import java.util.*;

public class CharacterCycle {
    private final List<PlayableCharacter> characters;
    private int currentIndex;

    public CharacterCycle() {
        characters = new ArrayList<>();
        currentIndex = -1;
    }

    public void initialize(PlayableCharacter currCharacter) {
        addCharacter(currCharacter);
    }

    public void addCharacter(PlayableCharacter character) {
        if (characters.contains(character)) return;
        characters.add(character);
        if (currentIndex == -1) {
            currentIndex = 0; // If the cycle was empty, set the current index to 0
        }
    }

    public void removeCharacter(PlayableCharacter character) {
        characters.remove(character);
        if (characters.isEmpty()) {
            currentIndex = -1; // If the cycle is empty after removal, set the current index to -1
        } else {
            currentIndex = currentIndex % characters.size(); // Adjust the current index after removal
        }
    }

    public PlayableCharacter getCurrentCharacter() {
        if (currentIndex == -1) {
            return null; // Return null if the cycle is empty
        }
        return characters.get(currentIndex);
    }

    public void cycleNext() {
        if (!characters.isEmpty()) {
            currentIndex = (currentIndex + 1) % characters.size();
        }
    }

}
