package view;

import model.Chemin;
import model.Troncon;

import java.awt.*;
import java.util.ArrayList;

public class VueTroncon {

    private Troncon m_troncon;
    private ArrayList<Chemin> m_chemins;
    private Boolean m_doubleVoie;

    private static Color COULEUR_DEFAUT = new Color(89, 147, 221);
    private static int STROKE_SIZE  = 2;
    private static Color tableauCouleurs[] = new Color[] {Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN,Color.CYAN,Color.MAGENTA,Color.ORANGE,Color.WHITE,Color.PINK,Color.BLACK};

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
    private float getOffset(int position){
        if (m_chemins.size()==1){
            return STROKE_SIZE;
        }
        else{
            return 2*STROKE_SIZE/(m_chemins.size()-1);
        }
    }

    private double getAngle(){
        int x1 = m_troncon.getArrivee().getM_x();
        int y1 = m_troncon.getArrivee().getM_y();
        int x2 = m_troncon.getDepart().getM_x();
        int y2 = m_troncon.getDepart().getM_y();
        return Math.atan2(y2 - y1, x2 - x1);
    }

    public void draw(Graphics2D g2, int voies) {

        g2.setStroke(new BasicStroke(STROKE_SIZE * voies));
        int x1 = m_troncon.getArrivee().getM_x();
        int y1 = m_troncon.getArrivee().getM_y();
        int x2 = m_troncon.getDepart().getM_x();
        int y2 = m_troncon.getDepart().getM_y();
        g2.setColor(COULEUR_DEFAUT);
        g2.drawLine(x1, y1, x2, y2);

        //TODO : Gérer l'offset
        if (m_chemins.size() != 0){
            g2.setStroke(new BasicStroke(1));
            for (int i=0;i<m_chemins.size();i++){
                g2.setColor(obtenirCouleur(m_chemins.get(i)));
                float offset = getOffset(i);
                double angle = getAngle();
                int xoffset = (int) (offset*Math.cos(angle));
                int yoffset = (int) (offset*Math.sin(angle));
                g2.drawLine(x1+xoffset, y1+yoffset, x2+xoffset, y2+yoffset);
            }
        }


        // Affichage du nom de la rue
        // TODO : Corriger ça pour que ce soit plus clair, ou l'enlever sinon
        /**
        g2.setColor(COULEUR_DEFAUT.brighter());
        g2.setFont(RUE_FONT);

        int t_x = (x1 + x2) / 2;
        int t_y = (y1 + y2) / 2;
        double angle = Math.atan2(Math.abs(y2 - y1), Math.abs(x2 - x1));
        if ( Math.abs(angle - Math.PI / 2) < ANGLE_TOLERANCE) {
            t_y += RUE_Y_OFFSET;
        }
        g2.drawString(m_troncon.getM_nom(), t_x, t_y);
        **/
    }

    public void supprimerChemins(){
        m_chemins.clear();
    }

    private Color obtenirCouleur(Chemin chemin){
        //Retourne la couleur associee au chemin
        int index = chemin.getListeTroncons().get(0).getDepart().getM_livraison().getM_plage().getM_indice();
        return tableauCouleurs[index%10];
    }

    public void ajouterChemin(Chemin chemin){
        m_chemins.add(chemin);
    }

    // Getters/Setters
    public Troncon getM_troncon() {
        return m_troncon;
    }

    public void setM_doubleVoie(Boolean m_doubleVoie) {
        this.m_doubleVoie = m_doubleVoie;
    }
}
