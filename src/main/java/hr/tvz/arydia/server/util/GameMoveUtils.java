package hr.tvz.arydia.server.util;

import hr.tvz.arydia.server.model.GameState;

import java.io.*;

public class GameMoveUtils {
    private static final String GAME_MOVE_HISTORY_FILE_NAME = "gameState/gameState.dat";

    public static GameState getLastGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(GAME_MOVE_HISTORY_FILE_NAME))) {
            System.out.println(new File(GAME_MOVE_HISTORY_FILE_NAME).exists());
            //System.out.println("Reading game state from file:" + ois.readObject());
            // Read the object once and store it in a variable
            GameState loadedState = (GameState) ois.readObject();

            // Print it for debugging
            System.out.println("Reading game state from file:" + loadedState);

            // Return the already read object
            return loadedState;

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading game state: " + e.getMessage(), e);
        }
    }

    public static void saveGameState(GameState gameState) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(GAME_MOVE_HISTORY_FILE_NAME))) {
            oos.writeObject(gameState);
        } catch (IOException e) {
            throw new RuntimeException("Error saving game state: " + e.getMessage(), e);
        }
    }
}