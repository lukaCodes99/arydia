package hr.tvz.arydia.server;

import hr.tvz.arydia.server.model.OpenWorld;
import hr.tvz.arydia.server.thread.PlayerOneServerThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import static hr.tvz.arydia.server.model.World.WORLD_SIZE;

public class MainController {
    @FXML
    private GridPane gameGrid;

    public void initialize() {
        gameGrid.setPadding(new Insets(10));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);

        PlayerOneServerThread.connectToServer();

//        Platform.runLater(() -> {
//
//        });
        //OpenWorld openWorld = ClientApplication.gameState.getOpenWorld();
        Platform.runLater(() -> {
            OpenWorld openWorld = PlayerOneServerThread.openWorld;
            for (int i = 0; i < WORLD_SIZE; i++) {
                for (int j = 0; j < WORLD_SIZE; j++) {

                    gameGrid.add(openWorld.getTile(i,j).getContainer(), i, j);
                }
            }
            ClientApplication.gameState.getPlayers().forEach(System.out::println);
        });
    }

}