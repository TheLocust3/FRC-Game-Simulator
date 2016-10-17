package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/17/16.
 */
public class LowgoalAction extends TimeAction {

    private RobotSide pickupSide;
    private double successChance;

    private static final Logger logger = LogManager.getLogger();

    public LowgoalAction(Command command, Robot robot, RobotSide pickupSide) {
        super(command, robot);

        this.pickupSide = pickupSide; // Assumes the robot shoots lowgoals on the same side as pickup
        this.successChance = (double) command.getArg(1);
    }

    @Override
    public void actionDone() {
        boolean score = (Math.random() >= (1 - this.successChance));

        if (score) {
            logger.info(this.robot.getRobotName() + " has shot a ball and scored in the lowgoal");
        } else {
            logger.info(this.robot.getRobotName() + " has shot a ball and missed the lowgoal");
        }

        this.success = score;

        this.robot.getRobotAlliance().getScore().scoreLowgoal(); // This flipping line...
        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }

    @Override
    public void actionStart() {
        if (this.robot.getBall() == null) {
            success = false;
            logger.info(this.robot.getRobotName() + " doesn't have a ball!");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        }
    }
}
