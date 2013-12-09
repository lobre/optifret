package view;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import controller.Controleur;
import model.DemandeLivraison;
import model.Livraison;
import model.Noeud;
import model.PlageHoraire;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

    private JTextField m_numéroClientTextField;
    private JTextField m_adresseDeLivraisonTextField;
    private JComboBox m_plageHoraireComboBox;
    private JButton m_ajouterLivraisonButton;
    private JButton m_annulerButton;
    private JPanel m_addPanel;

    private Controleur m_controleur;
    private DemandeLivraison m_demandeLivraison;

    public FenetreAjoutLivraison(Noeud noeud, DemandeLivraison demandeLivraison, Controleur controleur) {
    m_noeud = noeud;
    m_demandeLivraison = demandeLivraison;
    m_controleur = controleur;

    for (PlageHoraire p :m_demandeLivraison.getM_plagesHoraires())
    {
         m_plageHoraireComboBox.addItem(p.toString());
    }

    m_adresseDeLivraisonTextField.setText(Integer.toString(m_noeud.getM_id()));



    // Initialisation de la fenêtre
    m_frame = new JFrame("Ajouter une livraison");
    m_frame.setContentPane(this.m_addPanel);
    m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    m_frame.pack();   //?

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
            public void changed(){
                if (m_numéroClientTextField.getText().equals(""))
                {
                    m_ajouterLivraisonButton.setEnabled(false);
                }
                else{
                    m_ajouterLivraisonButton.setEnabled(true);
                }
            }
        });

    // Bouton "fermer"
        m_annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_frame.dispose();
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
