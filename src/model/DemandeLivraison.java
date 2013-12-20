package model;

import libs.ParseXmlException;
import model.tsp.GraphImpl;
import model.tsp.TSP;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Demande de livraison: un ensemble de plages horaires contenant chacune une ou plusieurs livraisons.
 */
public class DemandeLivraison {

    private Plan m_plan;
    private Noeud m_entrepot;
    private ArrayList<PlageHoraire> m_plagesHoraires;

    private final Map<Integer, Map<Integer, Chemin>> m_chemins;

    //
    // Constructors
    //

    /**
     * Constructeur d'une demande de livraison
     *
     * @param plan le plan de la zone g&eacute;ographique sur laquelle se fait la demande de livraison.
     */
    public DemandeLivraison(Plan plan) {
        m_chemins = new HashMap<>();
        this.m_plagesHoraires = new ArrayList<>();
        m_plan = plan;
    }

    //
    // Accessor methods
    //

    /**
     * @return le Nœud où est basé l'entrepôt.
     */
    public Noeud getEntrepot() {
        return m_entrepot;
    }

    /**
     * @return la liste ordonnée des plages horaires corncernées par cette demande de livraison.
     */
    public ArrayList<PlageHoraire> getPlagesHoraires() {
        return m_plagesHoraires;
    }

    private void ajouterPlageH(PlageHoraire plage) {
        m_plagesHoraires.add(plage);
    }

    /**
     * Ajoute la livraison à la plage horaire correspondante dans la liste des plages horaires.
     *
     * @param livraison livraison que l'on veut ajouter dans sa plage horaire.
     */
    public void ajouterLivraison(Livraison livraison) {
        for (PlageHoraire ph : m_plagesHoraires) {
            if (ph == livraison.getPlage()) {
                ph.addLivraison(livraison);
                return;
            }
        }
    }

    /**
     * Supprime la livraison de sa plage horaire correspondante dans la liste des plages horaires.
     *
     * @param livraison livraison que l'on veut supprimer de sa plage horaire.
     */
    public void supprimerLivraison(Livraison livraison) {
        for (PlageHoraire ph : m_plagesHoraires) {
            if (ph == livraison.getPlage()) {
                ph.removeLivraison(livraison);
                return;
            }
        }
    }

    //
    // Other methods
    //

    /**
     * Affecte à null les heures de passage effectif des livraisons de la demande de livraison this.
     */
    public void razHeuresLivraisons() {
        for (PlageHoraire plageHoraire : m_plagesHoraires) {
            for (Livraison livraison : plageHoraire.getLivraisons()) {
                livraison.setHeureLivraison(null);
            }
        }
    }

    /**
     * Obtient un ID numérique servant à identifier les livraisons. Cet ID est garanti unique, non utilisé pour une
     * autre livraison parmi l'ensemble des livraisons présentes dans toutes les plage horaire.
     *
     * @return un ID encore non utilisé pour identifier de façon unique une livraison.
     */
    public int getUniqueID() {
        int maxID = 0;
        for (PlageHoraire ph : m_plagesHoraires) {
            for (Livraison livraison : ph.getLivraisons()) {
                maxID = livraison.getId() > maxID ? livraison.getId() : maxID;
            }
        }

        return maxID + 1;
    }

    /**
     * Crée une FeuilleRoute à partir de l'objet this.
     *
     * @return la feuille de route créée.
     */
    public FeuilleRoute calculerFeuilleDeRoute(int timeLimit) {
        GraphImpl graph = new GraphImpl();
        remplirGrapheAvecPEtI(m_plagesHoraires.get(0), m_entrepot, graph, m_plan);
        for (int i = 0; i < m_plagesHoraires.size() - 1; i++) {
            remplirGrapheAvecIEtIPluzun(m_plagesHoraires.get(i), m_plagesHoraires.get(i + 1), graph, m_plan);
        }
        remplirGrapheAvecIEtP(m_plagesHoraires.get(m_plagesHoraires.size() - 1), m_entrepot, graph, m_plan);

        TSP tsp = new TSP(graph);
        // On tente de le résoudre en X\1000 secondes.
        tsp.solve(timeLimit, graph.getNbVertices() * graph.getMaxArcCost() + 1);
        return new FeuilleRoute(tsp, m_chemins, graph.getMatches(), graph.getCost(), this);
    }

