package view;

import model.Chemin;
import model.Noeud;
import model.Troncon;

import java.awt.*;
import java.util.ArrayList;

public class VueTroncon {

    private Troncon m_troncon;
    private ArrayList<Chemin> m_chemins;
    private Boolean m_doubleVoie;

    private static Color COULEUR_TRONCON = new Color(89, 147, 221);
    private static Color COULEUR_TRONCON_SIMPLE = COULEUR_TRONCON.darker();
    private static Color COULEUR_CHEMIN_NEUTRE = new Color(45, 79, 144);
    private static int LARGEUR_TRONCON = VueNoeud.RAYON_DEFAUT / 4;

    private static Stroke STROKE_TRONCON_SIMPLE = new BasicStroke(LARGEUR_TRONCON);
    private static Stroke STROKE_TRONCON_DOUBLE = new BasicStroke(LARGEUR_TRONCON * 2);
    private static Stroke STROKE_CHEMIN = new BasicStroke(1);

    private static int NOMBRE_COULEURS = 10;
    private static Color tableauCouleurs[] = new Color[] {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN,Color.CYAN,Color.MAGENTA,Color.ORANGE,Color.WHITE,Color.PINK,Color.BLACK};

    private static int RUE_FONTSIZE = 6;
    private static Font RUE_FONT = new Font("Arial", Font.PLAIN, RUE_FONTSIZE);
    private static int RUE_Y_OFFSET = 5;

    private static double ANGLE_TOLERANCE = Math.PI / 10;

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

    private int getGlobalOffset() {
        return - LARGEUR_TRONCON;
    }

    private Color getColor() {
        return m_doubleVoie ? COULEUR_TRONCON : COULEUR_TRONCON_SIMPLE;
    }

    public void drawBase(Graphics2D g2) {
        g2.setStroke(STROKE_TRONCON_DOUBLE);
        g2.setColor(getColor());

        double angle = m_troncon.getAngle();
        int offset = getGlobalOffset();
        int dx = (int) (offset * Math.cos(Math.PI / 2 + angle));
        int dy = (int) (offset * Math.sin(Math.PI / 2 + angle));

        int x1 = m_troncon.getArrivee().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y1 = m_troncon.getArrivee().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;
        int x2 = m_troncon.getDepart().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y2 = m_troncon.getDepart().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;

        g2.drawLine(x1 + dx, y1 + dy, x2 + dx, y2 + dy);
    }

    public void drawMidline(Graphics2D g2) {
        if (!m_doubleVoie) {
            return;
        }

        int x1 = m_troncon.getArrivee().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y1 = m_troncon.getArrivee().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;
        int x2 = m_troncon.getDepart().getM_x() * VueNoeud.AMPLIFICATION_FACTOR;
        int y2 = m_troncon.getDepart().getM_y() * VueNoeud.AMPLIFICATION_FACTOR;

        g2.setColor(COULEUR_TRONCON_SIMPLE);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(x1, y1, x2, y2);
    }

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

        for (int i = 0 ;i < m_chemins.size(); i++) {
            Chemin chemin = m_chemins.get(i);
            g2.setColor(getCouleurChemin(chemin));

            double offset = getGlobalOffset() + getCheminOffset(i);
            int dx = (int) (offset * Math.cos(Math.PI / 2 + angle));
            int dy = (int) (offset * Math.sin(Math.PI / 2 + angle));

            g2.drawLine(x1 + dx, y1 + dy, x2 + dx, y2 + dy);
        }
    }


    // Affichage du nom de la rue
    // TODO : Corriger ça pour que ce soit plus clair, ou... l'enlever
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
        if ( Math.abs(angle - Math.PI / 2) < ANGLE_TOLERANCE) {
            t_y += RUE_Y_OFFSET;
        }

        g2.drawString(m_troncon.getM_nom(), t_x, t_y);
    }

    public void supprimerChemins(){
        m_chemins.clear();
    }

    private Color getCouleurChemin(Chemin chemin){
        //Retourne la couleur associee au chemin
        Noeud depart = chemin.getDepart();
        Noeud arrivee = chemin.getArrivee();
        if ((!depart.hasLivraison() || !arrivee.hasLivraison()) || (depart.getM_livraison().getM_plage() != arrivee.getM_livraison().getM_plage())) {
            return COULEUR_CHEMIN_NEUTRE;
        }
        else {
            int plageID = depart.getM_livraison().getM_plage().getM_indice();
            return tableauCouleurs[plageID % NOMBRE_COULEURS];
        }
    }

    public void ajouterChemin(Chemin chemin){
        m_chemins.add(chemin);
    }

    // Getters/Setters
    public void setM_doubleVoie(Boolean doubleVoie) {
        m_doubleVoie = doubleVoie;
    }

}
