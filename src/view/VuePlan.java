package view;

import model.Noeud;
import model.Plan;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class VuePlan extends JPanel{

    private Plan m_plan;

	private Vector<VueNoeud> m_noeuds;
	private int m_largeur;
	private int m_hauteur;
	private Color m_couleurArrierePlan;
	private static int maxLargeurBoule=50;
	private static int minLargeurBoule=20;
    static public int PARSE_ERROR = -1;
    static public int PARSE_OK = 1;
    
    @Override
	public void paintComponent(Graphics g) {
		// methode appelee a chaque fois que le dessin doit etre redessine
		super.paintComponent(g);
		setBackground(m_couleurArrierePlan);
        for (VueNoeud n : m_noeuds) {
            g.setColor(n.getM_couleur());
            g.fillOval(n.getM_x(), n.getM_y(), n.getM_rayon(), n.getM_rayon());
        }
	}

 	public VuePlan (int x, int y, int largeur, int hauteur, Color arrierePlan) {
    	// Creation d'un panneau pour dessiner les boules
 		setSize(largeur,hauteur);
		setLocation(x,y);
        this.m_largeur = largeur;
        this.m_hauteur = hauteur;
        this.m_couleurArrierePlan = arrierePlan;
        m_noeuds = new Vector<VueNoeud>();
     }
  	
	public void setM_couleurArrierePlan(Color couleur){
		m_couleurArrierePlan = couleur;
	}
	
	private int alea(int max){
		return (int) (Math.random() * max);
	}
 	
    public void addBouleAleatoire(){
		int rayon = minLargeurBoule + alea(maxLargeurBoule-minLargeurBoule);
		int x = alea(m_largeur-2*rayon);
		int y = alea(m_hauteur-2*rayon);

        Noeud noeud = new Noeud();
        noeud.setM_x(x);
        noeud.setM_y(y);

        VueNoeud vueNoeud = new VueNoeud(noeud);

		m_noeuds.addElement(vueNoeud);
	}

    public void setM_plan(Plan plan) {
        m_plan = plan;
        m_noeuds = new Vector<VueNoeud>();
        for (Noeud n : plan.getM_noeuds()) {
            VueNoeud vueNoeud = new VueNoeud(n);
            m_noeuds.add(vueNoeud);
        }

        repaint();
    }


	
}
