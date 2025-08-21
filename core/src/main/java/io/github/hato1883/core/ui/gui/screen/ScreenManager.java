package io.github.hato1883.core.ui.gui.screen;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.ui.screen.IScreenManager;
import io.github.hato1883.api.ui.screen.ScreenRegistry;

import java.util.Stack;

public class ScreenManager implements IScreenManager {
    private final ScreenRegistry screenRegistry;
    private final Stack<Identifier> screenStack = new Stack<>();
    private Identifier defaultScreenId;

    public ScreenManager(ScreenRegistry screenRegistry) {
        this.screenRegistry = screenRegistry;
    }

    @Override
    public boolean setScreen(Identifier id) {
        if (screenRegistry.getScreen(id) != null) {
            if (!screenStack.isEmpty()) screenStack.pop();
            screenStack.push(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean pushScreen(Identifier id) {
        if (screenRegistry.getScreen(id) != null) {
            screenStack.push(id);
            return true;
        }
        return false;
    }

    @Override
    public void popScreen() {
        if (!screenStack.isEmpty()) screenStack.pop();
        if (screenStack.isEmpty() && defaultScreenId != null) {
            screenStack.push(defaultScreenId);
        }
    }

    @Override
    public Identifier getCurrentScreenId() {
        return screenStack.isEmpty() ? null : screenStack.peek();
    }

    @Override
    public Identifier getDefaultScreenId() {
        return defaultScreenId;
    }

    @Override
    public void setDefaultScreen(Identifier id) {
        this.defaultScreenId = id;
        if (screenStack.isEmpty()) {
            screenStack.push(id);
        }
    }

    public void showDefaultScreen() {
        if (defaultScreenId != null) {
            setScreen(defaultScreenId);
        }
    }
}
