package view;

import map.Junction;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link Junction} objektumokhoz rendel 2D képernyő-koordinátát körös elrendezéssel.
 * Mivel a modellben nincs pozícióadat, ez az osztály felelős a csomópontok
 * vizuális elhelyezéséért: a csomópontokat egyenlő szögtávolságra helyezi el
 * egy kör kerületén, amelynek átmérője a rendelkezésre álló terület méretéhez igazodik.
 */
public class CoordinateMapper {

    private final Map<Junction, Point> positions = new HashMap<>();

    /**
     * Kiszámítja és eltárolja az összes csomópont képernyő-koordinátáját.
     * A korábban számított pozíciók törlődnek; minden hívás teljes újraszámítást végez.
     *
     * @param junctions a leképezendő csomópontok listája
     * @param width     a rajzterület szélessége pixelben
     * @param height    a rajzterület magassága pixelben
     */
    public void compute(List<Junction> junctions, int width, int height) {
        positions.clear();
        int n = junctions.size();
        if (n == 0) return;

        int cx = width / 2;
        int cy = height / 2;
        int radius = Math.max(50, Math.min(width, height) / 2 - 70);

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            int x = cx + (int) (radius * Math.cos(angle));
            int y = cy + (int) (radius * Math.sin(angle));
            positions.put(junctions.get(i), new Point(x, y));
        }
    }

    /**
     * Visszaadja a megadott csomópont képernyő-koordinátáját.
     * Ha a csomópont nem szerepel a legutóbbi {@link #compute} hívás eredményében,
     * az origót {@code (0, 0)} adja vissza.
     *
     * @param j a lekérdezett csomópont
     * @return a csomópont képernyő-koordinátája
     */
    public Point get(Junction j) {
        return positions.getOrDefault(j, new Point(0, 0));
    }
}
