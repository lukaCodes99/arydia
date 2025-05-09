package hr.tvz.arydia.server.controller;


import hr.tvz.arydia.server.ClientApplication;
import hr.tvz.arydia.server.model.CharacterType;
import hr.tvz.arydia.server.model.Location;
import hr.tvz.arydia.server.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private TextField playerNameField;

    //private final Player player = new Player();

    //ovo nije dobro, treba dodati taj file za citanje i pisanje
    @FXML
    private void startGame(ActionEvent event) throws IOException {
        String playerName = playerNameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Player";
        }
        Player player = new Player(playerName, CharacterType.PLAYER_ONE, new Location(0,0), null, 2);

        if (ClientApplication.gameState.getPlayers().isEmpty() ) {
            ClientApplication.gameState.getPlayers().add(player);
        } else if (ClientApplication.gameState.getPlayers().size() == 1) {
            player.setPlayerType(CharacterType.PLAYER_TWO);
            ClientApplication.gameState.getPlayers().add(player);
        }else throw new RuntimeException("Maximum number of players reached!");


        //FXMLLoader loader = new FXMLLoader(ArydiaApplication.class.getResource("main-screen.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/tvz/arydia/server/hello-view.fxml"));


        Parent root = loader.load();

        //MainController controller = loader.getController();

        Scene currentScene = playerNameField.getScene(); //ako ne idem ovako mora se nekak koristit event..
        Stage stage = (Stage) currentScene.getWindow();

        Scene mainScene = new Scene(root, 800, 800);
        stage.setTitle("Open World - " + playerName);
        stage.setScene(mainScene);
        stage.show();
    }

}
