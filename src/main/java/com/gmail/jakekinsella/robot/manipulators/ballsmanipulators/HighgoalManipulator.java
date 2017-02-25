package com.gmail.jakekinsella.robot.manipulators.ballsmanipulators;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.robotactions.ShootAction;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Created by jakekinsella on 2/23/17.
 */
public class HighgoalManipulator extends BallsManipulator {

    private static final Logger logger = LogManager.getLogger();

    private double highgoalChance;
    private int highgoalBallsPerSecond;

    public HighgoalManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    @Override
    public Action getAction(Command commandInfo) {
        return new ShootAction(commandInfo, this, this.highgoalBallsPerSecond, this.highgoalChance);

    }

    @Override
    public boolean isInRange() {
        if (RobotServer.getField().checkIfBoilerInRange(this.getDetectionBox())) { // This isn't very DRY
            logger.info(this.robot.getRobotName() + " is in range for a lowgoal");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " isn't in range for a lowgoal");
            return false;
        }
    }

    @Override
    protected void setup(JSONObject robotSettings) {
        this.actionRobotSide = RobotSide.valueOf((String) robotSettings.get("highgoalSide"));
        this.actionRange = ((Long) robotSettings.get("highgoalRange")).intValue();
        this.highgoalChance = (Double) robotSettings.get("highgoalChance");
        this.highgoalBallsPerSecond = ((Long) robotSettings.get("highgoalBallsPerSecond")).intValue();
    }
}
