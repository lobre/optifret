package controller;

import libs.CustomFilenameFilter;
import libs.ParseXmlException;
import model.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import view.FenetreImprimerFeuilleRoute;
import view.FenetrePrincipale;
import view.VueFeuilleRoutePapier;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;

/**
 * Classe Controleur
 */
public class Controleur {

    private static final int DUREE_CALCUL = 3000;
    private static final String FEUILLES_ROUTE_FOLDER = "feuilles_route";
    private static final String XML_DATA_FOLDER = "xml_data";
    /**
     * Interface de l'application
     */
    private FenetrePrincipale m_window;

    /**
     * Plan de la zone g&eacute;ographique courante
     */
    private Plan m_plan;

    /**
     * Demande de livraison courante
     */
    private DemandeLivraison m_demandeLivraison;

    /**
     * Derni&egrave;re feuille de route calcul&eacute;e
     */
    private FeuilleRoute m_feuilleRoute;

    /**
     * Ensemble des commandes ex&eacute;cut&eacute;es qui peuvent &ecirc;tre annul&eacute;es ou r&eacute;ex&eacute;cut&eacute;es
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
     * Permet de choisir un fichier et en r&eacute;cup&egrave;re le chemin
     *
     * @return une instance de File correspondante au fichier choisi
     */
    private File ouvrirFichier() {
        FileDialog fileDialog = new FileDialog(m_window.getM_frame());
        fileDialog.setModal(true);

        FilenameFilter filter = new CustomFilenameFilter(".xml");
        fileDialog.setFilenameFilter(filter);

        fileDialog.setDirectory(XML_DATA_FOLDER);
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
     * Instancie un parseur xml et r&eacute;cup&egrave;re la balise racine d'en fichier
     *
     * @param fichierXML fichier d'extension 'xml' dont le contenu est r&eacute;cup&eacute;r&eacute;
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
     * A partir du fichier choisi par l'utilisateur, instancie un nouveau plan de la zone g&eacute;ographique,
     * c'est-&agrave;-dire les noeuds et les tron&ccedil;ons la composant, et rend possible, dans la suite, le chargement
     * d'une demande de livraisons. Si le fichier choisi est invalide (erreurs de syntaxes, &eacute;l&eacute;ments manquants,
     * ect) un message d'erreur est affich&eacute;
     */
    public void chargerPlan() {
        try {
            m_demandeLivraison = null;
            annulerFeuilleRoute();
            updateListeLivraisons();

            File fichierXML = ouvrirFichier();

            if (fichierXML == null) {
                return;
            }

            Document doc = lireDepuisXML(fichierXML);

            m_plan = new Plan();
            m_plan.fromXML(doc.getDocumentElement());

            m_window.getM_vuePlan().setPlan(m_plan);
            m_window.getM_zoneNotification().setSuccessMessage("Le plan '" + fichierXML.getName() + "' a été chargé avec succès !");

            // Active l'action "Charger une demande de livraison"
            m_window.getM_menuFichier().getItem(1).setEnabled(true);
            // Désactive le menu "Édition"
            m_window.getM_menuEdition().setEnabled(false);
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
     * rep&egrave;re l'adresse de l'entrep&ocirc;t. Rend aussi possible, dans la suite, le calcul d'une feuille de route.
     * Pourque l'analyse soit faite, un plan de la zone o&ugrave; seront effectu&eacute;es les livraisons doit &ecirc;tre
     * pr&eacute;cedemment charg&eacute;. Si le fichier choisi est invalide (erreurs de syntaxes, &eacute;l&eacute;ments manquants,
     * ect) un message d'erreur est affich&eacute;
     */
    public void chargerDemandeLivraison() {
        try {
            m_window.getVueListeLivraisons().raz();

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
            m_demandeLivraison = new DemandeLivraison(m_window.getM_vuePlan().getPlan());
            m_demandeLivraison.fromXML(doc.getDocumentElement());


            m_window.getM_zoneNotification().setSuccessMessage("La demande de livraison  '" + fichierXML.getName() + "' a été chargée avec succès !");

            // Désactive l'action "Éditer version papier" et annule la feuille de route
            m_feuilleRoute = null;
            m_window.getM_vuePlan().setFeuilleRoute(null);
            m_window.getM_menuFichier().getItem(2).setEnabled(false);

            // Active le menu "Édition"
            m_window.getM_menuEdition().setEnabled(true);

            // Annule la feuille de route actuelle, s'il y en avait une
            annulerFeuilleRoute();

            m_window.getM_vuePlan().repaint();

            updateListeLivraisons();

        } catch (ParseXmlException e) {
            m_window.getM_zoneNotification().setErrorMessage("Erreur: impossible de charger la demande de livraison" +
                    " demandée. Cause : " + e.getMessage());
            annulerFeuilleRoute();
            m_plan.resetNoeuds();
            m_demandeLivraison=null;
            annulerFeuilleRoute();
        }
    }

    /**
     * Calcule la tourn&eacute;e la plus efficace pour l'ensemble de livraisons du jour et la zone courants et
     * rend possible, dans la suite, l'&eacute;dition en format papier de la feuille de route.
     * Pourque l'analyse soit faite, un plan et une demande de livraisons doivent &ecirc;tre charg&eacute;s.
     */
    public void calculerFeuilleRoute() {
        if (m_demandeLivraison == null) {
            m_window.getM_zoneNotification().setErrorMessage("Impossible de calculer une feuille de route sans " +
                    "demande de livraison.");
            return;
        }
        else if (m_demandeLivraison.isEmpty()) {
            m_window.getM_zoneNotification().setErrorMessage("Impossible de calculer la feuille de route d'une" +
                    "demande de livraison vide");
            return;
        }

        m_feuilleRoute = m_demandeLivraison.calculerFeuilleDeRoute(DUREE_CALCUL);

        if (m_feuilleRoute.getEtatFeuille() == FeuilleRoute.EtatFeuille.INSOLUBLE) {
            m_window.getM_zoneNotification().setErrorMessage("Impossible de calculer la feuille de route: Demande de" +
                    "livraison insoluble ou trop complexe.");
            annulerFeuilleRoute();
            return;
        }
        else if (m_feuilleRoute.getM_reSchedule().size() != 0) {
            m_window.getM_zoneNotification().setErrorMessage("Certaines livraisons dépassent leur plage horaire " +
                    "dédiée.");
        }

        m_window.getM_vuePlan().setFeuilleRoute(m_feuilleRoute);

        m_window.getM_zoneNotification().setSuccessMessage("Feuille de route calculée avec succès !");

        updateListeLivraisons();

        // Active l'action "Éditer version papier"
        m_window.getM_menuFichier().getItem(2).setEnabled(true);
    }

    public void updateListeLivraisons() {
        if (m_feuilleRoute != null) {
            m_window.getVueListeLivraisons().setLivraisons(m_feuilleRoute.getLivraisons());
        }
        else if (m_demandeLivraison != null) {
            m_window.getVueListeLivraisons().setLivraisons(m_demandeLivraison.getLivraisons());
        }
        else {
            m_window.getVueListeLivraisons().raz();
        }
    }

    /**
     * &Eacute;dite et affiche la version papier de la feuille de route actuelle (s'il y en a une)
     */
    public void editerFeuilleRoutePapier() {
        if (m_feuilleRoute == null) {
            m_window.getM_zoneNotification().setErrorMessage("Veuillez d'abord calculer une feuille de route avant " +
                    "d'éditer une version papier");
            return;
        }
        FenetreImprimerFeuilleRoute dialog = new FenetreImprimerFeuilleRoute(this, m_feuilleRoute);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Enregistrer la version papier d'une feuille de route
     * @return true si la feuille de route a correctement été enregistrée, false sinon.
     */
    public boolean enregistrerFeuilleRoute(VueFeuilleRoutePapier vueFeuilleRoute) {
        FileDialog fileDialog = new FileDialog(m_window.getM_frame());
        fileDialog.setModal(true);

        fileDialog.setDirectory(FEUILLES_ROUTE_FOLDER);
        fileDialog.setAlwaysOnTop(true);

        fileDialog.setVisible(true);

        String filename = fileDialog.getFile();
        if (filename != null) {
            File f = new File(fileDialog.getDirectory(), fileDialog.getFile());
            PrintWriter writer;
            try {
                writer = new PrintWriter(f.getAbsolutePath(), "UTF-8");
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                return false;
            }
            writer.print(vueFeuilleRoute.getVersionPapier());
            writer.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Ajoute une livraison &agrave; la demande de livraison courante et, le cas &eacute;cheant, &eacute;limine la
     * feuille de route calcul&eacute;e
     *
     * @param livraison &agrave; rajouter
     */
    public void ajouterLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeAjout(m_demandeLivraison, livraison));

        annulerFeuilleRoute();
        updateListeLivraisons();
    }

    /**
     * Supprime une livraison &agrave; la demande de livraison courante et, le cas &eacute;cheant, &eacute;limine la
     * feuille de route calcul&eacute;e
     *
     * @param livraison la livraison à supprimer
     */
    public void supprimerLivraison(Livraison livraison) {
        m_commandes.executer(new CommandeSuppression(m_demandeLivraison, livraison));

        annulerFeuilleRoute();
        updateListeLivraisons();
        updateListeLivraisons();
    }

    /**
     * Permet d'ex&eacute;cuter la derni&egrave;re commande annul&eacute;e et met &agrave; jour le panel du plan
     */
    public void reexecuter() {
        boolean executed = m_commandes.reexecuter();
        if (executed) {
            annulerFeuilleRoute();
            updateListeLivraisons();

            hideSidebar();
            m_window.getM_vuePlan().repaint();
        }
    }

    /**
     * Permet de annuler la derni&egrave;re commande ex&eacute;cut&eacute;e et met &agrave; jour le panel du plan
     */
    public void annuler() {
        boolean canceled = m_commandes.annuler();
        if (canceled) {
            annulerFeuilleRoute();
            updateListeLivraisons();

            hideSidebar();
            m_window.getM_vuePlan().repaint();
        }
    }

    /**
     * Annulation de la feuille de route (s'il y en avait une)
     */
    private void annulerFeuilleRoute() {
        m_feuilleRoute = null;
        m_window.getM_vuePlan().setFeuilleRoute(null);
        m_window.getM_vuePlan().resetTroncons();
        if (m_demandeLivraison != null) {
            m_demandeLivraison.razHeuresLivraisons();
        }
    }

    /**
     * Affiche le panel contenant les informations d'une livraison
     *
     * @param livraison dont les informations vont &ecirc;tre affich&eacute;es
     */
    public void showInfosLivraison(Livraison livraison) {
        m_window.showInfosLivraison(livraison);
    }

    /**
     * Affiche le panel d'ajout d'une nouvelle livraison
     *
     * @param noeud auquel la nouvelle livraison va &ecirc;tre associ&eacute;e
     */
    public void showAjouterLivraison(Noeud noeud) {
        m_window.showAjouterLivraison(noeud);
    }

    /**
     * Ferme la barre lat&eacute;rale droite contenant les informations de la livraison ou du noeud
     * s&eacute;lectionn&eacute;s
     */
    public void hideSidebar() {
        m_window.hideSidebar();
    }

    public void selectionLivraison(Livraison livraison) {
        m_window.getM_vuePlan().selectLivraison(livraison);
    }
}

