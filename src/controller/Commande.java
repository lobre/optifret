package controller;

import model.DemandeLivraison;
import model.Livraison;

/**
 * Classe abstraite Commande: repr&eacute;sente une action sur le mod&egrave;le (impliquant une commande et une
 * demande de livraison) pouvant &ecirc;tre &eacute;x&eacute;cut&eacute;e et annul&eacute;e/r&eacute;tablie.
 */
abstract class Commande {

    protected DemandeLivraison m_demandeLivraison;
    protected Livraison m_livraison;

    /**
     * Constructeur d'une commande
     * @param demandeLivraison demande de livraison sur laquelle s'applique la commande
     * @param livraison livraison concern&eacute;e par la commande.
     */
    public Commande(DemandeLivraison demandeLivraison, Livraison livraison) {
        m_demandeLivraison = demandeLivraison;
        m_livraison = livraison;
    }


    public abstract void executer();

    public abstract void annuler();
}
