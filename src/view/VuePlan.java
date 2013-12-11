package view;

import controller.Controleur;
import javafx.util.Pair;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import static com.sun.org.apache.xalan.internal.lib.ExsltMath.abs;

public class VuePlan extends JPanel {

    static public Color COULEUR_BACKGROUND = new Color(50, 80, 180);
    static private int MARGIN = 10;

    private Plan m_plan;

    private HashMap<Integer, VueNoeud> m_noeuds;
    private HashMap<Pair, VueTroncon> m_troncons;

    private int m_largeur;
    private int m_hauteur;
    private int m_x_max;
    private int m_y_max;

    // Facteur de zoom de la vue
    private float m_zoom;

    // Attributs utilisés pour "dragger" la VuePlan
    private Point m_lastClick;
    private Point m_lastPosition;

    private Controleur m_controleur;

    //gère la selection
    private VueNoeud m_selectedNoeud;
    private VueNoeud m_focusedNoeud;

    public VuePlan(Controleur controleur) {

        setBackground(COULEUR_BACKGROUND);

        m_plan = null;
        m_controleur = controleur;
        m_largeur = 1;
        m_hauteur = 1;

        m_x_max = m_largeur + MARGIN;
        m_y_max = m_hauteur + MARGIN;

        m_zoom = 1;
        m_noeuds = new HashMap<Integer, VueNoeud>();

        // Drag attributes
        m_lastClick = getLocation();
        m_lastPosition = getLocation();

        // Selection / Focus
        m_selectedNoeud = null;
        m_focusedNoeud = null;
        initListeners();
    }

    public void setM_plan(Plan plan) {
        m_plan = plan;
        m_noeuds = new HashMap<Integer, VueNoeud>();
        m_troncons = new HashMap<Pair, VueTroncon>();


        m_x_max = -1;
        m_y_max = -1;

        for (Troncon t : m_plan.getM_troncons()) {
            VueTroncon vueTroncon = new VueTroncon(t);
            if (m_troncons.containsKey(t.getOppositePair())) {
                VueTroncon tInverse = m_troncons.get(t.getOppositePair());

                if (t.estDeSensPositif()) {
                    m_troncons.remove(t.getOppositePair());
                    vueTroncon.setM_doubleVoie(true);
                } else {
                    tInverse.setM_doubleVoie(true);
                    continue;
                }
            }
            m_troncons.put(t.getPair(), new VueTroncon(t));
        }

        for (Noeud n : m_plan.getM_noeuds().values()) {
            VueNoeud vueNoeud = new VueNoeud(n);
            m_noeuds.put(n.getM_id(), vueNoeud);
        }

        updateSize(m_zoom, null);
    }

    public void resetTroncons() {
        //Supprime les chemins des vues de tronçons
        for (VueTroncon vueTroncon : m_troncons.values()) {
            vueTroncon.supprimerChemins();
        }
    }

    public Plan getM_plan() {
        return m_plan;
    }

    public void setM_zoom(float zoom, Point position) {
        updateSize(zoom, position);
    }

    public float getM_zoom() {
        return m_zoom;
    }

    public void setM_lastClick(Point lastClick) {
        this.m_lastClick = lastClick;
    }

    public Point getM_lastClick() {
        return m_lastClick;
    }

    public void setM_lastPosition(Point lastPosition) {
        this.m_lastPosition = lastPosition;
    }
    public Point getM_lastPosition() {
        return m_lastPosition;
    }

    public int getM_x_max() {
        return m_x_max;
    }

    public int getM_y_max() {
        return m_y_max;
    }
// Other methods

