package hr.tvz.arydia.server.util;

import hr.tvz.arydia.server.model.GameState;

import java.io.*;

public class GameMoveUtils {
    private static final String GAME_MOVE_HISTORY_FILE_NAME = "gameState/gameState.dat";

    public static GameState getLastGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(GAME_MOVE_HISTORY_FILE_NAME))) {
            return (GameState) ois.readObject();
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