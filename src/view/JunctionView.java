package view;

import map.Junction;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Egy {@link Junction} csomópont vizuális megjelenítéséért felelős segédosztály.
 * A csomópontot kitöltött körként rajzolja ki a megadott pozícióba,
 * a kör közepén a csomópont nevével.
 *
 * <p>Az osztály kizárólag statikus rajzoló metódust tartalmaz; nem kell példányosítani.</p>
 */
public class JunctionView {

    private static final int RADIUS = 15;
    private static final Color FILL   = new Color(55, 55, 55);
    private static final Color BORDER = new Color(180, 180, 180);

    /**
     * Kirajzolja a megadott csomópontot a megadott képernyő-pozícióba.
     *
     * @param g2       a rajzoláshoz használt {@link Graphics2D} kontextus
     * @param junction a kirajzolandó csomópont
     * @param position a csomópont középpontjának képernyő-koordinátája
     */
    public static void draw(Graphics2D g2, Junction junction, Point position) {
        int x = position.x - RADIUS;
        int y = position.y - RADIUS;
        int d = RADIUS * 2;

        g2.setColor(FILL);
        g2.fillOval(x, y, d, d);

        g2.setColor(BORDER);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, d, d);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
        FontMetrics fm = g2.getFontMetrics();
        String name = junction.getName();
        g2.drawString(name,
            position.x - fm.stringWidth(name) / 2,
            position.y + fm.getAscent() / 2 - 1);
    }
}
