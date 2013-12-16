package view;

import model.Chemin;
import model.Noeud;
import model.Troncon;

import java.awt.*;
import java.util.ArrayList;
/**
 Vue d'un objet Troncon et (&eacute;ventuellement) des chemins le traversant
 */
public class VueTroncon {

    private Troncon m_troncon;
    private ArrayList<Chemin> m_chemins;
    private Boolean m_doubleVoie;

    private static Color COULEUR_TRONCON = new Color(89, 147, 221);
    private static Color COULEUR_MIDLINE = new Color(0xB8D6FF);
    private static Color COULEUR_CHEMIN_NEUTRE = COULEUR_TRONCON.darker();
    private static int LARGEUR_TRONCON = VueNoeud.RAYON_DEFAUT / 2;

    private static Stroke STROKE_TRONCON = new BasicStroke(LARGEUR_TRONCON * 2);
    private static Stroke STROKE_MIDLINE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{7}, 0);
    private static Stroke STROKE_CHEMIN = new BasicStroke(1);

    private static int NOMBRE_COULEURS = 10;
    private static Color tableauCouleurs[] = new Color[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.WHITE, Color.PINK, Color.BLACK};

    private static int RUE_FONTSIZE = 6;
    private static Font RUE_FONT = new Font("Arial", Font.PLAIN, RUE_FONTSIZE);
    private static int RUE_Y_OFFSET = 5;

    private static double ANGLE_TOLERANCE = Math.PI / 10;

    /**
     * Constructeur de la VueTroncon
     * @param troncon le Troncon &agrave; repr&eacute;senter
     * @see model.Troncon
     */
    public VueTroncon(Troncon troncon) {
        m_troncon = troncon;
        m_doubleVoie = false;
        m_chemins = new ArrayList<Chemin>();
    }

    // Methods
    private double getCheminOffset(int position){
        if (m_chemins.size() == 1){
            return 0;
        }
        else {
            return - 0.5 * LARGEUR_TRONCON + 1.0 * LARGEUR_TRONCON * position  / (m_chemins.size() - 1);
        }
    }

    /**
     * Dessine la base du tron&ccedil;on (sans chemin ni ligne s&eacute;paratrice)
     * @param g2 le contexte Graphics2D sur lequel se fait le dessin
     */
    public void drawBase(Graphics2D g2) {
        g2.setStroke(STROKE_TRONCON);
        g2.setColor(COULEUR_TRONCON);

        double angle = m_troncon.getAngle();
        int offset = - LARGEUR_TRONCON;
        int dx = (int) (offset * Math.cos(Math.PI / 2 + angle));
        int dy = (int) (offset * Math.sin(Math.PI / 2 + angle));

        int x1 = m_troncon.getArrivee().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y1 = m_troncon.getArrivee().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;
        int x2 = m_troncon.getDepart().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y2 = m_troncon.getDepart().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;

        g2.drawLine(x1 + dx, y1 + dy, x2 + dx, y2 + dy);
    }

    /**
     * Dessine une ligne s&eacute;paratrice si ce tron&ccedil;on est l'une des deux voies d'un chemin &agrave;
     * double voie.
     * @param g2 le contexte Graphics2D sur lequel se fait le dessin
     */
    public void drawMidline(Graphics2D g2) {
        if (!m_doubleVoie || !m_troncon.estDeSensPositif()) {
            return;
        }

        int x1 = m_troncon.getArrivee().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y1 = m_troncon.getArrivee().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;
        int x2 = m_troncon.getDepart().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y2 = m_troncon.getDepart().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;

        g2.setColor(COULEUR_MIDLINE);
        g2.setStroke(STROKE_MIDLINE);
        g2.drawLine(x1, y1, x2, y2);
    }

    /**
     * Dessine les chemins qui passe par le tron&ccedil;on repr&eacute;sent&eacute;, s'il y en a.
     * @param g2 le contexte Graphics2D sur lequel se fait le dessin
     */
    public void drawChemins(Graphics2D g2) {
        if (m_chemins.size() == 0) {
            return;
        }

        int x1 = m_troncon.getArrivee().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y1 = m_troncon.getArrivee().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;
        int x2 = m_troncon.getDepart().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y2 = m_troncon.getDepart().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;
        double angle = m_troncon.getAngle();

        g2.setStroke(STROKE_CHEMIN);

        for (int i = 0; i < m_chemins.size(); i++) {
            Chemin chemin = m_chemins.get(i);
            g2.setColor(getCouleurChemin(chemin));

            double offset = - LARGEUR_TRONCON + getCheminOffset(i);
            int dx = (int) (offset * Math.cos(Math.PI / 2 + angle));
            int dy = (int) (offset * Math.sin(Math.PI / 2 + angle));

            g2.drawLine(x1 + dx, y1 + dy, x2 + dx, y2 + dy);
        }
    }


    // TODO : Corriger ça pour que ce soit plus clair, ou... l'enlever
    /**
     * Dessine le nom de la rue dont fait partie le tron&ccedil;on.
     * @param g2 le contexte Graphics2D sur lequel se fait le dessin
     */
    public void drawNomRue(Graphics2D g2) {
        g2.setColor(COULEUR_TRONCON.brighter());
        g2.setFont(RUE_FONT);

        double angle = m_troncon.getAngle();
        int x1 = m_troncon.getArrivee().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y1 = m_troncon.getArrivee().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;
        int x2 = m_troncon.getDepart().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y2 = m_troncon.getDepart().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;

        int t_x = (x1 + x2) / 2;
        int t_y = (y1 + y2) / 2;
        if (Math.abs(angle - Math.PI / 2) < ANGLE_TOLERANCE) {
            t_y += RUE_Y_OFFSET;
        }

        g2.drawString(m_troncon.getM_nom(), t_x, t_y);
    }

    /**
     * R&eacute;initialise la liste de chemins qui passent par le tron&ccedil;on.
     */
    public void supprimerChemins(){
        m_chemins.clear();
    }

    /**
     * Définit la couleur du chemin qui passe par le tronçon selon sa nature.
     * @param chemin le chemin dont la couleur doit être déterminée
     * @return la couleur avec laquelle le chemin doit être dessiné
     */
    private Color getCouleurChemin(Chemin chemin){
        //Retourne la couleur associee au chemin
        Noeud depart = chemin.getDepart();
        Noeud arrivee = chemin.getArrivee();
        boolean noLivraison = !depart.hasLivraison() || !arrivee.hasLivraison();
        if (noLivraison || (depart.getM_livraison().getM_plage() != arrivee.getM_livraison().getM_plage())) {
            return COULEUR_CHEMIN_NEUTRE;
        } else {
            int plageID = depart.getM_livraison().getPlage().getM_indice();
            return tableauCouleurs[plageID % NOMBRE_COULEURS];
        }
    }

    /**
     * Ajoute un chemin &agrave; la liste des chemins passant par le tron&ccedil;on
     * @param chemin un Chemin passant par le tron&ccedil;on représenté
     */
    public void ajouterChemin(Chemin chemin){
        m_chemins.add(chemin);
    }

    // Getters/Setters
    /**
     * Indique si le tron&ccedil;on repr&eacute;sent&eacute; est l'une des deux voies d'une rue &agrave; double voie.
     * @param doubleVoie booléen quiset à true si le tronçon est une voie d'une rue à double voie.
     */
    public void setM_doubleVoie(Boolean doubleVoie) {
        m_doubleVoie = doubleVoie;
    }

}
