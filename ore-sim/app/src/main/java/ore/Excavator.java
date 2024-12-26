
/* Team name: Thu-13:00 Team 17
Euan Marshall, Dustin Susilo, Jeremy Tanasaleh
 */
package ore;

import ch.aplu.jgamegrid.Location;

import java.awt.*;

public class Excavator extends Machine{

    private String id;
    private static int lastID = 0; // Static counter for use in creating IDs when multiple excavators are present (future development)

    public Excavator() {
        super(true, "sprites/excavator.png");
        lastID++;
        this.id = "Excavator-"+lastID;
    }

    public String getId() {
        return id;
    }

    /**
     * This method determines whether the excavator can move into the given location. If blocked by
     * an ore, pusher, other excavator, bulldozer or the edge of the map, it will not move, otherwise it can move
     * and the method returns true.
     * @param location
     * @return
     */
    public boolean canMove(Location location) {
        Color c = gameGrid.getBg().getColor(location);
        Rock rock = (Rock)gameGrid.getOneActorAt(location, Rock.class);
        Clay clay = (Clay)gameGrid.getOneActorAt(location, Clay.class);
        Bulldozer bulldozer = (Bulldozer)gameGrid.getOneActorAt(location, Bulldozer.class);
        Excavator excavator = (Excavator)gameGrid.getOneActorAt(location, Excavator.class);
        Pusher pusher = (Pusher)gameGrid.getOneActorAt(location, Pusher.class);
        Ore ore = (Ore)gameGrid.getOneActorAt(location, Ore.class);

        if (c.equals(GameRenderer.getBorderColor()) || bulldozer != null || excavator != null || clay != null || pusher != null || ore != null) {
            return false;
        } else if (rock != null) {
            removeRock(rock);
            return true;
        }

        return true;
    }

    /**
     * This method hides the rock given in the parameter and increments the "rock removed" counter of the
     * excavator it is called on by 1.
     * @param rock
     * @return
     */
    public boolean removeRock(Rock rock) {
        this.incrementBlocksMoved();
        // Remove the clay
        rock.hide();
        return true;
    }
}
