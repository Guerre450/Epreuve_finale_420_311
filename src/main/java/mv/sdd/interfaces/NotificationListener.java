package mv.sdd.interfaces;

/**
 * Interface pour les classes qui écoutent NotificationSender
 * @see NotificationSender
 */
public interface NotificationListener {

    /**
     * Fonction executé par NotificationSender
     * @see NotificationSender
     */
    public void receiverNotification();

}
