package com.gmail.jakekinsella.robot.manipulators.gearmanipulators;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robot.manipulators.Manipulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.awt.*;

/**
 * Created by jakekinsella on 2/23/17.
 */
public abstract class GearManipulator extends Manipulator {

    RobotSide actionRobotSide;
    int actionRange;
    double actionChance;
    int actionTime;

    private static final Logger logger = LogManager.getLogger();

    public GearManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    public boolean hasGear() {
        if (this.robot.getGear() != null) {
            logger.info(this.robot.getRobotName() + " has a gear");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " doesn't have a gear");
            return false;
        }
    }

    @Override
    protected Shape getDetectionBox() {
        return this.createDetectionRect(this.actionRobotSide, this.actionRange);
    }
}
