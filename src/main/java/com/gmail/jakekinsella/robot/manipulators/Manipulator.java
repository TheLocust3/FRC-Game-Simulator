package com.gmail.jakekinsella.robot.manipulators;

import com.gmail.jakekinsella.JSONFileReader;
import com.gmail.jakekinsella.field.Score;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.robotactions.NoneAction;
import com.gmail.jakekinsella.socketcommands.Command;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by jakekinsella on 2/23/17.
 */
public abstract class Manipulator {

    protected Robot robot;

    public Manipulator(Robot robot, JSONObject robotSettings) {
        this.robot = robot;
        this.setup(robotSettings);
    }

    public abstract Action getAction(Command commandInfo);

    public abstract boolean isInRange();

    public Score getScore() {
        return this.robot.getRobotAlliance().getScore();
    }

    public void actionFinish() {
        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }

    protected abstract void setup(JSONObject robotSettings);

    protected abstract Shape getDetectionBox();

    protected Shape createDetectionRect(RobotSide robotSide, int range) {
        int rectWidth = this.robot.getWidth(), rectHeight = range, offset = 0;
        double rectAngle = 0;

        switch (robotSide) {
            case FRONT:
                rectAngle = this.robot.getAngle() - 180;
                break;
            case BACK:
                rectAngle = this.robot.getAngle();
                break;
            case LEFT:
                rectWidth = this.robot.getHeight();
                rectAngle = this.robot.getAngle() - 270;
                offset = -Math.abs(this.robot.getHeight() - this.robot.getWidth()) + 5; // This 5 is a tuned value. Idk why it is needed
                break;
            case RIGHT:
                rectWidth = this.robot.getHeight();
                rectAngle = this.robot.getAngle() - 90;
                offset = -Math.abs(this.robot.getHeight() - this.robot.getWidth()) + 5;
                break;
        }

        double rectX = this.robot.getCenterX() - (rectWidth / 2);
        double rectY = this.robot.getCenterY() + (this.robot.getHeight() / 2) + offset;

        Rectangle2D.Double rect = new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rectAngle), this.robot.getCenterX(), this.robot.getCenterY());
        return at.createTransformedShape(rect);
    }
}
