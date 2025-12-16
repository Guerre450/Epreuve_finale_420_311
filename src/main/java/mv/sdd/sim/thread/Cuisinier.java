package mv.sdd.sim.thread;

import mv.sdd.interfaces.NotificationListener;
import mv.sdd.model.Commande;
import mv.sdd.model.Horloge;
import mv.sdd.sim.Restaurant;

/**
 * Cuisinier du restaurant
 */
public class Cuisinier implements Runnable, NotificationListener {
    private Restaurant restaurant;

    private Commande commande = null;


    private static int tickCourantTachesFinit = 0;

    public synchronized static void setAjoutTickCourantTachesFinit(){
        tickCourantTachesFinit++;
    }
    public synchronized static void resetTickCourantTachesFinit(){
        tickCourantTachesFinit = 0;
    }
    public static int getTickCourantTachesFinit(){
        return tickCourantTachesFinit;
    }



    public Cuisinier(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    
    @Override
    public void run() {
        try {

            synchronized (this) {



                while (restaurant.isOuvert()){

                    this.wait();
                    if(!restaurant.isOuvert()) break;

                    if (commande == null) {

                        commande = restaurant.retirerProchaineCommande();
                        if (commande != null) {
                            commande.demarrerPreparation();
                            restaurant.marquerCommandeEnPreparation(commande);
                            restaurant.logCommandeThreadDebut(commande);
                            commande.decrementerTempsRestant();
                        }

                    } else {
                        switch (commande.getEtat()) {
                            case EN_ATTENTE -> {


                            }
                            case EN_PREPARATION -> {

                            }
                            case PRETE -> {
                                restaurant.marquerCommandeTerminee(commande);
                                restaurant.logCommandeThreadTerminee(commande);
                                commande = null;
                            }
                        }

                    }
                    setAjoutTickCourantTachesFinit();
            }
            }

            } catch(InterruptedException e){
                throw new RuntimeException(e);
            }

    }

    @Override
    public synchronized void receiverNotification() {
        notifyAll();
    }
}
