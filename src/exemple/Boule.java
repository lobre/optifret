package exemple;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;

public class Boule {

    private int x;
    private int y;
    private int rayon;
    private Color couleur;

    public Boule (int x, int y, int r, Color couleur) {
        this.x = x;
        this.y =y;
        this.couleur=couleur;
        rayon = r;
    }
    
    public Boule(){}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRayon() {
        return rayon;
    }

    public Color getCouleur() {
        return couleur;
    }
 
    public Element creerNoeudXML(Document document) {
        Element racine = document.createElement("boule");
        creerAttribut(document,racine,"x",Integer.toString(x));
        creerAttribut(document,racine,"y",Integer.toString(y));
        creerAttribut(document,racine,"rayon",Integer.toString(rayon));

        Element couleurElement = document.createElement("couleur");
        creerAttribut(document,couleurElement,"rouge",Integer.toString(couleur.getRed()));
        creerAttribut(document,couleurElement,"vert",Integer.toString(couleur.getGreen()));
        creerAttribut(document,couleurElement,"bleu",Integer.toString(couleur.getBlue()));

        racine.appendChild(couleurElement);
        return racine;
    }
    
    private void creerAttribut(Document document, Element racine, String nom, String valeur){
    	Attr attribut = document.createAttribute(nom);
    	racine.setAttributeNode(attribut);
    	attribut.setValue(valeur);
    }
    
    public int construireAPartirDeDOMXML(Element noeudDOMRacine){
    	// todo : gerer les erreurs de syntaxe dans le fichier XML !
    	// lecture des attributs
    	x = Integer.parseInt(noeudDOMRacine.getAttribute("x"));
    	y = Integer.parseInt(noeudDOMRacine.getAttribute("y"));
    	rayon = Integer.parseInt(noeudDOMRacine.getAttribute("rayon"));
    	String tag = "couleur";
    	NodeList liste = noeudDOMRacine.getElementsByTagName(tag);
    	if (liste.getLength() != 1) {
    		return Dessin.PARSE_ERROR;
    	}
    	Element couleurElement = (Element) liste.item(0);
    	int rouge = Integer.parseInt(couleurElement.getAttribute("rouge"));
    	int vert = Integer.parseInt(couleurElement.getAttribute("vert"));
    	int bleu = Integer.parseInt(couleurElement.getAttribute("bleu"));
    	couleur = new Color(rouge, vert, bleu);
    	return Dessin.PARSE_OK;
    }
}

