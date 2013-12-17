package controller;

import java.util.ArrayList;

/**
 * Permet de g&eacute;rer des commandes: les executer, les annuler, les r&eacute;eexcuter.
 * Garde un historique des derni&egrave;res commandes ex&eacute;cut&eacute;es.
 * @see Commande
 */
public class HistoriqueCommandes {

    /**
     * Liste des derni&egrave;res commandes ex&eacute;cut&eacute;es
     */
    private ArrayList<Commande> m_commandes;
    /**
     * Index de la derni&egrave;re commande ex&eacute;cut&eacute;e
     */
    private int m_derniere_commande;

    public HistoriqueCommandes() {
        m_commandes = new ArrayList<Commande>();
        m_derniere_commande = -1;
    }

    /**
     * Ex&eacute;cute une commande et l'ajoute &agrave; l'historique. Si des commandes ont &eacute;t&eacute;
     * annul&eacute;es auparavant, elles sont enlev&eacute;es de l'historique.
     * @param commande la commande &agrave; ex&eacute;cuter
     */
    public void executer(Commande commande) {
        // Si on a annulé quelques commandes avant d'exécuter la commande
        // On enlève ces dernières de la liste des commandes
        if (m_derniere_commande != -1 && m_derniere_commande < m_commandes.size() - 1) {
            m_commandes = new ArrayList<Commande>(m_commandes.subList(0, m_derniere_commande + 1));
        }

        commande.executer();
        m_commandes.add(commande);
        m_derniere_commande += 1;
    }

    /**
     * Annule la derni&egrave;re commande ex&eacute;cut&eacute;e (s'il y en a une)
     * @return true si une commande a &eacute;t&eacute; annul&eacute;e, false sinon.
     */
    public boolean annuler() {
        if (m_derniere_commande < 0) {
            return false;
        }

        m_commandes.get(m_derniere_commande).annuler();
        m_derniere_commande -= 1;
        return true;
    }

    /**
     * R&eacute;execute la derni&egrave;re commande annul&eacute;e, s'il y en a une.
     * @return true si une commande a &eacute;t&eacute; annul&eacute;e, false sinon.
     */
    public boolean reexecuter() {
        if (m_commandes.size() == 0 || (m_derniere_commande == m_commandes.size() - 1)) {
            return false;
        }

        m_derniere_commande += 1;
        m_commandes.get(m_derniere_commande).executer();
        return true;
    }

}
