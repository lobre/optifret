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

    protected Plan m_plan;
    protected Noeud entrepot;
    protected ArrayList<PlageHoraire> plagesHoraires;




    //
    // Constructors
    //
    public DemandeLivraison() {

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
    //
    // Other methods
    //
    public int fromXML(Element racineXML) {

        NodeList entre = racineXML.getElementsByTagName("Entrepot");
        if (entre.getLength() != 1) {
            return DemandeLivraison.PARSE_ERROR;
        }

        String adresse = ((Element) entre.item(0)).getAttribute("adresse");
        entrepot = m_plan.getNoeudParID(Integer.parseInt(adresse));

        NodeList n_plages = racineXML.getElementsByTagName("PlagesHoraires");
        if (n_plages.getLength() != 1) {
            return DemandeLivraison.PARSE_ERROR;
        }

        NodeList liste_plages = racineXML.getElementsByTagName("Plage");

        for (int i = 0; i < liste_plages.getLength(); i++) {
            Element noeud_xml = (Element) liste_plages.item(i);
            PlageHoraire plage = new PlageHoraire();

            //à terminer...

            this.ajouterPlageH(plage);
        }



        return Plan.PARSE_OK;

    }
}
