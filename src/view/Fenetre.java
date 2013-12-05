package view;

import libs.ExampleFileFilter;
import model.Plan;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Fenetre {

    private JFrame cadre;
    private JFileChooser jFileChooserXML;
    private VuePlan m_vuePlan;

    public static void main(String[] args) {
        new Fenetre(800, 600);
    }

    public Fenetre(int largeur, int hauteur) {
        // Creation d'un cadre contenant un menu, deux boutons,
        // un ecouteur de souris et une zone de dessin
        cadre = new JFrame("Optifret 2000");
        cadre.setLayout(null);
        cadre.setSize(largeur, hauteur);
        cadre.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Souris cliquee en x=" + e.getX() + " y=" + e.getY());
            }
        });
        creeMenus();
        creeBoutons(largeur, hauteur);
        m_vuePlan = new VuePlan(0, 0, largeur, hauteur - 80, Color.gray);
        cadre.getContentPane().add(m_vuePlan);
        m_vuePlan.repaint();
        cadre.setVisible(true);
    }

    private void creeMenus() {
        // Creation de deux menus, chaque menu ayant plusieurs items
        // et association d'un ecouteur d'action a chacun de ces items

        JMenu menuFichier = new JMenu("Fichier");
        ActionListener a5 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lireDepuisFichierXML();
            }
        };
        ajoutItem("Ouvrir un fichier XML", menuFichier, a5);


        JMenu menuCouleurs = new JMenu("Changer la couleur");
        ActionListener a1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_vuePlan.setM_couleurArrierePlan(Color.blue);
                m_vuePlan.repaint();
            }
        };
        ajoutItem("Bleu", menuCouleurs, a1);
        ActionListener a2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_vuePlan.setM_couleurArrierePlan(Color.red);
                m_vuePlan.repaint();
            }
        };
        ajoutItem("Rouge", menuCouleurs, a2);
        ActionListener a3 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_vuePlan.setM_couleurArrierePlan(Color.green);
                m_vuePlan.repaint();
            }
        };
        ajoutItem("Vert", menuCouleurs, a3);


        JMenuBar barreDeMenu = new JMenuBar();

        barreDeMenu.add(menuFichier);
        barreDeMenu.add(menuCouleurs);
        cadre.setJMenuBar(barreDeMenu);
    }

    private void ajoutItem(String intitule, JMenu menu, ActionListener a) {
        JMenuItem item = new JMenuItem(intitule);
        menu.add(item);
        item.addActionListener(a);
    }

    private void creeBoutons(int largeur, int hauteur) {
        // Creation de deux boutons et association d'un ecouteur d'action a chaque bouton
        ActionListener a1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_vuePlan.addBouleAleatoire();
                m_vuePlan.repaint();
            }
        };
        cadre.getContentPane().add(creeBouton("AjouterBoule", 0, hauteur - 80, largeur / 2, 40, a1));
        ActionListener a2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        cadre.getContentPane().add(creeBouton("Quitter", largeur / 2, hauteur - 80, largeur / 2, 40, a2));
    }

    private JButton creeBouton(String intitule, int x, int y, int largeur, int hauteur, ActionListener a) {
        JButton bouton = new JButton(intitule);
        bouton.setSize(largeur, hauteur);
        bouton.setLocation(x, y);
        bouton.addActionListener(a);
        return bouton;
    }

    public void lireDepuisFichierXML() {
        File fichierXML = ouvrirFichier();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(fichierXML);
            doc.getDocumentElement().normalize();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Plan plan = new Plan();
        int status = plan.fromXML(doc.getDocumentElement());

        if (status != Plan.PARSE_OK) {
            // TODO : afficher un message d'erreur
        }

        m_vuePlan.setM_plan(plan);
    }

    private File ouvrirFichier() {
        jFileChooserXML = new JFileChooser();
        // Note: source for ExampleFileFilter can be found in FileChooserDemo,
        // under the demo/jfc directory in the JDK.
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("xml");
        filter.setDescription("Fichier XML");
        jFileChooserXML.setFileFilter(filter);
        jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (jFileChooserXML.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return new File(jFileChooserXML.getSelectedFile().getAbsolutePath());
        return null;
    }

    private int ConstruireToutAPartirDeDOMXML(Element vueCadreDOMElement) {

           /*
            if (m_vuePlan.construireAPartirDeDOMXML(vueCadreDOMElement) != Dessin.PARSE_OK) {
	            return Dessin.PARSE_ERROR;
	        }
	        m_vuePlan.repaint();
	        */
        return 0; // Dessin.PARSE_OK
    }

}
