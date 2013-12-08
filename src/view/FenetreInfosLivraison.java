package view;

import model.Livraison;
import model.Noeud;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: ahmed
 * Date: 06/12/13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class FenetreInfosLivraison {
    private JTextField positionTextField;
    private JPanel mainPanel;
    private JTextField idTextField;
    private JLabel labelID;
    private JLabel labelPosition;
    private JTextField clientTextField;
    private JButton supprimerButton;
    private JButton fermerButton;
    private JLabel labelClient;
    private JLabel labelPlageHoraire;
    private JTextField plageTextField;

    private JFrame m_frame;
    private Noeud m_noeud;
    private Livraison m_livraison;

    public FenetreInfosLivraison(Livraison livraison) {

        m_livraison = livraison;
        m_noeud = m_livraison.getM_adresse();

        idTextField.setText(Integer.toString(m_noeud.getM_id()));
        positionTextField.setText(m_noeud.toString());
        clientTextField.setText(Integer.toString(m_noeud.getM_livraison().getM_noClient()));
        plageTextField.setText(m_livraison.getLaPlage().toString());

        // Initialisation de la fenêtre
        m_frame = new JFrame("Informations sur la livraison " + m_livraison.getM_id());
        m_frame.setContentPane(this.mainPanel);
        m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        m_frame.pack();

        // Bouton "fermer"
        fermerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_frame.dispose();
            }
        });

        // Bouton "Supprimer"
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO : Implémenter l'action "Supprimer noeud", et bouger ça niveau contrôleur
            }
        });

        m_frame.setVisible(true);

    }

}
