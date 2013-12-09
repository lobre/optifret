package view;

import controller.Controleur;
import model.Livraison;
import model.Noeud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreInfosLivraison {
    private JTextField m_positionTextField;
    private JPanel m_mainPanel;
    private JTextField m_idTextField;
    private JLabel m_labelID;
    private JLabel m_labelPosition;
    private JTextField m_clientTextField;
    private JButton m_supprimerButton;
    private JButton m_fermerButton;
    private JLabel m_labelClient;
    private JLabel m_labelPlageHoraire;
    private JTextField m_plageTextField;
    private ZoneNotification m_zoneNotification;

    private JFrame m_frame;
    private Noeud m_noeud;
    private Livraison m_livraison;
    private Controleur m_controleur;

    public FenetreInfosLivraison(Livraison livraison, Controleur controleur) {
        m_controleur = controleur;
        m_livraison = livraison;
        m_noeud = m_livraison.getM_adresse();

        m_zoneNotification.setPreferredSize(new Dimension(500, 35));
        m_zoneNotification.setInfoMessage("Informations sur la livraison n°" + m_livraison.getM_id());

        m_idTextField.setText(Integer.toString(m_noeud.getM_id()));
        m_positionTextField.setText(m_noeud.toString());
        m_clientTextField.setText(Integer.toString(m_noeud.getM_livraison().getM_noClient()));
        m_plageTextField.setText(m_livraison.getM_plage().toString());

        // Initialisation de la fenêtre
        m_frame = new JFrame("Informations sur la livraison " + m_livraison.getM_id());
        m_frame.setContentPane(this.m_mainPanel);
        m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        m_frame.pack();

        // Bouton "fermer"
        m_fermerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_frame.dispose();
            }
        });

        // Bouton "Supprimer"
        m_supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.supprimerLivraison(m_livraison);
                m_frame.dispose();
            }
        });

        m_frame.setVisible(true);
        m_frame.setResizable(false);


    }

}
