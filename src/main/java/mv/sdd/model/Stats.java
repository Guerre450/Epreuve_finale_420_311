package mv.sdd.model;

import mv.sdd.utils.Constantes;

import java.util.HashMap;
import java.util.Map;


public class Stats {
    private Horloge horloge;


    private int totalClients = 0;
    private int nbServis = 0;
    private int nbFaches = 0;
    private double chiffreAffaires = 0;
    private HashMap<MenuPlat, Integer> ventesParPlat = new HashMap<>();



    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }

    public void setNbServis(int nbServis) {
        this.nbServis = nbServis;
    }

    public void setNbFaches(int nbFaches) {
        this.nbFaches = nbFaches;
    }

    public void setChiffreAffaires(double chiffreAffaires) {
        this.chiffreAffaires = chiffreAffaires;
    }

    public void setVentesMenuPlat(MenuPlat menuPlat, Integer ventes){
        ventesParPlat.put(menuPlat,ventes);
    }


    public Stats(Horloge horloge) {
        this.horloge = horloge;
    }


    public static String statsPlatLine(MenuPlat codePlat, int quantite) {
        return "\n" + "\t\t" + codePlat + " : " + quantite;
    }


    public String toString() {
        String chaine = String.format(
                Constantes.STATS_GENERAL,
                horloge.getTempsSimule(),
                totalClients,
                nbServis,
                nbFaches,
                chiffreAffaires
        );

        StringBuilder ventesParPlatString = new StringBuilder();
        ventesParPlat.entrySet().forEach(ele ->{
            ventesParPlatString.append(statsPlatLine(ele.getKey(),ele.getValue()));
        });
        return chaine + ventesParPlatString.toString();
    }
}
