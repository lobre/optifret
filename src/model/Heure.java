package model;


/**
 * Classe heure, principalement utilis&eacute;e pour les plages horaires et heures de livraisons
 */
public class Heure {


    //
    // Fields
    //

    private int m_heures;
    private int m_minutes;
    private int m_secondes;

    public static final int PARSE_OK = 0;
    public static final int PARSE_ERROR = -1;

    //
    // Constructors
    //
    public Heure() {
    }

    public Heure(int heures, int minutes, int secondes) {
        this.m_heures = heures;
        this.m_minutes = minutes;
        this.m_secondes = secondes;
    }

    /**
     * Op&eacute;rateur de comparaison pour les objets Heure
     * @param a_comparer heures &agrave; laquelle on se compare
     * @return 1 si l'heure est plus grande (plus avanc&eacute;e, chronologiquement), -1 si elle est plus petite
     */
    public int compareTo(Heure a_comparer){
       if (this.m_heures==a_comparer.m_heures){
           if(this.m_minutes==a_comparer.m_minutes){
               return this.m_secondes > a_comparer.m_secondes ? 1:-1;
           }
           else{
               return this.m_minutes > a_comparer.m_minutes ? 1:-1;
           }
       }
        else {
           return this.m_heures > a_comparer.m_heures ? 1:-1;

       }
    }

    //
    // Methods
    //

    /**
     * Nombre total de secondes repr&eacute;sent&eacute;s par l'heure (depuis 00:00)
     * @return nombre total de secondes
     */
    public int getTotalSeconds() {
        return m_heures * 3600 + m_minutes * 60 + m_secondes;
    }

    //
    // Other methods
    //

    /**
     * Comparaison alternative des heures
     * @param h heure &agrave; comparer
     * @return vrai si l'heure est avant h, faux sinon.
     */
    public boolean estAvant(Heure h) {
        return getTotalSeconds() < h.getTotalSeconds();
    }

    /**
     * Parse un objet Heure depuis une repr&eacute;sentation textuelle de la forme "HH:MM:SS"
     * @param string repr&eacute;sentation textuelle d'une heure
     * @return PARSE_ERROR si le parsing a &eacute;chou&eacute;, PARSE_OK sinon.
     */
    public int fromString(String string) {

        String[] parts = string.split(":");
        if (parts.length != 3) {
            return Heure.PARSE_ERROR;
        }

        int h = Integer.parseInt(parts[0]);
        if (h < 0 || h > 23) {
            return Heure.PARSE_ERROR;
        }
        m_heures = h;

        int m = Integer.parseInt(parts[1]);
        if (h < 0 || h > 59) {
            return Heure.PARSE_ERROR;
        }
        m_minutes = m;

        int s = Integer.parseInt(parts[2]);
        if (h < 0 || h > 59) {
            return Heure.PARSE_ERROR;
        }
        m_secondes = s;

        return Heure.PARSE_OK;
    }

    /**
     * Repr&eacute;sentation textuelle de l'heure dans le format HH:MM:SS
     * @return repr&eacute;sentation textuelle de l'heure
     */
    @Override
    public String toString() {
        return String.format("%02d", m_heures) + ":" + String.format("%02d", m_minutes) + ":" + String.format("%02d", m_secondes);
    }
}
