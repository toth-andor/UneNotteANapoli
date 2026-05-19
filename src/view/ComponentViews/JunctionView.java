package view.ComponentViews;

import map.Junction;
import view.View;
import java.awt.Graphics;

public class JunctionView extends View {

    private final Junction junction;

    public JunctionView(Junction junction) {
        this.junction = junction;
        junction.addObserver(this);
    }

    @Override
    public void update() {
        // TODO: Frissítés a Junction modell állapota alapján
    }

    @Override
    public void draw(Graphics g) {
        // TODO: Kereszteződés kirajzolása
    }
}