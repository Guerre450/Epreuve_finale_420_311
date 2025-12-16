package mv.sdd;

import mv.sdd.io.Action;
import mv.sdd.io.ActionFileReader;
import mv.sdd.sim.Restaurant;
import mv.sdd.utils.Constantes;
import mv.sdd.utils.Logger;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 420-311 : STRUCTURE DE DONNÉES
 * ÉPREUVE FINALE : VOLET 1
 *
 * @author Cedric Champoux
 */
public class App {
    public static void main( String[] args ) {
        if (args.length < 2) {
            Arrays.stream(args).toList().forEach(System.out::println);
            System.err.println("Usage : java App <fichier_actions> <fichier_resultat>");
            return;
        }

        String actionsFile = args[0];
        String outputFile = args[1];

        try (PrintWriter out = new PrintWriter(new FileWriter(outputFile, false))) {

            Logger logger = new Logger(out , true /* pour voir aussi en console ce qui serait écrit dans le fichier */);
            logger.logLine(Constantes.HEADER_APP);
            logger.logLine(Constantes.HEADER_ACTIONS + actionsFile);
            logger.logEmpty();

            List<Action> actions = ActionFileReader.readActions(actionsFile);

            Restaurant restaurant = new Restaurant(logger);

            for (Action action : actions) {
                restaurant.executerAction(action);
            }
            restaurant.arreterService();
            logger.logLine(Constantes.FOOTER_APP);

        }
        catch (FileNotFoundException e){
            System.err.println("Argument 1 : Fichier Introuvable");
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
