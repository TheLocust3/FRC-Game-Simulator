package com.gmail.jakekinsella.robot.manipulators.gearmanipulators;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.robotactions.PickupGearFromStationAction;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Created by jakekinsella on 2/23/17.
 */
public class GearPickupStationManipulator extends GearManipulator {

    private static final Logger logger = LogManager.getLogger();

    public GearPickupStationManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    @Override
    public Action getAction(Command commandInfo) {
        return new PickupGearFromStationAction(commandInfo, this, this.actionTime, this.actionChance);
    }

    @Override
    public boolean isInRange() {
        if (RobotServer.getField().checkIfStationInRange(this.getDetectionBox())) {
            logger.info(this.robot.getRobotName() + " is in range of a human player station");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " isn't in range of a human player station");
            return false;
        }
    }

    @Override
    protected void setup(JSONObject robotSettings) {
        this.actionRobotSide = RobotSide.valueOf((String) robotSettings.get("pickupGearStationSide"));
        this.actionRange = ((Long) robotSettings.get("pickupGearStationRange")).intValue();
        this.actionChance = (Double) robotSettings.get("pickupGearStationChance");
        this.actionTime = ((Long) robotSettings.get("pickupGearStationTime")).intValue();
    }
}
