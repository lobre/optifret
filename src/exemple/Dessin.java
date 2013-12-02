package exemple;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Dessin extends JPanel{
	
	private Vector<Boule> lesBoules;
	private int largeur;
	private int hauteur;
	private Color couleurArrierePlan;
	private static int maxLargeurBoule=50;
	private static int minLargeurBoule=20;
    static public int PARSE_ERROR = -1;
    static public int PARSE_OK = 1;
    
    @Override
	public void paintComponent(Graphics g) {
		// methode appelee a chaque fois que le dessin doit etre redessine
		super.paintComponent(g);
		setBackground(couleurArrierePlan);
        for (Boule b : lesBoules) {
            g.setColor(b.getCouleur());
            g.fillOval(b.getX(), b.getY(), b.getRayon(), b.getRayon());
        }
	}

 	public Dessin (int x, int y, int largeur, int hauteur, Color arrierePlan) {
    	// Creation d'un panneau pour dessiner les boules
 		setSize(largeur,hauteur);
		setLocation(x,y);
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.couleurArrierePlan = arrierePlan;
        lesBoules = new Vector<Boule>();
     }
  	
	public void setCouleurArrierePlan(Color couleur){
		couleurArrierePlan = couleur;
	}
	
	private int alea(int max){
		return (int) (Math.random() * max);
	}
 	
    public void addBouleAleatoire(){
		int rayon = minLargeurBoule + alea(maxLargeurBoule-minLargeurBoule);
		int x = alea(largeur-2*rayon);
		int y = alea(hauteur-2*rayon); 
		lesBoules.addElement(new Boule(x,y,rayon,new Color(alea(255),alea(255),alea(255))));
	}

    public Element creerNoeudXML(Document document) {      
    	if (document == null) return null;
    	Element racine = document.createElement("dessin");
    	creerAttribut(document,racine,"hauteur",Integer.toString(hauteur));
    	creerAttribut(document,racine,"largeur",Integer.toString(largeur));        
        Element couleurElement = document.createElement("couleurArrierePlan");
    	creerAttribut(document,couleurElement,"rouge",Integer.toString(couleurArrierePlan.getRed()));
    	creerAttribut(document,couleurElement,"vert",Integer.toString(couleurArrierePlan.getGreen()));
    	creerAttribut(document,couleurElement,"bleu",Integer.toString(couleurArrierePlan.getBlue()));
        racine.appendChild(couleurElement);
        for (Boule boule : lesBoules) {
            Element bouleElement = boule.creerNoeudXML(document);
            if (bouleElement != null) {
                racine.appendChild(bouleElement);
            } else {
                return null;
            }
        }
        return racine;
    }
    
    private void creerAttribut(Document document, Element racine, String nom, String valeur){
    	Attr attribut = document.createAttribute(nom);
    	racine.setAttributeNode(attribut);
    	attribut.setValue(valeur);
    }

    public int construireAPartirDeDOMXML(Element noeudDOMRacine){
    	//todo : gerer les erreurs de syntaxe dans le fichier XML
    	
        //lecture des attributs
        hauteur = Integer.parseInt(noeudDOMRacine.getAttribute("hauteur"));
        largeur = Integer.parseInt(noeudDOMRacine.getAttribute("largeur"));

        NodeList liste = noeudDOMRacine.getElementsByTagName("couleurArrierePlan");
        if (liste.getLength() != 1) {
            return Dessin.PARSE_ERROR;
        }
        Element couleurElement = (Element) liste.item(0);
        int rouge = Integer.parseInt(couleurElement.getAttribute("rouge"));
        int vert = Integer.parseInt(couleurElement.getAttribute("vert"));
        int bleu = Integer.parseInt(couleurElement.getAttribute("bleu"));
        couleurArrierePlan = new Color(rouge, vert, bleu);

        //creation des Boules;
        String tag = "boule";
        liste = noeudDOMRacine.getElementsByTagName(tag);
        lesBoules.removeAllElements();
        for (int i = 0; i < liste.getLength(); i++) {
            Element bouleElement = (Element) liste.item(i);
            Boule laNouvelleBoule = new Boule();
            if (laNouvelleBoule.construireAPartirDeDOMXML(bouleElement)!= Dessin.PARSE_OK){
                return Dessin.PARSE_ERROR;
            }           
            //ajout des elements crees dans la structure objet
             lesBoules.addElement(laNouvelleBoule);
        }
        return PARSE_OK;
    }

	
}
