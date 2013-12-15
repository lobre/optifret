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
    private FeuilleRoute m_feuilleRoute;

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
    private Point m_lastPositionDrag;

    private Controleur m_controleur;

    //gère la selection
    private VueNoeud m_selectedNoeud;
    private VueNoeud m_focusedNoeud;

    public VuePlan(Controleur controleur) {

        setBackground(COULEUR_BACKGROUND);

        m_plan = null;
        m_feuilleRoute = null;
        m_controleur = controleur;
        m_largeur = 1;
        m_hauteur = 1;

        m_x_max = m_largeur + MARGIN;
        m_y_max = m_hauteur + MARGIN;

        m_zoom = 1;
        m_noeuds = new HashMap<Integer, VueNoeud>();

        // Drag attributes
        m_lastClick = getLocation();
        m_lastPositionDrag = getLocation();
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

        for (Troncon t : m_plan.getM_troncons()) {
            VueTroncon vueTroncon = new VueTroncon(t);
            if (m_troncons.containsKey(t.getOppositePair())) {
                VueTroncon tOpposite = m_troncons.get(t.getOppositePair());
                tOpposite.setM_doubleVoie(true);
                vueTroncon.setM_doubleVoie(true);
            }

            m_troncons.put(t.getPair(), vueTroncon);
        }

        for (Noeud n : m_plan.getM_noeuds().values()) {
            VueNoeud vueNoeud = new VueNoeud(n);
            m_noeuds.put(n.getM_id(), vueNoeud);
        }

        updateSize();
        centerOnCenter();
    }


    public void setM_feuilleRoute(FeuilleRoute feuilleRoute) {
        m_feuilleRoute = feuilleRoute;
        resetTroncons();
        if (feuilleRoute == null) {
            return;
        }

        for (Chemin chemin : m_feuilleRoute.getChemins()) {
            for (Troncon troncon : chemin.getListeTroncons()) {
                VueTroncon vueT = m_troncons.get(troncon.getPair());
                vueT.ajouterChemin(chemin);
            }
        }

        repaint();
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

    public void setM_zoom(float zoom) {
        m_zoom = zoom;
        updateSize();
    }

    public void setM_lastClick(Point lastClick) {
        this.m_lastClick = lastClick;
    }

    public void setM_lastPosition(Point lastPosition) {
        this.m_lastPosition = lastPosition;
    }

    public void setM_lastPositionDrag(Point lastPosition) {
        this.m_lastPositionDrag = lastPosition;
    }

    // Other methods
    private void updateSize() {
        m_x_max = -1;
        m_y_max = -1;
        for (VueNoeud n : m_noeuds.values()) {
            m_x_max = n.getM_x() + n.getM_rayon() > m_x_max ? n.getM_x() + n.getM_rayon() : m_x_max;
            m_y_max = n.getM_y() + n.getM_rayon() > m_y_max ? n.getM_y() + n.getM_rayon() : m_y_max;
        }
        m_x_max += MARGIN;
        m_y_max += MARGIN;

        // Redimensionnement du panel
        setSize((int) (m_x_max * m_zoom), (int) (m_y_max * m_zoom));
    }

    private void centerOnCenter() {
        int x = (getParent().getWidth() - getWidth()) / 2;
        int y = (getParent().getHeight() - getHeight()) / 2;
        setM_lastPosition(new Point(x, y));
        setLocation(x, y);
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
                setM_lastPositionDrag(getLocation());

                if (m_controleur.getM_demandeLivraison() == null) {
                    return;
                }
                VueNoeud clickedNoeud = getClickedNoeud(e.getX(), e.getY());
                if (clickedNoeud != m_selectedNoeud) {
                    setM_selectedNoeud(clickedNoeud);
                }
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                Point p = MouseInfo.getPointerInfo().getLocation();

                int x = (int) (m_lastPositionDrag.getX() + p.getX() - m_lastClick.getX());
                int y = (int) (m_lastPositionDrag.getY() + p.getY() - m_lastClick.getY());
                setLocation(x, y);
                setM_lastPosition(new Point(x, y));

                //pour empecher une partie du graph non visible de disparaitre quand on le drag
                setSize((int) (m_x_max * m_zoom), (int) (m_y_max * m_zoom));
                getParent().repaint();
            }

            //Permet d'augmenter la taille dun noeud survolé par la souris
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
                float zoom = m_zoom * (1 - (float) e.getWheelRotation() / 10);
                float deltaZoom = zoom - m_zoom;
                int x;
                int y;
                setM_zoom(zoom);

                // On fait en sorte que le plan zoom là où la souris est. 0.1 est le ratio de ce déplacement
                x = (int) (getX() - (0.1 * (deltaZoom / abs(deltaZoom)) * (e.getPoint().getX())));
                y = (int) (getY() - (0.1 * (deltaZoom / abs(deltaZoom))) * (e.getPoint().getY()));
                setM_lastPosition(new Point(x, y));
                setLocation(x, y);
                repaint();
            }
        });
    }

    //Fonction de recentrage du plan sur le noeud selectionné. Si aucun noeud selectionné, même position qu'avant.
    public void centerMapOnSelected() {
        // Redimensionnement du panel
        setSize((int) (m_x_max * m_zoom), (int) (m_y_max * m_zoom));

        if (m_selectedNoeud != null) {
            int x = (int) (-m_selectedNoeud.getM_x() * m_zoom - getX() + (getParent().getWidth() / 2));
            int y = (int) (-m_selectedNoeud.getM_y() * m_zoom - getY() + (getParent().getHeight() / 2));
            setLocation(x, y);
            repaint();
            setM_lastPosition(new Point(x, y));
        } else {
            setLocation(m_lastPosition);
            repaint();
        }
    }


    // Fonction de dessin du plan
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Transformée pour appliquer le zoom
        AffineTransform tr2 = g2.getTransform();
        tr2.scale(m_zoom, m_zoom);
        g2.setTransform(tr2);
        super.paintComponent(g2);

        if (m_plan == null) {
            return;
        }

        // Dessin des tronçons
        for (VueTroncon vueTroncon : m_troncons.values()) {
            vueTroncon.drawBase(g2);
        }

        // Dessin des lignes de départition des tronçons
        for (VueTroncon vueTroncon : m_troncons.values()) {
            vueTroncon.drawMidline(g2);
        }
        // Dessin des chemins sur les tronçons
        for (VueTroncon vueTroncon : m_troncons.values()) {
            vueTroncon.drawChemins(g2);
            // TODO : remove this later
            // vueTroncon.drawNomRue(g2);

        }

        // Dessin des noeuds
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
            m_selectedNoeud = null;
            m_controleur.hideSidebar();
            repaint();
            return;
        }

        if (selected.getM_noeud().isEntrepot()) {
            return;
        }


        selected.setM_selected(true);
        if (selected.getM_noeud().hasLivraison()) {
            m_controleur.showInfosLivraison(selected.getM_noeud().getM_livraison());
        } else {
            m_controleur.showAjouterLivraison(selected.getM_noeud());
        }

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


}
