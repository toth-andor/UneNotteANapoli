package view;

import controller.IController;
import controller.SetupState;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * A játék főablaka. {@link BorderLayout} elrendezést használ:
 * középen a {@link GamePanel} rajzfelület helyezkedik el, jobb oldalon
 * egy fenntartott vezérlőpanel, ahol a jövőbeli gombok kapnak helyet.
 *
 * <p>A jobb oldali panel alján egy jelmagyarázat mutatja a sávállapotok
 * és járműtípusok színkódjait.</p>
 *
 * <p>Az ablak fejléce 250 ms-enként frissül, és az aktuális játékállapotot
 * (beállítás vagy hányadik kör) jeleníti meg.</p>
 */
public class GameWindow extends JFrame {

    private final IController controller;

    /**
     * Létrehoz és inicializál egy új {@code GameWindow} példányt.
     * Az ablak elhelyezése a képernyő közepén történik.
     *
     * @param controller a megjelenítendő modellt és játékállapotot tartalmazó kontroller
     */
    public GameWindow(IController controller) {
        this.controller = controller;

        setTitle("Une Notte a Napoli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new GamePanel(controller), BorderLayout.CENTER);
        add(buildSidePanel(), BorderLayout.EAST);

        setPreferredSize(new Dimension(1050, 720));
        pack();
        setLocationRelativeTo(null);

        new Timer(250, e -> updateTitle()).start();
    }

    /**
     * Felépíti a jobb oldali vezérlőpanelt.
     * A panel közepén egy placeholder szöveg jelzi a jövőbeli gomboknak fenntartott helyet,
     * alján a sávállapotok és járműtípusok jelmagyarázata látható.
     *
     * @return a kész vezérlőpanel
     */
    private JPanel buildSidePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(190, 0));
        panel.setBackground(new Color(40, 40, 40));

        JLabel placeholder = new JLabel(
            "<html><center><b>Vezérlők</b><br><font color='#888'>(hamarosan)</font></center></html>",
            SwingConstants.CENTER);
        placeholder.setForeground(new Color(160, 160, 160));
        panel.add(placeholder, BorderLayout.CENTER);
        panel.add(buildLegend(), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Felépíti a jelmagyarázat panelt, amely tartalmazza a sávállapotok
     * és a járműtípusok színkódjait.
     *
     * @return a kész jelmagyarázat panel
     */
    private JPanel buildLegend() {
        JPanel legend = new JPanel(new GridLayout(0, 1, 1, 1));
        legend.setBackground(new Color(35, 35, 35));
        legend.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 90)),
            "Sávállapotok",
            0, 0,
            new Font("SansSerif", Font.BOLD, 10),
            new Color(170, 170, 170)));

        addRow(legend, new Color(115, 115, 115), "Száraz (OutdoorLane)");
        addRow(legend, new Color(173, 216, 230), "Havas");
        addRow(legend, new Color(65,  105, 225), "Jeges");
        addRow(legend, new Color(210, 170,   0), "Sózott");
        addRow(legend, new Color(139, 100,  20), "Zúzalékos");
        addRow(legend, new Color(200,  20,  50), "Baleset");
        addRow(legend, new Color(75,   75, 100), "Alagút (TunnelLane)");

        JPanel vehicleLegend = new JPanel(new GridLayout(0, 1, 1, 1));
        vehicleLegend.setBackground(new Color(35, 35, 35));
        vehicleLegend.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 90)),
            "Járművek",
            0, 0,
            new Font("SansSerif", Font.BOLD, 10),
            new Color(170, 170, 170)));

        addRow(vehicleLegend, new Color(0,   200, 220), "Hókotró (SnowPlow)");
        addRow(vehicleLegend, new Color(255, 140,   0), "Busz (Bus)");
        addRow(vehicleLegend, new Color(80,  160, 255), "Autó (Car)");

        JPanel combined = new JPanel(new GridLayout(2, 1));
        combined.setBackground(new Color(35, 35, 35));
        combined.add(legend);
        combined.add(vehicleLegend);
        return combined;
    }

    /**
     * Hozzáad egy színkódos sort a megadott panelhez.
     *
     * @param panel a célpanel
     * @param color a sor bal oldalán megjelenő színminta
     * @param text  a színminta mellé írt felirat
     */
    private void addRow(JPanel panel, Color color, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 1));
        row.setBackground(new Color(35, 35, 35));

        JLabel box = new JLabel("  ");
        box.setOpaque(true);
        box.setBackground(color);
        box.setPreferredSize(new Dimension(14, 10));

        JLabel label = new JLabel(text);
        label.setForeground(new Color(190, 190, 190));
        label.setFont(new Font("SansSerif", Font.PLAIN, 10));

        row.add(box);
        row.add(label);
        panel.add(row);
    }

    /**
     * Frissíti az ablak fejlécét az aktuális játékállapot alapján.
     * Beállítás fázisban "Beállítás", játék közben az aktuális körszám jelenik meg.
     */
    private void updateTitle() {
        String state = controller.getGameState() instanceof SetupState
            ? "Beállítás"
            : controller.getRoundNumber() + ". kör";
        setTitle("Une Notte a Napoli  –  " + state);
    }
}
