package hr.tvz.arydia.server.util;


import hr.tvz.arydia.server.model.Player;
import hr.tvz.arydia.server.model.World;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WorldHandlerUtil {

    public static void handleWorldClick(World world, String worldType, Player player) {
        if (world != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                String fxmlFile = "";

                if ("BATTLE".equals(worldType)) {
                    fxmlFile = "/hr/tvz/arydiag/battle-world-controller.fxml";
                } else if ("EXPLORATION".equals(worldType)) {
                    fxmlFile = "/hr/tvz/arydiag/exploration-world-controller.fxml";
                }

                loader.setLocation(WorldHandlerUtil.class.getResource(fxmlFile));
                Scene scene = new Scene(loader.load());

                if ("BATTLE".equals(worldType)) {
                    //BattleWorldController controller = loader.getController();
                    //controller.setWorld(world, player);
                } else if ("EXPLORATION".equals(worldType)) {
                    //ExplorationWorldController controller = loader.getController();
                    //controller.setWorld(world, player);
                }

                Stage stage = new Stage();
                stage.setScene(scene);
                //stage.setTitle(worldType + " WORLD - " + player.getName());
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

