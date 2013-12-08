package view;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import controller.Controleur;
import model.DemandeLivraison;
import model.Livraison;
import model.Noeud;
import model.PlageHoraire;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

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

    private Controleur m_controleur;
    private DemandeLivraison m_demandeLivraison;

    public FenetreAjoutLivraison(Noeud noeud, DemandeLivraison demandeLivraison, Controleur controleur) {
    m_noeud = noeud;
    m_demandeLivraison = demandeLivraison;
    m_controleur = controleur;

    for (PlageHoraire p :m_demandeLivraison.getM_plagesHoraires())
    {
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
            try {
                PlageHoraire ph = m_demandeLivraison.getM_plagesHoraires().get(plageHoraireComboBox.getSelectedIndex());
                int client = Integer.parseInt(numéroClientTextField.getText());
                Livraison livraison = new Livraison(m_demandeLivraison.getUniqueID(), client, m_noeud, ph);
                m_controleur.ajouterLivraison(livraison);
                m_frame.dispose();
            }
            catch (NumberFormatException exception) {
                exception.printStackTrace();
                // TODO : add a "ZoneNotification" in the window, write alerts in it
            }

        }
    });

    m_frame.setVisible(true);

    }

}
