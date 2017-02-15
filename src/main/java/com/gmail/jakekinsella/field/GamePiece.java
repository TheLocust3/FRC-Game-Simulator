package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.Paintable;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 2/15/17.
 */
public abstract class GamePiece implements Paintable{

    private int x, y;
    private Color color;
    private Rectangle2D.Double detectionBox;

    public GamePiece(int x, int y, int width, int height, int detectionPadding, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;

        this.detectionBox = new Rectangle2D.Double(this.x, this.y, width + detectionPadding, height + detectionPadding);
    }

    public int getX() {
        return (int) this.detectionBox.getX();
    }

    public void setX(int x) {
        this.detectionBox.setRect(x, this.getY(), this.detectionBox.getWidth(), this.detectionBox.getHeight());
    }

    public int getY() {
        return (int) this.detectionBox.getY();
    }

    public void setY(int y) {
        this.detectionBox.setRect(this.getX(), y, this.detectionBox.getWidth(), this.detectionBox.getHeight());
    }

    public Rectangle2D.Double getDetectionBox() {
        return this.detectionBox;
    }

    public abstract void tick();

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        graphics2D.setColor(this.color);
        graphics2D.fill(this.detectionBox);
    }
}
