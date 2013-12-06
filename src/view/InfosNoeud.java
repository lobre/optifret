package view;

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
public class InfosNoeud {
    private JTextField positionTextField;
    private JPanel mainPanel;
    private JTextField idTextField;
    private JLabel labelID;
    private JLabel labelPosition;
    private JTextField clientTextField;
    private JButton supprimerButton;
    private JButton fermerButton;
    private JLabel labelClient;

    private JFrame m_frame;
    private Noeud m_noeud;

    public InfosNoeud(Noeud noeud) {

        m_noeud = noeud;

        idTextField.setText(Integer.toString(m_noeud.getM_id()));
        positionTextField.setText("(" + m_noeud.getM_x() + ", " + m_noeud.getM_y() + ")");

        // TODO : Une fois les livraisons intégrées, ce test sera inutile (seuls les noeuds avec livraison doivent être affichés)
        if (m_noeud.getM_livraison() == null) {
            clientTextField.setVisible(false);
            labelClient.setVisible(false);
        }
        else {
            clientTextField.setText(Integer.toString(m_noeud.getM_livraison().getM_noClient()));
        }

        // Initialisation de la fenêtre
        m_frame = new JFrame("Informations: Noeud " + m_noeud.getM_id());
        m_frame.setContentPane(this.mainPanel);
        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                // TODO : Implémenter l'action "Supprimer noeud", via le contrôleur
            }
        });

        m_frame.setVisible(true);



    }

    public static void main(String[] args) {
        Noeud noeud = new Noeud(20, 32, 40);

        new InfosNoeud(noeud);
    }

}
