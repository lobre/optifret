package view;

import controller.Controleur;
import model.Livraison;
import model.Noeud;
import model.PlageHoraire;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;



/**
 * Fen&ecirc;tre principale de l'application: contient les menus, le composant VuePlan et une sidebar pour
 * l'ajout/suppression de livraison
 */
public class FenetrePrincipale {
    private JPanel m_mainPanel;
    private ZoneNotification m_zoneNotification;
    private VuePlan m_vuePlan;
    private JPanel m_planWrapper;
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
    private JLabel m_horaireLivraison;
    private BarreOutils m_barreOutils;
    private JPanel m_topWrapper;


    /**
     * Menu "Fichier"
     */
    private JMenu m_menuFichier;
    /**
     * Menu "&Eacute;dition"
     */
    private JMenu m_menuEdition;

    /**
     * Fen&ecirc;tre Swing contenant l'interface de l'application.
     */
    private JFrame m_frame;

    /**
     * Contr&ocirc;leur de l'application.
     * @see controller.Controleur
     */
    private Controleur m_controleur;

    /**
     * La livraison actuellement s&eacute;lectionn&eacute;e (pour la sidebar InfosLivraison)
     */
    private Livraison m_selectedLivraison;

    /**
     * Le noeud actuellement s&eacute;lectionn&eacute; (pour la sidebar AjouterLivraison)
     */
    private Noeud m_selectedNoeud;


    /**
     * Constructeur de la fen&ecirc;tre principale.
     * @param controleur contr&ocirc;leur de l'application
     */
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
        m_planWrapper.setBackground(VuePlan.COULEUR_BACKGROUND);

        initListeners();

        // Style du bouton "Cacher"
        m_hideSidebarButton.setContentAreaFilled(true);
        m_hideSidebarButton.setBackground(VuePlan.COULEUR_BACKGROUND);
        // Sidebar cachée par défaut
        hideSidebar();

        // Affichage de la fenêtre
        m_frame.setVisible(true);
    }

    /**
     * Cr&eacute;ation des composants personnalis&eacute;s
     */
     private void createUIComponents() {
        // Intialisation de la vu du Plan
        m_vuePlan = new VuePlan(m_controleur);
        m_barreOutils = new BarreOutils(m_controleur);
    }

    /**
     * Initialisation des listeners de la fen&ecirc;tre (raccourcis clavier, actions des boutons, ...)
     */
    private void initListeners() {
        m_frame.setFocusable(true);

        m_frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P && e.isControlDown()) {
                    m_controleur.chargerPlan();
                } else if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    m_controleur.chargerDemandeLivraison();
                } else if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
                    m_controleur.annuler();
                } else if (e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
                    m_controleur.reexecuter();
                }
            }

        });

        // Bouton "Supprimer"
        m_supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.supprimerLivraison(m_selectedLivraison);
                hideSidebar();
                m_frame.repaint();
            }
        });

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

        // Bouton qui cache la sidebar
        m_hideSidebarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideSidebar();
                m_frame.requestFocus();
                m_frame.repaint();
            }
        });

           /*
        m_frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                m_vuePlan.centerMapOnSelected();
            }
        });

        m_sidebar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                m_vuePlan.centerMapOnSelected();
            }
           @Override
        public void componentHidden(ComponentEvent e){
                m_vuePlan.centerMapOnSelected();
            }
        });*/
        //numéroclient checker
        m_clientTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void changed(){
                if (m_clientTextField.getText().equals(""))
                {
                    m_ajouterButton.setEnabled(false);
                }
                else{
                    m_ajouterButton.setEnabled(true);
                }
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

    // Other methods

    /**
     * Initialisation des menus de la fen&ecirc;tre principale (Menu "Fichier", menu "&Eacute;dition" et leurs
     * sous-&eacute;l&eacute;ments)
     */
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

        // Action "Éditer feuille de route"
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

    /**
     * Active une barre lat&eacute;rale affichant des informations sur une livraison, ainsi qu'un bouton permettant de
     * supprimer cette derni&egrave;re.
     * @param livraison la livraison dont les informations doivent &ecirc;tre affich&eacute;es
     */
    public void showInfosLivraison(Livraison livraison) {
        m_selectedLivraison = livraison;

        // Mise à jour des informations
        String id = Integer.toString(m_selectedLivraison.getId());
        String plage = m_selectedLivraison.getPlage().toString();

        m_notificationInfos.setInfoMessage(String.format("Livraison n°%s sur la plage %s", id, plage));
        m_labelNumClient.setText(Integer.toString(livraison.getNoClient()));
        m_labelAdresse.setText(Integer.toString(livraison.getAdresse().getM_id()));
        m_labelCoordonnees.setText(livraison.getAdresse().toString());
        m_labelPlageHoraire.setText(livraison.getPlage().toString());
        if (livraison.getHeureLivraison() != null) {
            m_horaireLivraison.setText(livraison.getHeureLivraison().toString());
        }
        else {
            m_horaireLivraison.setText(" - ");
        }

        panelInfosLivraison.setVisible(true);
        panelAjouterLivraison.setVisible(false);

        m_sidebar.setVisible(true);
    }

    /**
     * Active une barre lat&eacute;rale affichant des informations sur un noeud et permettant &agrave; l'utilisateur d'y ajouter
     * une livraison en choisissant num&eacute;ro de client et plage horaire parmi celles existantes dans la demande de
     * livraison en cours.
     * @param noeud le noeud o&ugrave; doit &ecirc;tre ajout&eacute;e la livraison saisie par l'utilisateur
     */
    public void showAjouterLivraison(Noeud noeud) {

        m_selectedNoeud = noeud;
        m_plagesHoraires.removeAllItems();
        for (PlageHoraire ph : m_controleur.getM_demandeLivraison().getM_plagesHoraires()) {
            m_plagesHoraires.addItem(ph.toString());
        }

        m_adresseNouvelleLivraison.setText(Integer.toString(noeud.getM_id()));

        // Zone de notification
        m_notificationAjout.setInfoMessage("Ajout d'une livraison");

        panelAjouterLivraison.setVisible(true);
        panelInfosLivraison.setVisible(false);

        m_sidebar.setVisible(true);
    }

    /**
     * Cache la barre lat&eacute;rale.
     */
    public void hideSidebar() {
        m_sidebar.setVisible(false);
        m_vuePlan.unselectNoeud();
    }

    // Getters/Setters
    public JFrame getM_frame() {
        return m_frame;
    }
}
