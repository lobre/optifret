package model;

import libs.ParseXmlException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.lang.System.out;
import static java.text.MessageFormat.format;
import static org.junit.Assert.assertTrue;

public class FeuilleRouteTest {
    // TODO : WIP @adelmarre

    @Test
    public void devraitCreerUneFeuilleDeRouteCorrectement() {
        // Given : une DemandeLivraison correcte et éprouvée (courtoisie de vzantedeschi)
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
        try{
            demande.fromXML(docDemande.getDocumentElement());
        }
        catch(ParseXmlException e){
            System.out.println(e.getMessage());
        }

        // When : on crée la FeuilleRoute correspondante
        FeuilleRoute feuilleRoute = null;
        //try {
        feuilleRoute = demande.calculerFeuilleDeRoute();
        /*} catch (Exception e) {
            System.out.println(e);
            return;
        }*/

        // Then : on affiche le trajet obtenu dans la console
        int i = 0, j = 0;
        for (Chemin chemin : feuilleRoute.getChemins()) {
            j = 0;
            for (Troncon troncon : chemin.getListeTroncons()) {
                out.println(format("Chemin {0,number,0}, tronçon {1,number,0} : de {2,number,0} à {3,number,0}.",
                        i, j, troncon.getDepart().getM_id(), troncon.getArrivee().getM_id()));
                j++;
            }
            ++i;
        }
        assertTrue("Failed to generate FeuilleRoute", j > 0 && i > 0);
        out.println("");
        for (Livraison livraison : feuilleRoute.getM_livraisonsOrdonnees()) {
            out.println(format("Livraison {0,number} réalisée en {1,number}:{2,number} à {3}",
                    livraison.getId(), livraison.getAdresse().getM_x(), livraison.getAdresse().getM_y(), livraison.getHeureLivraison().toString()));
        }
        out.println("");
        Map<Livraison, PlageHoraire> m_reSchedule = feuilleRoute.getM_reSchedule();
        out.println(format("Il y a {0,number} livraison(s) réalisable(s) uniquement dans une autre plage horaire.", m_reSchedule.size()));
        for (Map.Entry<Livraison, PlageHoraire> entry : m_reSchedule.entrySet()) {
            out.println(format("Livraison {0,number} doit être réalisée entre {3} et {4} \n" +
                    "                          au lieu de {1} et {2} \n" +
                    "                          Heure prévue : {5}",
                    entry.getKey().getId(), entry.getKey().getPlage().getHeureDebut().toString(), entry.getKey().getPlage().getHeureFin().toString(),
                    entry.getValue().getHeureDebut().toString(), entry.getValue().getHeureFin().toString(), entry.getKey().getHeureLivraison().toString()));
        }
    }

}
