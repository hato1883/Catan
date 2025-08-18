package io.github.hato1883.api.game.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.game.IResourceType;

import java.util.Optional;

public interface IPortType extends IStructureType {
    /**
     * @return ID of the port type, e.g. "generic", "ore_2to1"
     */
    Identifier getId();

    /**
     * Whether this port gives bonus trades for a specific resource.
     * e.g. 2:1 ore port
     */
    Optional<IResourceType> getSpecializedResource();

    /**
     * How many units a player must give up to get 1 unit of another resource.
     * E.g., 3:1 = 3
     */
    int getGiveAmount();

    /**
     * Always returns 1, unless modded.
     */
    default int getReceiveAmount() {
        return 1;
    }
}
