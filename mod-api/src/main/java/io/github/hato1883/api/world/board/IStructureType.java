package io.github.hato1883.api.world.board;

import io.github.hato1883.api.Identifier;

public interface IStructureType {
    /**
     * @return the unique ID for this building type (e.g. "settlement", "city", "custom_windmill")
     */
    Identifier getId();
}
