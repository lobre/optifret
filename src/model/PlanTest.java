package model;

import libs.ParseXmlException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.junit.Test;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

/**
 * Tests unitaires de la classe Plan
 */
public class PlanTest {

    public Document parseurPlan(String chemin) {
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
        return doc;
    }


    @Test
    public void testFromXMLNormal(){
        Plan myPlan=new Plan();
        Document myDoc=parseurPlan("xml_tests/planNormal.xml");
        myPlan.fromXML(myDoc.getDocumentElement());
        System.out.print(myPlan.getNoeudParID(0).getTroncons().get(0).getNom());
        assertTrue(myPlan.getNoeudParID(0).getTroncons().get(0).getNom().equals("rue1"));
        assertTrue(myPlan.getNoeudParID(0).getTroncons().get(1).getNom().equals("rue2"))      ;
        assertTrue(myPlan.getNoeudParID(1).getTroncons().get(0).getNom().equals("rue3"));
        assertTrue(myPlan.getNoeudParID(1).getTroncons().get(1).getNom().equals("rue4"))      ;


    }

    @Test (expected = ParseXmlException.class)
    public void testFromXMLErrorFile(){
        Plan myPlan=new Plan();
        Document myDoc=parseurPlan("xml_tests/planErreur.xml");
        myPlan.fromXML(myDoc.getDocumentElement());
    }


}

