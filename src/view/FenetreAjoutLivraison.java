package view;

import controller.Controleur;
import model.DemandeLivraison;
import model.Livraison;
import model.Noeud;
import model.PlageHoraire;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreAjoutLivraison {
    Noeud m_noeud;
    JDialog m_dialog;

    private JTextField m_numéroClientTextField;
    private JTextField m_adresseDeLivraisonTextField;
    private JComboBox m_plageHoraireComboBox;
    private JButton m_ajouterLivraisonButton;
    private JButton m_annulerButton;
    private JPanel m_addPanel;
    private ZoneNotification m_inputNotification;

    private Controleur m_controleur;
    private DemandeLivraison m_demandeLivraison;

    public FenetreAjoutLivraison(Noeud noeud, DemandeLivraison demandeLivraison, Controleur controleur) {
        m_noeud = noeud;
        m_demandeLivraison = demandeLivraison;
        m_controleur = controleur;

        for (PlageHoraire p : m_demandeLivraison.getM_plagesHoraires()) {
            m_plageHoraireComboBox.addItem(p.toString());
        }

        m_adresseDeLivraisonTextField.setText(Integer.toString(m_noeud.getM_id()));

        // Zone de notification
        m_inputNotification.setVisible(true);
        //m_inputNotification.setPreferredSize(new Dimension(500, 35));
        m_inputNotification.setInfoMessage("Veuillez entrer les informations relatives à la livraison");

        // Initialisation de la fenêtre
        m_dialog = new JDialog(m_controleur.getM_window().getM_frame(), "Ajouter une livraison", Dialog.ModalityType.APPLICATION_MODAL);
        m_dialog.setContentPane(this.m_addPanel);
        m_dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        m_dialog.pack();

        //numéroclient checker
        m_numéroClientTextField.getDocument().addDocumentListener(new DocumentListener() {
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

            public void changed() {
                if (m_numéroClientTextField.getText().equals("")) {
                    m_ajouterLivraisonButton.setEnabled(false);
                } else {
                    m_ajouterLivraisonButton.setEnabled(true);
                }
            }
        });

        // Bouton "fermer"
        m_annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_dialog.dispose();
            }
        });

        // Bouton "Supprimer"
        m_ajouterLivraisonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PlageHoraire ph = m_demandeLivraison.getM_plagesHoraires().get(m_plageHoraireComboBox.getSelectedIndex());
                    int client = Integer.parseInt(m_numéroClientTextField.getText());
                    Livraison livraison = new Livraison(m_demandeLivraison.getUniqueID(), client, m_noeud, ph);
                    m_controleur.ajouterLivraison(livraison);
                    m_dialog.dispose();
                } catch (NumberFormatException exception) {
                    m_inputNotification.setVisible(true);
                    m_inputNotification.setErrorMessage("N° client invalide !");
                }

            }
        });

        m_dialog.setVisible(true);
        m_dialog.setResizable(false);
    }

    private void createUIComponents() {
        m_inputNotification = new ZoneNotification();
    }
}
