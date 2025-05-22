package hr.tvz.arydia.server.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class World implements Serializable {

    public static final int WORLD_SIZE = 5;
    protected Tile[][] tiles = new Tile[WORLD_SIZE][WORLD_SIZE];

    public Tile getTile(int i, int j) {
        return tiles[i][j];
    }

    public void setTile(int i, int j, Tile tile) {
        this.tiles[i][j] = tile;
    }

}
