package controller;

import model.DemandeLivraison;
import model.Livraison;

/**
 * Commande de suppression d'une livraison.
 */
public class CommandeSuppression extends Commande {

    public CommandeSuppression(DemandeLivraison demandeLivraison, Livraison livraison) {
        super(demandeLivraison, livraison);
    }

    @Override
    public void executer() {
        m_demandeLivraison.supprimerLivraison(m_livraison);
    }

    @Override
    public void annuler() {
        m_demandeLivraison.ajouterLivraison(m_livraison);
    }
}
