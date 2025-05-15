package hr.tvz.arydia.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player implements Serializable {

    private String name;
    private CharacterType playerType;
    private Location openWorldLocation;
    private Location specialWorldLocation;
    private Integer moveAbilityLevel = 2;


    public static Player getNewPlayer(String playerName, int playerNumber) {
        Player player = new Player();
        player.setName(playerName);
        player.setPlayerType(CharacterType.values()[playerNumber-1]);
        player.setOpenWorldLocation(new Location(0, 0));
        player.setSpecialWorldLocation(new Location(0, 0));
        return player;
    }

    public int getOpenWorldI(){
        return openWorldLocation.getI();
    }
    public int getOpenWorldJ(){
        return openWorldLocation.getJ();
    }
    public int getSpecialWorldI(){
        return specialWorldLocation.getI();
    }
    public int getSpecialWorldJ(){
        return specialWorldLocation.getJ();
    }
    public void setOpenWorldI(int i){
        openWorldLocation.setI(i);
    }
    public void setOpenWorldJ(int j){
        openWorldLocation.setJ(j);
    }
}
