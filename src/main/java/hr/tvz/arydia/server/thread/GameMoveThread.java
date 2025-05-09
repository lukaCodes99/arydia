package hr.tvz.arydia.server.thread;


import hr.tvz.arydia.server.model.GameState;
import hr.tvz.arydia.server.util.GameMoveUtils;

public abstract class GameMoveThread {

    private static Boolean gameMoveFileAccessInProgress = false;

    protected synchronized void saveNewStateMoveToFile(GameState gameState) {
        while(gameMoveFileAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFileAccessInProgress = true;

        GameMoveUtils.saveGameState(gameState);

        gameMoveFileAccessInProgress = false;

        notifyAll();
    }

    protected synchronized GameState getLastGameStateFromFile() {
        while(gameMoveFileAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFileAccessInProgress = true;

        GameState lastGameMove = GameMoveUtils.getLastGameState();

        gameMoveFileAccessInProgress = false;

        notifyAll();

        return lastGameMove;
    }

}
