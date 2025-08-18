package io.github.hato1883.core.ui.tui.screen;

import io.github.hato1883.core.ui.tui.TUIInput;

public class MainMenuTUIScreen  extends BaseTUIScreen {
    private volatile boolean finished = false;
    private String selectedOption;

    @Override
    public void onShow() {
        System.out.println("=== Main Menu ===");
        System.out.println("1) Start Game");
        System.out.println("2) Load Game");
        System.out.println("3) Exit");
        System.out.println("Type your option and press Enter:");
    }

    @Override
    public void render() {
        if (!finished) {
            String input;
            // System.out.println("inputQueue.poll():" + inputQueue.poll());
            // Non-blocking read
            while ((input = TUIInput.pollInput()) != null) {
                switch (input) {
                    case "1":
                        selectedOption = "StartGame";
                        finished = true;
                        break;
                    case "2":
                        selectedOption = "LoadGame";
                        finished = true;
                        break;
                    case "3":
                        selectedOption = "Exit";
                        finished = true;
                        break;
                    default:
                        System.out.println("Invalid option. Try again:");
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public String getSelectedOption() {
        return selectedOption;
    }
}
