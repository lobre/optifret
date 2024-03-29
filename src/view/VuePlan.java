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

/**
 * Vue d'un objet Plan. Affiche les noeuds et les tron&ccedil;ons les reliant (qu'ils soient &agrave; double sens
 * ou &agrave; sens unique), peut aussi &ecirc;tre zoom&eacute; et d&eacute;cal&eacute; avec la souris.
 *
 * @see model.Plan
 */
public class VuePlan extends JPanel {

    private static int FACTOR_MINI_DRAG = 4;

    /**
     * Le plan repr&eacute;sent&eacute; par la VuePlan
     */
    private Plan m_plan;

    /**
     * La feuille de route courante.
     */
    private FeuilleRoute m_feuilleRoute;

    /**
     * Liste des VueNoeud repr&eacute;sent&eacute;s par le plan.
     */
    private HashMap<Integer, VueNoeud> m_noeuds;
    /**
     * Liste des VueTroncon repr&eacute;sent&eacute;s par le plan.
     */
    private HashMap<Pair, VueTroncon> m_troncons;


    /**
     * Facteur de zoom de la vue.
     */
    private float m_zoom;

    private int m_x_max;
    private int m_y_max;

    /**
     * Utiles pour le décalage de la map
     */
    private int m_x_off;
    private int m_y_off;


    /**
     * Utilis&eacute; pour le "drag" de la VuePlan
     */
    private Point m_lastClick;
    /**
     * Utilis&eacute; pour le "drag" de la VuePlan
     */
    private Point m_lastPositionDrag;

    /**
     * Contr&ocirc;leur de l'application
     */
    private Controleur m_controleur;

    /**
     * G&egrave;re la selection et le survol de noeuds
     */
    private VueNoeud m_selectedNoeud;

    /**
     * G&egrave;re la selection et le survol de noeuds
     */
    private VueNoeud m_focusedNoeud;


    //
    // CONSTANTES
    //

    /**
     * Couleur d'arri&egrave;re-plan de la VuePlan
     */
    static public Color COULEUR_BACKGROUND = new Color(50, 80, 180);

    /**
     * Marge en pixels entre les points extr&ecirc;mes du plan et son bords.
     */
    static private int MARGIN = 10;

    /**
     * Largeur minimale de la VuePlan
     */
    static private int LARGEUR_MINI = 300;

    /**
     * Cr&eacute;ation de la VuePlan.
     *
     * @param controleur le contr&ocirc;leur de l'application
     */
    public VuePlan(Controleur controleur) {

        setBackground(COULEUR_BACKGROUND);

        m_plan = null;
        m_feuilleRoute = null;
        m_controleur = controleur;

        m_noeuds = new HashMap<Integer, VueNoeud>();
        m_troncons = new HashMap<Pair, VueTroncon>();

        m_x_max = MARGIN;
        m_y_max = MARGIN;

        m_x_off = 0;
        m_y_off = 0;

        m_zoom = 1;
        m_noeuds = new HashMap<Integer, VueNoeud>();

        // Drag attributes
        m_lastClick = new Point(m_x_off, m_y_off);
        m_lastPositionDrag = new Point(m_x_off, m_y_off);

        // Selection / Focus
        m_selectedNoeud = null;
        m_focusedNoeud = null;
        initListeners();
    }

    //
    // Getters/Setters
    //

    /**
     * Mise en place du plan repr&eacute;sent&eacute; par la VuePlan
     *
     * @param plan le plan &agrave; repr&eacute;senter
     * @see model.Plan
     */
    public void setPlan(Plan plan) {
        m_plan = plan;
        m_noeuds = new HashMap<Integer, VueNoeud>();
        m_troncons = new HashMap<Pair, VueTroncon>();

        for (Troncon t : m_plan.getTroncons()) {
            VueTroncon vueTroncon = new VueTroncon(t);
            if (m_troncons.containsKey(t.getOppositePair())) {
                VueTroncon tOpposite = m_troncons.get(t.getOppositePair());
                tOpposite.setDoubleVoie(true);
                vueTroncon.setDoubleVoie(true);
            }

            m_troncons.put(t.getPair(), vueTroncon);
        }

        for (Noeud n : m_plan.getNoeuds().values()) {
            VueNoeud vueNoeud = new VueNoeud(n);
            m_noeuds.put(n.getId(), vueNoeud);
        }

        // Mise à jour de la taille du plan (à partir des coordonnées des noeuds)
        updateSize();

        // Valeur du zoom par défaut pour que le plan prenne toute la largeur
        setZoom((float) getParent().getWidth() / m_x_max);

        // Centre la map par défaut
        centerOnCenter();

        repaint();
    }


