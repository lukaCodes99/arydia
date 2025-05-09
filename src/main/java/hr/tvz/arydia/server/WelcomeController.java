package hr.tvz.arydia.server;

import hr.tvz.arydia.server.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private TextField playerNameField;

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        String playerName = playerNameField.getText();
        if (playerName != null && !playerName.isEmpty()) {
            ClientApplication.startGame("new", playerName);
        } else {
            System.out.println("Please enter a valid name.");
        }
    }
}
