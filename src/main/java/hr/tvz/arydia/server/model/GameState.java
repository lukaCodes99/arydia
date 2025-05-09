package hr.tvz.arydia.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState implements Serializable {

    private List<Player> players;
    private OpenWorld openWorld;
    private CharacterType playerTurn;


    public void addPlayer(Player newPlayer) {
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(newPlayer);
    }

    public void changePlayerTurn() {
        this.playerTurn =
                this.playerTurn == CharacterType.PLAYER_ONE ? CharacterType.PLAYER_TWO : CharacterType.PLAYER_ONE;

    }
}
