package view;

import map.Junction;

import java.awt.Point;
import java.util.List;

/**
 * {@link Junction} objektumokhoz rendel 2D képernyő-koordinátát.
 * A csomópontok [0, 1] normalizált koordinátáit ({@link Junction#getPosX()},
 * {@link Junction#getPosY()}) skálázza át a rajzterület pixel-koordinátáira.
 */
public class CoordinateMapper {

    private int width;
    private int height;

    /**
     * Eltárolja a rajzterület aktuális méretét a koordináta-konverzióhoz.
     *
     * @param junctions nem használt, az API kompatibilitás miatt maradt
     * @param width     a rajzterület szélessége pixelben
     * @param height    a rajzterület magassága pixelben
     */
    public void compute(List<Junction> junctions, int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Visszaadja a csomópont képernyő-koordinátáját a tárolt területméret alapján.
     *
     * @param j a lekérdezett csomópont
     * @return a csomópont pixel-koordinátája
     */
    public Point get(Junction j) {
        return new Point((int) (j.getPosX() * width), (int) (j.getPosY() * height));
    }
}
