package view;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import model.DemandeLivraison;
import model.Noeud;
import model.PlageHoraire;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

    private JTextField m_numéroClientTextField;
    private JTextField m_adresseDeLivraisonTextField;
    private JComboBox m_plageHoraireComboBox;
    private JButton m_ajouterLivraisonButton;
    private JButton m_annulerButton;
    private JPanel m_addPanel;

    public FenetreAjoutLivraison(Noeud noeud, DemandeLivraison demande) {
    m_noeud = noeud;

    StringArray listePlagesHoraire= new StringArray();
    for (PlageHoraire p :demande.getPlagesHoraires())
    {
        System.out.print(p.toString());
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
                // TODO : Implémenter l'action "Ajouter noeud", via le contrôleur
            }
        });

    m_frame.setVisible(true);

    }

}
