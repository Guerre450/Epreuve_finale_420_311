package mv.sdd.model;

import mv.sdd.interfaces.NotificationListener;
import mv.sdd.sim.Restaurant;

import java.util.Objects;

/**
 * Client du restaurant
 */
public class Client implements NotificationListener {
    private final int id;
    private final String nom;
    private int patience;
    private EtatClient etat;
    private Commande commande;
    private final Restaurant restaurant;
    public Client(int id, String nom, int patienceInitiale, Restaurant resto) {
        this.id = id;
        this.nom = nom;
        this.patience = patienceInitiale;
        this.etat = EtatClient.EN_ATTENTE;
        this.restaurant = resto;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getPatience() {
        return patience;
    }

    /**
     * Diminue la patience du client
     * @param minutes Montant de patience
     */
    public void diminuerPatience(int minutes) {

        if (etat == EtatClient.SERVI || etat == EtatClient.PARTI_FACHE  || commande == null){
            return;
        }
        if(commande.getEtat() != EtatCommande.EN_ATTENTE){
            setEtat(EtatClient.SERVI);
        }
        patience -= minutes;
        if(patience <= 0){
            etat = EtatClient.PARTI_FACHE;
            commande.setEtat(EtatCommande.PERDUE);
            restaurant.logCommandeClientPerdu(this);
        }
    }

    public EtatClient getEtat() {
        return etat;
    }

    public void setEtat(EtatClient etat) {
        this.etat = etat;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public void receiverNotification() {
        diminuerPatience(1);
    }
}
