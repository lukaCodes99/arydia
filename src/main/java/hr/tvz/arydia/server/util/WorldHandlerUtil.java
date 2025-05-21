package hr.tvz.arydia.server.util;

import hr.tvz.arydia.server.controller.BattleWorldController;
import hr.tvz.arydia.server.controller.ExplorationWorldController;
import hr.tvz.arydia.server.model.Player;
import hr.tvz.arydia.server.model.SpecialWorld;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WorldHandlerUtil {
    public static void handleWorldClick(SpecialWorld world, String worldType, Player player, int i, int j) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                String fxmlPath = "/hr/tvz/arydia/server/" + 
                    (worldType.equals("BATTLE") ? "battle" : "exploration") + 
                    "-world-controller.fxml";
                loader.setLocation(WorldHandlerUtil.class.getResource(fxmlPath));
                Parent root = loader.load();
                Stage stage = new Stage();
                
                // Get controller and set the world
                if (worldType.equals("BATTLE")) {
                    BattleWorldController controller = loader.getController();
                    controller.setWorld(world, player, i, j);
                    stage.setOnCloseRequest(e -> controller.cleanup());
                } else {
                    ExplorationWorldController controller = loader.getController();
                    controller.setWorld(world, player, i, j);
                    stage.setOnCloseRequest(e -> controller.cleanup());
                }
                
                stage.setTitle(worldType + " World");
                stage.setScene(new Scene(root, 800, 800));
                stage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}