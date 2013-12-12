package view;

import controller.Controleur;
import model.Livraison;
import model.Noeud;
import model.PlageHoraire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FenetrePrincipale {
    private JPanel m_mainPanel;
    private ZoneNotification m_zoneNotification;
    private JButton m_calculerButton;
    private VuePlan m_vuePlan;
    private JPanel planWrapper;
    private JPanel m_sidebar;
    private JLabel m_labelNumClient;
    private JLabel m_labelAdresse;
    private JLabel m_labelCoordonnees;
    private JLabel m_labelPlageHoraire;
    private JButton m_supprimerButton;
    private JPanel panelInfosLivraison;
    private JPanel panelAjouterLivraison;
    private JButton m_hideSidebarButton;
    private ZoneNotification m_notificationInfos;
    private JTextField m_clientTextField;
    private JComboBox m_plagesHoraires;
    private JLabel m_adresseNouvelleLivraison;
    private JButton m_ajouterButton;
    private ZoneNotification m_notificationAjout;
    private JFrame m_frame;

    private JMenu m_menuFichier;
    private JMenu m_menuEdition;

    private Controleur m_controleur;

    private Livraison m_selectedLivraison;
    private Noeud m_selectedNoeud;

    // Constructor(s)
    public FenetrePrincipale(Controleur controleur) {
        m_controleur = controleur;
        m_selectedLivraison = null;
        m_selectedNoeud = null;

        // Initialisation de la fenêtre
        m_frame = new JFrame("Application Optifret");
        m_frame.setContentPane(this.m_mainPanel);
        m_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        m_frame.setSize(1000, 700);
        m_frame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        // Redimensionnement de la zone de notification
        m_zoneNotification.setPreferredSize(new Dimension(800, 35));
        m_zoneNotification.setInfoMessage("Bienvenue sur l'application Optifret");

        // Création du menu
        creerMenus();

        m_frame.getContentPane().setBackground(VuePlan.COULEUR_BACKGROUND);
        planWrapper.setBackground(VuePlan.COULEUR_BACKGROUND);

        initListeners();

        // Style du bouton "Cacher"
        m_hideSidebarButton.setBackground(Color.white);
        m_hideSidebarButton.setContentAreaFilled(false);
        m_hideSidebarButton.setBorderPainted(false);

        // Sidebar cachée par défaut
        hideSidebar();

        // Affichage de la fenêtre
        m_frame.setVisible(true);

    }

    // Appelé à la création de l'interface
    private void createUIComponents() {
        // Intialisation de la vu du Plan
        m_vuePlan = new VuePlan(m_controleur);
        m_vuePlan.repaint();
    }

    private void initListeners() {

        m_frame.setFocusable(true);

        m_frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    m_controleur.chargerPlan();
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    m_controleur.chargerDemandeLivraison();
                } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                    m_controleur.annuler();
                } else if (e.getKeyCode() == KeyEvent.VK_Y) {
                    m_controleur.reexecuter();
                }
            }

        });
        // Pour adapter la map au redimensionnement de la fenêtre


        // Bouton "Supprimer"
        m_supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.supprimerLivraison(m_selectedLivraison);
                hideSidebar();
                m_frame.repaint();
            }
        });

        // Bouton qui cache la sidebar
        m_hideSidebarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideSidebar();
                m_frame.repaint();
            }
        });

        m_calculerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.calculerFeuilleRoute();
                m_frame.repaint();
            }
        });
        m_sidebar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
               m_vuePlan.centerMapOnSelected();
            }
            @Override
            public void componentHidden(ComponentEvent e) {
               m_vuePlan.centerMapOnSelected();
            }
        });
    }

    // Accessors
    public ZoneNotification getM_zoneNotification() {
        return m_zoneNotification;
    }

    public VuePlan getM_vuePlan() {
        return m_vuePlan;
    }

    public JMenu getM_menuFichier() {
        return m_menuFichier;
    }

    public JMenu getM_menuEdition() {
        return m_menuEdition;
    }

    public JButton getM_calculerButton() {
        return m_calculerButton;
    }

    // Other methods
    private void creerMenus() {
        //// Menu "Fichier"
        m_menuFichier = new JMenu("Fichier");

        // Action "Charger un plan"
        ActionListener actionChargerPlan = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_controleur.chargerPlan();
            }
        };
        ajoutItem("Charger un plan (CTRL + P)", m_menuFichier, actionChargerPlan);

        // Action "Charger une demande de livraison"
        ActionListener actionChargerDemandeLivraison = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_controleur.chargerDemandeLivraison();
            }
        };
        ajoutItem("Charger une demande de livraison (CTRL + D)", m_menuFichier, actionChargerDemandeLivraison);

        // Action "Éditer feuille de route papier"
        ActionListener actionVersionPapier = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_controleur.editerFeuilleRoutePapier();
            }
        };
        ajoutItem("Éditer feuille de route 'papier'", m_menuFichier, actionVersionPapier);

        // Désactive les menu "Charger une demande de livraison" et "Éditer feuille de route papier" par défaut
        m_menuFichier.getItem(1).setEnabled(false);
        m_menuFichier.getItem(2).setEnabled(false);


        // Action "Fermer"
        ActionListener actionFermerProgramme = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_frame.dispose();
            }
        };
        ajoutItem("Fermer", m_menuFichier, actionFermerProgramme);

        //// Menu "Édition"
        m_menuEdition = new JMenu("Édition");

        // Action "Défaire"
        ActionListener actionDefaire = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_controleur.annuler();
            }
        };
        ajoutItem("Défaire (CTRL + Z)", m_menuEdition, actionDefaire);

        // Action "Rétablir"
        ActionListener actionRetablir = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_controleur.reexecuter();
            }
        };
        ajoutItem("Rétablir (CTRL + Y)", m_menuEdition, actionRetablir);

        // Menu "Édition" désactivé par défaut
        m_menuEdition.setEnabled(false);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(m_menuFichier);
        menuBar.add(m_menuEdition);
        m_frame.setJMenuBar(menuBar);
    }


    private void ajoutItem(String intitule, JMenu menu, ActionListener a) {
        JMenuItem item = new JMenuItem(intitule);
        menu.add(item);
        item.addActionListener(a);
    }

    public void showInfosLivraison(Livraison livraison) {
        m_selectedLivraison = livraison;

        // Mise à jour des informations
        m_notificationInfos.setInfoMessage("Informations sur la livraison n°" + m_selectedLivraison.getM_id());
        m_labelNumClient.setText(Integer.toString(livraison.getM_noClient()));
        m_labelAdresse.setText(Integer.toString(livraison.getM_adresse().getM_id()));
        m_labelCoordonnees.setText(livraison.getM_adresse().toString());
        m_labelPlageHoraire.setText(livraison.getM_plage().toString());

        panelInfosLivraison.setVisible(true);
        panelAjouterLivraison.setVisible(false);
        m_sidebar.setVisible(true);
    }

    public void showAjouterLivraison(Noeud noeud) {
        panelInfosLivraison.setVisible(false);
        panelAjouterLivraison.setVisible(true);

        m_selectedNoeud = noeud;
        m_plagesHoraires.removeAllItems();
        for (PlageHoraire ph : m_controleur.getM_demandeLivraison().getM_plagesHoraires()) {
            m_plagesHoraires.addItem(ph.toString());
        }

        m_adresseNouvelleLivraison.setText(Integer.toString(noeud.getM_id()));

        // Zone de notification
        m_notificationAjout.setInfoMessage("Ajout d'une livraison");

        // Bouton "Ajouter"
        m_ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PlageHoraire ph = m_controleur.getM_demandeLivraison().getM_plagesHoraires().get(m_plagesHoraires.getSelectedIndex());
                    int client = Integer.parseInt(m_clientTextField.getText());
                    Livraison livraison = new Livraison(m_controleur.getM_demandeLivraison().getUniqueID(), client, m_selectedNoeud, ph);
                    m_controleur.ajouterLivraison(livraison);
                    hideSidebar();
                } catch (NumberFormatException exception) {
                    m_notificationAjout.setErrorMessage("N° client invalide !");
                }
            }
        });

        m_sidebar.setVisible(true);


    }

    public void hideSidebar() {
        m_sidebar.setVisible(false);
    }

    // Getters/Setters
    public JFrame getM_frame() {
        return m_frame;
    }


}
