/* Team name: Thu-13:00 Team 17
Euan Marshall, Dustin Susilo, Jeremy Tanasaleh
 */
package ore;

import ch.aplu.jgamegrid.*;


import java.awt.*;
import java.util.List;

public class Pusher extends Machine {

    private String id;
    private static int lastID = 0; // Static counter for use in creating IDs when multiple pushers are present (future development)


    public Pusher() {
        super(true, "sprites/pusher.png");
    }

    public String getId() {
        return id;
    }

    public void setupPusher(boolean isAutoMode) {
        lastID++;
        this.id = "Pusher-" + lastID;
    }


    /**
     * Determines whether the Pusher can move to a specified location.
     * @param location The Location object representing the target location.
     * @return boolean True if the Pusher can move to the location, false otherwise.
     */
    public boolean canMove(Location location) {
        // Test if try to move into border, rock or clay
        Color c = gameGrid.getBg().getColor(location);
        Rock rock = (Rock)gameGrid.getOneActorAt(location, Rock.class);
        Clay clay = (Clay)gameGrid.getOneActorAt(location, Clay.class);
        Bulldozer bulldozer = (Bulldozer)gameGrid.getOneActorAt(location, Bulldozer.class);
        Excavator excavator = (Excavator)gameGrid.getOneActorAt(location, Excavator.class);
        if (c.equals(GameRenderer.getBorderColor()) || rock != null || clay != null || bulldozer != null || excavator != null)
          return false;
        else // Test if there is an ore
        {
          Ore ore = (Ore)gameGrid.getOneActorAt(location, Ore.class);
          if (ore != null)
          {

              // Try to move the ore
              ore.setDirection(this.getDirection());
              if (moveOre(ore))
                return true;
              else
                return false;

          }
        }

        return true;
    }

    /**
     * Attempts to move an ore to its next location.
     * @param ore The Ore object to be moved.
     * @return boolean True if the ore is successfully moved, false otherwise.
     */
    public boolean moveOre(Ore ore) {
    Location next = ore.getNextMoveLocation();
    // Test if try to move into border
    Color c = gameGrid.getBg().getColor(next);;
    Rock rock = (Rock)gameGrid.getOneActorAt(next, Rock.class);
    Clay clay = (Clay)gameGrid.getOneActorAt(next, Clay.class);
    if (c.equals(GameRenderer.getBorderColor()) || rock != null || clay != null)
      return false;

    // Test if there is another ore
    Ore neighbourOre = (Ore)gameGrid.getOneActorAt(next, Ore.class);
    if (neighbourOre != null)
      return false;

    // Reset the target if the ore is moved out of target
    Location currentLocation = ore.getLocation();
    List<Actor> actors = gameGrid.getActorsAt(currentLocation);
    if (actors != null) {
      for (Actor actor : actors) {
        if (actor instanceof Target) {
          Target currentTarget = (Target) actor;
          currentTarget.show();
          ore.show(0);
        }
      }
    }
    // Move the ore
    ore.setLocation(next);
      // Check if we are at a target
      Target nextTarget = (Target)gameGrid.getOneActorAt(next, Target.class);
      if (nextTarget != null) {
          ore.show(1);
          nextTarget.hide();
      } else {
          ore.show(0);
      }

    return true;
  }
}
