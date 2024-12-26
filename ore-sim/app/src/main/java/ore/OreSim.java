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

public class OreSim extends GameGrid {
  // ------------- Inner classes -------------
  public enum ElementType{
    OUTSIDE("OS"), EMPTY("ET"), BORDER("BD"),
    PUSHER("P"), BULLDOZER("B"), EXCAVATOR("E"), ORE("O"),
    ROCK("R"), CLAY("C"), TARGET("T");
    private String shortType;

    ElementType(String shortType) {
      this.shortType = shortType;
    }

    public String getShortType() {
      return shortType;
    }

    public static ElementType getElementByShortType(String shortType) {
      ElementType[] types = ElementType.values();
      for (ElementType type: types) {
        if (type.getShortType().equals(shortType)) {
          return type;
        }
      }

      return ElementType.EMPTY;
    }
  }

  private MapGrid grid;
  private GameRenderer gameRenderer;

  private Ore[] ores;
  private Target[] targets;
  private boolean isFinished = false;
  private Properties properties;
  private boolean isAutoMode;
  private double gameDuration;
  private List<String> controls;
  private int movementIndex;
  private StringBuilder logResult = new StringBuilder();
  private Statistics stats = new Statistics();

  public OreSim(Properties properties, MapGrid grid)
  {
    super(grid.getNbHorzCells(), grid.getNbVertCells(), 30, false);
    this.grid = grid;
    this.properties = properties;
    gameRenderer = new GameRenderer(grid);

    ores = new Ore[grid.getNbOres()];
    targets = new Target[grid.getNbOres()];

    isAutoMode = properties.getProperty("movement.mode").equals("auto");
    gameDuration = Integer.parseInt(properties.getProperty("duration"));
    setSimulationPeriod(Integer.parseInt(properties.getProperty("simulationPeriod")));
    controls = Arrays.asList(properties.getProperty("machines.movements").split(","));
  }



  /**
   * Check the number of ores that are collected
   * @return
   */
  private int checkOresDone() {
    int nbTarget = 0;
    for (int i = 0; i < grid.getNbOres(); i++)
    {
      if (ores[i].getIdVisible() == 1)
        nbTarget++;
    }
    return nbTarget;
  }

  /**
   * The main method to run the game
   * @param isDisplayingUI
   * @return
   */
  public String runApp(boolean isDisplayingUI) {
    GGBackground bg = getBg();
    gameRenderer.drawBoard(bg);
    gameRenderer.drawActors(this);

    if (isDisplayingUI) {
      show();
    }

    if (isAutoMode) {
        doRun();
    }

    int oresDone = checkOresDone();
    double ONE_SECOND = 1000.0;
    while(oresDone < grid.getNbOres() && gameDuration >= 0) {
      try {
        Thread.sleep(simulationPeriod);
        double minusDuration = (simulationPeriod / ONE_SECOND);
        gameDuration -= minusDuration;
        String title = String.format("# Ores at Target: %d. Time left: %.2f seconds", oresDone, gameDuration);
        setTitle(title);
        if (isAutoMode) {
          gameRenderer.autoMoveNext(controls); // this may need changing to something else
          updateLogResult();
        }

        oresDone = checkOresDone();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    doPause();

    if (oresDone == grid.getNbOres()) {
      setTitle("Mission Complete. Well done!");
      stats.updateStatistics();
    } else if (gameDuration < 0) {
      setTitle("Mission Failed. You ran out of time");
      stats.updateStatistics();
    }

    isFinished = true;
    return logResult.toString();
  }


  /**
   * The method will generate a log result for all the movements of all actors
   * The log result will be tested against our expected output.
   * Your code will need to pass all the 3 test suites with 9 test cases.
   */
  public void updateLogResult() {
    movementIndex++;
    List<Actor> pushers = getActors(Pusher.class);
    List<Actor> ores = getActors(Ore.class);
    List<Actor> targets = getActors(Target.class);
    List<Actor> rocks = getActors(Rock.class);
    List<Actor> clays = getActors(Clay.class);
    List<Actor> bulldozers = getActors(Bulldozer.class);
    List<Actor> excavators = getActors(Excavator.class);

    logResult.append(movementIndex + "#");
    logResult.append(ElementType.PUSHER.getShortType()).append(gameRenderer.actorLocations(pushers)).append("#");
    logResult.append(ElementType.ORE.getShortType()).append(gameRenderer.actorLocations(ores)).append("#");
    logResult.append(ElementType.TARGET.getShortType()).append(gameRenderer.actorLocations(targets)).append("#");
    logResult.append(ElementType.ROCK.getShortType()).append(gameRenderer.actorLocations(rocks)).append("#");
    logResult.append(ElementType.CLAY.getShortType()).append(gameRenderer.actorLocations(clays)).append("#");
    logResult.append(ElementType.BULLDOZER.getShortType()).append(gameRenderer.actorLocations(bulldozers)).append("#");
    logResult.append(ElementType.EXCAVATOR.getShortType()).append(gameRenderer.actorLocations(excavators));

    logResult.append("\n");
  }

  public boolean isFinished() {
    return isFinished;
  }

  public boolean isAutoMode() {
    return isAutoMode;
  }

  public Ore[] getOres() {
    return ores;
  }

  public Target[] getTargets() {
    return targets;
  }

}
