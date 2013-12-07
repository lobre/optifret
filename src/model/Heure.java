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



    //
    // Methods
    //


    //
    // Accessor methods
    //

    public void setM_heures(int newVar) {
        m_heures = newVar;
    }
    public int getM_heures() {
        return m_heures;
    }

    public void setM_minutes(int newVar) {
        m_minutes = newVar;
    }
    public int getM_minutes() {
        return m_minutes;
    }

    public void setM_secondes(int newVar) {
        m_secondes = newVar;
    }
    public int getM_secondes() {
        return m_secondes;
    }
    //
    // Other methods
    //

    public boolean estAvant(Heure h){
        if (this.getM_heures() < h.getM_heures()) {
            return true;
        }
        if (this.getM_heures() == h.getM_heures()) {

            if (this.getM_minutes() < h.getM_minutes()) {
                return  true;
            }
        }
        return false;
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

    @Override
    public String toString() {
        return  String.format("%02d",m_heures) + ":" + String.format("%02d",m_minutes) + ":" + String.format("%02d",m_secondes) ;
    }
}
