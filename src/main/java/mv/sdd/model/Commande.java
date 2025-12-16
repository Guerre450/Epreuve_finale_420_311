package mv.sdd.model;

import mv.sdd.interfaces.NotificationListener;
import mv.sdd.utils.Constantes;

import java.util.ArrayList;

/**
 * Classe des commandes du restaurant
 */
public class Commande implements NotificationListener {
    private int id;
    private static int nbCmd = 0;
    private final Client client;
    private EtatCommande etat = EtatCommande.EN_ATTENTE;
    private int tempsRestant; // en minutes simulées
    private final ArrayList<Plat> plats = new ArrayList<>();

    public ArrayList<Plat> getPlats() {
        return plats;
    }

    public Commande(Client client, MenuPlat plat) {
        id = ++nbCmd;
        this.client = client;
        ajouterPlat(plat);
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public int getTempsRestant() {
        return tempsRestant;
    }

    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }

    public void ajouterPlat(MenuPlat menuPlat){
        plats.add(Constantes.MENU.get(menuPlat));
    }

    /**
     * Demarrer la preparation de la commande
     */
    public void demarrerPreparation(){
        tempsRestant = calculerTempsPreparationTotal();
        etat = EtatCommande.EN_PREPARATION;
    }

    public synchronized void decrementerTempsRestant(){
        tempsRestant--;
        if (tempsRestant < 1){
            setEtat(EtatCommande.PRETE);
        }
    }

    @Override
    public void receiverNotification() {
        decrementerTempsRestant();
    }

    public int calculerTempsPreparationTotal(){
        return plats.stream().mapToInt(Plat::getTempsPreparation).sum();
    }

    public double calculerMontant(){
        return plats.stream().mapToDouble(Plat::getPrix).sum();
    }
}
