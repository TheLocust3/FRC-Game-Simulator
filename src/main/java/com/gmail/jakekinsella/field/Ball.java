package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robotactions.NoneAction;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 9/20/16.
 */
public class Ball implements Paintable {

    public static final int BALL_DIAMETER = 5;

    private int x, y;
    private double velocity, angle;
    private long lastTick;
    private Rectangle2D.Double rectangle;

    /**
     * Ball initializer
     * @param x the position of the ball from the center
     * @param y the position of the ball from the center
     */
    public Ball(int x, int y) {
        this.x = x;
        this.y = y;

        this.angle = 0;
        this.velocity = 0;

        this.rectangle = new Rectangle2D.Double(this.x, this.y, BALL_DIAMETER + 7, BALL_DIAMETER + 7);
    }

    public int getX() {
        return (int) this.rectangle.getX();
    }

    public void setX(int x) {
        this.rectangle.setRect(x, this.getY(), this.rectangle.getWidth(), this.rectangle.getHeight());
    }

    public int getY() {
        return (int) this.rectangle.getY();
    }

    public void setY(int y) {
        this.rectangle.setRect(this.getX(), y, this.rectangle.getWidth(), this.rectangle.getHeight());
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

    public Rectangle2D.Double getRectangle() {
        return this.rectangle;
    }

    public boolean isStuck() {
        return RobotServer.getField().detectAllDefensesInRect(this.getRectangle()).size() > 0;
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

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        graphics.setColor(Color.GREEN);

        graphics.fillOval(this.getX(), this.getY(), BALL_DIAMETER, BALL_DIAMETER);
    }
}
