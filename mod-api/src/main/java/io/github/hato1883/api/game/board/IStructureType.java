package io.github.hato1883.api.game.board;

public interface IStructureType {
    /**
     * @return the unique ID for this building type (e.g. "settlement", "city", "custom_windmill")
     */
    String getId();
}
