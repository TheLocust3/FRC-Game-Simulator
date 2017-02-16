package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.field.Gear;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/11/16.
 */
public class PickupGearFromStationAction extends TimeAction {

    private double successChance;

    private RobotSide pickupSide;
    private Gear gear;

    private static final Logger logger = LogManager.getLogger();

    public PickupGearFromStationAction(Command command, Robot robot, int time, double successChance, RobotSide pickupSide) {
        super(command, robot, time);

        this.pickupSide = pickupSide;
        this.successChance = successChance;
    }

    public Gear getGear() {
        return this.gear;
    }

    @Override
    public void actionDone() {
        boolean score = (Math.random() >= (1 - this.successChance));

        if (score) {
            logger.info(this.robot.getRobotName() + " has picked up a gear");
        } else {
            logger.info(this.robot.getRobotName() + " has failed to pick up a gear");
        }

        this.success = score;

        if (this.robot.isReadyToReceiveGearFromStation()) {
            this.gear = new Gear(0, 0); // Random location
        }

        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }

    @Override
    public void actionStart() {
        if (this.robot.getGear() != null) {
            success = false;
            logger.info(this.robot.getRobotName() + " already has a gear!");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        }
    }

}
