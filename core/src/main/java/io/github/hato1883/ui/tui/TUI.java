package io.github.hato1883.ui.tui;

import io.github.hato1883.game.board.Board;
import io.github.hato1883.game.board.types.HexIslandBoard;

public class TUI {

	// The window handle
	private long window;
	private Board gameBoard;

	public void run() {
        this.gameBoard = new HexIslandBoard(3);

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

	private boolean shouldClose(Board gameBoard) {
        return false;
	}
}

