package io.github.hato1883.api.events;


/**
 * Priority groups for event listeners.
 * Defines relative ordering of event handler execution.
 */
public enum EventPriority {
    /**
     * Highest priority listeners run first.
     * Use for critical core logic or mods that need to override everything else.
     * Examples: core game engine rules, anti-cheat checks, early cancels.
     */
    HIGHEST,

    /**
     * High priority listeners run after HIGHEST.
     * Use for important mods that want to preempt default game rules.
     * Examples: mods that change core gameplay mechanics before defaults run.
     */
    HIGH,

    /**
     * Normal priority is the default priority.
     * Most default game rules and general mods should run here.
     * Examples: vanilla game logic, standard rule enforcement.
     */
    NORMAL,

    /**
     * Low priority listeners run after NORMAL.
     * Use for mods that react after core logic has run.
     * Examples: logging, analytics, post-processing.
     */
    LOW,

    /**
     * Lowest priority listeners run last.
     * Use for fallback logic, cleanup, or mods that should always run after others.
     * Examples: final adjustments, UI updates, undo/rollback triggers.
     */
    LOWEST;
}
