package com.gmail.jakekinsella.robot.manipulators.gearmanipulators;

import com.gmail.jakekinsella.robot.*;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.robotactions.PlaceGearAction;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Created by jakekinsella on 2/23/17.
 */
public class GearPlaceManipulator extends GearManipulator {

    private static final Logger logger = LogManager.getLogger();

    public GearPlaceManipulator(Robot robot, JSONObject robotSettings) {
        super(robot, robotSettings);
    }

    @Override
    public Action getAction(Command commandInfo) {
        return new PlaceGearAction(commandInfo, this, this.actionTime, this.actionChance);
    }

    @Override
    public boolean isInRange() {
        if (RobotServer.getField().checkIfAirshipStationInRange(this.getDetectionBox())) {
            logger.info(this.robot.getRobotName() + " is in actionRange of an Airship Station");
            return true;
        } else {
            logger.info(this.robot.getRobotName() + " isn't in actionRange of an Airship Station");
            return false;
        }
    }

    @Override
    protected void setup(JSONObject robotSettings) {
        this.actionRobotSide = RobotSide.valueOf((String) robotSettings.get("placeGearSide"));
        this.actionRange = ((Long) robotSettings.get("placeGearRange")).intValue();
        this.actionChance = (Double) robotSettings.get("placeGearChance");
        this.actionTime = ((Long) robotSettings.get("placeGearTime")).intValue();
    }
}
