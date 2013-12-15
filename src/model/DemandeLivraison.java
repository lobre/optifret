package model;

import model.tsp.GraphImpl;
import model.tsp.TSP;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import libs.ParseXmlException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class DemandeLivraison
 */
public class DemandeLivraison {

    static public int PARSE_ERROR = -1;
    static public int PARSE_OK = 0;

    private Plan m_plan;
    private Noeud m_entrepot;
    private ArrayList<PlageHoraire> m_plagesHoraires;

    private final Map<Integer, Map<Integer, Chemin>> m_chemins;

    //
    // Constructors
    //
    public DemandeLivraison(Plan plan) {
        m_chemins = new HashMap<>();
        this.m_plagesHoraires = new ArrayList<>();
        m_plan = plan;
    }

    //
    // Accessor methods
    //
    public Noeud getEntrepot() {
        return m_entrepot;
    }
    public ArrayList<PlageHoraire> getM_plagesHoraires() {
        return m_plagesHoraires;
    }

    private void ajouterPlageH(PlageHoraire plage) {
        m_plagesHoraires.add(plage);
    }

    /**
     * Ajoute la livraison à la plage horaire correspondante dans la liste des plages horaires.
     * @param livraison  livraison que l'on veut ajouter dans sa plage horaire.
     */
    public void ajouterLivraison(Livraison livraison) {
        for (PlageHoraire ph : m_plagesHoraires) {
            if (ph == livraison.getM_plage()) {
                ph.addLivraison(livraison);
                return;
            }
        }
    }

    /**
     * Supprime la livraison de sa plage horaire correspondante dans la liste des plages horaires.
     * @param livraison livraison que l'on veut supprimer de sa plage horaire.
     */
    public void supprimerLivraison(Livraison livraison) {
        for (PlageHoraire ph : m_plagesHoraires) {
            if (ph == livraison.getM_plage()) {
                ph.removeLivraison(livraison);
                return;
            }
        }
    }

    //
    // Other methods
    //

    /**
     * Obtient un ID numérique servant à identifier les livraisons. Cet ID est garanti unique, non utilisé pour une
     * autre livraison parmi l'ensemble des livraisons présentes dans toutes les plage horaire.
     * @return un ID encore non utilisé pour identifier de façon unique une livraison.
     */
    public int getUniqueID() {
        int maxID = 0;
        for (PlageHoraire ph : m_plagesHoraires) {
            for (Livraison livraison : ph.getM_livraisons()) {
                maxID = livraison.getM_id() > maxID ? livraison.getM_id() : maxID;
            }
        }

        return maxID + 1;
    }

    /**
     * Crée une FeuilleRoute à partir de l'objet this.
     *
     * @return la feuille de route créée.
     */
    public FeuilleRoute calculerFeuilleDeRoute() {
        GraphImpl graph = new GraphImpl();
        doSomeFirstCalc(m_plagesHoraires.get(0), m_entrepot, graph, m_plan);
        for (int i = 0; i < m_plagesHoraires.size() - 1; i++) {
            doSomeCalc(m_plagesHoraires.get(i), m_plagesHoraires.get(i + 1), graph, m_plan);
        }
        doSomeOtherCalc(m_plagesHoraires.get(m_plagesHoraires.size() - 1), m_entrepot, graph, m_plan);

        TSP tsp = new TSP(graph);
        // On tente de le résoudre en X\1000 secondes.
        tsp.solve(10000, graph.getNbVertices() * graph.getMaxArcCost() + 1);
        return new FeuilleRoute(tsp, m_chemins, graph.getMatches(), this);
    }

