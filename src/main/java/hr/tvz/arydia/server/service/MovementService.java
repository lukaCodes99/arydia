package hr.tvz.arydia.server.service;

import hr.tvz.arydia.server.exception.InvalidMoveException;
import hr.tvz.arydia.server.model.Location;
import hr.tvz.arydia.server.model.Player;
import hr.tvz.arydia.server.model.Tile;
import hr.tvz.arydia.server.util.DialogUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MovementService {

    private final Player player;

    //odvojit cu jer ima jako puno razlika u svijetovima
    public void moveToTileOpenWorld(Tile[][] openWorldGrid, int i, int j) {
        if(checkMoveInvalidOpenWorld(i, j, 1)) { //uvijek jedan zbog dijagonale i zato kad može samo po jednu ploču
            DialogUtils.invalidMoveAlert("Invalid move, you can only move to adjacent tiles");
            throw new InvalidMoveException("Invalid move");
        }
        if (player.getOpenWorldI() != i || player.getOpenWorldJ() != j) {

            Tile oldTile = openWorldGrid[player.getOpenWorldI()][player.getOpenWorldJ()];
            oldTile.setText("");

            //Location location = new Location(i, j);
            player.setOpenWorldLocation(new Location(i, j));

            //System.out.println(player.getX() + " " + player.getY());

            Tile newTile = openWorldGrid[player.getOpenWorldI()][player.getOpenWorldJ()];
            newTile.setText(player.getName());
        }
    }
//tu su problem dijagonale odnosno smije samo u pločice do trenutne
    private boolean checkMoveInvalidOpenWorld(int i, int j, int moveAbilityLevel) {
        //return !((i - player.getOpenWorldI() > moveAbilityLevel) || (j - player.getOpenWorldJ() <= moveAbilityLevel));
        //return !(((i - player.getOpenWorldI()) > moveAbilityLevel) || ((j - player.getOpenWorldJ()) <= moveAbilityLevel));
        //return (Math.abs(x - player.getX()) > moveAbilityLevel || Math.abs(y - player.getY()) > moveAbilityLevel);

        return (Math.abs(i - player.getOpenWorldI()) > moveAbilityLevel
                || (Math.abs(j - player.getOpenWorldJ()) > moveAbilityLevel));
    }

//    true true = true
//    true false = true
//    false true = true
//    false false = false

    public void moveToTileSpecialWorld(Tile[][] specialWorldGrid, int i, int j) {
        //int moveAbilityLevel = player.getMoveAbilityLevel();
        if(!checkMoveValidSpecialWorld(i, j, specialWorldGrid)) {
            //DialogUtils.invalidMoveAlert("Invalid move, move exceeds your ability level or tile inactive");
            throw new InvalidMoveException("Invalid move in special world");
        }
        System.out.printf("player type: %s\n", player.getPlayerType());
        System.out.println(player.getSpecialWorldI() +" "+ player.getSpecialWorldJ()
        +" swl"+ player.getSpecialWorldLocation().toString()
        +" openworld "+ player.getOpenWorldI()+" "+ player.getOpenWorldJ());


        if (player.getSpecialWorldI() != i || player.getSpecialWorldJ() != j) {

            specialWorldGrid[player.getSpecialWorldI()][player.getSpecialWorldJ()].setText("");

            player.setSpecialWorldLocation(new Location(i, j));

            //System.out.println(player.getX() + " " + player.getY());
            //ime na novom tileu
            specialWorldGrid[player.getSpecialWorldI()][player.getSpecialWorldJ()].setText(player.getName());

        }
    }

    private boolean checkMoveValidSpecialWorld(int newI, int newJ, Tile[][] specialWorldGrid) {

        if (!specialWorldGrid[newI][newJ].isActive()) {
            System.out.println(specialWorldGrid[newI][newJ].isActive());
            return false;
        }

        int deltaI = Math.abs(newI - player.getSpecialWorldI());
        int deltaJ = Math.abs(newJ - player.getSpecialWorldJ());

        if (deltaI > player.getMoveAbilityLevel() || deltaJ > player.getMoveAbilityLevel()) {
            return false;
        }

        if (deltaI > 0 || deltaJ > 0) {
            int stepI = Integer.compare(newI, player.getSpecialWorldI()); //malo gpt, vraca u kojem smjeru se ide zapravo
            //vrijednosti -1,0,1 i to nam je korak po koliko idemo gore dolje lijevo desno

            int stepJ = Integer.compare(newJ, player.getSpecialWorldJ()); //gore dole


            int currentX = player.getSpecialWorldI() + stepI;
            int currentY = player.getSpecialWorldJ() + stepJ;

            while (currentX != newI || currentY != newJ) {
                if (!specialWorldGrid[currentX][currentY].isActive()) {
                    return false;
                }
                currentX += stepI;
                currentY += stepJ;
            }
        }

        return true; // Move is valid
    }

}
