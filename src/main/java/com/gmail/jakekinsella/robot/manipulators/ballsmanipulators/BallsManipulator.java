package com.gmail.jakekinsella.robot.manipulators.ballsmanipulators;

import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robot.manipulators.Manipulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 2/23/17.
 */
public abstract class BallsManipulator extends Manipulator {

    private static final Logger logger = LogManager.getLogger();

    RobotSide actionRobotSide;
    int actionRange;

    public BallsManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    public ArrayList<Ball> getBalls() {
        return this.robot.getBalls();
    }

    public boolean hasBalls() {
        if (this.getBalls().size() > 0) {
            logger.info(this.robot.getRobotName() + " has balls");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " doesn't have any balls");
            return false;
        }
    }

    @Override
    protected Shape getDetectionBox() {
        return this.createDetectionRect(this.actionRobotSide, this.actionRange);
    }
}
