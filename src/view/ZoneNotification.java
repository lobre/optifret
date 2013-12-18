package view;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Composant graphique permettant d'afficher un message de notification.
 * Les messages affich&eacute;s peuvent &ecirc;tre de diff&eacute;rents types (information, erreur, succ&egrave;s)
 * et ont des apparences diff&eacute;rentes.
 */
public class ZoneNotification extends JLabel {

    private static final long NOTIFICATION_TIMEOUT = 8000;
    private Font m_font;
    private Timer m_timer;

    private static int HAUTEUR_DEFAUT = 35;
    private static int LARGEUR_DEFAUT = 350;

    private static Color COULEUR_INFO = new Color(60, 140, 235);
    private static Color COULEUR_SUCCES = new Color(42, 210, 48);
    private static Color COULEUR_ERREUR = new Color(230, 68, 25);

    /**
     * Construteur ed la zone de notification
     */
    public ZoneNotification() {
        super("", SwingConstants.CENTER);
        m_timer = new Timer();
        m_font = new Font("Arial", Font.BOLD, 14);
        setFont(m_font);
        setForeground(Color.white);
        setOpaque(true);
        setBackground(COULEUR_INFO);

        setPreferredSize(new Dimension(LARGEUR_DEFAUT, HAUTEUR_DEFAUT));
    }

    /**
     * Affiche un message "succ&egrave;s"
     * @param text message &agrave; afficher
     */
    public void setSuccessMessage(String text) {
        setBackground(COULEUR_SUCCES);
        getFontMetrics(getFont()).stringWidth(text);
        setText(text);
        hideAfterTimeout();
    }


    /**
     * Affiche un message "erreur"
     * @param text message &agrave; afficher
     */
    public void setErrorMessage(String text) {
        setBackground(COULEUR_ERREUR);
        setText(text);
        hideAfterTimeout();
    }


    /**
     * Affiche un message "info"
     * @param text message &agrave; afficher
     */
    public void setInfoMessage(String text) {
        setBackground(COULEUR_INFO);
        setText(text);
        hideAfterTimeout();
    }

    private void hideAfterTimeout() {
        setVisible(true);

        m_timer.cancel();
        m_timer.purge();
        m_timer = new Timer();

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setInfoMessage(" ");
            }
        }, NOTIFICATION_TIMEOUT);
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
