package io.github.hato1883.api.game.board;

import io.github.hato1883.api.game.IResourceType;

import java.util.Map;

public interface IBuildingType extends IStructureType {

    /**
     * @return a map of resource types to their production multiplier (e.g. 1 for basic, 2 for city)
     */
    Map<IResourceType, Integer> getProductionValues();

    /**
     * Optional: limits what tiles this building can be placed next to (for future rules)
     */
    default boolean isAllowedNearResource(IResourceType resource) {
        return true;
    }

    /**
     * @return whether this building type allows port usage, modifiable by rulesets
     */
    default boolean allowsPortAccess() {
        return true;
    }
}
