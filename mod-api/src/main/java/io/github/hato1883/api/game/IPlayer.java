package io.github.hato1883.api.game;

import com.badlogic.gdx.graphics.Color;
import io.github.hato1883.api.game.board.IStructure;

import java.util.List;

public interface IPlayer {
    String getName();
    Color getColor();
    IPlayerController getController(); // Human, AI, Remote, etc.
    int getVictoryPoints();
    IResourceBank getResourceBank();
    default void addResource(IResourceType resource, int i) {
        getResourceBank().add(resource, i);
    }
    List<IStructure> getStructures();
}
