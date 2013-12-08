package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow {
    private JPanel mainPanel;
    private ZoneNotification zoneNotification;
    private JButton calculerButton;
    private VuePlan vuePlan;
    private JFrame m_frame;
    private JMenu m_menu;

    private Controller m_controller;

    // Constructor(s)
    public MainWindow(Controller controller) {
        m_controller = controller;

        // Initialisation de la fenêtre
        m_frame = new JFrame("Application Optifret");
        m_frame.setContentPane(this.mainPanel);
        m_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        m_frame.setSize(1000, 700);

        // Redimensionnement de la zone de notification
        zoneNotification.setPreferredSize(new Dimension(800, 35));
        zoneNotification.setInfoMessage("Bienvenue sur l'application Optifret");

        // Création du menu
        creerMenus();

        // Affichage de la fenêtre
        m_frame.setVisible(true);
    }

    // Appelé à la création de l'interface
    private void createUIComponents() {
        // Intialisation de la vu du Plan
        vuePlan = new VuePlan();
        vuePlan.repaint();

        // Initialisation de la zone de notification
        zoneNotification = new ZoneNotification();
        zoneNotification.setPreferredSize(new Dimension(800, 35));
    }


    // Accessors
    public ZoneNotification getZoneNotification() {
        return zoneNotification;
    }
    public VuePlan getVuePlan() {
        return vuePlan;
    }
    public JMenu getM_menu() {
        return m_menu;
    }
    public JButton getCalculerButton() {
        return calculerButton;
    }


    // Other methods
    private void creerMenus() {
        // Creation de deux menus, chaque menu ayant plusieurs items
        // et association d'un ecouteur d'action a chacun de ces items

        m_menu = new JMenu("Fichier");
        ActionListener actionChargerPlan = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_controller.chargerPlan();
            }
        };
        ajoutItem("Charger un plan (.xml)", m_menu, actionChargerPlan);

        ActionListener actionChargerDemandeLivraison = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_controller.chargerDemandeLivraison();
            }
        };
        ajoutItem("Charger une demande de livraison (.xml)", m_menu, actionChargerDemandeLivraison);

        // Désactive le menu "Charger une demande de livraison" par défaut
        m_menu.getItem(1).setEnabled(false);

        JMenuBar barreDeMenu = new JMenuBar();

        barreDeMenu.add(m_menu);
        m_frame.setJMenuBar(barreDeMenu);
    }

    private void ajoutItem(String intitule, JMenu menu, ActionListener a) {
        JMenuItem item = new JMenuItem(intitule);
        menu.add(item);
        item.addActionListener(a);
    }

}
