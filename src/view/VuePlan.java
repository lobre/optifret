package view;

import model.Noeud;
import model.Plan;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class VuePlan extends JPanel {

    private Plan m_plan;

	private Vector<VueNoeud> m_noeuds;
	private int m_largeur;
	private int m_hauteur;
    private int m_x_off;
    private int m_y_off;
    private float m_zoom;
	private Color m_couleurArrierePlan;

    static public int PARSE_ERROR = -1;
    static public int PARSE_OK = 1;
    
    @Override
	public void paintComponent(Graphics g) {
		// methode appelee a chaque fois que le dessin doit etre redessine
		super.paintComponent(g);
		setBackground(m_couleurArrierePlan);
        for (VueNoeud n : m_noeuds) {
            g.setColor(n.getM_couleur());
            int x = (int) m_zoom * m_x_off + n.getM_x();
            int y = (int) m_zoom * m_y_off + n.getM_y();

            g.fillOval(x, y, n.getM_rayon(), n.getM_rayon());
        }
	}

    public VuePlan (int largeur, int hauteur) {
    	// Creation d'un panneau pour dessiner les boules

        m_largeur = largeur;
        m_hauteur = hauteur;
        setSize(m_largeur, m_hauteur);

        m_couleurArrierePlan = new Color(200, 200, 200);
        m_zoom = 1;
        m_x_off = 0;
        m_y_off = 0;
        m_noeuds = new Vector<VueNoeud>();
     }
  	
	public void setM_couleurArrierePlan(Color couleur){
		m_couleurArrierePlan = couleur;
	}

    public void setM_plan(Plan plan) {
        m_plan = plan;
        m_noeuds = new Vector<VueNoeud>();
        for (Noeud n : m_plan.getM_noeuds()) {
            VueNoeud vueNoeud = new VueNoeud(n);
            m_noeuds.add(vueNoeud);
        }

        repaint();
    }

    public int getM_x_off() {
        return m_x_off;
    }
    public void setM_x_off(int m_x_off) {
        this.m_x_off = m_x_off;
    }

    // TODO : Actually set the offsets and zoom somewhere

    public int getM_y_off() {
        return m_y_off;
    }
    public void setM_y_off(int m_y_off) {
        this.m_y_off = m_y_off;
    }

    public float getM_zoom() {
        return m_zoom;
    }
    public void setM_zoom(float m_zoom) {
        this.m_zoom = m_zoom;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(m_largeur, m_hauteur);
    }

    public Noeud getClickedNoeud(int c_x, int c_y) {

        // On convertit les coordonées vers des coordonnées du plan (hors zoom et offset)
        int x = (int) ((c_x - m_x_off) / m_zoom);
        int y = (int) ((c_y - m_y_off) / m_zoom);

        for (VueNoeud vueNoeud : m_noeuds) {
            int n_x = vueNoeud.getM_x();
            int n_y = vueNoeud.getM_y();
            int r = vueNoeud.getM_rayon();

            if (x >= n_x - r && x <= n_x + r && y >= n_y - r && y <= n_y + r) {
                return vueNoeud.getM_noeud();
            }
        }

        return null;

    }


	
}
