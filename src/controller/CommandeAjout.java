package controller;

import model.DemandeLivraison;
import model.Livraison;

public class CommandeAjout extends Commande {

    public CommandeAjout(DemandeLivraison demandeLivraison, Livraison livraison) {
        super(demandeLivraison, livraison);
    }

    @Override
    public void executer() {
        m_demandeLivraison.ajouterLivraison(m_livraison);
    }

    @Override
    public void annuler() {
        m_demandeLivraison.supprimerLivraison(m_livraison);
    }
}
