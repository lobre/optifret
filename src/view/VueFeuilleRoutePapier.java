package view;

import model.FeuilleRoute;
import model.Noeud;
import model.Troncon;

import java.util.ArrayList;

public class VueFeuilleRoutePapier {

    private FeuilleRoute m_feuilleRoute;

    public VueFeuilleRoutePapier(FeuilleRoute feuilleRoute) {
        m_feuilleRoute = feuilleRoute;
    }

    public String getVersionPapier() {
        String r = new String();
        ArrayList<Troncon> troncons = m_feuilleRoute.getTroncons();
        int longueur = 0;
        Troncon t;
        //Sert à vérifier que l'on doit bien livrer au moment du passage à un point de livraison
        ArrayList<Noeud> nodesFeuilleRoute= m_feuilleRoute.getNodes();
        int indexNodeFeuilleRoute=0;

        r += "###########################################\n";
        r += "#  Version papier de la feuille de route  #\n";
        r += "###########################################\n\n";

        r += "- Effectuez le départ depuis l'entrepot.\n\n";

        for (int i = 0; i < troncons.size(); i++) {
            t = troncons.get(i);
            boolean dernierTroncon = (i == troncons.size() - 1);

            //verifie que le l'arrivée du troncon est bien un noeud de la liste des noeuds de la feuille de route.
            boolean pointDansNoeudsFeuille = (indexNodeFeuilleRoute < nodesFeuilleRoute.size() &&
                    nodesFeuilleRoute.get(indexNodeFeuilleRoute).getM_id()==t.getArrivee().getM_id()) ;

            /*si on change de rue, ou bien si on est au dernier Troncon, ou encore si on arrive à un point de livraison
            du chemin :    */
            if ( ((i<troncons.size()-1) && !troncons.get(i+1).getM_nom().equals(t.getM_nom())) || dernierTroncon ||
                    (i>0 && t.getArrivee().hasLivraison()) && pointDansNoeudsFeuille) {
                //mise à jour de la longueur
                longueur += t.getM_longueur();

                //on suit la rue jusqu'au bout, donc on affiche la distance calculée depuis que l'on est dans cette rue
                r += "  . Suivre la rue " +  t.getM_nom() + " sur " + longueur + " mètres.\n";
                longueur = 0;

                // On peut alors arriver à un noeud de livraison que l'on doit livrer ou à l'entrepot à la fin de la tournée
                if (t.getArrivee().hasLivraison() && pointDansNoeudsFeuille) {
                    r += "  * Réalisez la livraison " + t.getArrivee().getM_livraison().getId() + " adresse "+
                            t.getArrivee().getM_id()+"\n";
                    indexNodeFeuilleRoute++;
                    pointDansNoeudsFeuille = (indexNodeFeuilleRoute < nodesFeuilleRoute.size() &&
                            nodesFeuilleRoute.get(indexNodeFeuilleRoute).getM_id()==t.getArrivee().getM_id());

                }
                else if (t.getArrivee().isEntrepot() && pointDansNoeudsFeuille){
                    r += "  * Vous êtes de retour à l'entrepôt " + t.getArrivee().getM_id() + "\n";
                    indexNodeFeuilleRoute++;
                    pointDansNoeudsFeuille = (indexNodeFeuilleRoute < nodesFeuilleRoute.size() &&
                            nodesFeuilleRoute.get(indexNodeFeuilleRoute).getM_id()==t.getArrivee().getM_id());
                }

                // si nous ne sommes pas au dernier tronçon, on affiche la manoeuvre à effectuer au prochain noeud
                if (!dernierTroncon) {
                    Troncon t2 = troncons.get(i + 1);
                    double angle = t.angleAvec(t2);

                    if(t.getDepart()==t2.getArrivee()){
                            r+="  . Faites demi-tour \n";
                    }
                    else if (Math.abs(angle - Math.PI / 2)%(2*Math.PI) < 0.6) {
                            r += "  . Tournez à droite.\n";
                    }
                    else if (Math.abs(angle + Math.PI / 2)%(Math.PI) < 0.6) {
                            r += "  . Tournez à gauche.\n";
                    }


                }
            }//ENDIF si on n'est pas dans la même rue
            /*si on est toujours dans la même rue, pas dans un cas particulier, alors on ajoute la distance du troncon
             à celle calculée depuis le début de la rue */
            else{
                longueur += t.getM_longueur();
            }


        }

        r += "\n  - Fin de la tournée.\n";


        return r;
    }

}
