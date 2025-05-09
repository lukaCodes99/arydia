package hr.tvz.arydia.server.server;

import hr.tvz.arydia.server.model.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class GameServerK {

    public static final int PORT = 1989;
    private static GameState gameState;


    private static void acceptRequests() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            do {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                // outer try catch blocks cannot handle the anonymous implementations
                //new Thread(() ->  processPrimitiveClient(clientSocket)).start();
                new Thread(() -> processSerializableClient(clientSocket)).start();
            } while (!LocalDateTime.now().isAfter(LocalDateTime.now().plusMinutes(300000)));
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());){

            oos.writeObject(gameState);
            oos.flush();

            GameState receivedGameState = (GameState) ois.readObject();
            if (receivedGameState != null) {
                gameState = receivedGameState;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void startServer(GameState newGameState) {
        gameState = newGameState;
        acceptRequests();
    }
}
