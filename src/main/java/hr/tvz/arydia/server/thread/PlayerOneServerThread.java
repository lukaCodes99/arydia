package hr.tvz.arydia.server.thread;

import hr.tvz.arydia.server.model.OpenWorld;
import hr.tvz.arydia.server.model.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerOneServerThread implements Runnable {
    public static final String HOST = "localhost";
    public static final int PORT = 1989;
    public static OpenWorld openWorld;
    private static Socket socket;


    public static void connectToServer() {
        new Thread(() -> {
            try {
                // Connect to the server
                socket = new Socket(HOST, PORT);
                System.out.println("Connected to server on port: " + PORT);

                // Set up object streams
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                // Receive the game state from server
                Object receivedObject = ois.readObject();
                if (receivedObject instanceof GameState) {
                    GameState gameState = (GameState) receivedObject;
                    openWorld = gameState.getOpenWorld();
                    System.out.println("Received world from server");
                }

                // Keep connection open for future updates
                // You might want to add a separate thread to handle ongoing communication

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error connecting to server: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // Method to send updated game state back to server if needed
    public static void sendGameState(GameState gameState) {
        try {
            if (socket != null && !socket.isClosed()) {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(gameState);
                oos.flush();
            }
        } catch (IOException e) {
            System.err.println("Error sending game state: " + e.getMessage());
        }
    }

    @Override
    public void run() {

    }
}