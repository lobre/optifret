package view;

import controller.Controleur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Barre d'outil procurant un acc&egrave;s rapide aux fonctionnalit&eacute;s de l'applications:
 * Charger un plan, Charger une demande de livraison, Calculer une feuille de route, Imprimer une feuille de route
 * et annuler/r&eacute;executer une action.
 * @see view.BoutonBarreOutils
 */
public class BarreOutils extends JToolBar {

    /**
     * Contr&ocirc;leur de l'application.
     */
    private Controleur m_controleur;

    /**
     * Constructeur de la barre d'outil. Ajoute tous les boutons de cette derni&egrave;re et leur donne une action.
     * @param controleur le contr&ocirc;leur de l'application.
     */
    public BarreOutils(Controleur controleur) {
        m_controleur = controleur;
        setAutoscrolls(false);
        setRollover(false);

        // Bouton plan
        BoutonBarreOutils button = new BoutonBarreOutils ("plan.png", "Charger plan");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.chargerPlan();
            }
        });
        add(button);

        // Bouton charger demande de livraison
        button = new BoutonBarreOutils ("livraison.png", "Charger demande de livraison");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.chargerDemandeLivraison();
            }
        });
        add(button);

        // Bouton calculer feuille de route
        button = new BoutonBarreOutils ("itinerary.png", "Calculer feuille de route");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.calculerFeuilleRoute();
            }
        });
        add(button);

        // Bouton éditer version papier de feuille de route
        button = new BoutonBarreOutils ("print.png", "Imprimer feuille de route");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.editerFeuilleRoutePapier();
            }
        });
        add(button);

        // Bouton undo
        button = new BoutonBarreOutils ("undo.png", "Annuler");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.annuler();
            }
        });
        add(button);

        // Bouton redo
        button = new BoutonBarreOutils ("redo.png", "Rétablir");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.reexecuter();
            }
        });
        add(button);

    }

}