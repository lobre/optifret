package model;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class DijkstraTest {

    static final String cheminXml = "xml_tests/planTestDijkstra.xml";

    @Test(expected = IllegalArgumentException.class)
    public void testPlanInvalide() {
        Plan plan = obtenirPlan(cheminXml);
        Noeud noeudDepart = plan.getM_noeuds().get(0);
        Noeud noeudArrivee = plan.getM_noeuds().get(1);
        Chemin resultat = Dijkstra.dijkstra_c(noeudDepart, noeudArrivee, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoeudInvalide() {
        Plan plan = obtenirPlan(cheminXml);
        Noeud noeudDepart = plan.getM_noeuds().get(0);
        Chemin resultat = Dijkstra.dijkstra_c(noeudDepart, null, plan);
    }

    @Test
    public void testeDijkstraLongueur0() {
        Plan plan = obtenirPlan(cheminXml);
        Noeud noeudDepart = plan.getM_noeuds().get(0);
        Chemin resultat = Dijkstra.dijkstra_c(noeudDepart, noeudDepart, plan);
        assertTrue(resultat.getListeTroncons().size() == 0);
    }

    @Test
    public void testeDijkstraLongueur1() {
        Plan plan = obtenirPlan(cheminXml);
        Noeud noeudDepart = plan.getM_noeuds().get(0);
        Noeud noeudArrivee = plan.getM_noeuds().get(1);
        Chemin resultat = Dijkstra.dijkstra_c(noeudDepart, noeudArrivee, plan);
        assertTrue(resultat.getListeTroncons().getFirst().getDepart().getM_id() == noeudDepart.getM_id());
        assertTrue(resultat.getListeTroncons().getLast().getArrivee().getM_id() == noeudArrivee.getM_id());
        assertTrue(resultat.getListeTroncons().size() == 1);
    }

    @Test
    public void testeDijkstraLongueurN() {
        Plan plan = obtenirPlan(cheminXml);
        Noeud noeudDepart = plan.getM_noeuds().get(0);
        Noeud noeudArrivee = plan.getM_noeuds().get(4);
        System.out.println(noeudArrivee.getM_id());
        Chemin resultat = Dijkstra.dijkstra_c(noeudDepart, noeudArrivee, plan);
        assertTrue(resultat.getListeTroncons().getFirst().getDepart().getM_id() == noeudDepart.getM_id());
        assertTrue(resultat.getListeTroncons().getLast().getArrivee().getM_id() == noeudArrivee.getM_id());
        assertTrue(resultat.getListeTroncons().size() == 3);
        assertTrue(resultat.getListeTroncons().get(1).getArrivee().getM_id() == 2);
    }

    public Plan obtenirPlan(String chemin) {
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
    }
}
