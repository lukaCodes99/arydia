package hr.tvz.arydia.server.util;

import javafx.scene.control.Alert;

public class DialogUtils {

    public static void invalidMoveAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid move");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
