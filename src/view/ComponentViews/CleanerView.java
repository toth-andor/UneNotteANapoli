package view.ComponentViews;

import vehicle.Cleaner;
import view.View;

import javax.swing.*;
import java.awt.*;

public class CleanerView extends View {

    private final Cleaner cleaner;

    public CleanerView(Cleaner cleaner) {
        this.cleaner = cleaner;
        cleaner.addObserver(this);
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
