package com.gmail.jakekinsella.robot.manipulators.ballsmanipulators;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.robotactions.PickupBallsFromStationAction;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.awt.*;

/**
 * Created by jakekinsella on 2/23/17.
 */
public class BallsPickupStationManipulator extends BallsManipulator {

    private static final Logger logger = LogManager.getLogger();
    private double ballsPickupStationChance;
    private int ballsPickupStationTime;

    public BallsPickupStationManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    @Override
    public Action getAction(Command commandInfo) {
        return new PickupBallsFromStationAction(commandInfo, this, this.ballsPickupStationTime, this.ballsPickupStationChance);

    }

    @Override
    public boolean isInRange() {
        if (RobotServer.getField().checkIfStationInRange(this.getDetectionBox())) { // This is not very DRY
            logger.info(this.robot.getRobotName() + " is in range of a human player station");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " isn't in range of a human player station");
            return false;
        }
    }

    @Override
    protected void setup(JSONObject robotSettings) {
        this.actionRobotSide = RobotSide.valueOf((String) robotSettings.get("pickupBallsStationSide"));
        this.actionRange = ((Long) robotSettings.get("pickupBallsStationRange")).intValue();
        this.ballsPickupStationChance = (Double) robotSettings.get("pickupBallsStationChance");
        this.ballsPickupStationTime = ((Long) robotSettings.get("pickupBallsStationTime")).intValue();
    }
}