    private void updateSize(float zoom, Point position) {
        float deltaZoom = zoom - m_zoom;
        m_zoom = zoom;
        for (VueNoeud n : m_noeuds.values()) {
            m_x_max = n.getM_x() + n.getM_rayon() > m_x_max ? n.getM_x() + n.getM_rayon() : m_x_max;
            m_y_max = n.getM_y() + n.getM_rayon() > m_y_max ? n.getM_y() + n.getM_rayon() : m_y_max;
        }
        m_x_max +=  MARGIN;
        m_y_max +=  MARGIN;
        // Redimensionnement du panel
        setSize((int) (m_x_max * m_zoom), (int) (m_y_max * m_zoom));

        // Centrage du panel
        if (position == null) {
            setLocation((getParent().getWidth() - getWidth()) / 2, (getParent().getHeight() - getHeight()) / 2);
        } else {
            //setLocation(new Point((int)( this.getX()-(0.1*(deltaZoom/abs(deltaZoom))*(position.getX()))),(int)(this.getY()-(0.1*(deltaZoom/abs(deltaZoom)))*(position.getY()))));

            setM_lastPosition(new Point((int) (this.getX() - (0.1 * (deltaZoom / abs(deltaZoom)) * (position.getX()))), (int) (this.getY() - (0.1 * (deltaZoom / abs(deltaZoom))) * (position.getY()))));
            setLocation(m_lastPosition);
        }

        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (m_x_max * m_zoom), (int) (m_y_max * m_zoom));
    }

    public VueNoeud getClickedNoeud(int x, int y) {

        for (VueNoeud vueNoeud : m_noeuds.values()) {
            int n_x = (int) (m_zoom * vueNoeud.getM_x());
            int n_y = (int) (m_zoom * vueNoeud.getM_y());
            float r = vueNoeud.getM_rayon() * m_zoom;

            if (x >= n_x - r && x <= n_x + r && y >= n_y - r && y <= n_y + r) {
                return vueNoeud;
            }
        }

        return null;
    }

    private void initListeners() {
        // Mouse listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);

                // Mise à jour de valeurs utiles pour le déplacement par "drag" de la vue
                setM_lastClick(MouseInfo.getPointerInfo().getLocation());
                setM_lastPosition(getLocation());

                if (m_controleur.getM_demandeLivraison() == null) {
                    return;
                }
                VueNoeud clickedNoeud = getClickedNoeud(e.getX(), e.getY());

               // TODO : Not sure it's good, cause clicking out of a node does not make the side bar disepear.
               /*
                if (clickedNoeud == null) {
                    return;
                }  */

                // TODO : Remove this and the two classes when we're sure we'll only use the sidebar
                /**
                if (e.getClickCount() == 2) {
                    if (clickedNoeud.getM_noeud().hasLivraison()) {
                        new FenetreInfosLivraison(clickedNoeud.getM_noeud().getM_livraison(), m_controleur);

                    } else if (!clickedNoeud.getM_noeud().isEntrepot()) {
                        new FenetreAjoutLivraison(clickedNoeud.getM_noeud(), m_controleur.getM_demandeLivraison(), m_controleur);
                    }
                }
                **/
                if (e.getClickCount() == 1 && clickedNoeud != m_selectedNoeud) {
                    setM_selectedNoeud(clickedNoeud);
                    repaint();
                }
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                Point p = MouseInfo.getPointerInfo().getLocation();

                int x = (int) (m_lastPosition.getX() + p.getX() - m_lastClick.getX());
                int y = (int) (m_lastPosition.getY() + p.getY() - m_lastClick.getY());
                setLocation(x, y);
                getParent().repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                VueNoeud clickedNoeud = getClickedNoeud(e.getX(), e.getY());
                if (clickedNoeud != m_focusedNoeud) {
                    setM_focusedNoeud(clickedNoeud);
                    repaint();
                }
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                setM_zoom(getM_zoom() * (1 - (float) e.getWheelRotation() / 10), e.getPoint());
            }
        });

        // Pour adapter la map au redimensionnement de la fenêtre
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Redimensionnement du panel
                //System.out.println("resize");
                setSize((int) (m_x_max * m_zoom), (int) (m_y_max * m_zoom));
               /* if (m_selectedNoeud!=null){
                    setLocation(m_selectedNoeud.getM_x()+getX(),m_selectedNoeud.getM_y()+getHeight()/2);
                } */
                // else{
                setLocation(m_lastPosition);
                //}
                repaint();
            }
        });

    }


    // Fonction de dessin du plan
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Transformée pour appliquer le zoom
        AffineTransform tr2 = g2.getTransform();
        tr2.scale(m_zoom, m_zoom);
        g2.setTransform(tr2);

        super.paintComponent(g2);

        if (m_plan == null) {
            return;
        }

        for (VueTroncon vueTroncon : m_troncons.values()) {
            vueTroncon.draw(g2);
        }

        for (VueNoeud vueNoeud : m_noeuds.values()) {
            vueNoeud.draw(g2);
        }
    }

    // Getters/Setters


    private void setM_selectedNoeud(VueNoeud selected) {
        if (m_selectedNoeud != null) {
            m_selectedNoeud.setM_selected(false);
        }
        if (selected == null) {
            m_controleur.hideSidebar();
        } else if (!selected.getM_noeud().isEntrepot()) {
            selected.setM_selected(true);
            if (selected.getM_noeud().hasLivraison()) {
                m_controleur.showInfosLivraison(selected.getM_noeud().getM_livraison());
            } else {
                m_controleur.showAjouterLivraison(selected.getM_noeud());
            }
        }
        //TODO : A supprimer plus tard
        if (selected != null && m_selectedNoeud != null) {
            resetTroncons();
            Chemin chemin = Dijkstra.dijkstra_c(selected.getM_noeud(), m_selectedNoeud.getM_noeud(), m_plan);
            for (Troncon troncon : chemin.getListeTroncons()) {
                VueTroncon vueT = getVueTroncon(troncon);
                vueT.ajouterChemin(chemin);
            }
        }
        //TODO : Fin à supprimer
        m_selectedNoeud = selected;
    }

    private void setM_focusedNoeud(VueNoeud focused) {
        if (m_focusedNoeud != null) {
            m_focusedNoeud.setM_focused(false);
        }
        if (focused != null) {
            focused.setM_focused(true);
        }

        m_focusedNoeud = focused;
    }
    
    private VueTroncon getVueTroncon(Troncon t) {
        if (m_troncons.containsKey(t.getPair())) {
            return m_troncons.get(t.getPair());
        } else if (m_troncons.containsKey(t.getOppositePair())) {
            return m_troncons.get(t.getOppositePair());
        } else {
            return null;
        }
    }
}
