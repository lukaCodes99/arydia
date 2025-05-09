package hr.tvz.arydia.server.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class World implements Serializable {

    public static final int WORLD_SIZE = 5;
    protected Tile[][] tiles = new Tile[WORLD_SIZE][WORLD_SIZE];

}
