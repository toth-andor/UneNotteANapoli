package view.ComponentViews;

import map.Lane;
import view.View;

import java.awt.Graphics;

public class LaneView extends View {

    private final Lane lane;

    public LaneView(Lane lane) {
        this.lane = lane;
        lane.addObserver(this);
    }

    @Override
    public void update() {
        // TODO: Frissítés a Lane modell állapota alapján
    }

    @Override
    public void draw(Graphics g) {
        // TODO: Sáv kirajzolása
    }
}