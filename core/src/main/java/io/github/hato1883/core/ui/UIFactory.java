package io.github.hato1883.core.ui;

import io.github.hato1883.api.ui.IUI;
import io.github.hato1883.core.ui.gui.GameGUI;
import io.github.hato1883.core.ui.tui.GameTUI;

public class UIFactory {

    public static IUI createUI(boolean useGui) {
        if (useGui) {
            return new GameGUI();  // implements IUI
        } else {
            return new GameTUI();  // implements IUI
        }
    }
}
