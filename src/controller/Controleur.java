package controller;

import libs.ExampleFileFilter;
import model.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import view.FenetrePrincipale;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Controleur {

    private FenetrePrincipale m_window;

    private Plan m_plan;
    private DemandeLivraison m_demandeLivraison;
    private FeuilleRoute m_feuilleRoute;
    private HistoriqueCommandes m_commandes;


    // Point d'entrée de l'application:
    public static void main(String[] args) {
        new Controleur();
    }


    public Controleur() {
        m_plan = null;
        m_demandeLivraison = null;
        m_feuilleRoute = null;
        m_commandes = new HistoriqueCommandes();

        m_window = new FenetrePrincipale(this);
    }

    public FenetrePrincipale getM_window() {
        return m_window;
    }

    public DemandeLivraison getM_demandeLivraison() {
        return m_demandeLivraison;
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
        m_demandeLivraison = null;

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


        // Active l'action "Charger une demande de livraison"
        m_window.getM_menuFichier().getItem(1).setEnabled(true);
        // Désactive le menu "Édition"
        m_window.getM_menuEdition().setEnabled(false);
    }

    public void chargerDemandeLivraison() {

        if (m_plan == null) {
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
            m_plan.resetNoeuds();
            return;
        }

        m_window.getM_zoneNotification().setSuccessMessage("La demande de livraison  '" + fichierXML.getName() + "' a été chargée avec succès !");

        // Active le menu "Édition"
        m_window.getM_menuEdition().setEnabled(true);
        m_window.getM_vuePlan().resetTroncons();

        // Activer le bouton "Calculer feuille de route"
        m_window.getM_calculerButton().setEnabled(true);

        m_window.getM_vuePlan().repaint();

    }

    public void calculerFeuilleRoute() {
        if (m_demandeLivraison == null) {
            return;
        }
        // TODO : gérer les erreurs au calcul d'une feuille de route
        m_feuilleRoute = m_demandeLivraison.calculerFeuilleDeRoute();
        m_window.getM_vuePlan().setM_feuilleRoute(m_feuilleRoute);

        System.out.println("Chemins calculés : " + m_feuilleRoute.getChemins());
    }


    public void ajouterLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeAjout(m_demandeLivraison, livraison));

        // Annulation de la feuille de route (s'il y en avait une)
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setM_feuilleRoute(null);

        m_window.getM_vuePlan().repaint();
    }

    public void supprimerLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeSuppression(m_demandeLivraison, livraison));

        // Annulation de la feuille de route (s'il y en avait une)
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setM_feuilleRoute(null);

        m_window.getM_vuePlan().repaint();
    }

    public void reexecuter() {
        m_commandes.reexecuter();
        m_window.getM_vuePlan().repaint();
    }

    public void annuler() {
        m_commandes.annuler();
        m_window.getM_vuePlan().repaint();
    }

    public void showInfosLivraison(Livraison livraison) {
        m_window.showInfosLivraison(livraison);
    }

    public void showAjouterLivraison(Noeud noeud) {
        m_window.showAjouterLivraison(noeud);
    }

    public void hideSidebar() {
        m_window.hideSidebar();
    }
}
