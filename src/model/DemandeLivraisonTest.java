package model;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DemandeLivraisonTest {

    public static void main(String[] args) {

        File xmlPlan = new File("xml_data/plan20x20.xml");
        File xmlDemande = new File("xml_data/livraison20x20-2.xml");

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
        demande.fromXML(docDemande.getDocumentElement());

    }
}
