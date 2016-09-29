package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.Paintable;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by jakekinsella on 9/20/16.
 */
public class Ball implements Paintable {

    public final int BALL_DIAMETER = 10;

    private int x, y;

    /**
     * Ball initializer
     * @param x the position of the ball from the center
     * @param y the position of the ball from the center
     */
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        graphics.setColor(Color.BLACK);

        graphics.fillOval(this.x - (this.BALL_DIAMETER / 2), this.y - (this.BALL_DIAMETER / 2), this.BALL_DIAMETER, this.BALL_DIAMETER);
    }

    // TODO: Physics stuff
}
