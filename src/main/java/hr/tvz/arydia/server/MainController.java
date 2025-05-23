package hr.tvz.arydia.server;

import exception.InvalidMoveException;
import hr.tvz.arydia.server.model.*;
import hr.tvz.arydia.server.service.MovementService;
import hr.tvz.arydia.server.util.DialogUtils;
import hr.tvz.arydia.server.util.WorldGenerationUtil;
import hr.tvz.arydia.server.util.WorldHandlerUtil;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import manager.GameStateManager;

import static hr.tvz.arydia.server.model.World.WORLD_SIZE;

public class MainController {
    @FXML
    private GridPane gameGrid;
    private OpenWorld openWorld;
    private MovementService movementService;
    private Player player;
    private CharacterType playerTurn;


    public void initialize() {
        gameGrid.setPadding(new Insets(10));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);


        Platform.runLater(() -> {
            if (ClientApplication.gameState.getOpenWorld() != null) {
                OpenWorld newOpenWorld = ClientApplication.gameState.getOpenWorld();
                playerTurn = ClientApplication.gameState.getPlayerTurn();
                newOpenWorld = WorldGenerationUtil.recreateTileUIAndSetPlayerText(newOpenWorld, ClientApplication.gameState.getPlayers());
                ClientApplication.gameState.setOpenWorld(newOpenWorld);
                this.openWorld = newOpenWorld;
                for (int i = 0; i < WORLD_SIZE; i++) {
                    for (int j = 0; j < WORLD_SIZE; j++) {
                        gameGrid.add(openWorld.getTile(i, j).getContainer(), i, j);
                        if (openWorld.getTile(i, j).isActive())
                            openWorld.getTile(i, j).getRect().setOnMouseClicked(createTileHandler(i, j));
                    }
                }
                ClientApplication.gameState.getPlayers().forEach(System.out::println);
            } else {
                System.out.println("Open world is null.");
            }
        });
        
    }

    private EventHandler<? super MouseEvent> createTileHandler(int i, int j) {
        return event -> {
            if (player.getPlayerType() == playerTurn) {
                tileHandler(i, j);
                movementService.moveToTileOpenWorld(openWorld.getTiles(), i, j);
            } else {
                DialogUtils.notYourTurn("NIJE TVOJ RED!!!");
            }
        };
    }

    //TODO U SPECIAL WORLDOVIMA MI NE RADI VALIDACIJA kretanja, IZGLEDA DA SE OPET POJAVIO PROBLEM JER SAM PROSLIJEDIO PLAYERA ALI NE BI TO SMIO BITI PROBLEM!!!!!!!!!!!!!
    private void tileHandler(int i, int j) {
        System.out.println("Tile clicked: " + i + ", " + j);
        //System.out.println("Player position: " + player.getX() + ", " + player.getY());
        if (checkMoveInvalidOpenWorld(i,j,1)) { //mora apsoluton jer nekad ide u rikverc
            DialogUtils.invalidMoveAlert("Invalid move, you can only move to adjacent tiles");
            throw new InvalidMoveException("Invalid move");
        }


        //TileType tileType = worlds.get(i + "," + j).getTiles()[0][0].getTileType();
        Tile tile = openWorld.getTile(i, j);
        SpecialWorld specialWorld = openWorld.getSpecialWorld(i,j);
        if (tile.getTileType().equals(TileType.BATTLE)) {
            WorldHandlerUtil.handleWorldClick(specialWorld, TileType.BATTLE.name(), player);
        } else if (tile.getTileType().equals(TileType.EXPLORATION)) {
            WorldHandlerUtil.handleWorldClick(specialWorld, TileType.EXPLORATION.name(), player);
        } else if (tile.getTileType().equals(TileType.OPEN_WORLD)) {
            tile.setTileType(specialWorld.getTiles()[0][0].getTileType()); //dobar side-effect moram double click da se otvori novi svijet
        }

    }

    //ovo ce se pozvat prije svega
    public void setPlayer(Player player) {
        this.player = player;
        movementService = new MovementService(player);
        //initializeOpenWorld();
    }

    private boolean checkMoveInvalidOpenWorld(int i, int j, int moveAbilityLevel) {

        return (Math.abs(i - player.getOpenWorldI()) > moveAbilityLevel
                || (Math.abs(j - player.getOpenWorldJ()) > moveAbilityLevel));
    }
}