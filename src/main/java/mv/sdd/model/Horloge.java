package mv.sdd.model;

import mv.sdd.interfaces.NotificationSender;

/**
 * Classe qui s'occupe du temps globale.
 */
public class Horloge implements NotificationSender {
    private int tempsSimule = 0;




    public int getTempsSimule() {
        return tempsSimule;
    }

    public void avancerTempsSimule(int minutes) {
        this.tempsSimule += minutes;
        sendNotification();
    }
}
