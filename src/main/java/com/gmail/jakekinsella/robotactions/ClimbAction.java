package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/17/16.
 */
public class ClimbAction extends TimeAction {

    private static final Logger logger = LogManager.getLogger();

    private double successChance;

    public ClimbAction(Command command, Robot robot, int time, double successChance) {
        super(command, robot, time);

        this.successChance = successChance;
    }

    @Override
    public void actionDone() {
        boolean score = (Math.random() >= (1 - this.successChance));

        if (score) {
            logger.info(this.robot.getRobotName() + " has climbed");
        } else {
            logger.info(this.robot.getRobotName() + " has failed to climb");
        }

        this.success = score;

        this.robot.getRobotAlliance().getScore().climb();
        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }

    @Override
    public void actionStart() {
        if (this.robot.isInRangeOfRopeStation()) {
            success = false;
            logger.info(this.robot.getRobotName() + " isn't in range of a rope station");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        }

        // TODO: Check time
    }
}
