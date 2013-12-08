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
    }

    public void setSuccessMessage(String text) {
        setBackground(new Color(42, 210, 48));
        setValue("  " + text);
    }

    public void setErrorMessage(String text) {
        setBackground(new Color(230, 68, 25));
        setValue("  " + text);
    }

    public void setInfoMessage(String text) {
        setBackground(new Color(60, 140, 235));
        setValue("  " + text);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        super.paintComponent(g2);
    }
}
