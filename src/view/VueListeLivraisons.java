package view;

import controller.Controleur;
import model.Livraison;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by ahmed on 18/12/13.
 */
public class VueListeLivraisons extends JScrollPane{

    private DefaultListModel m_listModel;
    private JList m_jListe;
    private ArrayList<Livraison> m_livraisons;
    private Controleur m_controleur;
    private boolean m_vide;

    public VueListeLivraisons(Controleur controleur) {
        super();

        m_vide = true;

        m_controleur = controleur;

        m_listModel = new DefaultListModel();
        m_listModel.addElement("Aucune livraison n'est chargée");

        m_jListe = new JList(m_listModel);
        m_jListe.setForeground(Color.BLACK);
        m_jListe.setVisibleRowCount(-1);

        getViewport().setView(m_jListe);

        m_livraisons = new ArrayList<>();

        m_jListe.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (m_vide) {
                    return;
                }
                JList list = (JList)evt.getSource();
                int index = list.locationToIndex(evt.getPoint());
                m_controleur.selectionLivraison(m_livraisons.get(index));
            }
        });
    }

    public void setLivraisons(ArrayList<Livraison> livraisons) {
        m_vide = false;
        m_livraisons = livraisons;
        m_listModel.clear();

        for (final Livraison livraison : m_livraisons) {
            String repr = String.format("Livraison %s | Client %s", Integer.toString(livraison.getId()),
                    Integer.toString(livraison.getNoClient()));
            m_listModel.addElement(repr);
        }
    }

    public void raz() {
        m_vide = true;
        m_listModel.clear();
        m_listModel.addElement("Aucune livraison n'est chargée");

    }

}
