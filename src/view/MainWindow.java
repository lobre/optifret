package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow {
    private JPanel m_mainPanel;
    private ZoneNotification m_zoneNotification;
    private JButton m_calculerButton;
    private VuePlan m_vuePlan;
    private JFrame m_frame;
    private JMenu m_menu;

    private Controller m_controller;

    // Constructor(s)
    public MainWindow(Controller controller) {
        m_controller = controller;

        // Initialisation de la fenêtre
        m_frame = new JFrame("Application Optifret");
        m_frame.setContentPane(this.m_mainPanel);
        m_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        m_frame.setSize(1000, 700);

        // Redimensionnement de la zone de notification
        m_zoneNotification.setPreferredSize(new Dimension(800, 35));
        m_zoneNotification.setInfoMessage("Bienvenue sur l'application Optifret");

        // Création du menu
        creerMenus();

        // Affichage de la fenêtre
        m_frame.setVisible(true);
    }

    // Appelé à la création de l'interface
    private void createUIComponents() {
        // Intialisation de la vu du Plan
        m_vuePlan = new VuePlan();
        m_vuePlan.repaint();

        // Initialisation de la zone de notification
        m_zoneNotification = new ZoneNotification();
        m_zoneNotification.setPreferredSize(new Dimension(800, 35));
    }


    // Accessors
    public ZoneNotification getM_zoneNotification() {
        return m_zoneNotification;
    }
    public VuePlan getM_vuePlan() {
        return m_vuePlan;
    }
    public JMenu getM_menu() {
        return m_menu;
    }
    public JButton getM_calculerButton() {
        return m_calculerButton;
    }


    // Other methods
    private void creerMenus() {
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
