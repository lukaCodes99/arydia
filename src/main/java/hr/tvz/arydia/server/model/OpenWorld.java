package hr.tvz.arydia.server.model;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class OpenWorld extends World implements Serializable {

    private SpecialWorld[][] specialWorlds = new SpecialWorld[10][10];


    public SpecialWorld getSpecialWorld(int i, int j) {
        return specialWorlds[i][j];
    }

    public void setSpecialWorld(int i, int j, SpecialWorld specialWorld) {
        this.specialWorlds[i][j] = specialWorld;
    }

    public Tile getTile(int i, int j) {
        return tiles[i][j];
    }

    public void setTile(int i, int j, Tile tile) {
        this.tiles[i][j] = tile;
    }

}
