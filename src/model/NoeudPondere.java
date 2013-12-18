package model;

/**
 * Created with IntelliJ IDEA.
 * User: karimalaoui
 * Date: 05/12/2013
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */

public class NoeudPondere {
    private final Noeud m_noeud;
    private Double m_poids;
    private Troncon m_rejointDepuis;

    //Constructor
    NoeudPondere(Noeud noeud) {
        this.m_noeud = noeud;
        this.m_poids = Double.POSITIVE_INFINITY;  //infini comme valeur par défaut, pour Dikjstra
    }

    //Getter-setter

    public void setPoids(Double m_poids) {
        this.m_poids = m_poids;
    }

    public int get_id() {
        return this.m_noeud.getId();
    }

    public Noeud getNoeud() {
        return m_noeud;
    }

    public Double getPoids() {
        return m_poids;
    }

    public void setRejointDepuis(Troncon m_rejointDepuis) {
        this.m_rejointDepuis = m_rejointDepuis;
    }

    public Troncon getRejointDepuis() {
        return m_rejointDepuis;
    }
}
