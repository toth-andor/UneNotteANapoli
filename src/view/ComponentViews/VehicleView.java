package view.ComponentViews;

import vehicle.Vehicle;
import view.View;

import java.awt.Graphics;

public class VehicleView extends View {

    private final Vehicle vehicle;

    public VehicleView(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicle.addObserver(this);
    }

    @Override
    public void update() {
        // TODO: Frissítés a Vehicle modell állapota alapján
    }

    @Override
    public void draw(Graphics g) {
        // TODO: Jármű kirajzolása
    }
}