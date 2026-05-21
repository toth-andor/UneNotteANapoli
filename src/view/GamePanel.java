package view;

import controller.ControllerLogic.IController;
import map.Junction;
import map.Lane;
import map.Road;
import vehicle.Vehicle;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.List;

/**
 * A játék fő rajzfelülete. Minden képkockában kirajzolja a teljes pályát:
 * a sávokat ({@link LaneView}), a csomópontokat ({@link JunctionView})
 * és a járműveket ({@link VehicleView}).
 *
 * <p>A renderelés {@code javax.swing.Timer} segítségével {@value #REPAINT_INTERVAL_MS} ms-enként
 * frissül, ami immediate-mode stílusú megjelenítést valósít meg Swing keretrendszerben.</p>
 *
 * <p>A csomópontok képernyő-koordinátáit a {@link CoordinateMapper} számítja,
 * amelyet minden {@code paintComponent} hívás frissít.</p>
 */
public class GamePanel extends JPanel {

    /** A képernyőfrissítés időköze milliszekundumban. */
    private static final int REPAINT_INTERVAL_MS = 50;

    /** Az egyes sávindexekhez tartozó merőleges eltolás pixelben az út középvonalától. */
    private static final int[] LANE_OFFSETS = {-9, -3, 3, 9};

    private final IController controller;
    private final CoordinateMapper mapper = new CoordinateMapper();

    /**
     * Létrehoz egy új {@code GamePanel} példányt és elindítja a renderelési timert.
     *
     * @param controller a megjelenítendő modellt és játékállapotot tartalmazó kontroller
     */
    public GamePanel(IController controller) {
        this.controller = controller;
        setBackground(new Color(25, 25, 25));
        new Timer(REPAINT_INTERVAL_MS, e -> repaint()).start();
    }

    /**
     * Kirajzolja a teljes játékállapotot: sávokat, csomópontokat és járműveket.
     * A csomópontok képernyő-koordinátái minden híváskor újraszámítódnak,
     * hogy az ablakméret-változásokat is kövesse a megjelenítés.
     *
     * @param g a Swing által biztosított grafikus kontextus
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        List<Junction> junctions = new ArrayList<>(controller.getMapModel().getJunctions());
        mapper.compute(junctions, getWidth(), getHeight());

        drawRoads(g2);
        drawJunctions(g2, junctions);
        drawEmptyHint(g2, junctions);
    }

    /**
     * Kirajzolja az összes utat: minden úthoz meghatározza a kanonikus irányvektort,
     * majd minden sávját megjeleníti {@link LaneView} segítségével, a sávon lévő
     * járművekkel együtt.
     *
     * @param g2 a rajzoláshoz használt {@link Graphics2D} kontextus
     */
    private void drawRoads(Graphics2D g2) {
        for (Road road : new ArrayList<>(controller.getMapModel().getRoads())) {
            Point roadEnd1 = mapper.get(road.getEnd1());
            Point roadEnd2 = mapper.get(road.getEnd2());
            List<Lane> lanes = new ArrayList<>(road.getLanes());
            for (int i = 0; i < lanes.size(); i++) {
                Lane lane = lanes.get(i);
                Point src = mapper.get(lane.getSource());
                Point dst = mapper.get(lane.getDestination());
                LaneView.draw(g2, lane, i, src, dst, roadEnd1, roadEnd2);
                drawVehiclesOnLane(g2, lane, src, dst, i, roadEnd1, roadEnd2);
            }
        }
    }

    /**
     * Kirajzolja a megadott sávon tartózkodó összes járművet.
     * A járművek egyenletes távolságra helyezkednek el a sávszakasz mentén.
     * Az eltolás számítása megegyezik a {@link LaneView#draw} által használttal,
     * hogy a körök valóban a sávvonalon legyenek.
     *
     * @param g2       a rajzoláshoz használt {@link Graphics2D} kontextus
     * @param lane     a sáv, amelynek járműveit ki kell rajzolni
     * @param src      a sáv forráscsomópontjának képernyő-koordinátája
     * @param dst      a sáv célcsomópontjának képernyő-koordinátája
     * @param index    a sáv indexe az útján belül (0–3)
     * @param roadEnd1 az út {@code end1} csomópontjának koordinátája
     * @param roadEnd2 az út {@code end2} csomópontjának koordinátája
     */
    private void drawVehiclesOnLane(Graphics2D g2, Lane lane,
                                    Point src, Point dst, int index,
                                    Point roadEnd1, Point roadEnd2) {
        List<Vehicle> vehicles = new ArrayList<>(lane.getVehicles());
        if (vehicles.isEmpty()) return;

        double rdx = roadEnd2.x - roadEnd1.x;
        double rdy = roadEnd2.y - roadEnd1.y;
        double rlen = Math.sqrt(rdx * rdx + rdy * rdy);
        if (rlen == 0) return;

        double nx = -rdy / rlen;
        double ny =  rdx / rlen;
        int offset = LANE_OFFSETS[index % LANE_OFFSETS.length];

        int x1 = (int) (src.x + nx * offset);
        int y1 = (int) (src.y + ny * offset);
        int x2 = (int) (dst.x + nx * offset);
        int y2 = (int) (dst.y + ny * offset);

        int n = vehicles.size();
        for (int i = 0; i < n; i++) {
            double t = (i + 1.0) / (n + 1);
            int vx = (int) (x1 + t * (x2 - x1));
            int vy = (int) (y1 + t * (y2 - y1));
            VehicleView.draw(g2, vehicles.get(i), vx, vy);
        }
    }

    /**
     * Kirajzolja az összes csomópontot {@link JunctionView} segítségével.
     *
     * @param g2        a rajzoláshoz használt {@link Graphics2D} kontextus
     * @param junctions a kirajzolandó csomópontok listája
     */
    private void drawJunctions(Graphics2D g2, List<Junction> junctions) {
        for (Junction j : junctions) {
            JunctionView.draw(g2, j, mapper.get(j));
        }
    }

    /**
     * Ha nincs betöltött térkép, egy segítő feliratot jelenít meg a rajzfelület közepén.
     *
     * @param g2        a rajzoláshoz használt {@link Graphics2D} kontextus
     * @param junctions az aktuális csomópontlista; ha üres, megjelenik a hint
     */
    private void drawEmptyHint(Graphics2D g2, List<Junction> junctions) {
        if (!junctions.isEmpty()) return;
        g2.setColor(new Color(100, 100, 100));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        String msg = "Térkép még nincs betöltve – használd a CLI-t a beállításhoz";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }
}
