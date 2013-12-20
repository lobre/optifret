package model;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import libs.ParseXmlException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
/**
 * Tests unitaires de la classe DemandeLivraison.
 */
public class DemandeLivraisonTest {

    static final String cheminXml = "xml_tests/planTestDijkstra.xml";


    public DemandeLivraison parseur(String chemin)throws ParseXmlException  {

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
         demande.fromXML(docDemande.getDocumentElement());
        }
        catch(ParseXmlException e){
            System.out.println(e.getMessage());
            throw e;
        }

        return demande;


    }

    @Test
    public void testFromXMLNormal(){
        DemandeLivraison myDemande= parseur( "xml_tests/livraison.xml");
        assertTrue(myDemande.getEntrepot().getId() == 14)   ;
        assertTrue(myDemande.getPlagesHoraires().get(0).getHeureDebut().toString().equals(new Heure(8,0,0).toString())) ;
        assertTrue(myDemande.getPlagesHoraires().get(0).getHeureFin().toString().equals(new Heure(9,30,0).toString())) ;
        assertTrue(myDemande.getPlagesHoraires().get(0).getLivraisons().get(0).getId()==1); ;
        assertTrue(myDemande.getPlagesHoraires().get(0).getLivraisons().get(0).getAdresse().getId()==77); ;
        assertTrue(myDemande.getPlagesHoraires().get(0).getLivraisons().get(0).getNoClient()==608); ;
    }


    @Test (expected = ParseXmlException.class)
    public void testFromXMLChevauchent(){
        DemandeLivraison myDemande= parseur( "xml_tests/livraisonChevauchementPlages.xml");

    }
    @Test (expected = ParseXmlException.class)
    public void testFromXMLWrongFile(){
        DemandeLivraison myDemande= parseur( "xml_tests/livraisonMauvaisFichier.xml");

    }
    @Test (expected = ParseXmlException.class)
    public void testFromXMLHeureNonValable(){
        DemandeLivraison myDemande= parseur( "xml_tests/livraisonHeureNonValable.xml");

    }
    @Test (expected = ParseXmlException.class)
    public void testFromXMLLivraisonDouble(){
        DemandeLivraison myDemande=parseur( "xml_tests/livraisonDouble.xml");

    }
}
