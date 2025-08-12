package io.github.hato1883.ui.tui;

import io.github.hato1883.api.game.board.IBoard;
import io.github.hato1883.game.board.Map;
import io.github.hato1883.game.board.types.StandardBoard;

public class TUI {

	// The window handle
	private long window;
	private IBoard gameBoard;

	public void run() {
        this.gameBoard = new StandardBoard();

		init();
		loop();
	}

	private void init() {
	}

	private void loop() {

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !shouldClose(gameBoard) ) {
            System.out.println(gameBoard);
		}
	}

	private boolean shouldClose(Map gameBoard) {
        return false;
	}
}

