package hr.tvz.arydia.server.thread;

import hr.tvz.arydia.server.model.GameState;

public class SaveNewGameStateThread extends GameMoveThread implements Runnable {

    private final GameState gameState;

    public SaveNewGameStateThread(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {
        saveNewStateMoveToFile(gameState);
    }
}
