package controller;

import libs.ExampleFileFilter;
import model.DemandeLivraison;
import model.Noeud;
import model.Plan;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import view.FenetreAjoutLivraison;
import view.FenetreInfosLivraison;
import view.MainWindow;
import view.VuePlan;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Controller {

    private MainWindow m_window;

    private Plan m_plan;
    private DemandeLivraison m_demande_livraison;


    // Point d'entrée de l'application:
    public static void main(String[] args) {
        new Controller();
    }


    public Controller() {
        m_plan = null;
        m_demande_livraison = null;

        m_window = new MainWindow(this);
        initListeners();
    }

    private void initListeners() {
        VuePlan vue_plan = m_window.getVuePlan();

        // Mouse listeners
        vue_plan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);

                VuePlan vue_plan = m_window.getVuePlan();
                // Mise à jour de valeurs utiles pour le déplacement par "drag" de la vue
                vue_plan.setM_last_click(MouseInfo.getPointerInfo().getLocation());
                vue_plan.setM_last_position(vue_plan.getLocation());

                if (m_demande_livraison == null) {
                    return;
                }

                Noeud clickedNoeud = vue_plan.getClickedNoeud(e.getX(), e.getY());
                if (clickedNoeud != null) {
                    if (clickedNoeud.hasLivraison()) {
                        new FenetreInfosLivraison(clickedNoeud.getM_livraison());

                    } else if (!clickedNoeud.isM_entrepot()) {
                        new FenetreAjoutLivraison(clickedNoeud, m_demande_livraison);
                    }
                }
            }
        });

        vue_plan.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                VuePlan vue_plan = m_window.getVuePlan();

                Point p = MouseInfo.getPointerInfo().getLocation();
                vue_plan.getM_last_position().getX();
                p.getX();
                vue_plan.getM_last_click().getX();

                int x = (int) (vue_plan.getM_last_position().getX() + p.getX() - vue_plan.getM_last_click().getX());
                int y = (int) (vue_plan.getM_last_position().getY() + p.getY() - vue_plan.getM_last_click().getY());
                vue_plan.setLocation(x, y);
            }
        });

        vue_plan.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                VuePlan vue_plan = m_window.getVuePlan();
                vue_plan.setM_zoom(vue_plan.getM_zoom() * (1 - (float) e.getWheelRotation() / 10));
            }
        });

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

    private Document lireDepuisXML(File fichierXML) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            m_window.getZoneNotification().setErrorMessage("Impossible d'instantier le parseur XML");
        }

        try {
            Document doc = dBuilder.parse(fichierXML);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | NullPointerException e) {
            m_window.getZoneNotification().setErrorMessage("Impossible d'ouvrir le fichier XML demandé");
            return null;
        }
    }

    public void chargerPlan() {
        File fichierXML = ouvrirFichier();

        if (fichierXML == null) {
            return;
        }

        Document doc = lireDepuisXML(fichierXML);

        m_plan = new Plan();
        int status = m_plan.fromXML(doc.getDocumentElement());

        if (status != Plan.PARSE_OK) {
            m_window.getZoneNotification().setErrorMessage("Erreur: impossible de charger le plan demandé.");
            return;
        }

        m_window.getVuePlan().setM_plan(m_plan);

        m_window.getZoneNotification().setSuccessMessage("Le plan '" + fichierXML.getName() + "' a été chargé avec succès !");


        // Active le menu "Charger une demande de livraison"
        m_window.getM_menu().getItem(1).setEnabled(true);
    }

    public void chargerDemandeLivraison() {

        if (m_window.getVuePlan().getM_plan() == null) {
            m_window.getZoneNotification().setErrorMessage("Veuillez d'abord charger un plan avant de charger une demande de livraison.");
            return;
        }

        File fichierXML = ouvrirFichier();
        if (fichierXML == null) {
            return;
        }
        Document doc = lireDepuisXML(fichierXML);

        // On réinitialise l'état des noeuds (livraison liée, entrepot ou pas)
        m_plan.resetNoeuds();

        // On parse la demande de livraison
        m_demande_livraison = new DemandeLivraison(m_window.getVuePlan().getM_plan());
        int status = m_demande_livraison.fromXML(doc.getDocumentElement());

        if (status != DemandeLivraison.PARSE_OK) {
            m_window.getZoneNotification().setErrorMessage("Erreur: impossible de charger la demande de livraison demandée.");
            return;
        }

        m_window.getZoneNotification().setSuccessMessage("La demande de livraison  '" + fichierXML.getName() + "' a été chargée avec succès !");
        m_window.getVuePlan().repaint();

    }



}
