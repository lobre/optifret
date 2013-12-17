package view;

import controller.Controleur;
import model.FeuilleRoute;

import javax.swing.*;
import java.awt.event.*;

public class FenetreImprimerFeuilleRoute extends JDialog {
    private JPanel contentPane;
    private JButton m_buttonFermer;
    private JTextArea m_textArea;
    private JButton m_enregistrerButton;

    private Controleur m_controleur;
    private VueFeuilleRoutePapier m_vueFeuilleRoute;

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
