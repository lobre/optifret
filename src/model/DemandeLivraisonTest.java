package model;

//import libs.ExampleFileFilter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import libs.ParseXmlException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by vzantedeschi on 05/12/13.
 */
public class DemandeLivraisonTest {

    static final String cheminXml = "xml_tests/planTestDijkstra.xml";


    public void parseur(String chemin)throws ParseXmlException  {

        File xmlPlan = new File("xml_data/plan20x20.xml");
        File xmlDemande = new File(chemin);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document docPlan = null;
        Document docDemande = null;
        try {
            docPlan = dBuilder.parse(xmlPlan);
            docPlan.getDocumentElement().normalize();
            docDemande = dBuilder.parse(xmlDemande);
            docDemande.getDocumentElement().normalize();
        } catch (IOException | NullPointerException | SAXException e) {
            e.printStackTrace();
        }
        Plan plan = new Plan();
        plan.fromXML(docPlan.getDocumentElement());

        DemandeLivraison demande = new DemandeLivraison(plan);
        try{
        int i = demande.fromXML(docDemande.getDocumentElement());
        }
        catch(ParseXmlException e){
            System.out.println(e.getMessage());
            throw e;
        }




    }

    @Test
    public void testFromXMLNormal(){
            parseur( "xml_data/livraison20x20-2.xml");
    }


    @Test (expected = ParseXmlException.class)
    public void testFromXMLChevauchent(){
       parseur( "xml_tests/livraisonChevauchementPlages.xml");

    }
    @Test (expected = ParseXmlException.class)
    public void testFromXMLWrongFile(){
        parseur( "xml_tests/livraisonMauvaisFichier.xml");

    }
    @Test (expected = ParseXmlException.class)
    public void testFromXMLHeureNonValable(){
        parseur( "xml_tests/livraisonHeureNonValable.xml");

    }
    @Test (expected = ParseXmlException.class)
    public void testFromXMLLivraisonDouble(){
        parseur( "xml_tests/livraisonDouble.xml");

    }

  /* @Test
   public void testDoSomeFirstCalc(){
       Plan myPlan = obtenirPlan(cheminXml)  ;
       PlageHoraire myPlage= new PlageHoraire(new Heure(18,45,00),new Heure(19,45,00)) ;
       Noeud myNoeud1=new Noeud(01,12,15);
       Noeud myNoeud2=new Noeud(02,18,20);
       Livraison myLivraison1=new Livraison(80,4069,myNoeud1, myPlage) ;
       Livraison myLivraison2=new Livraison(81,4070,myNoeud2, myPlage) ;
       DemandeLivraison myDemande = new DemandeLivraison(myPlan)        ;
       myDemande.ajouterLivraison(myLivraison1);
       myDemande.ajouterLivraison(myLivraison2);

       myDemande

   }

    //fonctions necessaires à la réalisation des tests:

    private Document lireDepuisXML(File fichierXML) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Impossible d'instantier le parseur XML");
        }

        try {
            Document doc = dBuilder.parse(fichierXML);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (SAXException | IOException | NullPointerException e) {
            System.out.println("Impossible d'ouvrir le fichier XML demandé");
            return null;
        }
    }

    private Plan obtenirPlan(String chemin) {
        //Récupération du plan
        File xmlFile = new File(chemin);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Plan plan = new Plan();
        plan.fromXML(doc.getDocumentElement());
        return plan;
    }        */
}
