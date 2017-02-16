package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class ShootAction extends TimeAction {

    private double accuracy;
    private int ballsPerSecond;
    private int ballsScored;

    private static final Logger logger = LogManager.getLogger();

    public ShootAction(Command command, Robot robot, int ballsPerSecond, double accuracy) {
        super(command, robot, 0);

        this.ballsPerSecond = ballsPerSecond;
        this.accuracy = accuracy;
    }

    public int getBallsScored() {
        return this.ballsScored;
    }

    @Override
    public void actionDone() {
        // TODO: Currently scores all balls at the end of the action and not throughout it

        for (int i = 0; i < this.robot.getBalls().size(); i++) {
            boolean score = (Math.random() >= (1 - this.accuracy));
            if (score) {
                this.ballsScored++;
                this.robot.getRobotAlliance().getScore().scoreHighgoal();
            }
        }

        logger.info(this.robot.getRobotName() + " has shot " + this.getBallsScored() + " balls into the highgoal");

        this.success = true;

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

        this.remainingTime = this.robot.getBalls().size() / this.ballsPerSecond;
    }
}
