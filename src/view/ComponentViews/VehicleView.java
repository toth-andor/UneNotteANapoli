package view.ComponentViews;

import vehicle.Bus;
import vehicle.Car;
import vehicle.Commuter;
import vehicle.SnowPlow;
import vehicle.Vehicle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Egy {@link Vehicle} jármű vizuális megjelenítéséért felelős segédosztály.
 * A járművet kitöltött körként rajzolja ki; a kör színe a jármű típusát jelzi.
 *
 * <p>Az osztály kizárólag statikus metódusokat tartalmaz; nem kell példányosítani.</p>
 */
public class VehicleView {

    /**
     * Kirajzolja a megadott járművet a megadott képernyő-pozícióba.
     *
     * @param g2      a rajzoláshoz használt {@link Graphics2D} kontextus
     * @param vehicle a kirajzolandó jármű
     * @param x       a jármű középpontjának x-koordinátája
     * @param y       a jármű középpontjának y-koordinátája
     */
    public static void draw(Graphics2D g2, Vehicle vehicle, int x, int y, boolean isActive, int timestamp) {
        if (isActive) {
            Color glow  = new Color(255, 50, 200, 80);
            g2.setColor(glow);
            g2.fillOval(x - 14, y - 14, 28, 28);
            Color glow2 = new Color(255, 50, 200, 40);
            g2.setColor(glow2);
            g2.fillOval(x - 20, y - 20, 40, 40);

           
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - 10, y - 10, 20, 20);

            g2.setColor(new Color(255, 50, 200, 220));
            g2.setStroke(new BasicStroke(1));
            g2.drawOval(x - 15, y - 15, 30, 30);
        }

        boolean immobile = vehicle.isImmobile(timestamp);
        if (immobile) {
            g2.setColor(new Color(255, 0, 0, 150));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(x - 10, y - 10, 20, 20);
        }

        Color fill = colorOf(vehicle);
        g2.setColor(fill);
        g2.fillOval(x - 6, y - 6, 12, 12);
        g2.setColor(fill.darker());
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(x - 6, y - 6, 12, 12);
    }

    /**
     * Visszaadja a jármű típusának megfelelő megjelenítési színt.
     * <ul>
     *   <li>{@link SnowPlow} — cián</li>
     *   <li>{@link Bus} — narancs</li>
     *   <li>{@link Car} — kék</li>
     *   <li>{@link Commuter} (egyéb) — zöld</li>
     *   <li>ismeretlen típus — fehér</li>
     * </ul>
     *
     * @param v a lekérdezett jármű
     * @return a jármű megjelenítési színe
     */
    public static Color colorOf(Vehicle v) {
        if (v instanceof SnowPlow) return new Color(0,   200, 220);
        if (v instanceof Bus)      return new Color(255, 140,   0);
        if (v instanceof Car)      return new Color(80,  160, 255);
        if (v instanceof Commuter) return new Color(100, 200, 100);
        return Color.WHITE;
    }
}
