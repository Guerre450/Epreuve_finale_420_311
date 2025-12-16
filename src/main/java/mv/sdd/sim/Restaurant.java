package mv.sdd.sim;

import mv.sdd.exceptions.ExceptionClientDoesNotExist;
import mv.sdd.exceptions.ExceptionOrderIsProcessed;
import mv.sdd.exceptions.ExceptionSimulationIsStopped;
import mv.sdd.io.Action;
import mv.sdd.model.*;
import mv.sdd.sim.thread.Cuisinier;
import mv.sdd.sim.thread.ThreadPool;
import mv.sdd.utils.Logger;
import mv.sdd.utils.Formatter;

import java.awt.image.AreaAveragingScaleFilter;
import java.beans.PropertyChangeListener;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/**
 * Le restaurant
 */
public class Restaurant {
    // logsDeffered
    private final Logger logger;
    private LinkedBlockingQueue<String> deferedLogs = new LinkedBlockingQueue<>();
    private final Horloge horloge = new Horloge();
    //Clients
    private HashMap<Integer,Client> clients = new HashMap<Integer, Client>();


    // Commandes
    private LinkedBlockingQueue<Commande> commandes = new LinkedBlockingQueue<Commande>();
    private Set<Commande> commandesEnPreparation = Collections.synchronizedSet(new HashSet<Commande>());
    private LinkedBlockingQueue<Commande> commandesTerminees = new LinkedBlockingQueue<Commande>();


    //Cuisinier
    private ThreadPool cusinierPool;
    private ArrayList<Cuisinier> cuisiniers = new ArrayList<>();


    //restaurant status
    private boolean ouvert = false;


    public boolean isOuvert() {
        return ouvert;
    }






    public Restaurant(Logger logger) {
        this.logger = logger;
    }

    // Méthode appelée depuis App pour chaque action

    /**
     * Executer une action du fichier
     * @param action l'action à executer
     * @throws InterruptedException si le thread est interrompu
     */
    public void executerAction(Action action) throws InterruptedException {
        // Votre code ici.
        try {

            switch (action.getType()) {
                case DEMARRER_SERVICE -> demarrerService(action.getParam1(), action.getParam2());
                //needs logging
                case AJOUTER_CLIENT ->
                        logger.logLine(Formatter.eventArriveeClient(horloge.getTempsSimule()
                                ,ajouterClient(action.getParam1(), action.getParam3(), action.getParam2())));
                // needs logging
                case PASSER_COMMANDE -> {Commande commande = passerCommande(action.getParam1(), MenuPlat.values()[MenuPlat.valueOf(action.getParam3()).ordinal()]);
                    logger.logLine(Formatter.eventCommandeCree(horloge.getTempsSimule(),commande.getId(),commande.getClient(),MenuPlat.values()[MenuPlat.valueOf(action.getParam3()).ordinal()]));
                }
                case AVANCER_TEMPS -> avancerTemps(action.getParam1());
                case AFFICHER_ETAT -> afficherEtat();
                case AFFICHER_STATS -> afficherStatistiques();
                case QUITTER -> arreterService();

            }
        }

        catch (ExceptionOrderIsProcessed | IllegalArgumentException | ExceptionSimulationIsStopped |
               ExceptionClientDoesNotExist e){
            logger.logLine(e.getMessage());
        }


    }

    /**
     * Demarrer le service et les listeners
     * @param dureeMax dure max de la simulation
     * @param nbCuisiniers nombre de cuisinier
     */
    public void demarrerService(int dureeMax, int nbCuisiniers) {
        ouvert = true;
        // start les threads
        cusinierPool = new ThreadPool(nbCuisiniers);
        for (int i = 0; i < nbCuisiniers; i++) {
            Cuisinier newCuisinier = new Cuisinier(this);
            cuisiniers.add(newCuisinier);
            cusinierPool.submitTask(newCuisinier);
        }
        //region add les listeners
        horloge.addListenerList(clients.values());
        horloge.addListenerList(commandesEnPreparation);
        horloge.addListenerList(cuisiniers);
        cuisiniers.forEach(cuisinier -> cusinierPool.submitTask(cuisinier));
        //log demarrer
        logger.logLine(Formatter.demarrerService(dureeMax,nbCuisiniers));

    }

