package com.gmail.jakekinsella.field;

import java.awt.*;

/**
 * Created by jakekinsella on 9/20/16.
 */
public class Ball extends GamePiece {

    public static final int BALL_DIAMETER = 5; // px

    private double velocity, angle;
    private long lastTick;

    public Ball(int x, int y) {
        super(x, y, 5, 5, 0, Color.GREEN);
    }

    public double getVelocity() {
        return this.velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean isStuck() {
        return false; // TODO: implement this method
    }

    public void tick() {
        long delta = System.currentTimeMillis() - this.lastTick;
        double deltaSeconds = delta / 1000.0;

        double radians = Math.toRadians(this.getAngle());
        double deltaX = deltaSeconds * (this.getVelocity() * Math.sin(radians));
        double deltaY = -deltaSeconds * (this.getVelocity() * Math.cos(radians));

        this.setX((int) (this.getX() + deltaX));
        this.setY((int) (this.getY() + deltaY));

        if (this.isStuck()) {
            this.setVelocity(0);
        }

        this.lastTick = System.currentTimeMillis();
    }
}
