package com.mygdx.Handlers;

import com.mygdx.Helpers.DirectedGraph;
import com.mygdx.RoleCast.PlayableCharacter;

// Range of vision handler
public class ROVHandler {

    private DirectedGraph targetAssociation;

    public ROVHandler(PlayableCharacter character) {
        targetAssociation = new DirectedGraph(character);
    }

    public PlayableCharacter getNextNeighbour(PlayableCharacter character) {
        return (PlayableCharacter) targetAssociation.getNextNeighbour(character);
    }

    public PlayableCharacter getNextNeighbour(PlayableCharacter character, PlayableCharacter mask) {
        return (PlayableCharacter) targetAssociation.getNextNeighbour(character, mask);
    }

    public void addNeighbour(PlayableCharacter source, PlayableCharacter target) {
        targetAssociation.addNode(source, target);
    }

    public void removeNeighbour(PlayableCharacter target) {
        targetAssociation.removeNode(target);
    }

    public void printROV() {
        targetAssociation.printGraph();
    }

}
