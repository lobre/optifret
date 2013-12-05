package view;

import javax.swing.*;
import java.awt.*;

public class ZoneNotification extends JFormattedTextField {

    private Font m_font;

    public ZoneNotification() {

        setEditable(false);

        m_font = new Font("Arial", Font.BOLD, 14);
        setFont(m_font);
        setForeground(Color.white);

        setInfoMessage("Bienvenue sur l'application Optifret !");
    }

    public void setSuccessMessage(String text) {
        setBackground(new Color(144, 230, 76));
        setValue(text);
    }

    public void setErrorMessage(String text) {
        setBackground(new Color(230, 68, 25));
        setValue(text);
    }

    public void setInfoMessage(String text) {
        setBackground(new Color(230, 180, 0));
        setValue(text);
    }

}