    /**
     * Parse un élément XML correspondant à une demande de livraison, afin d'en extraire tous les éléments. Met à jour
     * la liste de plage horaire en la remplissant avec les différentes livraisons correspondantes.
     *
     * @param racineXML (Element) correspondant au document XML à parser.
     * @throws ParseXmlException :
     *                           si les plages horaires se chevauchent
     *                           si il y a plusieurs ou aucun entrepôts,
     *                           si il y a plusieurs listes de plages horaires
     *                           si les horaires sont mal formattés
     *                           si il y a plus d'une liste de livraisons par plage horaire,
     *                           si une livraison doit avoir lieu sur un noeud du graph (une adresse) non existant
     */
    public void fromXML(Element racineXML) throws ParseXmlException {

        //récupération de l'entrepôt
        NodeList entre = racineXML.getElementsByTagName("Entrepot");
        if (entre.getLength() != 1) {
            throw new ParseXmlException("Pas d'élément entrepot");
        }

        String ad = ((Element) entre.item(0)).getAttribute("adresse");
        m_entrepot = m_plan.getNoeudParID(Integer.parseInt(ad));
        if (m_entrepot == null) {
            throw new ParseXmlException("Adresse de l'entrepot introuvable");
        }
        m_entrepot.setEntrepot(true);

        //récupération des plages horaires
        NodeList n_plages = racineXML.getElementsByTagName("PlagesHoraires");
        if (n_plages.getLength() != 1) {
            throw new ParseXmlException("Élément '<PlagesHoraires>' introuvable");
        }

        NodeList liste_plages = racineXML.getElementsByTagName("Plage");
        if (liste_plages.getLength() < 1) {
            throw new ParseXmlException("Élément '<Plage>' introuvable");
        }

        for (int i = 0; i < liste_plages.getLength(); i++) {
            //nouvelle plage horaire
            Element e_plage = (Element) liste_plages.item(i);
            PlageHoraire plage = new PlageHoraire();
            if (plage.fromXML(e_plage, m_plan) != PlageHoraire.PARSE_OK) {
                throw new ParseXmlException("Plage horaire invalide");
            }
            this.ajouterPlageH(plage);
        }

        // Validation des plages horaires
        if (!plagesValides()) {
            throw new ParseXmlException("Des plages horaires se chevauchent");
        }

    }

    /**
     * Valide les plages horaires de la demande de livraison: v&eacute;rifie qu'il n y a pas de chevauchement.
     *
     * @return true s'il n y a pas de chevauchement de plages horaires, false sinon.
     */
    private boolean plagesValides() {
        Collections.sort(m_plagesHoraires);
        for (int i = 0; i < m_plagesHoraires.size() - 1; i++) {
            PlageHoraire ph1 = m_plagesHoraires.get(i);
            PlageHoraire ph2 = m_plagesHoraires.get(i + 1);
            if (ph2.getHeureDebut().estAvant(ph1.getHeureFin())) {
                // Chevauchement de deux plages horaires
                throw new ParseXmlException("Plages horaires en conflit");
            }
            ph1.setIndice(i);
            ph2.setIndice(i + 1);
        }

        return true;
    }

