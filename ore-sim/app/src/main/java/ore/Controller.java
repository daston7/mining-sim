/* Team name: Thu-13:00 Team 17
Euan Marshall, Dustin Susilo, Jeremy Tanasaleh
 */
package ore;

import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.*;

import java.awt.event.KeyEvent;
import java.util.List;


public class Controller implements GGKeyListener {

    private Pusher pusher;
    private OreSim game;
    private boolean isAutoMode;

    public Controller(OreSim game, Pusher pusher, boolean isAutoMode) {
        this.game = game;
        this.pusher = pusher;
        this.isAutoMode = isAutoMode;
    }

    /**
     * The method is automatically called by the framework when a key is pressed. Based on the pressed key, the pusher
     *  will change the direction and move
     * @param evt
     * @return
     */
    public boolean keyPressed(KeyEvent evt) {
        if (game.isFinished())
            return true;

        Location next = null;

        if (!isAutoMode && !game.isFinished()) {
            switch (evt.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    next = pusher.getLocation().getNeighbourLocation(Location.WEST);
                    pusher.setDirection(Location.WEST);
                    break;
                case KeyEvent.VK_UP:
                    next = pusher.getLocation().getNeighbourLocation(Location.NORTH);
                    pusher.setDirection(Location.NORTH);
                    break;
                case KeyEvent.VK_RIGHT:
                    next = pusher.getLocation().getNeighbourLocation(Location.EAST);
                    pusher.setDirection(Location.EAST);
                    break;
                case KeyEvent.VK_DOWN:
                    next = pusher.getLocation().getNeighbourLocation(Location.SOUTH);
                    pusher.setDirection(Location.SOUTH);
                    break;
            }
        }

        Target curTarget = (Target) game.getOneActorAt(pusher.getLocation(), Target.class);

        if (curTarget != null) {
            curTarget.show();
        }

        if (next != null && pusher.canMove(next)) {
            pusher.setLocation(next);
            game.updateLogResult();
        }

        game.refresh();
        return true;
    }

    public boolean keyReleased(KeyEvent evt) {
        return true;
    }
}
