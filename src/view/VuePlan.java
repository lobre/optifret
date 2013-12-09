package view;

import model.DemandeLivraison;
import model.Noeud;
import model.Plan;
import model.Troncon;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static com.sun.org.apache.xalan.internal.lib.ExsltMath.abs;

public class VuePlan extends JPanel {

    static public Color COULEUR_BACKGROUND = new Color(50, 80, 180);
    static public Color COULEUR_BACKGROUND_LIGHT = new Color(55, 86, 221);
    static private int STROKE_SIZE  = 5;
    
    private Plan m_plan;

	private HashMap<Integer, VueNoeud> m_noeuds;
	private int m_largeur;
	private int m_hauteur;
    private int m_x_max;
    private int m_y_max;

    // Facteur de zoom de la vue
    private float m_zoom;

    // Attributs utilisés pour "dragger" la VuePlan
    private Point m_lastClick;
    private Point m_lastPosition;


    public VuePlan() {

        setBackground(COULEUR_BACKGROUND);

        m_plan = null;

        m_largeur = 1;
        m_hauteur = 1;

        m_x_max = m_largeur;
        m_y_max = m_hauteur;

        m_zoom = 1;
        m_noeuds = new HashMap<Integer, VueNoeud>();

        // Drag attributes
        m_lastClick = getLocation();
        m_lastPosition = getLocation();

    }

    public void setM_plan(Plan plan) {
        m_plan = plan;
        m_noeuds = new HashMap<Integer, VueNoeud>();

        m_x_max = -1;
        m_y_max = -1;

        for (Noeud n : m_plan.getM_noeuds().values()) {
            VueNoeud vueNoeud = new VueNoeud(n);
            m_noeuds.put(n.getM_id(), vueNoeud);
        }

        updateSize(m_zoom,(Point) null);
    }
    public Plan getM_plan() {
        return m_plan;
    }

    public void setM_zoom(float zoom,Point position) {
        updateSize(zoom,position);
    }
    public float getM_zoom() {
        return m_zoom;
    }

    public void setM_lastClick(Point m_lastClick) {
        this.m_lastClick = m_lastClick;
    }
    public Point getM_lastClick() {
        return m_lastClick;
    }

    public void setM_lastPosition(Point m_lastPosition) {
        this.m_lastPosition = m_lastPosition;
    }
    public Point getM_lastPosition() {

        return m_lastPosition;
    }

    // Other methods

    private void updateSize(float zoom,Point position) {
         float deltaZoom=zoom-m_zoom;
        m_zoom=zoom;
        for (VueNoeud n : m_noeuds.values()) {
            m_x_max = n.getM_x() + n.getM_rayon() > m_x_max ? n.getM_x() + n.getM_rayon() : m_x_max;
            m_y_max = n.getM_y() + n.getM_rayon() > m_y_max ? n.getM_y() + n.getM_rayon() : m_y_max;
        }

        // Redimensionnement du panel
        setSize((int) ((m_x_max + 10) * m_zoom) , (int) ((m_y_max + 10) * m_zoom));

        // Centrage du panel
      if (position==null){
          setLocation((getParent().getWidth() - getWidth()) / 2, (getParent().getHeight() - getHeight()) / 2);
          //System.out.print("position Null");
      }
        else {
          /////ATTENTION DANGEREUX
          setLocation((int)( this.getX()-(0.1*(deltaZoom/abs(deltaZoom))*(position.getX()))),(int)(this.getY()-(0.1*(deltaZoom/abs(deltaZoom)))*(position.getY())));
         // setLocation((int)( this.getX()+(getParent().getWidth()/2-position.getX())),(int)(this.getY()+getParent().getHeight()/2-position.getY()));
          System.out.println(m_zoom);
          System.out.println(deltaZoom);
      }

        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public Noeud getClickedNoeud(int x, int y) {

        for (VueNoeud vueNoeud : m_noeuds.values()) {
            int n_x = (int) (m_zoom * vueNoeud.getM_x());
            int n_y = (int) (m_zoom * vueNoeud.getM_y());
            float r = vueNoeud.getM_rayon() * m_zoom;

            if (x >= n_x - r && x <= n_x + r && y >= n_y - r && y <= n_y + r) {
                return vueNoeud.getM_noeud();
            }
        }

        return null;

    }

    // Fonction de dessin du plan
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        super.paintComponent(g2);

        if (m_plan == null) {
            return;
        }

        g2.setColor(VueTroncon.COULEUR_DEFAUT);
        g2.setStroke(new BasicStroke(STROKE_SIZE * m_zoom));
        for (Troncon troncon : m_plan.getM_troncons()) {
            int x1 = (int) ( troncon.getArrivee().getM_x() * m_zoom);
            int y1 = (int) ( troncon.getArrivee().getM_y() * m_zoom);
            int x2 = (int) ( troncon.getDepart().getM_x() * m_zoom);
            int y2 = (int) ( troncon.getDepart().getM_y() * m_zoom);

            g2.drawLine(x1, y1, x2, y2);
        }

        for (VueNoeud n : m_noeuds.values()) {
            g2.setColor(n.getM_couleur());
            int x = (int) ( m_zoom * (n.getM_x() - n.getM_rayon()));
            int y = (int) ( m_zoom * (n.getM_y() - n.getM_rayon()));
            //int x = (int) ( m_zoom * (n.getM_x() - n.getM_rayon()));
            //int y = (int) ( m_zoom * (n.getM_y() - n.getM_rayon()));

            g2.fillOval(x, y, (int) (2 * n.getM_rayon() * m_zoom), (int) (2 * n.getM_rayon() * m_zoom));
        }
    }



}
