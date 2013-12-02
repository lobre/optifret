package exemple;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import libs.ExampleFileFilter;

public class Fenetre {
	
	private Dessin dessin;
	private JFrame cadre;
	private JFileChooser jFileChooserXML;

	public static void main(String[] args) {
		new Fenetre(400,400);
	}

	public Fenetre(int largeur, int hauteur) {
		// Creation d'un cadre contenant un menu, deux boutons, 
		// un ecouteur de souris et une zone de dessin
		cadre = new JFrame("Mon application");
		cadre.setLayout(null);
		cadre.setSize(largeur,hauteur);
		cadre.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				System.out.println("Souris cliquee en x="+e.getX()+" y="+e.getY());
			}
		});
		creeMenus();
		creeBoutons(largeur,hauteur);
		dessin =  new Dessin(0,0,largeur,hauteur-80, Color.gray);
		cadre.getContentPane().add(dessin);
		dessin.repaint();
		cadre.setVisible(true);
	}

	private void creeMenus(){
		// Creation de deux menus, chaque menu ayant plusieurs items
		// et association d'un ecouteur d'action a chacun de ces items
		JMenu menuCouleurs = new JMenu("Changer la couleur");
		ActionListener a1 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dessin.setCouleurArrierePlan(Color.blue); 
				dessin.repaint();
			}
		};
		ajoutItem("Bleu", menuCouleurs, a1);
		ActionListener a2 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dessin.setCouleurArrierePlan(Color.red); 
				dessin.repaint();
			}
		};
		ajoutItem("Rouge", menuCouleurs, a2);
		ActionListener a3 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dessin.setCouleurArrierePlan(Color.green); 
				dessin.repaint();
			}
		};
		ajoutItem("Vert", menuCouleurs, a3);

		JMenu menuFichier = new JMenu("Fichier");
		ActionListener a4 = new ActionListener(){
			public void actionPerformed(ActionEvent e){sauveGarder();}
		};
		ajoutItem("Sauvegarder en XML", menuFichier, a4);
		ActionListener a5 = new ActionListener(){
			public void actionPerformed(ActionEvent e){lireDepuisFichierXML();}
		};
		ajoutItem("Ouvrir un fichier XML", menuFichier, a5);
		
		JMenuBar barreDeMenu = new JMenuBar();
        barreDeMenu.add(menuCouleurs);
        barreDeMenu.add(menuFichier);
        cadre.setJMenuBar(barreDeMenu);
	}
	
	private void ajoutItem(String intitule, JMenu menu, ActionListener a){
		JMenuItem item = new JMenuItem(intitule);
		menu.add(item);
		item.addActionListener(a);
	}
	
	private void creeBoutons(int largeur, int hauteur){
		// Creation de deux boutons et association d'un ecouteur d'action a chaque bouton
		ActionListener a1 = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dessin.addBouleAleatoire(); 
				dessin.repaint();
			}
		};
		cadre.getContentPane().add(creeBouton("AjouterBoule",0,hauteur-80,largeur/2,40,a1));
		ActionListener a2 = new ActionListener(){
			public void actionPerformed(ActionEvent e){System.exit(0);}
		};
		cadre.getContentPane().add(creeBouton("Quitter",largeur/2,hauteur-80,largeur/2,40,a2));
	}
	
	private JButton creeBouton(String intitule, int x, int y, int largeur, int hauteur, ActionListener a){
		JButton bouton = new JButton(intitule);
		bouton.setSize(largeur,hauteur);
		bouton.setLocation(x,y);
		bouton.addActionListener(a);
		return bouton;
	}

	public void sauveGarder(){
		// Sauvegarde des donnees dans un fichier XML inspiree par
        // http://www.exampledepot.com/egs/javax.xml.transform/WriteDom.html
		File xml = ouvrirFichier();
        if (xml != null) {
    		StreamResult result = new StreamResult(xml);
             //Creation de l'infrastructure pour construire le XML
        	DocumentBuilder constructeur;
            try {
                constructeur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            } catch (ParserConfigurationException pCe) {
                return;
            }
            Document document = constructeur.newDocument();
            //Creation du noeud XML
            Element dessinElement = dessin.creerNoeudXML(document);
            if (dessinElement == null) return;
            document.appendChild(dessinElement);
            try {
                DOMSource source = new DOMSource(document);
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.setOutputProperty(OutputKeys.INDENT, "yes");
                xformer.transform(source, result);
            } catch (TransformerConfigurationException tCe) {
                System.out.println(tCe.getMessage());
                return;
            } catch (TransformerException te) {
                System.out.println(te.getMessage());
                return;
            }
        }
	}
	
	public void lireDepuisFichierXML(){
        File xml = ouvrirFichier();
        if (xml != null) {
             try {
                 // creation d'un constructeur de documents a l'aide d'une fabrique
                DocumentBuilder constructeur = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
                // lecture du contenu d'un fichier XML avec DOM
                Document document = constructeur.parse(xml);
                Element racine = document.getDocumentElement();
                if (racine.getNodeName().equals("dessin")) {
                    int resultatConstruction = ConstruireToutAPartirDeDOMXML(racine);
                    if (resultatConstruction != Dessin.PARSE_OK) {
                    	System.out.println("PB de lecture de fichier!");
                    } 
                }
            // todo : traiter les erreurs
            } catch (ParserConfigurationException pce) {
                System.out.println("Erreur de configuration du parseur DOM");
                System.out.println("lors de l'appel a fabrique.newDocumentBuilder();");
            } catch (SAXException se) {
                System.out.println("Erreur lors du parsing du document");
                System.out.println("lors de l'appel a construteur.parse(xml)");
            } catch (IOException ioe) {
                System.out.println("Erreur d'entree/sortie");
                System.out.println("lors de l'appel a construteur.parse(xml)");
            }
        }  
	}

	private File ouvrirFichier(){
        jFileChooserXML = new JFileChooser();
        // Note: source for ExampleFileFilter can be found in FileChooserDemo,
        // under the demo/jfc directory in the JDK.
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("xml");
        filter.setDescription("Fichier XML");
        jFileChooserXML.setFileFilter(filter);
        jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (jFileChooserXML.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
                return new File(jFileChooserXML.getSelectedFile().getAbsolutePath());
        return null;
	}

	private int ConstruireToutAPartirDeDOMXML(Element vueCadreDOMElement) {
	        if (dessin.construireAPartirDeDOMXML(vueCadreDOMElement) != Dessin.PARSE_OK) {
	            return Dessin.PARSE_ERROR;
	        }
	        dessin.repaint();
	        return Dessin.PARSE_OK;
	    }

}
