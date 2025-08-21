package io.github.hato1883.api.world.board;

import java.util.Collection;

/**
 * Represents a tile on the board. Holds its position and type.
 */
public interface ITile {
    /**
     * Returns the position of this tile.
     */
    ITilePosition getPosition();

    /**
     * Returns the type of this tile.
     */
    ITileType getType();

    /**
     * Returns the collection of production numbers that activate this tile.
     * For example, in Catan, these are the dice numbers that produce resources.
     */
    Collection<Integer> getProductionNumbers();

    /**
     * Default implementation of ITile, grouping position, type, and production numbers.
     */
    class DefaultTile implements ITile {
        private final ITilePosition position;
        private final ITileType type;
        private final Collection<Integer> productionNumbers;

        public DefaultTile(ITileType type, ITilePosition position, Collection<Integer> productionNumbers) {
            this.position = position;
            this.type = type;
            this.productionNumbers = productionNumbers;
        }

        @Override
        public ITilePosition getPosition() {
            return position;
        }

        @Override
        public ITileType getType() {
            return type;
        }

        @Override
        public Collection<Integer> getProductionNumbers() {
            return productionNumbers;
        }
    }
}
