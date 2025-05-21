package hr.tvz.arydia.server.manager;

import hr.tvz.arydia.server.MainController;
import hr.tvz.arydia.server.controller.BattleWorldController;
import hr.tvz.arydia.server.controller.ExplorationWorldController;
import hr.tvz.arydia.server.model.*;
import hr.tvz.arydia.server.thread.PlayerThread;
import hr.tvz.arydia.server.util.WorldGenerationUtil;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class GameStateManager {
    @Getter
    private static GameStateManager instance;
    @Getter
    private GameState currentState;
    private final PlayerThread playerThread;
    @Setter
    private MainController mainController;
    
    // Maps to track controllers and their associated worlds
    private final Map<BattleWorldController, Location> battleControllers;
    private final Map<ExplorationWorldController, Location> explorationControllers;
    
    @Getter
    private CharacterType whoAmI;

    private GameStateManager(PlayerThread playerThread) {
        this.playerThread = playerThread;
        this.whoAmI = playerThread.getWhoAmI();
        this.battleControllers = new HashMap<>();
        this.explorationControllers = new HashMap<>();
    }

    public static GameStateManager getInstance(PlayerThread playerThread) {
        if (instance == null) {
            instance = new GameStateManager(playerThread);
        }
        return instance;
    }

    public void addBattleController(BattleWorldController controller, int i, int j) {
        battleControllers.put(controller, new Location(i, j));
    }

    public void addExplorationController(ExplorationWorldController controller, int i, int j) {
        explorationControllers.put(controller, new Location(i, j));
    }

    public void removeBattleController(BattleWorldController controller) {
        battleControllers.remove(controller);
    }

    public void removeExplorationController(ExplorationWorldController controller) {
        explorationControllers.remove(controller);
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
            // Update main controller
            if (mainController != null) {
                mainController.refreshGrid(newState);
            }
            
            // Update battle world controllers
            battleControllers.forEach((controller, location) -> {
                SpecialWorld world = newState.getOpenWorld().getSpecialWorld(location.getI(), location.getJ());
                if (world.getTile(0,0).getTileType() == TileType.BATTLE) {
                    controller.refreshGrid(world);
                }
            });
            
            // Update exploration world controllers
            explorationControllers.forEach((controller, location) -> {
                SpecialWorld world = newState.getOpenWorld().getSpecialWorld(location.getI(), location.getJ());
                if (world.getTile(0,0).getTileType() == TileType.EXPLORATION) {
                    controller.refreshGrid(world);
                }
            });
        });
        playerThread.sendGameState(currentState);

    }

    public void updateGameStateAfterMove(Player movedPlayer) {
        // Update the player in the current state
        for (Player p : currentState.getPlayers()) {
            if (p.getPlayerType() == movedPlayer.getPlayerType()) {
                // Update player's locations
                p.setOpenWorldLocation(movedPlayer.getOpenWorldLocation());
                p.setSpecialWorldLocation(movedPlayer.getSpecialWorldLocation());
                break;
            }
        }

        // Change turn
        //currentState.changePlayerTurn();

        // This will trigger UI updates and send state to server
        updateGameState(currentState);
    }


    public boolean isPlayerTurn() {
        System.out.println(whoAmI);
        System.out.println(currentState.getPlayerTurn());
        return playerThread.getWhoAmI() == currentState.getPlayerTurn();
    }
}