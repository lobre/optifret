package model;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Class DemandeLivraison
 */
public class DemandeLivraison {

    static public int PARSE_ERROR = -1;
    static public int PARSE_OK = 0;

    private Plan m_plan;
    private Noeud entrepot;
    private ArrayList<PlageHoraire> plagesHoraires;




    //
    // Constructors
    //
    public DemandeLivraison() {
        this.plagesHoraires = new ArrayList<PlageHoraire>();
    }


    //
    // Accessor methods
    //
    public Noeud getEntrepot() {
        return entrepot;
    }

    public ArrayList<PlageHoraire> getPlagesHoraires() {
        return plagesHoraires;
    }

    public void setEntrepot(Noeud entrepot) {
        this.entrepot = entrepot;
    }

    private void ajouterPlageH(PlageHoraire plage) {
        plagesHoraires.add(plage);
    }

    private void supprimerPlageH(PlageHoraire plage) {
        plagesHoraires.remove(plage);
    }

    public void setPlagesHoraires(ArrayList<PlageHoraire> plagesHoraires) {
        this.plagesHoraires = plagesHoraires;
    }

    public void setM_plan(Plan m_plan) {
        this.m_plan = m_plan;
    }

    //
    // Other methods
    //

    private int heureFromString(Heure heure, String string) {

        String[] parts = string.split(":");
        if (parts.length != 3) {
            return DemandeLivraison.PARSE_ERROR;
        }

        int h = Integer.parseInt(parts[0]);
        if (h < 0 || h > 23) {
            return DemandeLivraison.PARSE_ERROR;
        }
        heure.setM_heures(h);

        int m = Integer.parseInt(parts[1]);
        if (h < 0 || h > 59) {
            return DemandeLivraison.PARSE_ERROR;
        }
        heure.setM_minutes(m);

        int s = Integer.parseInt(parts[2]);
        if (h < 0 || h > 59) {
            return DemandeLivraison.PARSE_ERROR;
        }
        heure.setM_secondes(s);

        return DemandeLivraison.PARSE_OK;
    }

    public int fromXML(Element racineXML) {

        //récupération de l'entrepôt
        NodeList entre = racineXML.getElementsByTagName("Entrepot");
        if (entre.getLength() != 1) {
            return DemandeLivraison.PARSE_ERROR;
        }

        String ad = ((Element) entre.item(0)).getAttribute("adresse");
        this.entrepot = m_plan.getNoeudParID(Integer.parseInt(ad));
        if (entrepot == null) {
            return DemandeLivraison.PARSE_ERROR;
        }

        //récupération des plages horaires
        NodeList n_plages = racineXML.getElementsByTagName("PlagesHoraires");
        if (n_plages.getLength() != 1) {
            return DemandeLivraison.PARSE_ERROR;
        }

        NodeList liste_plages = racineXML.getElementsByTagName("Plage");

        for (int i = 0; i < liste_plages.getLength(); i++) {

            //nouvelle plage horaire
            Element e_plage = (Element) liste_plages.item(i);

            //récupération heures de début et de départ
            String h1 = e_plage.getAttribute("heureDebut");
            String h2 = e_plage.getAttribute("heureFin");
            Heure hDepart = new Heure();
            Heure hFin = new Heure();

            if (heureFromString(hDepart, h1) == PARSE_ERROR || heureFromString(hFin, h2) == PARSE_ERROR || !hDepart.estAvant(hFin)) {
                return DemandeLivraison.PARSE_ERROR;
            }
            PlageHoraire plage = new PlageHoraire(hDepart,hFin);

            //récupération des livraisons de la plage horaire
            NodeList livraisons = e_plage.getElementsByTagName("Livraisons");
            if (livraisons.getLength() != 1) {
                return PARSE_ERROR;
            }

            NodeList listeLivraisons = ((Element) livraisons.item(0)).getElementsByTagName("Livraison");
            for (int j = 0; j < listeLivraisons.getLength(); j++) {
                Element eLivraison = (Element) listeLivraisons.item(j);
                int id = Integer.parseInt(eLivraison.getAttribute("id"));
                int client = Integer.parseInt(eLivraison.getAttribute("client"));
                int adNoeud = Integer.parseInt(eLivraison.getAttribute("adresse"));
                Noeud noeud = m_plan.getNoeudParID(adNoeud);

                Livraison livraison = new Livraison(id,client,noeud);
                livraison.setLaPlage(plage);
                plage.addLesLivraisons(livraison);
            }
            this.ajouterPlageH(plage);
        }

        return Plan.PARSE_OK;

    }
}
