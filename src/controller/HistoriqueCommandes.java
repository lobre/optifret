package controller;

import java.util.ArrayList;

public class HistoriqueCommandes {

    private ArrayList<Commande> m_commandes;
    private int m_derniere_commande;

    public HistoriqueCommandes() {
        m_commandes = new ArrayList<Commande>();
        m_derniere_commande = -1;
    }

    public void executer(Commande commande) {
        // TODO : Vérifier si on garde bien les commandes qu'il faut / S'il y a une meilleure méthode
        if (m_derniere_commande != -1 && m_derniere_commande < m_commandes.size()) {
            m_commandes = new ArrayList<Commande>(m_commandes.subList(0, m_derniere_commande));
        }

        commande.executer();
        m_commandes.add(commande);
        m_derniere_commande += 1;
    }

    public void annuler() {
        if (m_derniere_commande < 0) {
            return;
        }

        m_commandes.get(m_derniere_commande).annuler();
        m_derniere_commande -= 1;
    }
}
