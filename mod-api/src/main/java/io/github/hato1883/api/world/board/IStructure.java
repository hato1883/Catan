package io.github.hato1883.api.world.board;

import io.github.hato1883.api.entities.player.IPlayer;
import io.github.hato1883.api.entities.resource.IResourceType;

import java.util.Map;

public interface IStructure {
    /**
     * @return the player who owns this building
     */
    IPlayer getOwner();

    /**
     * @return The number of each resource it cost to build this structure
     */
    Map<IResourceType, Integer> getBuildCost();

    /**
     * Called when a connected tile has produced a resource
     * @param offer the produced resources and their origin
     */
    void receiveProductionOffer(ProductionOffer offer);
}
