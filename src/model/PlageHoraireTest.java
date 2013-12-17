package model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: karimalaoui
 * Date: 17/12/2013
 * Time: 21:08
 * To change this template use File | Settings | File Templates.
 */
public class PlageHoraireTest {

    Heure heure1 = new Heure(10,0,0);
    Heure heure2 = new Heure(12,30,30);
    Heure heure3 = new Heure(13,0,0);
    PlageHoraire plage1 = new PlageHoraire(heure1,heure2);
    PlageHoraire plage2 = new PlageHoraire(heure2,heure3);

    @Test
    public void testeToString(){
        assertTrue(plage1.toString().equals("10:00:00 - 12:30:30"));
    }

    @Test
    public void testeCompareTo(){
        assertTrue(plage1.compareTo(plage2)==-9030);
        assertTrue(plage1.compareTo(plage1)==0);
        assertTrue(plage2.compareTo(plage1)==9030);
    }

    @Test
    public void testeFromXML(){
        Plan plan = DijkstraTest.obtenirPlan("xml_tests/plan20x20-sens-unique.xml");
        // TODO: Finir
        // plageRetour.fromXML(?,plan);
        // J'ai créé un fichier XML qui contient une plage horaire, intitulé "testPlageHoraire.xml", pensez à l'utiliser ou le supprimer du repo.
    }
}
