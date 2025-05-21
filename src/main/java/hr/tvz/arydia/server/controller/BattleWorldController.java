package hr.tvz.arydia.server.controller;


import hr.tvz.arydia.server.manager.GameStateManager;
import hr.tvz.arydia.server.model.*;
import hr.tvz.arydia.server.service.MovementService;
import hr.tvz.arydia.server.util.DialogUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import static hr.tvz.arydia.server.model.World.WORLD_SIZE;

public class BattleWorldController {
    @FXML
    private GridPane gameGrid;
    private World world;
    private Player player;
    private MovementService moveService;
    private GameStateManager gameStateManager;
    private Location openWorldLocation; // Add this field

    public void setWorld(World world, Player player, int openWorldI, int openWorldJ) {
        this.world = world;
        this.player = player;
        this.openWorldLocation = new Location(openWorldI, openWorldJ);
        this.moveService = new MovementService(this.player);
        
        // Now we can properly register with GameStateManager
        gameStateManager.addBattleController(this, openWorldI, openWorldJ);
        
        initializeBattleWorld();
    }

    @FXML
    public void initialize() {
        gameGrid.setPadding(new Insets(10));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);
        gameStateManager = GameStateManager.getInstance();
        // Remove the addBattleController call from here as we'll do it in setWorld
    }

    private void initializeBattleWorld() {
        if (world == null) {
            throw new IllegalStateException("World is not set!");
        }
        Tile[][] tiles = world.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if(i == 0 && j == 0)
                    tiles[i][j].setText(player.getName());

                gameGrid.add(tiles[i][j].getContainer(), i, j);
                tiles[i][j].getRect().setOnMouseClicked(createTileClickHandler(i, j));
            }
        }
    }

    private EventHandler<? super MouseEvent> createTileClickHandler(int i, int j) {
        return event -> {
            if (!gameStateManager.isPlayerTurn()) {
                DialogUtils.notYourTurn("NIJE TVOJ RED!!!");
                return;
            }
            moveService.moveToTileSpecialWorld(world.getTiles(), i, j);
        };
    }


    public void refreshGrid(World updatedWorld) {
        if (world == null || world.equals(updatedWorld)) {
            return;
        }

        gameGrid.getChildren().clear();
        Tile[][] tiles = updatedWorld.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                gameGrid.add(tiles[i][j].getContainer(), i, j);

                if (tiles[i][j].isActive()) {
                    tiles[i][j].getRect().setOnMouseClicked(createTileClickHandler(i, j));
                }
            }
        }
    }

    public void cleanup() {
        gameStateManager.removeBattleController(this);
    }
}