    /**
     * Parse un élément XML correspondant à une demande de livraison, afin d'en extraire tous les éléments. Met à jour
     * la liste de plage horaire en la remplissant avec les différentes livraisons correspondantes.
     * @param racineXML  (Element) correspondant au document XML à parser.
     * @throws ParseXmlException :
     *                  si les plages horaires se chevauchent
     *                  si il y a plusieurs ou aucun entrepôts,
     *                  si il y a plusieurs listes de plages horaires
     *                  si les horaires sont mal formattés
     *                  si il y a plus d'une liste de livraisons par plage horaire,
     *                  si une livraison doit avoir lieu sur un noeud du graph (une adresse) non existant
     */
    public void fromXML(Element racineXML) throws ParseXmlException {

        //récupération de l'entrepôt
        NodeList entre = racineXML.getElementsByTagName("Entrepot");
        if (entre.getLength() != 1) {
            throw new ParseXmlException("Element Entrepot");
        }

        String ad = ((Element) entre.item(0)).getAttribute("adresse");
        m_entrepot = m_plan.getNoeudParID(Integer.parseInt(ad));
        if (m_entrepot == null) {
            throw new ParseXmlException("Adresse entrepot introuvable");
        }
        m_entrepot.setM_entrepot(true);

        //récupération des plages horaires
        NodeList n_plages = racineXML.getElementsByTagName("PlagesHoraires");
        if (n_plages.getLength() != 1) {
            throw new ParseXmlException("Element<PlagesHoraires> introuvable");
        }

        NodeList liste_plages = racineXML.getElementsByTagName("Plage");
        if (liste_plages.getLength() < 1) {
            throw new ParseXmlException("Element <Plage> introuvable");
        }

        for (int i = 0; i < liste_plages.getLength(); i++) {

            //nouvelle plage horaire
            Element e_plage = (Element) liste_plages.item(i);

            //récupération heures de début et de départ
            String h1 = e_plage.getAttribute("heureDebut");
            String h2 = e_plage.getAttribute("heureFin");
            Heure hDepart = new Heure();
            Heure hFin = new Heure();

            if (hDepart.fromString(h1) == PARSE_ERROR || hFin.fromString(h2) == PARSE_ERROR || !hDepart.estAvant(hFin)) {
                throw new ParseXmlException("Heure non valide");
            }
            PlageHoraire plage = new PlageHoraire(hDepart, hFin);

            //récupération des livraisons de la plage horaire
            NodeList livraisons = e_plage.getElementsByTagName("Livraisons");
            if (livraisons.getLength() != 1) {
                throw new ParseXmlException("Element <Livraisons> introuvable");
            }

            NodeList listeLivraisons = ((Element) livraisons.item(0)).getElementsByTagName("Livraison");
            for (int j = 0; j < listeLivraisons.getLength(); j++) {
                Element eLivraison = (Element) listeLivraisons.item(j);
                int id = Integer.parseInt(eLivraison.getAttribute("id"));
                int client = Integer.parseInt(eLivraison.getAttribute("client"));
                int adNoeud = Integer.parseInt(eLivraison.getAttribute("adresse"));
                Noeud noeud = m_plan.getNoeudParID(adNoeud);
                for ( Livraison l: plage.getM_livraisons() ){
                    if (l.getM_id()==id){
                        throw new ParseXmlException("id livraison non-unique");
                    }
                }
                if (noeud == null) {
                    throw new ParseXmlException("null node exception");
                }
                Livraison livraison = new Livraison(id, client, noeud, plage);
                plage.addLivraison(livraison);
            }
            this.ajouterPlageH(plage);
        }

        // Validation des plages horaires
        Collections.sort(m_plagesHoraires);
        for (int i = 0; i < m_plagesHoraires.size() - 1; i++) {
            PlageHoraire ph1 = m_plagesHoraires.get(i);
            PlageHoraire ph2 = m_plagesHoraires.get(i + 1);
            if (ph2.getHeureDebut().estAvant(ph1.getHeureFin())) {
                // Chevauchement de deux plages horaires
                throw new ParseXmlException("plages horaires en conflit");
            }
            ph1.setM_indice(i);
            ph2.setM_indice(i + 1);
        }

    }

    /**
     * Calcule le poids optimal du trajet entre un noeud particulier du graphe et les noeuds d'ordre i.
     *
     * @param i     PlageHoraire d'ordre i (i étant le rang de la PlageHoraire dans la journée)
     * @param noeud noeud particulier du graphe
     * @param graph graphe à remplir
     * @param plan  l'environnement de calcul
     */
    private void doSomeFirstCalc(PlageHoraire i, Noeud noeud, GraphImpl graph, Plan plan) {
        for (Livraison livraison : i.getM_livraisons()) {
            Chemin chemin = Dijkstra.dijkstra_c(noeud, livraison.getM_adresse(), plan);
            int id1 = noeud.getM_id();
            int id2 = livraison.getM_adresse().getM_id();
            fillCost(id1, id2, chemin.getLongueur(), graph);
            fillChemin(id1, id2, chemin);
        }
    }

