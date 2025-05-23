package hr.tvz.arydia.server.controller;


import hr.tvz.arydia.server.model.Location;
import hr.tvz.arydia.server.service.MovementService;
import hr.tvz.arydia.server.model.Player;
import hr.tvz.arydia.server.model.Tile;
import hr.tvz.arydia.server.model.World;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class BattleWorldController {

    @FXML
    private GridPane gameGrid;

    private World world;

    private Player player;

    private MovementService moveService;

    public void setWorld(World world, Player player) {//ne prosljeđujem igrača jer se kosi logika za invalid move
        this.world = world;

//        this.player = new Player();
//        this.player.setName(player.getName());
//        this.player.setMoveAbilityLevel(player.getMoveAbilityLevel());
//        Location location = new Location(0,0);
//        this.player.setSpecialWorldLocation(location);
        this.player = player;

        this.moveService = new MovementService(this.player);
        initializeBattleWorld();
    }

    //ovo je prije pozvano tak ta moja inicijalizacija ide poslje kroz setter, možda može kroz konstruktor, nez
    public void initialize() {
        gameGrid.setPadding(new Insets(10));
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);

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
        return event -> moveService.moveToTileSpecialWorld(world.getTiles(), i, j);
    }
}
