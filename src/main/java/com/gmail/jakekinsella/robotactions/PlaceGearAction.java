package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/17/16.
 */
public class PlaceGearAction extends TimeAction {

    private static final Logger logger = LogManager.getLogger();

    public PlaceGearAction(Command command, Robot robot, int time) {
        super(command, robot, time);
    }

    @Override
    public void actionDone() {
        this.robot.getRobotAlliance().getScore().placeGear();
        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }

    @Override
    public void actionStart() {
        if (this.robot.getGear() == null) {
            success = false;
            logger.info(this.robot.getRobotName() + " doesn't have a gear!");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        } else if (this.robot.isInRangeOfAirshipStation()) {
            success = false;
            logger.info(this.robot.getRobotName() + " isn't in range of an airship station");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        }
    }
}
