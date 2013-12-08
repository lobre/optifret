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
    private DemandeLivraison m_demandeLivraison;


    // Point d'entrée de l'application:
    public static void main(String[] args) {
        new Controller();
    }


    public Controller() {
        m_plan = null;
        m_demandeLivraison = null;

        m_window = new MainWindow(this);
        initListeners();
    }

    private void initListeners() {
        VuePlan vuePlan = m_window.getM_vuePlan();
        vuePlan.setBackground(new Color(46, 98, 255));
        // Mouse listeners
        vuePlan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);

                VuePlan vuePlan = m_window.getM_vuePlan();
                // Mise à jour de valeurs utiles pour le déplacement par "drag" de la vue
                vuePlan.setM_lastClick(MouseInfo.getPointerInfo().getLocation());
                vuePlan.setM_lastPosition(vuePlan.getLocation());

                if (m_demandeLivraison == null) {
                    return;
                }

                Noeud clickedNoeud = vuePlan.getClickedNoeud(e.getX(), e.getY());
                if (clickedNoeud != null) {
                    if (clickedNoeud.hasLivraison()) {
                        new FenetreInfosLivraison(clickedNoeud.getM_livraison());

                    } else if (!clickedNoeud.isM_entrepot()) {
                        new FenetreAjoutLivraison(clickedNoeud, m_demandeLivraison);
                    }
                }
            }
        });

        vuePlan.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                VuePlan vuePlan = m_window.getM_vuePlan();

                Point p = MouseInfo.getPointerInfo().getLocation();
                vuePlan.getM_lastPosition().getX();
                p.getX();
                vuePlan.getM_lastClick().getX();

                int x = (int) (vuePlan.getM_lastPosition().getX() + p.getX() - vuePlan.getM_lastClick().getX());
                int y = (int) (vuePlan.getM_lastPosition().getY() + p.getY() - vuePlan.getM_lastClick().getY());
                vuePlan.setLocation(x, y);
            }
        });

        vuePlan.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                VuePlan vuePlan = m_window.getM_vuePlan();
                vuePlan.setM_zoom(vuePlan.getM_zoom() * (1 - (float) e.getWheelRotation() / 10),e.getPoint());
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
            m_window.getM_zoneNotification().setErrorMessage("Impossible d'instantier le parseur XML");
        }

        try {
            Document doc = dBuilder.parse(fichierXML);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | NullPointerException e) {
            m_window.getM_zoneNotification().setErrorMessage("Impossible d'ouvrir le fichier XML demandé");
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
            m_window.getM_zoneNotification().setErrorMessage("Erreur: impossible de charger le plan demandé.");
            return;
        }

        m_window.getM_vuePlan().setM_plan(m_plan);

        m_window.getM_zoneNotification().setSuccessMessage("Le plan '" + fichierXML.getName() + "' a été chargé avec succès !");


        // Active le menu "Charger une demande de livraison"
        m_window.getM_menu().getItem(1).setEnabled(true);
    }

    public void chargerDemandeLivraison() {

        if (m_window.getM_vuePlan().getM_plan() == null) {
            m_window.getM_zoneNotification().setErrorMessage("Veuillez d'abord charger un plan avant de charger une demande de livraison.");
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
        m_demandeLivraison = new DemandeLivraison(m_window.getM_vuePlan().getM_plan());
        int status = m_demandeLivraison.fromXML(doc.getDocumentElement());

        if (status != DemandeLivraison.PARSE_OK) {
            m_window.getM_zoneNotification().setErrorMessage("Erreur: impossible de charger la demande de livraison demandée.");
            return;
        }

        m_window.getM_zoneNotification().setSuccessMessage("La demande de livraison  '" + fichierXML.getName() + "' a été chargée avec succès !");
        m_window.getM_vuePlan().repaint();

    }



}