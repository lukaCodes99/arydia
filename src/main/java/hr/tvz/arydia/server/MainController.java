package hr.tvz.arydia.server;

import hr.tvz.arydia.server.model.OpenWorld;
import hr.tvz.arydia.server.thread.PlayerOneServerThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import static hr.tvz.arydia.server.model.World.WORLD_SIZE;

public class MainController {
    @FXML
    private GridPane gameGrid;

    public void initialize() {
        gameGrid.setPadding(new Insets(10));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);

        //PlayerOneServerThread.connectToServer();

//        Platform.runLater(() -> {
//
//        });
//        Platform.runLater(() -> {
//            OpenWorld openWorld = ClientApplication.gameState.getOpenWorld();
//            //OpenWorld openWorld = PlayerOneServerThread.openWorld;
//            for (int i = 0; i < WORLD_SIZE; i++) {
//                for (int j = 0; j < WORLD_SIZE; j++) {
//
//                    gameGrid.add(openWorld.getTile(i,j).getContainer(), i, j);
//                }
//            }
//            ClientApplication.gameState.getPlayers().forEach(System.out::println);
//        });
        if (ClientApplication.gameState != null) {
            Platform.runLater(() -> {
                if (ClientApplication.gameState.getOpenWorld() != null) {
                    OpenWorld openWorld = ClientApplication.gameState.getOpenWorld();
                    for (int i = 0; i < WORLD_SIZE; i++) {
                        for (int j = 0; j < WORLD_SIZE; j++) {
                            Node container = openWorld.getTile(i, j).getContainer();
                            if (container != null) {
                                gameGrid.add(container, i, j);
                            } else {
                                System.out.println("Container is null at: i=" + i + ", j=" + j);
                            }
                        }
                    }
                    ClientApplication.gameState.getPlayers().forEach(System.out::println);
                } else {
                    System.out.println("Open world is null.");
                }
            });
        } else {
            System.out.println("Game state is null.");
        }
    }

}