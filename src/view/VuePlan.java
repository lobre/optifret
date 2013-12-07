package view;

import model.Noeud;
import model.Plan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class VuePlan extends JPanel {

    private Plan m_plan;

	private Vector<VueNoeud> m_noeuds;
	private int m_largeur;
	private int m_hauteur;
    private int m_x_max;
    private int m_y_max;

    // Facteur de zoom de la vue
    private float m_zoom;

    // Attributs utilisés pour "dragger" la VuePlan
    private Point m_last_click;
    private Point m_last_position;
    
    @Override
	public void paintComponent(Graphics g) {
		// methode appelee a chaque fois que le dessin doit etre redessine
		super.paintComponent(g);
        for (VueNoeud n : m_noeuds) {
            g.setColor(n.getM_couleur());
            int x = (int) ( m_zoom * n.getM_x());
            int y = (int) ( m_zoom * n.getM_y());

            g.fillOval(x, y, (int) (n.getM_rayon() * m_zoom), (int) (n.getM_rayon() * m_zoom));
        }
	}

    public VuePlan () {
    	// Creation d'un panneau pour dessiner les boules

        m_largeur = 1;
        m_hauteur = 1;

        m_x_max = m_largeur;
        m_y_max = m_hauteur;

        m_zoom = 1;
        m_noeuds = new Vector<VueNoeud>();

        // Drag attributes
        m_last_click = null;
        m_last_position = getLocation();

        // Mouse listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("Mouse pressed at : (" + e.getX() + ", " + e.getY() + ")");

                m_last_click = getParent().getMousePosition();
                m_last_position = getLocation();

                Noeud clickedNoeud = getClickedNoeud(e.getX(), e.getY());
                if (clickedNoeud != null) {
                    // TODO : Normalement, on ne devrait ouvrir la fenêtre d'info que si le noeud a déjà une livraison
                    new InfosNoeud(clickedNoeud);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point p = getParent().getMousePosition();

                int x = (int) (m_last_position.getX() + p.getX() - m_last_click.getX());
                int y = (int) (m_last_position.getY() + p.getY() - m_last_click.getY());
                setLocation(x, y);
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                setM_zoom(m_zoom * (1 - (float) e.getWheelRotation() / 10));
            }
        });

    }


    public void setM_plan(Plan plan) {
        m_plan = plan;
        m_noeuds = new Vector<VueNoeud>();

        m_x_max = -1;
        m_y_max = -1;

        for (Noeud n : m_plan.getM_noeuds().values()) {
            VueNoeud vueNoeud = new VueNoeud(n);
            m_noeuds.add(vueNoeud);

            m_x_max = n.getM_x() + vueNoeud.getM_rayon() > m_x_max ? n.getM_x() + vueNoeud.getM_rayon() : m_x_max;
            m_y_max = n.getM_y() + vueNoeud.getM_rayon() > m_y_max ? n.getM_y() + vueNoeud.getM_rayon() : m_y_max;
        }

        // Redimensionnement du panel
        setSize((int) ((m_x_max + 10) * m_zoom) , (int) ((m_y_max + 10) * m_zoom));

        // Centrage du panel
        setLocation((getParent().getWidth() - getWidth()) / 2, (getParent().getHeight() - getHeight()) / 2);

        repaint();
    }
    public Plan getM_plan() {
        return m_plan;
    }

    public float getM_zoom() {
        return m_zoom;
    }
    public void setM_zoom(float m_zoom) {
        this.m_zoom = m_zoom;
        this.repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public Noeud getClickedNoeud(int x, int y) {

        for (VueNoeud vueNoeud : m_noeuds) {
            int n_x = (int) (m_zoom * vueNoeud.getM_x());
            int n_y = (int) (m_zoom * vueNoeud.getM_y());
            float r = vueNoeud.getM_rayon() * m_zoom;

            if (x >= n_x - r && x <= n_x + r && y >= n_y - r && y <= n_y + r) {
                return vueNoeud.getM_noeud();
            }
        }

        return null;

    }


	
}
