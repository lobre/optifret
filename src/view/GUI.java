package view;

import libs.ExampleFileFilter;
import model.Noeud;
import model.Plan;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GUI {
    private JPanel mainPanel;
    private ZoneNotification zoneNotification;
    private JButton calculerButton;
    private VuePlan vuePlan;
    private JFrame m_frame;

    public static void main(String[] args) {
        new GUI(1000, 700);
    }

    public GUI(int largeur, int hauteur) {

        // Initialisation de la fenêtre
        m_frame = new JFrame("Application Optifret");
        m_frame.setContentPane(this.mainPanel);
        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_frame.setSize(largeur, hauteur);

        // Mouse listener
        vuePlan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("Mouse pressed at : (" + e.getX() + ", " + e.getY() + ")");
                Noeud clickedNoeud = vuePlan.getClickedNoeud(e.getX(), e.getY());
                if (clickedNoeud != null) {
                    System.out.println("Clicked on noeud: " + clickedNoeud.getM_id());
                }
            }
        });
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            // TODO : retravailler cette partie
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.println("Mouse dragged to : (" + e.getX() + ", " + e.getY() + ")");
                vuePlan.setLocation(e.getX() - vuePlan.getWidth() / 2, e.getY() - vuePlan.getHeight() / 2);
            }
        });

        // Redimensionnement de la zone de notification
        zoneNotification.setPreferredSize(new Dimension(800, 35));

        // Création du menu
        creerMenus();

        // Affichage de la fenêtre
        m_frame.setVisible(true);

    }

    private void createUIComponents() {
        // Intialisation de la vu du Plan
        vuePlan = new VuePlan(800, 600);
        vuePlan.repaint();

        // Initialisation de la zone de notification
        zoneNotification = new ZoneNotification();
        zoneNotification.setInfoMessage("Bienvenue sur l'appliation Optifret !");
        zoneNotification.setPreferredSize(new Dimension(800, 35));
    }

    private void creerMenus() {
        // Creation de deux menus, chaque menu ayant plusieurs items
        // et association d'un ecouteur d'action a chacun de ces items

        JMenu menuFichier = new JMenu("Fichier");
        ActionListener actionChargerPlan = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chargerPlan();
            }
        };
        ajoutItem("Charger un plan (.xml)", menuFichier, actionChargerPlan);

        ActionListener actionChargerDemandeLivraison = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chargerDemandeLivraison();
            }
        };
        ajoutItem("Charger une demande de livraison (.xml)", menuFichier, actionChargerDemandeLivraison);

        JMenuBar barreDeMenu = new JMenuBar();

        barreDeMenu.add(menuFichier);
        m_frame.setJMenuBar(barreDeMenu);
    }

    private void ajoutItem(String intitule, JMenu menu, ActionListener a) {
        JMenuItem item = new JMenuItem(intitule);
        menu.add(item);
        item.addActionListener(a);
    }

    private File ouvrirFichier() {
        JFileChooser jFileChooserXML = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("xml");
        filter.setDescription("Fichier XML");
        jFileChooserXML.setFileFilter(filter);
        jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (jFileChooserXML.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return new File(jFileChooserXML.getSelectedFile().getAbsolutePath());
        return null;
    }

    public Document lireDepuisXML() {
        File fichierXML = ouvrirFichier();

        if (fichierXML == null) {
            zoneNotification.setErrorMessage("Aucun fichier selectionné");
            return null;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            zoneNotification.setErrorMessage("Impossible d'instantier le parseur XML");
        }

        try {
            Document doc = dBuilder.parse(fichierXML);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | NullPointerException e) {
            zoneNotification.setErrorMessage("Impossible d'ouvrir le fichier XML demandé");
            return null;
        }
    }

    public void chargerPlan() {
        Document doc = lireDepuisXML();

        Plan plan = new Plan();
        int status = plan.fromXML(doc.getDocumentElement());

        if (status != Plan.PARSE_OK) {
            zoneNotification.setErrorMessage("Erreur XML: impossible de parser le fichier choisi.");
            return;
        }

        vuePlan.setM_plan(plan);
    }

    public void chargerDemandeLivraison() {
        // TODO : Intégrer le parseur de demande de livraison
    }


}
