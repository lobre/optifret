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

/**
 * Classe Controleur
 */
public class Controleur {

    /**
     * Interface de l'application
     */
    private FenetrePrincipale m_window;

    /**
     * Plan de la zone géographique courante
     */
    private Plan m_plan;

    /**
     * Demande de livraison courante
     */
    private DemandeLivraison m_demandeLivraison;

    /**
     * Dernière feuille de route calculée
     */
    private FeuilleRoute m_feuilleRoute;

    /**
     * Ensemble des commandes exécutées qui peuvent être annulées ou réexécutées
     */
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

    /**
     * Constructeur de la classe
     */
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

    /**
     * Permet de choisir un fichier et en récupère le chemin
     * @return une instance de File correspondante au fichier choisi
     */
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
        } else {
            return null;
        }
    }

    /**
     * Instancie un parseur xml et récupère la balise racine d'en fichier
     * @param fichierXML fichier d'extension 'xml' dont le contenu est récupéré
     * @return racine du fichier xml
     */
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

    /**
     * à partir du fichier choisi par l'utilisateur, instancie un nouveau plan de la zone géographique,
     * c'est-à-dire les noeuds et les tronçons la composant, et rend possible, dans la suite, le chargement
     * d'une demande de livraisons. Si le fichier choisi est invalide (erreurs de syntaxes, éléments manquants,
     * ect) un message d'erreur est affiché
     */
    public void chargerPlan() {
        try {
            m_demandeLivraison = null;

            File fichierXML = ouvrirFichier();

            if (fichierXML == null) {
                return;
            }

            Document doc = lireDepuisXML(fichierXML);

            m_plan = new Plan();
            m_plan.fromXML(doc.getDocumentElement());


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
        } catch (ParseXmlException e) {
            m_window.getM_zoneNotification().setErrorMessage("Erreur: impossible de charger le plan demandé. Cause : "
                    + e.getMessage());
        }
    }

    /**
     * A partir du fichier choisi par l'utilisateur, instancie une nouvelle demande de livraisons, ainsi que
     * les plages horaires et livraisons la composant, associe les livraisons aux noeuds correspondants et
     * repère l'adresse de l'entrepôt. Rend aussi possible, dans la suite, le calcul d'une feuille de route.
     * Pourque l'analyse soit faite, un plan de la zone où seront effectuées les livraisons doit être
     * précedemment chargé. Si le fichier choisi est invalide (erreurs de syntaxes, éléments manquants,
     * ect) un message d'erreur est affiché
     */
    public void chargerDemandeLivraison() {
        try {
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
            m_demandeLivraison.fromXML(doc.getDocumentElement());


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
        } catch (ParseXmlException e) {
            m_window.getM_zoneNotification().setErrorMessage("Erreur: impossible de charger la demande de livraison" +
                    " demandée. Cause : " + e.getMessage());
            m_plan.resetNoeuds();
        }
    }

    /**
     * Calcule la tournée la plus efficace pour l'ensemble de livraisons du jour et la zone courants et
     * rend possible, dans la suite, l'édition en format papier de la feuille de route.
     * Pourque l'analyse soit faite, un plan et une demande de livraisons doivent être chargés.
     */
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

    /**
     * Affiche en console la dernière tournée calculée
     */
    public void editerFeuilleRoutePapier() {
        if (m_feuilleRoute == null) {
            throw new IllegalStateException("Aucune feuille de route n'est chargée.");
        }
        // TODO => Ajouter des choses ici (afficher une fenêtre, etc.)
        VueFeuilleRoutePapier feuilleRoutePapier = new VueFeuilleRoutePapier(m_feuilleRoute);
        System.out.println(feuilleRoutePapier.getVersionPapier());
    }

    /**
     * Ajoute une livraison à la demande de livraison courante et, le cas écheant, élimine la
     * feuille de route calculée
     * @param livraison à rajouter
     */
    public void ajouterLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeAjout(m_demandeLivraison, livraison));

        // Annulation de la feuille de route (s'il y en avait une)
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setM_feuilleRoute(null);
    }

    /**
     * Supprime une livraison à la demande de livraison courante et, le cas écheant, élimine la
     * feuille de route calculée
     * @param livraison
     */
    public void supprimerLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeSuppression(m_demandeLivraison, livraison));

        // Annulation de la feuille de route (s'il y en avait une)
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setM_feuilleRoute(null);
    }

    /**
     * Permet d'exécuter la dernière commande annulée et met à jour le panel du plan
     */
    public void reexecuter() {
        m_commandes.reexecuter();
        m_window.getM_vuePlan().repaint();
    }

    /**
     * Permet de annuler la dernière commande exécutée et met à jour le panel du plan
     */
    public void annuler() {
        m_commandes.annuler();
        m_window.getM_vuePlan().repaint();
    }

    /**
     * Affiche le panel contenant les informations d'une livraison
     * @param livraison dont les informations vont être affichées
     */
    public void showInfosLivraison(Livraison livraison) {
        m_window.showInfosLivraison(livraison);
    }

    /**
     * Affiche le panel d'ajout d'une nouvelle livraison
     * @param noeud auquel la nouvelle livraison va être associée
     */
    public void showAjouterLivraison(Noeud noeud) {
        m_window.showAjouterLivraison(noeud);
    }

    /**
     * Ferme la barre latérale droite contenant les informations de la livraison ou du noeud
     * sélectionnés
     */
    public void hideSidebar() {
        m_window.hideSidebar();
    }
}
