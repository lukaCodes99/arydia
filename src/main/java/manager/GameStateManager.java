package manager;

import hr.tvz.arydia.server.MainController;
import hr.tvz.arydia.server.controller.BattleWorldController;
import hr.tvz.arydia.server.controller.ExplorationWorldController;
import hr.tvz.arydia.server.model.GameState;
import hr.tvz.arydia.server.thread.PlayerThread;
import hr.tvz.arydia.server.util.WorldGenerationUtil;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;

public class GameStateManager {
    private static GameStateManager instance;
    @Getter
    @Setter
    private GameState currentState;
    private final PlayerThread playerThread;
    @Setter
    private MainController mainController;
    @Setter
    private BattleWorldController battleController;
    @Setter
    private ExplorationWorldController explorationController;

    private GameStateManager(PlayerThread playerThread) {
        this.playerThread = playerThread;
    }

    public static GameStateManager getInstance(PlayerThread playerThread) {
        if (instance == null) {
            instance = new GameStateManager(playerThread);
        }
        return instance;
    }

    public void updateGameState(GameState newState) {
        // Recreate UI elements
        newState.setOpenWorld(WorldGenerationUtil.recreateTileUIAndSetPlayerText(
                newState.getOpenWorld(),
                newState.getPlayers()
        ));

        this.currentState = newState;

        // Update UI on JavaFX thread
        Platform.runLater(() -> {
            if (mainController != null) {
                mainController.refreshGrid();
            }
            if (battleController != null) {
                battleController.refreshGrid();
            }
            if (explorationController != null) {
                explorationController.refreshGrid();
            }
        });

        // Send to server
        playerThread.sendGameState(newState);
    }

}
