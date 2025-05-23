package hr.tvz.arydia.server;

import hr.tvz.arydia.server.exception.InvalidMoveException;
import hr.tvz.arydia.server.manager.GameStateManager;
import hr.tvz.arydia.server.model.*;
import hr.tvz.arydia.server.service.MovementService;
import hr.tvz.arydia.server.util.DialogUtils;
import hr.tvz.arydia.server.util.WorldHandlerUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import static hr.tvz.arydia.server.model.World.WORLD_SIZE;

public class MainController {
    @FXML
    private GridPane gameGrid;
    private OpenWorld openWorld;
    private MovementService movementService;
    private Player player;
    private GameStateManager gameStateManager;

    @FXML
    public void initialize() {
        gameGrid.setPadding(new Insets(10));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.movementService = new MovementService(player);
        this.gameStateManager = GameStateManager.getInstance();
        this.gameStateManager.setMainController(this);
        
        // Initial setup with current game state
        if (gameStateManager.getCurrentState() != null) {
            refreshGrid(gameStateManager.getCurrentState());
        }
    }

    public void refreshGrid(GameState newState) {
        gameGrid.getChildren().clear();
        this.openWorld = newState.getOpenWorld();

        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                Tile tile = openWorld.getTile(i, j);
                gameGrid.add(tile.getContainer(), i, j);
                
                if (tile.isActive()) {
                    final int finalI = i;
                    final int finalJ = j;
                    tile.getRect().setOnMouseClicked(event -> handleTileClick(finalI, finalJ));
                }
            }
        }
    }

    private void handleTileClick(int i, int j) {
        if (!gameStateManager.isPlayerTurn()) {
            DialogUtils.notYourTurn("NIJE TVOJ RED!!!");
            return;
        }

        try {
//            if (checkMoveInvalidOpenWorld(i, j, 1)) {
//                DialogUtils.invalidMoveAlert("Invalid move, you can only move to adjacent tiles");
//                throw new InvalidMoveException("Invalid move");
//            }

            tileHandler(i, j);
            movementService.moveToTileOpenWorld(openWorld.getTiles(), i, j);
            GameState currentState = gameStateManager.getCurrentState();
            currentState.setOpenWorld(openWorld);
            updatePlayerAfterMove(currentState);

            // Update game state after successful move
            //GameState currentState = gameStateManager.getCurrentState();
            //currentState.changePlayerTurn();

            gameStateManager.updateGameState(currentState);
        } catch (InvalidMoveException e) {
            // Exception already handled by dialog
        }
    }

    public void updatePlayerAfterMove(GameState currentState) {
        System.out.println("Players before update: " + currentState.getPlayers().size());
        for (Player p : currentState.getPlayers()) {
            if (p.getPlayerType() == player.getPlayerType()) {
                // Update player's locations
                p.setOpenWorldLocation(player.getOpenWorldLocation());
                p.setSpecialWorldLocation(player.getSpecialWorldLocation());
            }
        }
        System.out.println("Players after update: " + currentState.getPlayers().size());
        for (Player p : currentState.getPlayers()) {
            System.out.println("Player in list: " + p.getPlayerType());
        }
    }

    private void tileHandler(int i, int j) {
        Tile tile = openWorld.getTile(i, j);
        SpecialWorld specialWorld = openWorld.getSpecialWorld(i, j);

        switch (tile.getTileType()) {
            case BATTLE -> WorldHandlerUtil.handleWorldClick(specialWorld, TileType.BATTLE.name(), player, i, j);
            case EXPLORATION -> WorldHandlerUtil.handleWorldClick(specialWorld, TileType.EXPLORATION.name(), player, i, j);
            case OPEN_WORLD -> tile.setTileType(specialWorld.getTiles()[0][0].getTileType());
        }
    }

//    private boolean checkMoveInvalidOpenWorld(int i, int j, int moveAbilityLevel) {
//        return (Math.abs(i - player.getOpenWorldI()) > moveAbilityLevel
//                || (Math.abs(j - player.getOpenWorldJ()) > moveAbilityLevel));
//    }
}