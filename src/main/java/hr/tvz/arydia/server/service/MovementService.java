package hr.tvz.arydia.server.service;

import exception.InvalidMoveException;
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
//tu su problem dijagonale
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
            DialogUtils.invalidMoveAlert("Invalid move, move exceeds your ability level");
            throw new InvalidMoveException("Invalid move in special world");
        }
        if (player.getSpecialWorldI() != i || player.getSpecialWorldJ() != j) {

            Tile oldTile = specialWorldGrid[player.getSpecialWorldI()][player.getSpecialWorldJ()];
            oldTile.setText("");

            player.setSpecialWorldLocation(new Location(i, j));

            //System.out.println(player.getX() + " " + player.getY());

            Tile newTile = specialWorldGrid[player.getSpecialWorldI()][player.getSpecialWorldJ()];
            newTile.setText(player.getName());
        }
    }

    private boolean checkMoveValidSpecialWorld(int i, int j, Tile[][] specialWorldGrid) {

        if (!specialWorldGrid[j][i].isActive()) return false;

        int deltaX = Math.abs(i - player.getOpenWorldI());
        int deltaY = Math.abs(j - player.getOpenWorldJ());

        if (deltaX > player.getMoveAbilityLevel() || deltaY > player.getMoveAbilityLevel()) {
            return false;
        }

        if (deltaX > 1 || deltaY > 1) {
            int stepX = Integer.compare(i, player.getSpecialWorldI()); //malo gpt lijevo desno
            int stepY = Integer.compare(j, player.getSpecialWorldJ()); //gore dole

            int currentX = player.getSpecialWorldI() + stepX;
            int currentY = player.getSpecialWorldJ() + stepY;

            while (currentX != i || currentY != j) {
                if (!specialWorldGrid[currentY][currentX].isActive()) {
                    return false;
                }
                currentX += stepX;
                currentY += stepY;
            }
        }

        return true; // Move is valid
    }

}
