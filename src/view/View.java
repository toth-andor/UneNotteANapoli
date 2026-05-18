package view;

import view.ObserverLogic.Observer;

import java.awt.Color;
import java.awt.Graphics;

public abstract class View implements Observer {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Color color;

    public abstract void draw(Graphics g);

    @Override
    public abstract void update();
}