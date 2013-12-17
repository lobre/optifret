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
        // Si on a annulé quelques commandes avant d'exécuter la commande
        // On enlève ces dernières de la liste des commandes
        if (m_derniere_commande != -1 && m_derniere_commande < m_commandes.size() - 1) {
            m_commandes = new ArrayList<Commande>(m_commandes.subList(0, m_derniere_commande + 1));
        }

        commande.executer();
        m_commandes.add(commande);
        m_derniere_commande += 1;
    }

    // Annule la dernière commande exécutée
    public boolean annuler() {
        if (m_derniere_commande < 0) {
            return false;
        }

        m_commandes.get(m_derniere_commande).annuler();
        m_derniere_commande -= 1;
        return true;
    }

    // Réexécute la dernière commande annulée
    public boolean reexecuter() {
        if (m_commandes.size() == 0 || (m_derniere_commande == m_commandes.size() - 1)) {
            return false;
        }
        m_derniere_commande += 1;
        m_commandes.get(m_derniere_commande).executer();
        return true;
    }

}