    /**
     * Avancer le temps par minutes
     * @param minutes les minutes qui avancent le temps
     * @throws InterruptedException si le thread est interrompu
     * @throws ExceptionSimulationIsStopped si la simulation n'est pas en cours
     */
    public void avancerTemps(int minutes) throws InterruptedException, ExceptionSimulationIsStopped {
        if(!isOuvert()){
            throw new ExceptionSimulationIsStopped("Temps ne peux pas être avancer pendant la fermeture du restaurant");
        }
        logger.logLine(Formatter.avancerTemps(minutes));
        for (int i = 0; i < minutes; i++) {
            tick();
        }


    }

    /**
     * arreter le service
     */
    public void arreterService(){
        if(!ouvert){
            return;
        }
        ouvert = false;
        if(cusinierPool == null){
            return;
        }
        cuisiniers.forEach(Cuisinier::receiverNotification);
        cusinierPool.shutdownAndAwaitTermination();

    }

    /**
     * executer un tick à travers l'application
     * @throws InterruptedException
     * @throws ExceptionSimulationIsStopped
     */
    public void tick() throws InterruptedException, ExceptionSimulationIsStopped {
        if(!isOuvert()){
            throw new ExceptionSimulationIsStopped("Tick ne peux pas avancer pendant la fermeture du restaurant");
        }
        horloge.avancerTempsSimule(1);
        while (Cuisinier.getTickCourantTachesFinit() < cusinierPool.getNombrePool()){
            Thread.sleep(100);
        }
        Cuisinier.resetTickCourantTachesFinit();
        logDeffered();
    }

    /**
     * Afficher un l'etat de l'application
     */
     public void afficherEtat(){
        int clientsServit = clients.values().stream().filter(client -> client.getEtat() == EtatClient.SERVI).toList().size();
        int clientsFacher = clients.values().stream().filter(client -> client.getEtat() == EtatClient.PARTI_FACHE).toList().size();
        int commandeEnAttente = commandes.size();
        int commandeEnPreparation = commandesEnPreparation.size();
        logger.logLine(Formatter.resumeEtat(horloge.getTempsSimule(), clients.size(), clientsServit, clientsFacher, commandeEnAttente,commandeEnPreparation));
         clients.values().forEach(client -> {logger.logLine(Formatter.clientLine(client,client.getCommande().getPlats().stream().map(Plat::getCode).toList()));
         });
    }

    /**
     * Afficher les statistiques de l'application
     */
     public void afficherStatistiques(){
        Stats statObject = new Stats(horloge);
         int clientsServit = clients.values().stream().filter(client -> client.getEtat() == EtatClient.SERVI).mapToInt(e -> 1).sum();
         int clientsFacher = clients.values().stream().filter(client -> client.getEtat() == EtatClient.PARTI_FACHE).mapToInt(e -> 1).sum();
         double chiffreDaffaire = commandesTerminees.stream()
                 .filter(commande -> commande.getEtat() == EtatCommande.LIVREE)
                 .mapToDouble(Commande::calculerMontant).sum();
         statObject.setTotalClients(clients.size());
         statObject.setNbServis(clientsServit);
         statObject.setNbFaches(clientsFacher);
         List<Plat> platsVendu = commandesTerminees.stream()
                 .filter(commande -> commande.getEtat() == EtatCommande.LIVREE)
                 .flatMap(commande -> {return commande.getPlats().stream();})
                 .toList();
         int pizzaVendu = platsVendu.stream().filter(plat -> plat.getCode() == MenuPlat.PIZZA).mapToInt(e -> 1).sum();
         int burgerVendu = platsVendu.stream().filter(plat -> plat.getCode() == MenuPlat.BURGER).mapToInt(e -> 1).sum();
         int fritesVendu = platsVendu.stream().filter(plat -> plat.getCode() == MenuPlat.FRITES).mapToInt(e -> 1).sum();
         statObject.setVentesMenuPlat(MenuPlat.PIZZA, pizzaVendu);
         statObject.setVentesMenuPlat(MenuPlat.BURGER,burgerVendu);
         statObject.setVentesMenuPlat(MenuPlat.FRITES,fritesVendu);
         statObject.setChiffreAffaires(chiffreDaffaire);
         logger.logLine(statObject.toString());
     }

