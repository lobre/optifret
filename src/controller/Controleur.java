package controller;

import libs.CustomFilenameFilter;
import libs.ParseXmlException;
import model.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import view.FenetrePrincipale;
import view.VueFeuilleRoutePapier;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Controleur {

    private FenetrePrincipale m_window;

    private Plan m_plan;
    private DemandeLivraison m_demandeLivraison;
    private FeuilleRoute m_feuilleRoute;
    private HistoriqueCommandes m_commandes;


    // Point d'entrée de l'application:
    public static void main(String[] args) {
        // Paramètre Swing pour utiliser une apparence native

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Interface native non gérée, fallback sur l'interface Swing par défaut.");
        }

        new Controleur();
    }


    public Controleur() {
        m_plan = null;
        m_demandeLivraison = null;
        m_feuilleRoute = null;
        m_commandes = new HistoriqueCommandes();

        m_window = new FenetrePrincipale(this);
    }

    public DemandeLivraison getM_demandeLivraison() {
        return m_demandeLivraison;
    }

    private File ouvrirFichier() {
        FileDialog fileDialog = new FileDialog(m_window.getM_frame());
        fileDialog.setModal(true);

        FilenameFilter filter = new CustomFilenameFilter(".xml");
        fileDialog.setFilenameFilter(filter);

        fileDialog.setDirectory("xml_data");
        fileDialog.setAlwaysOnTop(true);

        fileDialog.setVisible(true);

        String filename = fileDialog.getFile();
        if (filename != null) {
            return new File(fileDialog.getDirectory(), fileDialog.getFile());
        }
        else {
            return null;
        }
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
        // Désactive le bouton "Calculer feuille de route"
        m_window.getM_calculerButton().setEnabled(false);
        // Désactive l'action "Éditer version papier"
        m_window.getM_menuFichier().getItem(2).setEnabled(false);
    }

    public void chargerDemandeLivraison() {
        try{
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
        int status;

        status = m_demandeLivraison.fromXML(doc.getDocumentElement());


        m_window.getM_zoneNotification().setSuccessMessage("La demande de livraison  '" + fichierXML.getName() + "' a été chargée avec succès !");

        // Désactive l'action "Éditer version papier" et annule la feuille de route
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setM_feuilleRoute(null);
        m_window.getM_menuFichier().getItem(2).setEnabled(false);

        // Active le menu "Édition"
        m_window.getM_menuEdition().setEnabled(true);

        // Activer le bouton "Calculer feuille de route"
        m_window.getM_calculerButton().setEnabled(true);

        // Réinitialise les vues troncons
        m_window.getM_vuePlan().resetTroncons();

        m_window.getM_vuePlan().repaint();
        }
        catch(ParseXmlException e){
                m_window.getM_zoneNotification().setErrorMessage("Erreur: impossible de charger la demande de livraison" +
                        " demandée. Cause : "+e.getMessage());
                m_plan.resetNoeuds();
                return;
        }
    }

    public void calculerFeuilleRoute() {
        if (m_demandeLivraison == null) {
            return;
        }
        // TODO : gérer les erreurs au calcul d'une feuille de route
        m_feuilleRoute = m_demandeLivraison.calculerFeuilleDeRoute();
        m_window.getM_vuePlan().setM_feuilleRoute(m_feuilleRoute);

        m_window.getM_zoneNotification().setSuccessMessage("Feuille de route calculée avec succès !");

        // Active l'action "Éditer version papier"
        m_window.getM_menuFichier().getItem(2).setEnabled(true);

        System.out.println("Chemins calculés : " + m_feuilleRoute.getChemins());
    }

    public void editerFeuilleRoutePapier() {
        if (m_feuilleRoute == null) {
            throw new IllegalStateException("Aucune feuille de route n'est chargée.");
        }
        // TODO => Ajouter des choses ici (afficher une fenêtre, etc.)
        VueFeuilleRoutePapier feuilleRoutePapier = new VueFeuilleRoutePapier(m_feuilleRoute);
        System.out.println(feuilleRoutePapier.getVersionPapier());
    }


    public void ajouterLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeAjout(m_demandeLivraison, livraison));

        // Annulation de la feuille de route (s'il y en avait une)
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setM_feuilleRoute(null);
    }

    public void supprimerLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeSuppression(m_demandeLivraison, livraison));

        // Annulation de la feuille de route (s'il y en avait une)
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setM_feuilleRoute(null);
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
