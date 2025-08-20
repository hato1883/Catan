package io.github.hato1883.core.ui.tui.screen;

import io.github.hato1883.api.world.board.IBoard;

public class GamePlayTUIScreen  extends BaseTUIScreen {

    private volatile boolean finished = false;
    private IBoard board;

    public GamePlayTUIScreen(IBoard board) {
        this.board = board;
    }

    @Override
    public void onShow() {
        System.out.println("=== Game Screen ===");
    }

    @Override
    public void render() {
        System.out.println(board);
        finished = true;
        // Nothing else to do; setup already finished in show()
    }

    @Override
    public void onHide() {
        System.out.println("Game is finished!");
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
