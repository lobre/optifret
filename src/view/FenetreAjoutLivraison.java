package view;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import model.DemandeLivraison;
import model.Noeud;
import model.PlageHoraire;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: nightwish
 * Date: 07/12/13
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public class FenetreAjoutLivraison {
    Noeud m_noeud;
    JFrame m_frame;

    private JTextField numéroClientTextField;
    private JTextField adresseDeLivraisonTextField;
    private JComboBox plageHoraireComboBox;
    private JButton ajouterLivraisonButton;
    private JButton annulerButton;
    private JPanel addPanel;

    public FenetreAjoutLivraison(Noeud noeud, DemandeLivraison demande) {
    m_noeud = noeud;

    StringArray listePlagesHoraire= new StringArray();
    for (PlageHoraire p :demande.getPlagesHoraires())
    {
        System.out.print(p.toString());
         plageHoraireComboBox.addItem(p.toString());
    }

    adresseDeLivraisonTextField.setText(Integer.toString(m_noeud.getM_id()));



    // Initialisation de la fenêtre
    m_frame = new JFrame("Ajouter une livraison");
    m_frame.setContentPane(this.addPanel);
    m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    m_frame.pack();   //?

    // Bouton "fermer"
        annulerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            m_frame.dispose();
        }
    });

    // Bouton "Supprimer"
        ajouterLivraisonButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO : Implémenter l'action "Ajouter noeud", via le contrôleur
        }
    });

    m_frame.setVisible(true);

    }

}
