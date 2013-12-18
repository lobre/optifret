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
                    nodesFeuilleRoute.get(indexNodeFeuilleRoute).getId()==t.getArrivee().getId()) ;

            /*si on change de rue, ou bien si on est au dernier Troncon, ou encore si on arrive à un point de livraison
            du chemin :    */
            if ( ((i<troncons.size()-1) && !troncons.get(i+1).getNom().equals(t.getNom())) || dernierTroncon ||
                    (i>0 && t.getArrivee().hasLivraison()) && pointDansNoeudsFeuille) {
                //mise à jour de la longueur
                longueur += t.getLongueur();

                //on suit la rue jusqu'au bout, donc on affiche la distance calculée depuis que l'on est dans cette rue
                r += "  . Suivre la rue " +  t.getNom() + " sur " + longueur + " mètres.\n";
                longueur = 0;

                // On peut alors arriver à un noeud de livraison que l'on doit livrer ou à l'entrepot à la fin de la tournée
                if (t.getArrivee().hasLivraison() && pointDansNoeudsFeuille) {
                    r += " \n*****\n** " + t.getArrivee().getLivraison().getHeureLivraison().toString()+ " \n** Réalisez la livraison " + t.getArrivee().getLivraison().getId() + " adresse "+
                            t.getArrivee().getId() +"\n** Client "+t.getArrivee().getLivraison().getNoClient() +"  \n***** \n \n";
                    indexNodeFeuilleRoute++;
                }
                else if (t.getArrivee().isEntrepot() && pointDansNoeudsFeuille){
                    r += "  * Vous êtes de retour à l'entrepôt " + t.getArrivee().getId() + "\n";
                    indexNodeFeuilleRoute++;
                }

                // si nous ne sommes pas au dernier tronçon, on affiche la manoeuvre à effectuer au prochain noeud
                if (!dernierTroncon) {
                    Troncon t2 = troncons.get(i + 1);
                    double angle = t.angleAvec(t2);
                    if(t.getDepart()==t2.getArrivee()){
                            r+="  . Faites demi-tour \n";
                    }
                    else if ((Math.cos(angle) < 0.5) &&((Math.sin(angle))>0)) {

                            r += "  . Tournez à droite.\n";
                    }
                    else if ((Math.cos(angle) < 0.7) &&((Math.sin(angle))>0)) {

                            r += "  . Tournez légèrement à droite.\n";
                    }
                    else if ((Math.cos(angle) < 0.5) &&((Math.sin(angle))<0)) {

                            r += "  . Tournez  à gauche.\n";
                    }
                    else if ((Math.cos(angle) < 0.7) &&((Math.sin(angle))<0)) {

                            r += "  . Tournez légèrement à gauche.\n";
                    }


                }
            }//ENDIF si on n'est pas dans la même rue
            /*si on est toujours dans la même rue, pas dans un cas particulier, alors on ajoute la distance du troncon
             à celle calculée depuis le début de la rue */
            else{
                longueur += t.getLongueur();
            }


        }

        r += "\n  - Fin de la tournée.\n";


        return r;
    }

}
