package hr.tvz.arydia.server.thread;

import hr.tvz.arydia.server.ClientApplication;
import hr.tvz.arydia.server.MainController;
import hr.tvz.arydia.server.manager.GameStateManager;
import hr.tvz.arydia.server.model.CharacterType;
import hr.tvz.arydia.server.model.GameState;
import hr.tvz.arydia.server.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class PlayerThread {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String gameChoice;
    private String playerName;
    @Getter
    private CharacterType whoAmI;
    private Player player;


    public PlayerThread(Socket clientSocket, String gameChoice, String playerName) {
        try {
            this.gameChoice = gameChoice;
            this.playerName = playerName;
            socket = clientSocket;
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(gameChoice + "," + playerName);
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            GameStateManager.getInstance(this);


        } catch (IOException e) {
            System.err.println("Error setting up connection: " + e.getMessage());
        }
    }
//wittcode kaže da s obzirom da je blokirajuće najbolje je da napravimo novi thread tako da taj poseban thread kad se spoji čeka na promjene
    public void connectToServer() {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    Object receivedObject = ois.readObject();
                    if (receivedObject instanceof GameState newGameState) {
                        //    ClientApplication.gameState = newGameState;
//    gameState = newGameState;
                        if (whoAmI == null) setInitialWhoAmI(newGameState.getPlayers());

                        System.out.println("whoAmI: " + whoAmI);
                        // Update GameStateManager before loading the screen
                        GameStateManager.getInstance(this).updateGameState(newGameState);
                        
                        //System.out.println("Received game state: " + gameState);
                        loadMainScreen();
                    } else {
                        System.out.println("Received unknown object: " + receivedObject);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error receiving data from server: " + e.getMessage());
            }
        }).start();
    }



    private void setInitialWhoAmI(List<Player> players) {
        System.out.println("Setting initial whoAmI");
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                System.out.println("true");
                this.whoAmI = player.getPlayerType();
                this.player = player;
                break;
            }
        }
    }

    private void loadMainScreen() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/arydia/server/hello-view.fxml"));
                Parent root = loader.load(); //  ovo uvijek treba prvo pozvati!!
                MainController controller = loader.getController();
                controller.setPlayer(player);//zbog servisa
                Stage stage = new Stage();
                stage.setTitle("Arydia - "  + whoAmI + ": " + playerName);
                stage.setScene(new Scene(root, 800, 800));
                stage.show();
                ClientApplication.closeWelcomeScreen();
            } catch (IOException e) {
                System.err.println("Error loading main screen: " + e.getMessage());
            }
        });
    }

    public void sendGameState(GameState gameState) {
        //System.out.println("Sending game state: " + gameState);
        try {
            if (socket != null && !socket.isClosed() && oos != null) {
                oos.writeObject(gameState);
                oos.flush();
            }
        } catch (IOException e) {
            System.err.println("Error sending game state: " + e.getMessage());
        }
    }
}