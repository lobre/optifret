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
        System.out.println("m_derniere_commande = " + m_derniere_commande);
        if (m_derniere_commande < 0) {
            return;
        }

        m_commandes.get(m_derniere_commande).annuler();
        System.out.println("Vient d'annuler l'action : " + m_commandes.get(m_derniere_commande).getClass());
        m_derniere_commande -= 1;
    }

    public void reexecuter() {
        if ((m_derniere_commande == m_commandes.size() - 1) || m_derniere_commande == -1) {
            return;
        }
        m_derniere_commande += 1;
        m_commandes.get(m_derniere_commande).executer();
    }
}