    /**
     * Mise en place de la feuille de route de la livraison courante.
     *
     * @param feuilleRoute la feuille de route &agrave; afficher
     * @see model.FeuilleRoute
     */
    public void setFeuilleRoute(FeuilleRoute feuilleRoute) {
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

    public void setZoom(float zoom) {
        m_zoom = zoom;
    }

    public Plan getPlan() {
        return m_plan;
    }

    /**
     * D&eacute;selectionne le noeud actuellement s&eacute;lectionn&eacute; (s'il y en a un)
     */
    public void unselectNoeud() {
        setSelectedNoeud(null);
    }

    /**
     * Change le noeud actuellement s&eacute;lectionn&eacute;. Se centre sur le noeud et affiche la fen&ecirc;tre d'ajout de livraison si
     * le noeud n'en contient pas, ou la fen&ecirc;tre d'infos/suppression de livraison s'il en contient une. Si le nouveau noeud s&eacute;lectionn&eacute; est
     * nul (null), cache la barre lat&eacute;rale.
     *
     * @param selected le noeud actuellement s&eacute;lectionn&eacute;
     */
    private void setSelectedNoeud(VueNoeud selected) {
        if (m_selectedNoeud != null) {
            m_selectedNoeud.setSelected(false);
        }

        if (selected == null) {
            m_selectedNoeud = null;
            return;
        }

        selected.setSelected(true);

        m_selectedNoeud = selected;
    }

    /**
     * Change le noeud actuellement survol&eacute;. (Le rayon de ce noeud est amplifi&eacute; dans la vue)
     *
     * @param focused le noeud actuellement survol&eacute;
     */
    private void setFocusedNoeud(VueNoeud focused) {
        if (m_focusedNoeud != null) {
            m_focusedNoeud.setFocused(false);
        }
        if (focused != null) {
            focused.setFocused(true);
        }

        m_focusedNoeud = focused;
    }

    //
    // Other methods
    //

    /**
     * Remise &agrave; z&eacute;ro des VueTroncon (suppression des chemins affich&eacute;s)
     *
     * @see VueTroncon
     */
    public void resetTroncons() {
        //Supprime les chemins des vues de tronçons
        for (VueTroncon vueTroncon : m_troncons.values()) {
            vueTroncon.supprimerChemins();
        }
    }


    /**
     * Centrage du plan sur la fen&ecirc;tre.
     */
    private void centerOnCenter() {
        m_x_off = (getParent().getWidth() - getWidth()) / 2;
        m_y_off = (getParent().getHeight() - getHeight()) / 2;

        repaint();
    }

    /**
     * Centre la VuePlan sur le noeud actuellement sélectionné
     */
    //Fonction de recentrage du plan sur le noeud selectionné. Si aucun noeud selectionné, même position qu'avant.
    public void centerMapOnSelected() {
        if (m_selectedNoeud == null) {
            getParent().repaint();
            return;
        }

        m_x_off = (int) (-m_selectedNoeud.getX() * m_zoom + (getParent().getWidth() / 2));
        m_y_off = (int) (-m_selectedNoeud.getY() * m_zoom + (getParent().getHeight() / 2));
        getParent().repaint();
    }

    /**
     * Renvoie la VueNoeud qui se trouve aux coordonn&eacute;es (x, y) de la VuePlan
     *
     * @param x coordon&eacute;e x du noeud recherch&eacute;.
     * @param y coordon&eacute;e y du noeud recherch&eacute;.
     * @return un VueNoeud si un noeud a &eacute;t&eacute; cliqu&eacute;, sinon null.
     * @see VueNoeud
     */
    public VueNoeud getClickedNoeud(int x, int y) {
        // On annule l'offset actuel sur les coordonnées cliquées
        x -= m_x_off;
        y -= m_y_off;

        for (VueNoeud vueNoeud : m_noeuds.values()) {
            int n_x = (int) (m_zoom * vueNoeud.getX());
            int n_y = (int) (m_zoom * vueNoeud.getY());
            float r = vueNoeud.getRayon() * m_zoom;

            if (x >= n_x - r && x <= n_x + r && y >= n_y - r && y <= n_y + r) {
                return vueNoeud;
            }
        }

        return null;
    }

    /**
     * Initialisation des listeners (clic sur les noeuds, d&eacute;placement du plan, zoom, ...)
     */
    private void initListeners() {
        // Listener au clic de la souris
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);

                // Mise à jour de valeurs utiles pour le déplacement par "drag" de la vue
                m_lastClick = MouseInfo.getPointerInfo().getLocation();
                m_lastPositionDrag = new Point(m_x_off, m_y_off);

                if (m_controleur.getDemandeLivraison() == null) {
                    return;
                }
                VueNoeud selected = getClickedNoeud(e.getX(), e.getY());
                if (selected == m_selectedNoeud) {
                    return;
                }
                setSelectedNoeud(selected);

                // En fonction du noeud sélectionné, plusieurs actions possibles:
                if (selected == null || selected.getNoeud().isEntrepot()) {
                    m_controleur.hideSidebar();
                } else if (selected.getNoeud().hasLivraison()) {
                    m_controleur.showInfosLivraison(selected.getNoeud().getLivraison());
                } else {
                    m_controleur.showAjouterLivraison(selected.getNoeud());
                }
                centerMapOnSelected();

            }

        });

        // Listener au déplacement de la souris (drag, hover)
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                Point p = MouseInfo.getPointerInfo().getLocation();
                int x = (int) (m_lastPositionDrag.getX() + p.getX() - m_lastClick.getX());
                int y = (int) (m_lastPositionDrag.getY() + p.getY() - m_lastClick.getY());

                int largeurMap = (int) (m_x_max * m_zoom);
                int hauteurMap = (int) (m_y_max * m_zoom);
                boolean isNotTooDown = y > (getHeight()) / FACTOR_MINI_DRAG - hauteurMap;
                boolean isNotTooUp = y < ((-getHeight()) / FACTOR_MINI_DRAG + getHeight());
                boolean isNotTooRight = x < (getWidth() - getWidth() / FACTOR_MINI_DRAG);
                boolean isNotTooLeft = x > (-largeurMap + getWidth() / FACTOR_MINI_DRAG);
                if (isNotTooDown && isNotTooUp) {
                    m_y_off = y;
                }
                if (isNotTooLeft && isNotTooRight) {
                    m_x_off = x;
                }
                repaint();
            }

            // Permet d'augmenter la taille dun noeud survolé par la souris
            @Override
            public void mouseMoved(MouseEvent e) {
                VueNoeud clickedNoeud = getClickedNoeud(e.getX(), e.getY());
                if (clickedNoeud != m_focusedNoeud) {
                    setFocusedNoeud(clickedNoeud);
                    repaint();
                }

            }


        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerMapOnSelected();
            }
        });


        // Listener au défilement de la souris (zoom)
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Mise à jour de valeurs utiles pour le déplacement par "drag" de la vue
                m_lastClick = MouseInfo.getPointerInfo().getLocation();

                float deltaZoom = -m_zoom * (float) e.getWheelRotation() / 10;

                // Vrai si la vue plan est trop petite
                boolean tropPetit = deltaZoom < 0 && m_x_max * m_zoom < LARGEUR_MINI;
                // Vrai si un noeud est plus grand qu'un quart de la taille de la vue
                boolean tropGrand = deltaZoom > 0 && VueNoeud.RAYON_DEFAUT * m_zoom > getWidth() / 30;
                if (tropPetit || tropGrand) {
                    // Le dézoom est annulé dans ces deux cas
                    return;
                }

                setZoom(m_zoom + deltaZoom);

                // On fait en sorte que le plan zoom là où la souris est. 0.1 est le ratio de ce déplacement
                m_x_off = (int) (m_x_off - (0.1 * deltaZoom / abs(deltaZoom)) * (e.getX() - m_x_off));
                m_y_off = (int) (m_y_off - (0.1 * deltaZoom / abs(deltaZoom)) * (e.getY() - m_y_off));

                //setLocation(x, y);
                repaint();
            }
        });
    }

    /**
     * Dessine le plan repr&eacute;sent&eacute; ainsi que les points de livraison et l'entrepot si une demande de livraison a &eacute;t&eacute;
     * charg&eacute;e, et les chemins si une feuille de route a &eacute;t&eacute; calcul&eacute;e.
     * Les op&eacute;rations de dessin sont d&eacute;l&eacute;gu&eacute;es aux vues de chaque entit&eacute; (VueNoeud, VueTroncon)
     *
     * @param g l'objet Graphics o&ugrave; se fait le rendu
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (m_plan == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Transformée pour appliquer le zoom
        AffineTransform tr2 = g2.getTransform();
        tr2.scale(m_zoom, m_zoom);
        tr2.translate(m_x_off / m_zoom, m_y_off / m_zoom);
        g2.transform(tr2);

        // System.out.println(getHeight()+"   "+ getY());

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
            vueTroncon.drawNomRue(g2);
        }

        // Dessin des noeuds
        for (VueNoeud vueNoeud : m_noeuds.values()) {
            vueNoeud.draw(g2);
        }

    }

    /**
     * Mise &agrave; jour de la taille de la VuePlan (apr&egrave;s une mise &agrave; jour des noeuds, etc.)
     */
    private void updateSize() {
        m_x_max = 1;
        m_y_max = 1;
        for (VueNoeud n : m_noeuds.values()) {
            m_x_max = n.getX() + n.getRayon() > m_x_max ? n.getX() + n.getRayon() : m_x_max;
            m_y_max = n.getY() + n.getRayon() > m_y_max ? n.getY() + n.getRayon() : m_y_max;
        }
        m_x_max += MARGIN;
        m_y_max += MARGIN;
    }

    /**
     * S&eacute;lectionne une le noeud/adresse d'une livraison et centre la map dessus.
     *
     * @param livraison la livraison &agrave; s&eacute;lectionner
     */
    public void selectLivraison(Livraison livraison) {
        setSelectedNoeud(m_noeuds.get(livraison.getAdresse().getId()));
        centerMapOnSelected();
        m_controleur.showInfosLivraison(livraison);
    }


}
