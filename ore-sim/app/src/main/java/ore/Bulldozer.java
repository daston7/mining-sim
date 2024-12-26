/* Team name: Thu-13:00 Team 17
Euan Marshall, Dustin Susilo, Jeremy Tanasaleh
 */

package ore;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.List;

public class Bulldozer extends Machine{

    private String id;
    private static int lastID = 0; // Static counter for use in creating IDs when multiple bulldozers are present (future development)

    public Bulldozer() {
        super(true, "sprites/bulldozer.png");
        lastID++;
        this.id = "Bulldozer-"+lastID;
    }

    public String getId() {
        return id;
    }

    /**
     * This method determines whether the excavator can move into the given location. If blocked by
     * an ore, pusher, excavator, other bulldozer or the edge of the map, it will not move, otherwise it can move
     * and the method returns true.
     * @param location
     * @return
     */
    public boolean canMove(Location location) {
        Color c = gameGrid.getBg().getColor(location);
        Rock rock = (Rock) gameGrid.getOneActorAt(location, Rock.class);
        Clay clay = (Clay) gameGrid.getOneActorAt(location, Clay.class);
        Bulldozer bulldozer = (Bulldozer) gameGrid.getOneActorAt(location, Bulldozer.class);
        Excavator excavator = (Excavator) gameGrid.getOneActorAt(location, Excavator.class);
        Pusher pusher = (Pusher) gameGrid.getOneActorAt(location, Pusher.class);
        Ore ore = (Ore)gameGrid.getOneActorAt(location, Ore.class);
        if (c.equals(GameRenderer.getBorderColor()) || rock != null || bulldozer != null || excavator != null || pusher != null || ore != null)
            return false;
        else if (clay != null){
            removeClay(clay);
            return true;
        }

        return true;
    }

    /**
     * This method hides a clay block and increments the "clay removed" counter
     * of the bulldozer it is called on by 1.
     * @param clay
     * @return
     */
    public boolean removeClay(Clay clay) {
        Location location = clay.getLocation();
        this.incrementBlocksMoved();
        // Remove the clay
        clay.hide();

        return true;
    }
}
