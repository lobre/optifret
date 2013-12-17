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

    public void setM_poids(Double m_poids) {
        this.m_poids = m_poids;
    }

    public int get_id() {
        return this.m_noeud.getM_id();
    }

    public Noeud getM_noeud() {
        return m_noeud;
    }

    public Double getM_poids() {
        return m_poids;
    }

    public void setM_rejointDepuis(Troncon m_rejointDepuis) {
        this.m_rejointDepuis = m_rejointDepuis;
    }

    public Troncon getM_rejointDepuis() {
        return m_rejointDepuis;
    }
}
