package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class ShootAction extends TimeAction {

    private RobotSide shootSide;
    private double successChance;
    private int range;

    private static final Logger logger = LogManager.getLogger();

    public ShootAction(Command command, Robot robot, int time, double successChance, RobotSide shootSide, int range) {
        super(command, robot, time);

        this.shootSide = shootSide;
        this.successChance = successChance;
        this.range = range;
    }

    @Override
    public void actionDone() {
        boolean score = (Math.random() >= (1 - this.successChance));

        if (score) {
            logger.info(this.robot.getRobotName() + " has shot a ball and scored in the highgoal");
        } else {
            logger.info(this.robot.getRobotName() + " has shot a ball and missed the highgoal");
        }

        this.success = score;

        this.robot.getRobotAlliance().getScore().scoreHighgoal(); // This flipping line...
        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }

    @Override
    public void actionStart() {
        if (this.robot.getBalls().size() == 0) {
            success = false;
            logger.info(this.robot.getRobotName() + " doesn't have a ball!");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        } else if (this.robot.isInRangeOfHighGoal()) {
            success = false;
            logger.info(this.robot.getRobotName() + " isn't in range of the highgoal");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        }
    }
}
