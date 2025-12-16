package mv.sdd.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe qui executer les NotificationListener
 * @see NotificationListener
 */
public interface NotificationSender {
    public List<Collection<? extends NotificationListener>> listeners = new ArrayList<>();


    public default void addListenerList(Collection<? extends NotificationListener> listenerList){
        listeners.add(listenerList);
    }
    public default void removeListener(Collection<? extends NotificationListener> listenerList){
        listeners.remove(listenerList);
    }

    /**
     * executer tous les NotificationListener.receiverNotification()
     * @see NotificationListener
     */
    public default void sendNotification(){
        listeners.forEach(collection -> collection.iterator().forEachRemaining(NotificationListener::receiverNotification));
    }
}
