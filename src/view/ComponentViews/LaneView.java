package view.ComponentViews;

import map.Lane;
import map.OutdoorLane;
import map.TunnelLane;
import states.CrashedState;
import states.DryState;
import states.GraveledState;
import states.IcyState;
import states.LaneState;
import states.SaltedState;
import states.SnowyState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Egy {@link Lane} sáv vizuális megjelenítéséért felelős segédosztály.
 * A sávot az útján belüli indexe alapján párhuzamosan eltolt egyenes vonalként rajzolja ki,
 * a sáv menetirányát jelző nyílheggyel a szakasz közepén.
 *
 * <p>Az eltolás mindig az út kanonikus irányából (end1 → end2) számítódik,
 * hogy az ellentétes irányú sávok az út két különböző oldalán jelenjenek meg.</p>
 *
 * <p>Az osztály kizárólag statikus metódusokat tartalmaz; nem kell példányosítani.</p>
 */
public class LaneView {

    /** Az egyes sávindexekhez tartozó merőleges eltolás pixelben az út középvonalától. */
    public static final int[] OFFSETS = {-9, -3, 3, 9};

    /**
     * Kiszámolja az eltolt sávvonal két végpontját.
     * Visszatér: {x1, y1, x2, y2}, vagy null ha az irányvektor nulla.
     */
    public static int[] computeEndpoints(int index, Point src, Point dst,
                                          Point roadEnd1, Point roadEnd2) {
        double rdx = roadEnd2.x - roadEnd1.x;
        double rdy = roadEnd2.y - roadEnd1.y;
        double rlen = Math.sqrt(rdx * rdx + rdy * rdy);
        if (rlen == 0) return null;
        double nx = -rdy / rlen;
        double ny =  rdx / rlen;
        int offset = OFFSETS[index % OFFSETS.length];
        return new int[]{
            (int) (src.x + nx * offset), (int) (src.y + ny * offset),
            (int) (dst.x + nx * offset), (int) (dst.y + ny * offset)
        };
    }

    public static void draw(Graphics2D g2, Lane lane, int index,
                            Point src, Point dst,
                            Point roadEnd1, Point roadEnd2,
                            boolean highlighted, boolean destination) {
        int[] ep = computeEndpoints(index, src, dst, roadEnd1, roadEnd2);
        if (ep == null) return;
        int x1 = ep[0], y1 = ep[1], x2 = ep[2], y2 = ep[3];

        double dx = dst.x - src.x;
        double dy = dst.y - src.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;

        if (destination) {
            g2.setColor(new Color(0, 200, 80, 160));
            g2.setStroke(new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);
        } else if (highlighted) {
            g2.setColor(new Color(255, 220, 0, 160));
            g2.setStroke(new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);
        }

        Color color = colorOf(lane);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);

        drawArrow(g2, x1, y1, x2, y2, dx / len, dy / len, color);
    }

    /**
     * Iránynyilat rajzol a sávszakasz közepére.
     *
     * @param g2   a rajzoláshoz használt {@link Graphics2D} kontextus
     * @param x1   a szakasz kezdőpontjának x-koordinátája
     * @param y1   a szakasz kezdőpontjának y-koordinátája
     * @param x2   a szakasz végpontjának x-koordinátája
     * @param y2   a szakasz végpontjának y-koordinátája
     * @param dirX a menetirány egységvektorának x-komponense
     * @param dirY a menetirány egységvektorának y-komponense
     * @param base a nyíl alapszíne (a tényleges szín ennek világosabb változata)
     */
    private static void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2,
                                   double dirX, double dirY, Color base) {
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;
        int s = 5;
        int[] xs = {
            mx + (int) ( dirX * s),
            mx + (int) (-dirX * s - dirY * s),
            mx + (int) (-dirX * s + dirY * s)
        };
        int[] ys = {
            my + (int) ( dirY * s),
            my + (int) (-dirY * s + dirX * s),
            my + (int) (-dirY * s - dirX * s)
        };
        g2.setColor(base.brighter());
        g2.fillPolygon(xs, ys, 3);
    }

    /**
     * Visszaadja a sáv állapotának megfelelő megjelenítési színt.
     * {@link TunnelLane} esetén sötétszürke; {@link OutdoorLane} esetén az aktuális
     * {@link LaneState} alapján kerül kiválasztásra a szín.
     *
     * @param lane a lekérdezett sáv
     * @return a sáv megjelenítési színe
     */
    public static Color colorOf(Lane lane) {
        if (lane instanceof TunnelLane)     return new Color(75, 75, 100);
        if (lane instanceof OutdoorLane ol) {
            LaneState s = ol.getCurrentState();
            if (s instanceof DryState)      return new Color(115, 115, 115);
            if (s instanceof SnowyState)    return new Color(173, 216, 230);
            if (s instanceof IcyState)      return new Color(65, 105, 225);
            if (s instanceof SaltedState)   return new Color(210, 170,   0);
            if (s instanceof GraveledState) return new Color(139, 100,  20);
            if (s instanceof CrashedState)  return new Color(200,  20,  50);
        }
        return Color.GRAY;
    }
}