    /**
     * ajouter un client
     * @param id id du client
     * @param nom nom du client
     * @param patienceInitiale patience du client
     * @return un client
     * @throws ExceptionSimulationIsStopped si la simulation est arrêter
     */
     public Client ajouterClient(int id, String nom, int patienceInitiale) throws ExceptionSimulationIsStopped {
         if(!isOuvert()){
             throw new ExceptionSimulationIsStopped("Client ne peux être rajouter pendant la fermeture du restaurant");
         }
        Client newClient = new Client(id,nom,patienceInitiale,this);
        clients.put(id,newClient);

        return newClient ;
     }

    /**
     * Passe une commande
     * @param idClient id du client
     * @param codePlat le menu du plat
     * @return la commande
     * @throws ExceptionOrderIsProcessed si la commande à déja commencer
     * @throws ExceptionSimulationIsStopped si la simulation est arrêté
     * @throws ExceptionClientDoesNotExist si l'id du client correspond à aucun client
     */
     public Commande passerCommande(int idClient, MenuPlat codePlat) throws ExceptionOrderIsProcessed, ExceptionSimulationIsStopped, ExceptionClientDoesNotExist {
         if(!isOuvert()){
             throw new ExceptionSimulationIsStopped("Commande ne peux être rajouter pendant la fermeture du restaurant");
         }
         if (clients.get(idClient) == null){
            throw  new ExceptionClientDoesNotExist("le ID du client de la commande ne correspond a aucun client");
         }
        return creerCommandePourClient(clients.get(idClient),codePlat);
     }

    /**
     * Retirer une commande si elle l'existe
     * @return retourne la commande
     * @throws InterruptedException si interromp le thread
     */
    public synchronized Commande retirerProchaineCommande() throws InterruptedException {
        Commande commande = null;
        while (commande == null){
            if (commandes.isEmpty()){
                return null;
            }
            commande = commandes.take();
            if (commande.getEtat() == EtatCommande.PERDUE){
                marquerCommandePerdu(commande);
                commande = null;
            }

        }
        return commande;
    }

    /**
     * marque une commande comme en preparation
     * @param commande la commande
     */
    public void marquerCommandeEnPreparation(Commande commande){
        commandesEnPreparation.add(commande);
    }
    /**
     * marque une commande comme terminée
     * @param commande la commande
     */
    public void marquerCommandeTerminee(Commande commande){
        commandesEnPreparation.remove(commande);
        commande.setEtat(EtatCommande.LIVREE);
        commandesTerminees.offer(commande);
    }
    /**
     * marque une commande comme perdu
     * @param commande la commande
     */
    public void marquerCommandePerdu(Commande commande){
        commandesEnPreparation.remove(commande);
        commandesTerminees.offer(commande);
    }

    /**
     * cree une command pour client
     * @param client le client
     * @param codePlat le plat
     * @return la commande crée
     * @throws ExceptionOrderIsProcessed Si la commande est déja en preparation
     */
    public Commande creerCommandePourClient(Client client, MenuPlat codePlat) throws ExceptionOrderIsProcessed {
        Commande commande = client.getCommande();
        if ( commande == null){
            commande = new Commande(client,codePlat);
            client.setCommande(commande);
            commandes.add(commande);
        }
        else {
            if (commande.getEtat() != EtatCommande.EN_ATTENTE){
                throw new ExceptionOrderIsProcessed("ERREUR : La Commande à déja commencer à être preparé, la ligne sera ignoré ");
            }
            commande.ajouterPlat(codePlat);
        }
        return commande;
    }
    //deferred logging, (fin de tick)

    /**
     * Log commande evenement debut
     * @param commande la commande
     */
    public void logCommandeThreadDebut(Commande commande){
        deferedLogs.offer(Formatter.eventCommandeDebut(horloge.getTempsSimule(),commande.getId(),commande.getTempsRestant()));
    }
    /**
     * Log commande evenement terminées
     * @param commande la commande
     */
    public void logCommandeThreadTerminee(Commande commande){
        deferedLogs.offer(Formatter.eventCommandeTerminee(horloge.getTempsSimule(),commande.getId(),commande.getClient()));
    }

    /**
     * log le client faché
     * @param client le client
     */
    public void logCommandeClientPerdu(Client client){
        deferedLogs.offer(Formatter.eventClientFache(horloge.getTempsSimule(),client));
    }

    /**
     * log à la fin d'un tick
     */
    private void logDeffered(){
        deferedLogs.forEach(logger::logLine);
        deferedLogs.clear();
    }

}
