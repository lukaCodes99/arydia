package hr.tvz.arydia.server.util;

import hr.tvz.arydia.server.model.*;

import java.util.List;
import java.util.Random;

import static hr.tvz.arydia.server.model.World.WORLD_SIZE;

public class WorldGenerationUtil {

    private static final Random random = new Random();

    public static OpenWorld generateOpenWorld() {
        OpenWorld openWorld = new OpenWorld();
        List<Integer> worldSelectorList = GenerateValueUtils.generateBinaryList();
        boolean active = false;
        int wsi = 0;
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                if (active) {
                    if (worldSelectorList.get(wsi) == 0) {
                        openWorld.setSpecialWorld(i,j, createSpecialWorld(TileType.BATTLE));
                    } else {
                        openWorld.setSpecialWorld(i,j, createSpecialWorld(TileType.EXPLORATION));
                    }
                    wsi++;
                }
                openWorld.setTile(i, j, new Tile(TileType.OPEN_WORLD, active));
                active = !active;
            }
        }
        openWorld.setTile(0,0,new Tile(TileType.EXPLORATION, true));
        openWorld.setSpecialWorld(0,0, createSpecialWorld(TileType.EXPLORATION));
        return openWorld;
    }

    private static SpecialWorld createSpecialWorld(TileType tileType) {
        SpecialWorld world = new SpecialWorld();
        int countInactive = 0;
        for (int i = 0; i < WORLD_SIZE; i++) {
            for (int j = 0; j < WORLD_SIZE; j++) {
                boolean active = random.nextInt(100) % 2 == 0;
                if(!active) {
                    if(countInactive > 5) {
                        active = true;
                    }else countInactive++;
                }
                if(i==j) active = true; //tak da imamo put uvijek, da nas ne zatvori randomizator
                world.getTiles()[i][j] = new Tile(tileType, active);
            }
        }
        return world;
    }

}
