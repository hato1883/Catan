package io.github.hato1883.api.world.board;

public interface IProductionBoard extends IBoard {
    /**
     * Trigger resource production for a given dice roll (or other production event).
     * @param rolledNumber the number/event that triggers production
     */
    void triggerProductionForRoll(int rolledNumber);
}

