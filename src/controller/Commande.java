package controller;

import model.DemandeLivraison;
import model.Livraison;

abstract class Commande {

    protected DemandeLivraison m_demandeLivraison;
    protected Livraison m_livraison;

    public Commande(DemandeLivraison demandeLivraison, Livraison livraison) {
        m_demandeLivraison = demandeLivraison;
        m_livraison = livraison;
    }

    public abstract void executer();

    public abstract void annuler();
}
