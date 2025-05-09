package hr.tvz.arydia.server.thread;

import hr.tvz.arydia.server.model.GameState;

public class GetLastGameStateThread extends GameMoveThread implements Runnable {

    private GameState lastGameState;

    public GetLastGameStateThread() {
        super();
    }

    @Override
    public void run() {
        lastGameState = getLastGameStateFromFile();
        if (lastGameState != null) {
            System.out.println("Last game state retrieved successfully.");
            // Process the last game state as needed
        } else {
            System.out.println("Failed to retrieve the last game state.");
        }
    }

    public synchronized GameState getLastGameState() {
        return this.lastGameState;
    }
}
