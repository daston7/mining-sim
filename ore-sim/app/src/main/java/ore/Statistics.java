/* Team name: Thu-13:00 Team 17
Euan Marshall, Dustin Susilo, Jeremy Tanasaleh
 */
package ore;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Statistics {

    /**
     * Method to write statistics for pusher, excavator and bulldozer to statistics.txt file.
     * Statistics include total moves for each, clay removed for bulldozers and rocks removed for excavators.
     */
    public void updateStatistics() {
        // Clear the file content by opening FileWriter in overwrite mode (default behavior)
        try (FileWriter fileWriter = new FileWriter("statistics.txt", false)) {
            List<Machine> pushers = new ArrayList<>();
            List<Machine> excavators = new ArrayList<>();
            List<Machine> bulldozers = new ArrayList<>();

            // Categorize each machine (pusher data must come first, followed by excavator, then bulldozer)
            for (Machine m : GameRenderer.getMachines()) {
                if (m instanceof Pusher) {
                    pushers.add(m);
                } else if (m instanceof Excavator) {
                    excavators.add(m);
                } else if (m instanceof Bulldozer) {
                    bulldozers.add(m);
                }
            }
            // Write Pusher data
            fileWriter.write(pushers.get(0).getId() + " Moves: " + pushers.get(0).getMoves() + "\n");

            // Write Excavator data
            // note this is hard coded, should be changed for dealing with multiple excavators etc
            if (!excavators.isEmpty()) {
                fileWriter.write(excavators.get(0).getId() + " Moves: " + excavators.get(0).getMoves() + "\n");
                fileWriter.write(excavators.get(0).getId() + " Rock removed: " + excavators.get(0).getBlocksMoved() + "\n");
            }
            // Write Bulldozer data
            if (!bulldozers.isEmpty()) {
                fileWriter.write(bulldozers.get(0).getId() + " Moves: " + bulldozers.get(0).getMoves() + "\n");
                fileWriter.write(bulldozers.get(0).getId() + " Clay removed: " + bulldozers.get(0).getBlocksMoved() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Cannot write to file - e: " + e.getLocalizedMessage());
        }
    }

}
