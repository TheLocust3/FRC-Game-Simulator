package com.gmail.jakekinsella.robot.manipulators.ballsmanipulators;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.robotactions.LowgoalAction;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.awt.*;

/**
 * Created by jakekinsella on 2/23/17.
 */
public class LowgoalManipulator extends BallsManipulator {

    private static final Logger logger = LogManager.getLogger();
    private double lowgoalChance;
    private int lowgoalBallsPerSecond;

    public LowgoalManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    @Override
    public Action getAction(Command commandInfo) {
        return new LowgoalAction(commandInfo, this, this.lowgoalBallsPerSecond, this.lowgoalChance);

    }

    @Override
    public boolean isInRange() {
        if (RobotServer.getField().checkIfBoilerInRange(this.getDetectionBox())) {
            logger.info(this.robot.getRobotName() + " is in range for a lowgoal");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " isn't in range for a lowgoal");
            return false;
        }
    }

    @Override
    protected void setup(JSONObject robotSettings) {
        this.actionRobotSide = RobotSide.valueOf((String) robotSettings.get("lowgoalSide"));
        this.actionRange = ((Long) robotSettings.get("lowgoalRange")).intValue();
        this.lowgoalChance = (Double) robotSettings.get("lowgoalChance");
        this.lowgoalBallsPerSecond = ((Long) robotSettings.get("lowgoalBallsPerSecond")).intValue();
    }
}
