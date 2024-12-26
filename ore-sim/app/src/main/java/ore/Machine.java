/* Team name: Thu-13:00 Team 17
Euan Marshall, Dustin Susilo, Jeremy Tanasaleh
 */
package ore;

import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Properties;

import static java.time.zone.ZoneRulesProvider.refresh;

abstract class Machine extends Actor {

    private int moves;
    private int blocksMoved;
    private boolean isFinished = false;

    public Machine(boolean isRotateable, String filename) {
        super(isRotateable, filename);
    }
    public int getMoves() {
        return moves;
    }

    public int getBlocksMoved() {
        return blocksMoved;
    }

    abstract String getId();

    public void setMoves(int moves) {
        this.moves = moves;
    }


    public void incrementBlocksMoved() {
        this.blocksMoved++;}

    abstract boolean canMove(Location location);


    /**
     * This method moves the machine it was called on in the direction specified, and refreshes the game to reflect the move
     * @param move
     */
    public void doMove(String move) {
        if (isFinished)
            return;

        Location next = null;
        switch (move) {
            case "L":
                next = this.getLocation().getNeighbourLocation(Location.WEST);
                this.setDirection(Location.WEST);
                break;
            case "U":
                next = this.getLocation().getNeighbourLocation(Location.NORTH);
                this.setDirection(Location.NORTH);
                break;
            case "R":
                next = this.getLocation().getNeighbourLocation(Location.EAST);
                this.setDirection(Location.EAST);
                break;
            case "D":
                next = this.getLocation().getNeighbourLocation(Location.SOUTH);
                this.setDirection(Location.SOUTH);
                break;
        }

        Target curTarget = (Target) gameGrid.getOneActorAt(this.getLocation(), Target.class);
        if (curTarget != null) {
            curTarget.show();
        }
        if (next != null && this.canMove(next)) {
            this.setLocation(next);
            this.setMoves(this.getMoves() + 1);
        }

        refresh();
    }


}