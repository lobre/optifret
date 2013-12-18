package view;

import model.Noeud;

import java.awt.*;

public class VueNoeud {


    private Noeud m_noeud;

    private boolean m_selected;
    private boolean m_focused;

    private static Color COULEUR_DEFAUT = new Color(202, 216, 255);
    private static Color COULEUR_ENTREPOT = new Color(193, 28, 185);
    private static final Color COULEUR_LIVRAISON_HORSHORAIRE = new Color(207, 0, 11);
    public static int RAYON_DEFAUT = 13;
    private static int RAYON_LIVRAISON = 17;
    private static int RAYON_ENTREPOT = 24;

    // Facteur par lequel les coordonées de la vue sont amplifiées par rapport à ceux du modèle
    public static int AMPLIFICATION_FACTOR = 2;

    private static double FOCUS_FACTOR = 1.5;
    private static int BORDER_WIDTH = 1;

    private static int FONT_SIZE = 16;
    private static Font ID_FONT = new Font("Arial", Font.BOLD, FONT_SIZE);

    public VueNoeud(Noeud noeud) {
        m_noeud = noeud;
        m_focused = false;
        m_selected = false;
    }

    // Methods

    public void draw(Graphics2D g2) {

        int r = getM_rayon();
        if (!m_noeud.isEntrepot() && (m_selected || m_focused)) {
            r *= FOCUS_FACTOR;
        }

        int x = getM_x() - r;
        int y = getM_y() - r;

        Color color = getM_couleur();

        if (m_noeud.hasLivraison() && m_noeud.getM_livraison().isHorsHoraire()) {
            r += 3;
        }

        // Ombre du cercle
        g2.setColor(color.darker());
        g2.fillOval(x, y, 2 * (r + 1), 2 * (r + 1));


        // Remplissage du cercle
        g2.setColor(color);
        g2.fillOval(x, y, 2 * r, 2 * r);


       /* if ((m_noeud.hasLivraison() && m_noeud.getM_livraison().isHorsHoraire()))      {
            g2.setStroke(new BasicStroke(BORDER_WIDTH+1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(255, 0, 9));
            Point hPoint=new Point((int)(getM_x()+0.1*r),(int)(y+0.1*r));
            Point bgPoint=new Point((int)(x+0.40*r),(int)(getM_y()+((float)15/21)*r));
            Point bdPoint=new Point((int)(getM_x()+0.60*r),(int)(getM_y()+(((float)15/21)*r)));
             
            Point hPoint=new Point((int)(getM_x()-0.1*r),(int)(y-(2.0/3)*r));
            Point bgPoint=new Point((int)(x-0.66*r),(int)(getM_y()+(1.66)*r));
            Point bdPoint=new Point((int)(getM_x()+1.66*r),(int)(getM_y()+(((float)1.66)*r))); 

            g2.drawLine((int)hPoint.getX(),(int)hPoint.getY(),(int)bgPoint.getX(),(int)bgPoint.getY());
            g2.drawLine((int)bgPoint.getX(),(int)bgPoint.getY(),(int)bdPoint.getX(),(int)bdPoint.getY());
            g2.drawLine((int)hPoint.getX(),(int)hPoint.getY(),(int)bdPoint.getX(),(int)bdPoint.getY()); 
        }*/

        // Si un noeud est sélectionné, on affiche son ID ; Si c'est un entrepot, on affiche un grand E par dessus
        if (m_selected || m_noeud.isEntrepot() || m_noeud.hasLivraison() ) {
            g2.setFont(ID_FONT);
            String message;
            if(m_noeud.hasLivraison() && m_noeud.getM_livraison().isHorsHoraire()){
                message = "!";
                g2.setColor(Color.WHITE);
            }else if (m_noeud.isEntrepot()) {
                message = "E";
            } else {
                message = Integer.toString(m_noeud.getM_id());
            }
            int text_x = x + r - g2.getFontMetrics().stringWidth(message) / 2;
            int text_y = y + r + g2.getFontMetrics().getHeight() / 3;

            g2.drawString(message, text_x, text_y);
        }
    }


    // Getters/Setters

    public Noeud getM_noeud() {
        return m_noeud;
    }

    public int getM_x() {
        return m_noeud.getM_x() * AMPLIFICATION_FACTOR;
    }

    public int getM_y() {
        return m_noeud.getM_y() * AMPLIFICATION_FACTOR;
    }

    public int getM_rayon() {
        if (m_noeud.isEntrepot()) {
            return RAYON_ENTREPOT;
        } else if (m_noeud.hasLivraison()) {
            return RAYON_LIVRAISON;
        } else {
            return RAYON_DEFAUT;
        }
    }

    public Color getM_couleur() {
        Color color = COULEUR_DEFAUT;
        if (m_noeud.isEntrepot()) {
            color = COULEUR_ENTREPOT;
        }
        else if (m_noeud.hasLivraison()) {
            if (m_noeud.getM_livraison().isHorsHoraire())  {
                color= new Color(255, 0, 0); // TODO a mettre en haut;
            } else {
                color = VueTroncon.getCouleurPlageHoraire(m_noeud.getM_livraison().getPlage());
            }
        }

        if (m_selected) {
            color = color.brighter();
        }

        return color;
    }

    public void setM_selected(boolean m_selected) {
        this.m_selected = m_selected;
    }

    public void setM_focused(boolean m_focused) {
        this.m_focused = m_focused;
    }
}

