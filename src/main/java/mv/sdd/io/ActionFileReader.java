package mv.sdd.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lecture du fichier d'actions
 */
public class ActionFileReader {
    /**
     * Lit les action d'un fichier d'actions
     * @param filePath chemin du fichier d'action
     * @return Une liste d'actions
     * @throws IOException Lorsque la lecture du fichier est interrompu
     */
    public static List<Action> readActions(String filePath) throws IOException {
        List<Action> actions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        br.lines().forEach( ligne -> {
            try {
                Action newAction = ActionParser.parseLigne(ligne);
                actions.add(newAction);
            }catch (IllegalArgumentException e){
                System.err.println("Action Inconnue: " + ligne.split(";")[0].trim());
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.err.println("Parametres de l'action invalide");
            }

                }
        );
        return actions;
    }
}
