package view.DisplayLogic;

import view.View;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;



public class GamePanel extends JPanel {

    private final List<View> views;

    public GamePanel() {
        this.views = new ArrayList<>();
    }

    public void addView(View v) {
        views.add(v);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (View view : views) {
            view.draw(g);
        }
    }
}