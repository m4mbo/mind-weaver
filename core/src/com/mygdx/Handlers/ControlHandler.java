package com.mygdx.Handlers;

import com.mygdx.RoleCast.PlayableCharacter;

import java.util.Stack;

public class ControlHandler {

    private Stack<CharacterPair> controlChain;    // Chain of current and prev characters

    public ControlHandler(PlayableCharacter currCharacter) {
        controlChain = new Stack<>();
        controlChain.push(new CharacterPair(currCharacter, null));
    }

    public boolean characterRollback() {
        if (controlChain.peek().prevCharacter == null) return false;
        controlChain.pop();
        return true;
    }

    public void setCurrCharacter(PlayableCharacter currCharacter) {
        controlChain.push(new CharacterPair(currCharacter, controlChain.peek().currCharacter));
    }

    public PlayableCharacter getCurrCharacter() {
        return controlChain.peek().currCharacter;
    }

    private class CharacterPair {
        PlayableCharacter currCharacter;
        PlayableCharacter prevCharacter;

        public CharacterPair(PlayableCharacter currCharacter, PlayableCharacter prevCharacter) {
            this.currCharacter = currCharacter;
            this.prevCharacter = prevCharacter;
        }
    }
}
