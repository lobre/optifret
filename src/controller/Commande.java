package controller;

import model.DemandeLivraison;
import model.Livraison;

public abstract class Commande {

    protected DemandeLivraison m_demandeLivraison;
    protected Livraison m_livraison;

    public Commande() {
    }

    public Commande(DemandeLivraison demandeLivraison, Livraison livraison) {
        m_demandeLivraison = demandeLivraison;
        m_livraison = livraison;
    }

    public abstract void executer();

    public abstract void annuler();
}
