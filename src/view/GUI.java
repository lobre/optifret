package view;

import libs.ExampleFileFilter;
import model.DemandeLivraison;
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
    private JMenu m_menu;

    public static void main(String[] args) {
        // enable anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        new GUI(1000, 700);
    }

    public GUI(int largeur, int hauteur) {
        // Initialisation de la fenêtre
        m_frame = new JFrame("Application Optifret");
        m_frame.setContentPane(this.mainPanel);
        m_frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        m_frame.setSize(largeur, hauteur);

        // Redimensionnement de la zone de notification
        zoneNotification.setPreferredSize(new Dimension(800, 35));
        zoneNotification.setInfoMessage("Bienvenue sur l'application Optifret");

        // Création du menu
        creerMenus();

        // Affichage de la fenêtre
        m_frame.setVisible(true);



    }

    private void createUIComponents() {
        // Intialisation de la vu du Plan
        vuePlan = new VuePlan();
        vuePlan.repaint();

        // Initialisation de la zone de notification
        zoneNotification = new ZoneNotification();
        zoneNotification.setPreferredSize(new Dimension(800, 35));
    }

    private void creerMenus() {
        // Creation de deux menus, chaque menu ayant plusieurs items
        // et association d'un ecouteur d'action a chacun de ces items

        m_menu = new JMenu("Fichier");
        ActionListener actionChargerPlan = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chargerPlan();
            }
        };
        ajoutItem("Charger un plan (.xml)", m_menu, actionChargerPlan);

        ActionListener actionChargerDemandeLivraison = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chargerDemandeLivraison();
            }
        };
        ajoutItem("Charger une demande de livraison (.xml)", m_menu, actionChargerDemandeLivraison);

        // Désactive le menu "Charger une demande de livraison" par défaut
        m_menu.getItem(1).setEnabled(false);

        JMenuBar barreDeMenu = new JMenuBar();

        barreDeMenu.add(m_menu);
        m_frame.setJMenuBar(barreDeMenu);
    }

    private void ajoutItem(String intitule, JMenu menu, ActionListener a) {
        JMenuItem item = new JMenuItem(intitule);
        menu.add(item);
        item.addActionListener(a);
    }

    private File ouvrirFichier() {
        JFileChooser jFileChooserXML = new JFileChooser("./xml_data");
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("xml");
        filter.setDescription("Fichier XML");
        jFileChooserXML.setFileFilter(filter);
        jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (jFileChooserXML.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return new File(jFileChooserXML.getSelectedFile().getAbsolutePath());
        return null;
    }

    public Document lireDepuisXML(File fichierXML) {
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
        File fichierXML = ouvrirFichier();

        if (fichierXML == null) {
            zoneNotification.setErrorMessage("Aucun fichier selectionné");
            return;
        }

        Document doc = lireDepuisXML(fichierXML);

        Plan plan = new Plan();
        int status = plan.fromXML(doc.getDocumentElement());

        if (status != Plan.PARSE_OK) {
            zoneNotification.setErrorMessage("Erreur XML: impossible de parser le fichier sélectionné.");
            return;
        }

        vuePlan.setM_plan(plan);

        zoneNotification.setSuccessMessage("Le plan '" + fichierXML.getName() + "' a été chargé avec succès !");

        // Active le menu "Charger une demande de livraison"
        m_menu.getItem(1).setEnabled(true);
    }

    public void chargerDemandeLivraison() {

        if (vuePlan.getM_plan() == null) {
            zoneNotification.setErrorMessage("Veuillez d'abord charger un plan avant de charger une demande de livraison.");
            return;
        }

        File fichierXML = ouvrirFichier();

        if (fichierXML == null) {
            return;
        }

        Document doc = lireDepuisXML(fichierXML);

        DemandeLivraison demandeLivraison = new DemandeLivraison(vuePlan.getM_plan());
        int status = demandeLivraison.fromXML(doc.getDocumentElement());

        if (status != DemandeLivraison.PARSE_OK) {
            zoneNotification.setErrorMessage("Erreur: impossible de parser le fichier XML sélectionné.");
            return;
        }

        zoneNotification.setSuccessMessage("La demande de livraison  '" + fichierXML.getName() + "' a été chargée avec succès !");
        vuePlan.setM_demande_livraison(demandeLivraison);

    }

}
