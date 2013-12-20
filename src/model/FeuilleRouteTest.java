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
import java.util.List;

import static java.lang.System.out;
import static java.text.MessageFormat.format;
import static org.junit.Assert.assertTrue;

public class FeuilleRouteTest {

    @Test
    public void devraitCreerDesFeuillesDeRouteCorrectement() {
        // Pour la clarté du résultat (atomicité), il aurait mieux valu utilier un dataprovider de testNg.
        File xmlPlan = new File("xml_data/plan20x20.xml");
        File xmlDemande = new File("xml_data/livraison20x20-1.xml");
        devraitCreerUneFeuilleDeRouteCorrectement(xmlPlan, xmlDemande);

        xmlPlan = new File("xml_data/plan20x20.xml");
        xmlDemande = new File("xml_data/livraison20x20-2.xml");
        devraitCreerUneFeuilleDeRouteCorrectement(xmlPlan, xmlDemande);

        xmlPlan = new File("xml_data/plan10x10.xml");
        xmlDemande = new File("xml_data/livraison10x10-1.xml");
        devraitCreerUneFeuilleDeRouteCorrectement(xmlPlan, xmlDemande);

        xmlPlan = new File("xml_data/plan10x10.xml");
        xmlDemande = new File("xml_data/livraison10x10-2.xml");
        devraitCreerUneFeuilleDeRouteCorrectement(xmlPlan, xmlDemande);

        xmlPlan = new File("xml_data/plan10x10.xml");
        xmlDemande = new File("xml_data/livraison10x10-3.xml");
        devraitCreerUneFeuilleDeRouteCorrectement(xmlPlan, xmlDemande);
    }

    private void devraitCreerUneFeuilleDeRouteCorrectement(File xmlPlan, File xmlDemande) {
        // Given : une DemandeLivraison correcte et éprouvée
        DemandeLivraison demande = generateDemandeFromXml(xmlPlan, xmlDemande);


        // When : on crée la FeuilleRoute correspondante
        FeuilleRoute feuilleRoute = null;
        try {
            feuilleRoute = demande.calculerFeuilleDeRoute(1500);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        // Then : on vérifie la validité du trajet obtenu et on l'affiche dans la console
        int i = 0, j = 0;
        for (Chemin chemin : feuilleRoute.getChemins()) {
            j = 0;
            for (Troncon troncon : chemin.getListeTroncons()) {
                out.println(format("Chemin {0,number,0}, tronçon {1,number,0} : de {2,number,0} à {3,number,0}.",
                        i, j, troncon.getDepart().getId(), troncon.getArrivee().getId()));
                j++;
            }
            ++i;
        }
        assertTrue("Failed to init State", feuilleRoute.getEtatFeuille() == FeuilleRoute.EtatFeuille.RESOLU || feuilleRoute.getEtatFeuille() == FeuilleRoute.EtatFeuille.SOLUBLE);
        assertTrue("Failed to generate FeuilleRoute", j > 0 && i > 0);
        out.println("");
        for (Livraison livraison : feuilleRoute.getLivraisonsOrdonnees()) {
            out.println(format("Livraison {0,number} réalisée en {1,number}:{2,number} à {3}",
                    livraison.getId(), livraison.getAdresse().getX(), livraison.getAdresse().getY(), livraison.getHeureLivraison().toString()));
        }
        out.println("");
        List<Livraison> reSchedule = feuilleRoute.getReSchedule();
        out.println(format("Il y a {0,number} livraison(s) réalisable(s) uniquement en dehors de leur plage horaire.", reSchedule.size()));
        for (Livraison livraison : reSchedule) {
            out.println(format("Livraison {0,number,0} sera effectuée à {1} alors qu''elle était prévue entre {2} et {3}.",
                    livraison.getId(), livraison.getHeureLivraison().toString(), livraison.getPlage().getHeureDebut().toString(), livraison.getPlage().getHeureFin().toString()));
        }
        out.println("");
    }

    private DemandeLivraison generateDemandeFromXml(File xmlPlan, File xmlDemande) {
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
        try {
            demande.fromXML(docDemande.getDocumentElement());
        } catch (ParseXmlException e) {
            System.out.println(e.getMessage());
        }
        return demande;
    }


}
