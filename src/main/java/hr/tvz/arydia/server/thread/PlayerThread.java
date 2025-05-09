package hr.tvz.arydia.server.thread;

import hr.tvz.arydia.server.model.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerThread {

    public static final String HOST = "localhost";
    public static final int PORT = 1989;
    private GameState gameState;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    public PlayerThread(Socket clientSocket) {
        try {
            socket = clientSocket;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error setting up connection: " + e.getMessage());
        }
    }
//wittcode kaže da s obzirom da je blokirajuće najbolje je da napravimo novi thread tako da taj poseban thread kad se spoji čeka na promjene
    public void connectToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {
                        Object receivedObject = ois.readObject();
                        if (receivedObject instanceof GameState newGameState) {
                            gameState = newGameState;
                            System.out.println("Received game state: " + gameState);
                        } else {
                            System.out.println("Received unknown object: " + receivedObject);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error receiving data from server: " + e.getMessage());
                }
            }
        }).start();
    }

    public void sendGameState(GameState gameState) {
        try {
            if (socket != null && !socket.isClosed()) {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(gameState);
                oos.flush();
            }
        } catch (IOException e) {
            System.err.println("Error sending game state: " + e.getMessage());
        }
    }
}
