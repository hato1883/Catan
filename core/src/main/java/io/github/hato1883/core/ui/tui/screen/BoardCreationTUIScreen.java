package io.github.hato1883.core.ui.tui.screen;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.api.world.board.IBoardGenerator;
import io.github.hato1883.api.services.IServiceLocator;
import io.github.hato1883.core.ui.tui.TUIInput;

public class BoardCreationTUIScreen extends BaseTUIScreen {
    private volatile boolean finished = false;
    private IBoard board;
    private String selectedOption;
    private final IServiceLocator serviceLocator;

    public BoardCreationTUIScreen(IServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void onShow() {
        board = null;
        System.out.println("=== Board Creation ===");
        System.out.println("1) Classic Hexagon");
        System.out.println("2) Donut shaped map");
    }

    @Override
    public void render() {
        if (!finished) {
            String input;
            while ((input = TUIInput.pollInput()) != null) { // Non-blocking read
                IBoardGenerator boardGenerator;
                switch (input) {
                    case "1":
                        selectedOption = "basemod:classic_hex";
                        boardGenerator = serviceLocator.get(IBoardGenerator.class).orElseThrow(() -> new IllegalStateException("Required service not found: IBoardGenerator"));
                        board = boardGenerator.generateBoard(Identifier.of(selectedOption), null); // your board creation logic
                        finished = true;
                        break;
                    case "2":
                        selectedOption = "basemod:donut";
                        boardGenerator = serviceLocator.get(IBoardGenerator.class).orElseThrow(() -> new IllegalStateException("Required service not found: IBoardGenerator"));
                        board = boardGenerator.generateBoard(Identifier.of(selectedOption), null);
                        finished = true;
                        break;
                    default:
                        System.out.println("Invalid option. Try again:");
                }
            }
        }
    }

    @Override
    public void onHide() {
        System.out.println("Board creation complete. Ready to start the game!");
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public IBoard getBoard() {
        return board;
    }
}