    /**
     * Signale si la demande de livraison est vide (ne contient aucune livraison).
     *
     * @return true si la demande de livraison ne contient aucune livraison, false sinon.
     */
    public boolean isEmpty() {
        for (PlageHoraire ph : m_plagesHoraires) {
            if (ph.getLivraisons().size() != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Permet d'obtenir la liste des livraisons prévues par la demande this.
     *
     * @return
     */
    public ArrayList<Livraison> getLivraisons() {
        ArrayList<Livraison> livraisons = new ArrayList<>();
        for (PlageHoraire ph : m_plagesHoraires) {
            livraisons.addAll(ph.getLivraisons());
        }
        return livraisons;
    }


    /**
     * Calcule le poids optimal du trajet entre un noeud particulier du graphe et les noeuds d'ordre i.
     *
     * @param i     PlageHoraire d'ordre i (i étant le rang de la PlageHoraire dans la journée)
     * @param noeud noeud particulier du graphe
     * @param graph graphe à remplir
     * @param plan  l'environnement de calcul
     */
    private void remplirGrapheAvecPEtI(PlageHoraire i, Noeud noeud, GraphImpl graph, Plan plan) {
        for (Livraison livraison : i.getLivraisons()) {
            Chemin chemin = Dijkstra.dijkstra_c(noeud, livraison.getAdresse(), plan);
            int id1 = noeud.getId();
            int id2 = livraison.getAdresse().getId();
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
    private void remplirGrapheAvecIEtIPluzun(PlageHoraire i, PlageHoraire iPluzun, GraphImpl graph, Plan plan) {
        for (Livraison livraison : i.getLivraisons()) {
            for (Livraison autreLivraisonDeRangI : i.getLivraisons()) {
                if (!livraison.equals(autreLivraisonDeRangI)) {
                    Chemin chemin = Dijkstra.dijkstra_c(livraison.getAdresse(), autreLivraisonDeRangI.getAdresse(), plan);
                    int id1 = livraison.getAdresse().getId();
                    int id2 = autreLivraisonDeRangI.getAdresse().getId();
                    fillCost(id1, id2, chemin.getLongueur(), graph);
                    fillChemin(id1, id2, chemin);
                }
            }
            for (Livraison autreLivraisonDeRangIPluzun : iPluzun.getLivraisons()) {
                Chemin chemin = Dijkstra.dijkstra_c(livraison.getAdresse(), autreLivraisonDeRangIPluzun.getAdresse(), plan);
                int id1 = livraison.getAdresse().getId();
                int id2 = autreLivraisonDeRangIPluzun.getAdresse().getId();
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
    private void remplirGrapheAvecIEtP(PlageHoraire i, Noeud noeud, GraphImpl graph, Plan plan) {
        for (Livraison livraison : i.getLivraisons()) {
            for (Livraison autreLivraisonDeRangI : i.getLivraisons()) {
                if (!livraison.equals(autreLivraisonDeRangI)) {
                    Chemin chemin = Dijkstra.dijkstra_c(livraison.getAdresse(), autreLivraisonDeRangI.getAdresse(), plan);
                    int id1 = livraison.getAdresse().getId();
                    int id2 = autreLivraisonDeRangI.getAdresse().getId();
                    fillCost(id1, id2, chemin.getLongueur(), graph);
                    fillChemin(id1, id2, chemin);
                }
            }
            Chemin chemin = Dijkstra.dijkstra_c(livraison.getAdresse(), noeud, plan);
            int id1 = livraison.getAdresse().getId();
            int id2 = noeud.getId();
            fillCost(id1, id2, chemin.getLongueur(), graph);
            fillChemin(id1, id2, chemin);
        }
    }

    /**
     * Ajoute le chemin <chemin>, partant de <from> et arrivant <to> dans l'attribut m_chemin de notre classe.
     *
     * @param from   ID de la livraison de départ
     * @param to     ID de la livraison d'arrivé du chemin
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
     *
     * @param from  ID de la livraison d'où commence le chemin auquel on veut donner un cout (poids)
     * @param to    ID de la livraison d'où fini le chemin auquel on veut donner un cout (poids)
     * @param cost  coût (poids) à donner au chemin
     * @param graph graphique sur lequel on veut donner un cout (poids) à un chemin.
     */
    private void fillCost(int from, int to, int cost, GraphImpl graph) {
        if (graph.getCosts().get(from) == null) {
            graph.getCosts().put(from, new HashMap<Integer, Integer>());
        }
        graph.getCosts().get(from).put(to, cost);
    }
}
