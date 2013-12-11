package model;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static java.text.MessageFormat.format;
import static org.junit.Assert.assertTrue;

public class FeuilleRouteTest {
    // TODO : WIP @adelmarre

    @Test
    public void devraitCreerUneFeuilleDeRouteCorrectement() {
        // Given : une DemandeLivraison correcte et éprouvée (courtoisie de vzantedeschi)
        File xmlPlan = new File("xml_data/plan20x20.xml");
        File xmlDemande = new File("xml_data/livraison20x20-1.xml");

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
        int status = demande.fromXML(docDemande.getDocumentElement());


        // When : on crée la FeuilleRoute correspondante
        FeuilleRoute feuilleRoute = null;
        //try {
        feuilleRoute = demande.calculerFeuilleDeRoute();
        /*} catch (Exception e) {
            System.out.println(e);
            return;
        *///}

        // Then : on affiche le trajet obtenu dans la console
        int i = 0, j = 0;
        for (Chemin chemin : feuilleRoute.getChemins()) {
            j = 0;
            for (Troncon troncon : chemin.getListeTroncons()) {
                System.out.println(format("Chemin {0,number,0}, tronçon {0,number,0} : de {0,number,0} à {0,number,0}.", i, j, troncon.getDepart().getM_id(), troncon.getArrivee().getM_id()));
                j++;
            }
            ++i;
        }
        assertTrue("Failed to generate FeuilleRoute", j > 0 && i > 0);

    }

}