    /**
     * Met à jour les poids optimaux entre les livraisons d'ordre i.
     * Résout également les relations avec les livraisons d'ordre i+1
     *
     * @param i       PlageHoraire d'ordre i
     * @param iPluzun PlageHoraire d'ordre i+1
     * @param graph   graphe à remplir
     * @param plan    l'environnement de calcul
     */
    private void doSomeCalc(PlageHoraire i, PlageHoraire iPluzun, GraphImpl graph, Plan plan) {
        for (Livraison livraison : i.getM_livraisons()) {
            for (Livraison autreLivraisonDeRangI : i.getM_livraisons()) {
                if (!livraison.equals(autreLivraisonDeRangI)) {
                    Chemin chemin = Dijkstra.dijkstra_c(livraison.getM_adresse(), autreLivraisonDeRangI.getM_adresse(), plan);
                    int id1 = livraison.getM_adresse().getM_id();
                    int id2 = autreLivraisonDeRangI.getM_adresse().getM_id();
                    fillCost(id1, id2, chemin.getLongueur(), graph);
                    fillChemin(id1, id2, chemin);
                }
            }
            for (Livraison autreLivraisonDeRangIPluzun : iPluzun.getM_livraisons()) {
                Chemin chemin = Dijkstra.dijkstra_c(livraison.getM_adresse(), autreLivraisonDeRangIPluzun.getM_adresse(), plan);
                int id1 = livraison.getM_adresse().getM_id();
                int id2 = autreLivraisonDeRangIPluzun.getM_adresse().getM_id();
                fillCost(id1, id2, chemin.getLongueur(), graph);
                fillChemin(id1, id2, chemin);
            }
        }
    }

    /**
     * Met à jour les poids optimaux entre les livraisons d'ordre i, en excluant les relations avec les livraisons d'ordre i+1.
     * Calcule également le poids optimal du trajet entre les livraisons d'ordre i et un noeud particulier du graphe
     *
     * @param i     PlageHoraire d'ordre i (i étant le rang de la PlageHoraire dans la journée)
     * @param noeud noeud particulier du graphe
     * @param graph graphe à remplir
     * @param plan  l'environnement de calcul
     */
    private void doSomeOtherCalc(PlageHoraire i, Noeud noeud, GraphImpl graph, Plan plan) {
        for (Livraison livraison : i.getM_livraisons()) {
            for (Livraison autreLivraisonDeRangI : i.getM_livraisons()) {
                if (!livraison.equals(autreLivraisonDeRangI)) {
                    Chemin chemin = Dijkstra.dijkstra_c(livraison.getM_adresse(), autreLivraisonDeRangI.getM_adresse(), plan);
                    int id1 = livraison.getM_adresse().getM_id();
                    int id2 = autreLivraisonDeRangI.getM_adresse().getM_id();
                    fillCost(id1, id2, chemin.getLongueur(), graph);
                    fillChemin(id1, id2, chemin);
                }
            }
            Chemin chemin = Dijkstra.dijkstra_c(livraison.getM_adresse(), noeud, plan);
            int id1 = livraison.getM_adresse().getM_id();
            int id2 = noeud.getM_id();
            fillCost(id1, id2, chemin.getLongueur(), graph);
            fillChemin(id1, id2, chemin);
        }
    }

    /**
     * Ajoute le chemin <chemin>, partant de <from> et arrivant <to> dans l'attribut m_chemin de notre classe.
     * @param from ID de la livraison de départ
     * @param to  ID de la livraison d'arrivé du chemin
     * @param chemin chemin entre deux livraisons.
     */
    private void fillChemin(int from, int to, Chemin chemin) {
        if (m_chemins.get(from) == null) {
            m_chemins.put(from, new HashMap<Integer, Chemin>());
        }
        m_chemins.get(from).put(to, chemin);
    }

    /**
     * Donne le cout (poids) <cost> au chemin partant de la livraison d'ID <from> à la livraison d'ID <to>
     * dans le graphique <graph>
     * @param from ID de la livraison d'où commence le chemin auquel on veut donner un cout (poids)
     * @param to ID de la livraison d'où fini le chemin auquel on veut donner un cout (poids)
     * @param cost coût (poids) à donner au chemin
     * @param graph graphique sur lequel on veut donner un cout (poids) à un chemin.
     */
    private void fillCost(int from, int to, int cost, GraphImpl graph) {
        if (graph.getCosts().get(from) == null) {
            graph.getCosts().put(from, new HashMap<Integer, Integer>());
        }
        graph.getCosts().get(from).put(to, cost);
    }
}
