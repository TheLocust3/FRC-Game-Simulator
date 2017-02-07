package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.RobotAllianceColor;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 2/2/17.
 */
public class FieldElement implements Paintable {

    protected int x, y, width, height, angle, detectionPadding;

    private RobotAllianceColor allianceColor;
    private Rectangle2D.Double rectangle, detectionBox;

    public FieldElement(int x, int y, int width, int height, int angle, int detectionPadding, RobotAllianceColor robotAllianceColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.detectionPadding = detectionPadding;
        this.allianceColor = robotAllianceColor;

        this.rectangle = new Rectangle2D.Double(x, y, width, height);
        this.detectionBox = new Rectangle2D.Double(x - (detectionPadding / 2), y - (detectionPadding / 2), width + detectionPadding, height + detectionPadding);
    }

    public Rectangle2D.Double getRectangle() {
        return this.rectangle;
    }

    public Rectangle2D.Double getDetectionBox() {
        return this.detectionBox;
    }

    public RobotAllianceColor getAllianceColor() {
        return allianceColor;
    }

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        if (this.getAllianceColor() == RobotAllianceColor.BLUE) {
            graphics2D.setColor(Color.BLUE);
        } else if (this.getAllianceColor() == RobotAllianceColor.RED){
            graphics2D.setColor(Color.RED);
        } else {
            graphics2D.setColor(Color.LIGHT_GRAY);
        }

        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(this.angle), this.getRectangle().getCenterX(), this.getRectangle().getCenterY());
        Shape shape = at.createTransformedShape(this.getRectangle());
        graphics2D.fill(shape);

        graphics2D.setColor(Color.YELLOW);
        at = AffineTransform.getRotateInstance(Math.toRadians(this.angle), this.getDetectionBox().getCenterX(), this.getDetectionBox().getCenterY());
        shape = at.createTransformedShape(this.getDetectionBox());
        graphics2D.draw(shape);
    }

    protected void updateRectangles() {
        this.rectangle = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
        this.detectionBox = new Rectangle2D.Double(this.x - (this.detectionPadding / 2), this.y - (this.detectionPadding / 2), this.width + this.detectionPadding, this.height + this.detectionPadding);
    }
}
