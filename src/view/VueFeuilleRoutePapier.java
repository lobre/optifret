package view;

import model.Chemin;
import model.FeuilleRoute;
import model.Troncon;

import java.util.ArrayList;

public class VueFeuilleRoutePapier {

    private FeuilleRoute m_feuilleRoute;

    public VueFeuilleRoutePapier(FeuilleRoute feuilleRoute) {
        m_feuilleRoute = feuilleRoute;
    }

    public String getVersionPapier() {
        String r = new String();

        r += "###########################################\n";
        r += "#  Version papier de la feuille de route  #\n";
        r += "###########################################\n\n";

        r += "- Effectuez le départ depuis l'entrepot.\n\n";

        ArrayList<Troncon> troncons = m_feuilleRoute.getTroncons();
        String rueCourante = troncons.get(0).getM_nom();
        int longueur = 0;
        Troncon t;
        for (int i = 0; i < troncons.size(); i++) {
            t = troncons.get(i);
            boolean dernierTroncon = (i == troncons.size() - 1);
            if (!t.getM_nom().equals(rueCourante) || dernierTroncon) {
                if (dernierTroncon) {
                    longueur += t.getM_longueur();
                }
                r += "  . Suivre la rue " + rueCourante + " sur " + longueur + " mètres.\n";

                // Mise à jour du nom de la rue courante et réinitialisation de la longueur totale
                rueCourante = t.getM_nom();
                longueur = 0;


                if (!dernierTroncon) {
                    Troncon t2 = troncons.get(i +1);
                    double angle = t.angleAvec(t2);
                    if (Math.abs(angle - Math.PI / 2) < 0.6) {
                        r += "  . Tournez à droite.\n";
                    }
                    else if (Math.abs(angle + Math.PI / 2) < 0.6) {
                        r += "  . Tournez à gauche.\n";
                    }

                }


            }
            longueur += t.getM_longueur();


            if (t.getArrivee().hasLivraison()) {
                r += "  * Réalisez la livraison " + t.getArrivee().getM_livraison().getM_id() + "\n";
            }

        }

        r += "\n  - Fin de la tournée.\n";


        return r;
    }

}
