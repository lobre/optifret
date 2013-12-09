package view;

import model.Troncon;

import java.awt.*;

public class VueTroncon {

    private Troncon m_troncon;

    public static Color COULEUR_DEFAUT = new Color(89, 147, 221);

    public VueTroncon(Troncon troncon) {
        m_troncon = troncon;
    }

    // Accessors:
    public Troncon getM_troncon() {
        return m_troncon;
    }
    public void setM_troncon(Troncon troncon) {
        this.m_troncon = troncon;
    }

    // Other methods:
    public Color getColor() {
        return COULEUR_DEFAUT;
    }

}
