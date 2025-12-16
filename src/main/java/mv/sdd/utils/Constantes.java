package mv.sdd.utils;

import mv.sdd.model.MenuPlat;
import mv.sdd.model.Plat;

import java.util.HashMap;
import java.util.Map;

public class Constantes {
    private Constantes(){}

    // ========= Emojis généraux =========
    private static final String EMO_CLIENTS      = "👥";
    private static final String EMO_FILE_CMD     = "📥";
    private static final String EMO_PREP         = "🍳";
    private static final String EMO_STATS        = "📊";
    private static final String EMO_RESTO        = "🍽️";
    private static final String EMO_CLIENT       = "🧍";
    private static final String EMO_ENTREE       = "🚪";
    private static final String EMO_OK           = "✅";
    private static final String EMO_AVANCER      = "⏩";
    private static final String EMO_STATS_CMD      = "📈";
    private static final String EMO_ARGENT      = "💰";
    private static final String EMO_SERVICE      = "⏱️";
    private static final String EMO_CUISINIER      = "👨‍🍳";

    // États clients (pour les lignes d’affichage)
    public static final String EMO_CLIENT_ATTENTE = "🙂";
    public static final String EMO_CLIENT_SERVI   = "😋";
    public static final String EMO_CLIENT_FACHE   = "😡";

    // ========= Emojis plats =========
    public static final String EMO_PIZZA  = "🍕";
    public static final String EMO_BURGER = "🍔";
    public static final String EMO_FRITES = "🍟";

    // ========= noms de plats =========
    private static final String NOM_PIZZA   = "Pizza fromage";
    private static final String NOM_BURGER  = "Burger classique";
    private static final String NOM_FRITES  = "Frites";

    // ========= Temps de préparation (minutes simulées) =========
    private static final int PREP_MIN_PIZZA  = 3;
    private static final int PREP_MIN_BURGER = 2;
    private static final int PREP_MIN_FRITES = 1;

    // ========= Prix =========
    private static final double PRIX_PIZZA  = 14.99;
    private static final double PRIX_BURGER = 11.49;
    private static final double PRIX_FRITES = 3.99;

    // ========= Formatage / libellés =========
    public static final String HEADER_APP = "========== RUSH AU RESTO ==========";
    public static final String HEADER_ACTIONS = EMO_RESTO + " Lecture des actions : ";
    public static final String FOOTER_APP = "===================================";
    private static final String LABEL_PLATS_VENDUS = "\tPlats vendus :";
    private static final String LABEL_CLIENTS_TOTAUX = "\tClients totaux : ";
    private static final String LABEL_SERVIS        = "\t" + EMO_CLIENT_SERVI + " Servis\t\t: ";
    private static final String LABEL_FACHES        = "\t" + EMO_CLIENT_FACHE + " Fâchés\t\t: ";
    private static final String LABEL_CA            = "\t"+ EMO_ARGENT + " Chiffre d'affaires : ";


    public static final String HEADER_AFFICHER_STATS = EMO_STATS_CMD + " AFFICHER_STATS";
    public static final String DEMARRER_SERVICE = "[" + EMO_SERVICE + "] Service = %d min, " + EMO_CUISINIER + " = %d";
    public static final String AVANCER_TEMPS = EMO_AVANCER + " AVANCER_TEMPS;";
    public static final String RESUME_ETAT         = "[t=%d] " +
            Constantes.EMO_CLIENTS      + "%d " +
            Constantes.EMO_CLIENT_SERVI + "%d " +
            Constantes.EMO_CLIENT_FACHE + "%d " +
            Constantes.EMO_FILE_CMD     + "%d " +
            Constantes.EMO_PREP         + "%d";

    public static final String STATS_GENERAL       = "[t=%d] " + Constantes.EMO_STATS + " Stats\n" +
            LABEL_CLIENTS_TOTAUX + "%d\n" +
            LABEL_SERVIS + "%d\n" +
            LABEL_FACHES + "%d\n" +
            LABEL_CA + "%.2f $\n" +
            LABEL_PLATS_VENDUS;

    public static final String EVENT_ARRIVEE_CLIENT = "[" + EMO_ENTREE + " t=%d] " + EMO_CLIENT + " Client #%d \"%s\" (pat=%d)";
    public static final String EVENT_CMD_CREE = "[" + EMO_FILE_CMD + " t=%d] Cmd #%d (%s) → %s";
    public static final String EVENT_CMD_DEBUT = "[" + EMO_PREP + " t=%d] Cmd #%d commence (%d min)";
    public static final String EVENT_CMD_TERMINEE = "[" + EMO_OK + " t=%d] Cmd #%d terminée → %s " + EMO_CLIENT_SERVI;
    public static final String EVENT_CLIENT_FACHE = "[" + EMO_CLIENT_FACHE + " t=%d] %s part fâché (pat=0)";
    public static final String LIGNE_CLIENT = " #%d %s %s (pat=%d, %s)";
    // ============== Menu ==========================
    public static final Map<MenuPlat, Plat> MENU;

    static {
        MENU = new HashMap<>();
        MENU.put(MenuPlat.PIZZA, new Plat(MenuPlat.PIZZA, Constantes.NOM_PIZZA, Constantes.PREP_MIN_PIZZA, Constantes.PRIX_PIZZA));
        MENU.put(MenuPlat.BURGER, new Plat(MenuPlat.BURGER, Constantes.NOM_BURGER, Constantes.PREP_MIN_BURGER, Constantes.PRIX_BURGER));
        MENU.put(MenuPlat.FRITES, new Plat(MenuPlat.FRITES, Constantes.NOM_FRITES, Constantes.PREP_MIN_FRITES, Constantes.PRIX_FRITES));
    }
}