package model;


/**
 * Class Heure
 */
public class Heure {


    //
    // Fields
    //

    private int m_heures;
    private int m_minutes;
    private int m_secondes;

    public static int PARSE_OK = 0;
    public static int PARSE_ERROR = -1;

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
    public int getTotalSeconds() {
        return m_heures * 3600 + m_minutes * 60 + m_secondes;
    }

    //
    // Other methods
    //

    public boolean estAvant(Heure h) {
        return getTotalSeconds() < h.getTotalSeconds();
    }

    //format hh:mm:ss
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

    //format hh:mm:ss
    @Override
    public String toString() {
        return String.format("%02d", m_heures) + ":" + String.format("%02d", m_minutes) + ":" + String.format("%02d", m_secondes);
    }
}
