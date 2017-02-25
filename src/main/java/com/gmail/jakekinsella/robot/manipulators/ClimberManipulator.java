package com.gmail.jakekinsella.robot.manipulators;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.awt.*;

/**
 * Created by jakekinsella on 2/23/17.
 */
public class ClimberManipulator extends Manipulator {

    private static final Logger logger = LogManager.getLogger();
    private RobotSide climbRobotSide;
    private int climbRange;
    private double climbChance;
    private int climbTime;

    public ClimberManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    @Override
    public Action getAction(Command commandInfo) {
        return null;
    }

    @Override
    public boolean isInRange() {
        if (RobotServer.getField().checkIfRopeStationInRange(this.getDetectionBox())) {
            logger.info(this.robot.getRobotName() + " is in range for climbing");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " isn't in range for climbing");
            return false;
        }
    }

    @Override
    protected void setup(JSONObject robotSettings) {
        this.climbRobotSide = RobotSide.valueOf((String) robotSettings.get("climbSide"));
        this.climbRange = ((Long) robotSettings.get("climbRange")).intValue();
        this.climbChance = (Double) robotSettings.get("climbChance");
        this.climbTime = ((Long) robotSettings.get("climbTime")).intValue();
    }

    @Override
    protected Shape getDetectionBox() {
        return this.createDetectionRect(this.climbRobotSide, this.climbRange);
    }
}
