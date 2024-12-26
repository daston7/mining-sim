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

public class GameRenderer {
    private MapGrid grid;
    private int autoMovementIndex = 0;
    private Pusher pusher;
    private Bulldozer bulldozer;
    private Excavator excavator;
    private Controller controller;
    private static List<Machine> machines = new ArrayList<>();
    private static final Color borderColor = new Color(100, 100, 100);

    public GameRenderer(MapGrid grid) {
        this.grid = grid;
    }



    /**
     * Draw the basic board with outside color and border color
     * @param bg
     */

    public void drawBoard(GGBackground bg) {
        bg.clear(new Color(230, 230, 230));
        bg.setPaintColor(Color.darkGray);
        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {
                Location location = new Location(x, y);
                OreSim.ElementType a = grid.getCell(location);
                if (a != OreSim.ElementType.OUTSIDE) {
                    bg.fillCell(location, Color.lightGray);
                }

                if (a == OreSim.ElementType.BORDER)  // Border
                    bg.fillCell(location, borderColor);
            }
        }
    }

    /**
     * Draw all different actors on the board: pusher, ore, target, rock, clay, bulldozer, excavator
     */
    public void drawActors(OreSim game) {
        int oreIndex = 0;
        int targetIndex = 0;


        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {
                Location location = new Location(x, y);
                OreSim.ElementType a = grid.getCell(location);
                if (a == OreSim.ElementType.PUSHER) {
                    pusher = new Pusher();
                    machines.add(pusher);
                    game.addActor(pusher, location);
                    pusher.setupPusher(game.isAutoMode());


                    controller = new Controller(game, pusher, game.isAutoMode());
                    game.addKeyListener(controller);


                }
                if (a == OreSim.ElementType.ORE) {
                    game.getOres()[oreIndex] = new Ore();
                    game.addActor(game.getOres()[oreIndex], location);
                    oreIndex++;
                }

                if (a == OreSim.ElementType.TARGET) {
                    game.getTargets()[targetIndex] = new Target();
                    game.addActor(game.getTargets()[targetIndex], location);
                    targetIndex++;
                }

                if (a == OreSim.ElementType.ROCK) {
                    game.addActor(new Rock(), location);
                }

                if (a == OreSim.ElementType.CLAY) {
                    game.addActor(new Clay(), location);
                }

                if (a == OreSim.ElementType.BULLDOZER) {
                    bulldozer = new Bulldozer();
                    machines.add(bulldozer);
                    game.addActor(bulldozer, location);

                }
                if (a == OreSim.ElementType.EXCAVATOR) {
                    excavator = new Excavator();
                    machines.add(excavator);
                    game.addActor(excavator, location);

                }
            }
        }
        System.out.println("ores = " + Arrays.asList(game.getOres()));
        game.setPaintOrder(Target.class);
    }

    /**
     * This method reads the auto controls fed to the program and calls doMove on the relevant machine
     * for that movement.
     */
    public void autoMoveNext(List<String> controls) {
        if (controls != null && autoMovementIndex < controls.size()) {
            String[] currentMove = controls.get(autoMovementIndex).split("-");
            String machine = currentMove[0];
            String move = currentMove[1];
            autoMovementIndex++;
            if (machine.equals("P")) {
                pusher.doMove(move);
            }
            if (machine.equals("E")) {
                excavator.doMove(move);
            }
            if (machine.equals("B")) {
                bulldozer.doMove(move);
            }

        }

    }


    /**
     * Transform the list of actors to a string of location for a specific kind of actor.
     * @param actors
     * @return
     */
    public String actorLocations(List<Actor> actors) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean hasAddedColon = false;
        boolean hasAddedLastComma = false;
        for (int i = 0; i < actors.size(); i++) {
          Actor actor = actors.get(i);
          if (actor.isVisible()) {
            if (!hasAddedColon) {
              stringBuilder.append(":");
              hasAddedColon = true;
            }
            stringBuilder.append(actor.getX() + "-" + actor.getY());
            stringBuilder.append(",");
            hasAddedLastComma = true;
          }
        }

        if (hasAddedLastComma) {
          stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        }

        return stringBuilder.toString();
    }


    public static Color getBorderColor() {
        return borderColor;
    }

    public static List<Machine> getMachines() {
        return machines;
    }

}
