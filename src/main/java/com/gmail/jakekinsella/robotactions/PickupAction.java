package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotSide;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/11/16.
 */
public class PickupAction extends Action {

    private long remainingTime;
    private double successChance;
    private long lastTick;

    private RobotSide pickupSide;
    private Ball ball;

    private static final Logger logger = LogManager.getLogger();

    public PickupAction(Command command, Robot robot, RobotSide pickupSide) {
        super(command, robot);

        this.pickupSide = pickupSide;
        this.remainingTime = (long) command.getArg(0);
        this.successChance = (double) command.getArg(1);
        this.lastTick = System.currentTimeMillis();
    }

    public Ball getBall() {
        return this.ball;
    }

    @Override
    public void tick() {
        long delta = System.currentTimeMillis() - this.lastTick;
        this.remainingTime -= delta;

        if (remainingTime <= 0) { // Must be finished
            boolean score = (Math.random() >= (1 - this.successChance));

            if (score) {
                logger.info(this.robot.getRobotName() + " has picked up a ball");
            } else {
                logger.info(this.robot.getRobotName() + " has failed to pick up a ball");
            }

            this.success = score;

            ArrayList<Ball> balls = this.robot.getBallsInFrontOf(this.pickupSide);
            if (balls.size() == 0 || balls.size() > 1) {
                logger.info(this.robot.getRobotName() + " has tried to pickup a nonexistent ball!");
                success = false;
            } else {
                ball = balls.get(0);
            }

            this.robot.sendActionResponse();
            this.robot.actionFinish();
        }

        this.lastTick = System.currentTimeMillis();
    }
}
