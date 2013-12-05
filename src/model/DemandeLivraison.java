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
    protected Noeud m_entrepot;
    protected ArrayList<PlageHoraire> m_plagesHoraires;


    //
    // Constructors
    //
    public DemandeLivraison() {
    }

    //
    // Accessor methods
    //
    public Noeud getM_Entrepot() {
        return m_entrepot;
    }
    public void setM_entrepot(Noeud entrepot) {
        m_entrepot = entrepot;
    }


    public ArrayList<PlageHoraire> getM_plagesHoraires() {
        return m_plagesHoraires;
    }

    public void setM_plagesHoraires(ArrayList<PlageHoraire> plagesHoraires) {
        m_plagesHoraires = plagesHoraires;
    }


    private void ajouterPlageH(PlageHoraire plage) {
        m_plagesHoraires.add(plage);
    }

    private void supprimerPlageH(PlageHoraire plage) {
        m_plagesHoraires.remove(plage);
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
        m_entrepot = m_plan.getNoeudParID(Integer.parseInt(adresse));

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

        return DemandeLivraison.PARSE_OK;

    }
}
