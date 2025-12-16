package mv.sdd.utils;

import mv.sdd.model.Client;
import mv.sdd.model.EtatClient;
import mv.sdd.model.MenuPlat;
import mv.sdd.model.Stats;

import java.util.List;

public final class Formatter {

    private Formatter() {
        // Classe utilitaire tous les attributs statiques
    }

    /**
     * Ligne de résumé pour AFFICHER_ETAT.
     * Format attendu :
     * [t=3] 👥2 😋1 😡0 📥1 🍳1
     */
    public static String resumeEtat(int temps,
                                    int nbClients,
                                    int nbServis,
                                    int nbFaches,
                                    int nbEnFile,
                                    int nbEnPreparation) {
        return String.format(Constantes.RESUME_ETAT, temps,
                nbClients,
                nbServis,
                nbFaches,
                nbEnFile,
                nbEnPreparation);
    }

    /**
     * Emoji d'état pour un client.
     * EN_ATTENTE -> 🙂
     * SERVI      -> 😋
     * PARTI_FACHE-> 😡
     */
    public static String emojiEtatClient(EtatClient etat) {
        return switch (etat) {
            case EN_ATTENTE -> Constantes.EMO_CLIENT_ATTENTE;
            case SERVI      -> Constantes.EMO_CLIENT_SERVI;
            case PARTI_FACHE -> Constantes.EMO_CLIENT_FACHE;
        };
    }

    /**
     * Emoji du plat
     * @param plat PIZZA, BURGER, FRITES
     * @return Emoji correspondant
     */
    public static String emojiPlat(MenuPlat plat) {
        return switch (plat) {
            case PIZZA  -> Constantes.EMO_PIZZA;
            case BURGER -> Constantes.EMO_BURGER;
            case FRITES -> Constantes.EMO_FRITES;
        };
    }

    /**
     * Ligne d'affichage pour un client.
     * Exemple :
     * "    #1 Alice 🙂 (pat=4, 🍕)"
     *
     * @param client client à afficher
     * @param codePlat représentation des plats
     */
    public static String clientLine(Client client, List<MenuPlat> codePlat) {
        StringBuilder strBuilder = new StringBuilder();
        codePlat.stream().map(Formatter::emojiPlat).forEach( emoji -> strBuilder.append(emoji).append(" "));
        strBuilder.setLength(strBuilder.length()-1);
        return String.format(Constantes.LIGNE_CLIENT,client.getId(),client.getNom(),emojiEtatClient(client.getEtat()),
                client.getPatience(),
                strBuilder);
    }

    // ---------- Lignes événements ---------- //
    public static String eventArriveeClient(int temps, Client client) {
        return String.format(Constantes.EVENT_ARRIVEE_CLIENT,
                temps,
                client.getId(),
                client.getNom(),
                client.getPatience()
        );
    }

    public static String eventCommandeCree(int temps, int idCommande, Client client, MenuPlat codePlat) {
        return String.format(
                Constantes.EVENT_CMD_CREE,
                temps,
                idCommande,
                client.getNom(),
                emojiPlat(codePlat)
        );
    }

    public static String eventCommandeDebut(int temps, int idCommande, int duree) {
        return String.format(Constantes.EVENT_CMD_DEBUT,temps, idCommande,duree );
    }

    public static String eventCommandeTerminee(int temps, int idCommande, Client client) {
        return String.format(Constantes.EVENT_CMD_TERMINEE, temps, idCommande, client.getNom());
    }

    public static String eventClientFache(int temps, Client client) {
        return String.format(Constantes.EVENT_CLIENT_FACHE, temps, client.getNom());
    }
    // TODO : Ajouter le formattage des affichages manquants au besoin
    public static String demarrerService(int tempsMaximal, int nbCuisinier){
        return String.format(Constantes.DEMARRER_SERVICE,tempsMaximal, nbCuisinier);
    }

    public static String avancerTemps(int nbAvance){
        return Constantes.AVANCER_TEMPS + nbAvance;
    }



}

