package view;

import controller.Controleur;
import model.FeuilleRoute;

import javax.swing.*;
import java.awt.event.*;

/**
 * Fen&ecirc;tre affichant la version texte de l'itin&eacute;raire d'une tourn&eacute;e (Feuille de route)
 */
public class FenetreImprimerFeuilleRoute extends JDialog {
    private JPanel contentPane;
    private JButton m_buttonFermer;
    private JTextArea m_textArea;
    private JButton m_enregistrerButton;

    private Controleur m_controleur;
    private VueFeuilleRoutePapier m_vueFeuilleRoute;

    /**
     * Constructeur de la fen&ecirc;tre
     *
     * @param controleur   contr&ocirc;leur de l'application
     * @param feuilleRoute feuille de route dont la version texte doit &ecirc;tre affich&eacute;e
     */
    public FenetreImprimerFeuilleRoute(Controleur controleur, FeuilleRoute feuilleRoute) {
        m_controleur = controleur;
        m_vueFeuilleRoute = new VueFeuilleRoutePapier(feuilleRoute);
        m_textArea.setText(m_vueFeuilleRoute.getVersionPapier());
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(m_buttonFermer);


        m_enregistrerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m_controleur.enregistrerFeuilleRoute(m_vueFeuilleRoute);
            }
        });
        m_buttonFermer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onCancel() {
        dispose();
    }
}
